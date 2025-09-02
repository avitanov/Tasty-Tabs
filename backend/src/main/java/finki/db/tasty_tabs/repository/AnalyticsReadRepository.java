package finki.db.tasty_tabs.repository;
import finki.db.tasty_tabs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AnalyticsReadRepository {

    private final JdbcTemplate jdbc;

    public List<ServerPerformanceDto> serverPerformanceLast3Months() {
        String sql = """
            WITH server_metrics AS (
                SELECT
                    fs.employee_id,
                    u.email as server_email,
                    u.phone_number,
                    e.net_salary,
                    e.gross_salary,
                    fs.tip_percent,
                    sr.name as staff_role_name,
                    COUNT(DISTINCT a.id) as total_assignments,
                    COUNT(DISTINCT s.date) as days_worked,
                    AVG(EXTRACT(EPOCH FROM (a.clock_out_time - a.clock_in_time))/3600) as avg_hours_per_shift,
                    COUNT(DISTINCT o.id) as orders_processed,
                    COALESCE(SUM(oi.quantity * oi.price), 0) as total_revenue_generated
                FROM front_staff fs
                JOIN employees e ON fs.employee_id = e.user_id
                JOIN users u ON e.user_id = u.id
                JOIN staff_roles sr ON fs.staff_role_id = sr.id
                LEFT JOIN assignments a ON fs.employee_id = a.employee_id
                LEFT JOIN shifts s ON a.shift_id = s.id
                LEFT JOIN tab_orders to2 ON to2.front_staff_id = fs.employee_id
                LEFT JOIN orders o ON o.id = to2.order_id
                   AND o.datetime >= CURRENT_DATE - INTERVAL '3 months'
                LEFT JOIN order_items oi ON o.id = oi.order_id
                WHERE LOWER(sr.name) = 'server'
                GROUP BY fs.employee_id, u.email, u.phone_number,
                         e.net_salary, e.gross_salary, fs.tip_percent, sr.name
            ),
            performance_ranking AS (
                SELECT *,
                    RANK() OVER (ORDER BY total_revenue_generated DESC) as revenue_rank,
                    RANK() OVER (ORDER BY orders_processed DESC) as orders_rank,
                    CASE WHEN total_assignments > 0
                         THEN (orders_processed::float / total_assignments)
                         ELSE 0 END as orders_per_assignment,
                    CASE WHEN gross_salary > 0
                         THEN total_revenue_generated / gross_salary
                         ELSE 0 END as revenue_to_salary_ratio,
                    CASE WHEN orders_processed > 0
                         THEN total_revenue_generated / orders_processed
                         ELSE 0 END as avg_revenue_per_order,
                    CASE WHEN tip_percent > 0 AND total_revenue_generated > 0
                         THEN (total_revenue_generated * tip_percent / 100)
                         ELSE 0 END as estimated_tips_earned
                FROM server_metrics
            )
            SELECT
                server_email,
                phone_number,
                total_assignments,
                days_worked,
                orders_processed,
                total_revenue_generated,
                revenue_rank,
                orders_rank,
                ROUND(orders_per_assignment::numeric, 2) as avg_orders_per_shift,
                ROUND(avg_revenue_per_order::numeric, 2) as avg_order_value
            FROM performance_ranking
            ORDER BY total_revenue_generated DESC, orders_processed DESC
            """;
        return jdbc.query(sql, (rs, i) -> new ServerPerformanceDto(
                rs.getString("server_email"),
                rs.getString("phone_number"),
                getLong(rs, "total_assignments"),
                getLong(rs, "days_worked"),
                getLong(rs, "orders_processed"),
                rs.getBigDecimal("total_revenue_generated"),
                rs.getInt("revenue_rank"),
                rs.getInt("orders_rank"),
                rs.getBigDecimal("avg_orders_per_shift"),
                rs.getBigDecimal("avg_order_value")
        ));
    }

    public List<MonthlyRevenueVsLaborDto> monthlyRevenueVsLabor() {
        String sql = """
            WITH monthly_revenue AS (
                SELECT DATE_TRUNC('month', o.datetime) as operation_month,
                       SUM(oi.quantity * oi.price) as revenue
                FROM orders o
                JOIN order_items oi ON o.id = oi.order_id
                GROUP BY DATE_TRUNC('month', o.datetime)
            ),
            monthly_labor_cost AS (
                SELECT monthly_assignments.operation_month,
                       SUM(e.gross_salary) as labor_cost
                FROM (
                    SELECT DISTINCT DATE_TRUNC('month', s.date) as operation_month, a.employee_id
                    FROM shifts s
                    JOIN assignments a ON s.id = a.shift_id
                ) monthly_assignments
                JOIN employees e ON monthly_assignments.employee_id = e.user_id
                GROUP BY monthly_assignments.operation_month
            )
            SELECT TO_CHAR(COALESCE(mr.operation_month, mlc.operation_month), 'YYYY-MM') as period,
                   ROUND(COALESCE(mr.revenue, 0)::numeric, 2) as total_revenue,
                   ROUND(COALESCE(mlc.labor_cost, 0)::numeric, 2) as total_labor_cost,
                   ROUND(
                     CASE WHEN COALESCE(mr.revenue, 0) > 0
                          THEN (COALESCE(mlc.labor_cost, 0) / mr.revenue * 100)
                          ELSE 0 END::numeric, 2
                   ) as labor_as_percent_of_revenue
            FROM monthly_revenue mr
            FULL OUTER JOIN monthly_labor_cost mlc ON mr.operation_month = mlc.operation_month
            ORDER BY period DESC
            """;
        return jdbc.query(sql, (rs, i) -> new MonthlyRevenueVsLaborDto(
                rs.getString("period"),
                rs.getBigDecimal("total_revenue"),
                rs.getBigDecimal("total_labor_cost"),
                rs.getBigDecimal("labor_as_percent_of_revenue")
        ));
    }

    public List<DailyOpsDto> dailyOpsSummary(int daysBack) {
        String sql = """
            SELECT
                dates.operation_date,
                COUNT(DISTINCT r.id) as total_reservations,
                COUNT(DISTINCT o.id) as total_orders,
                COUNT(DISTINCT r.user_id) as unique_customers,
                COUNT(DISTINCT fs.employee_id) as active_employees,
                COALESCE(SUM(oi.quantity * oi.price), 0) as daily_revenue,
                CASE WHEN COUNT(DISTINCT r.id) = 0 THEN false
                     ELSE (COUNT(DISTINCT o.id) * 100.0 / COUNT(DISTINCT r.id)) >= 70
                END as meets_conversion_target,
                CASE
                    WHEN COALESCE(SUM(oi.quantity * oi.price), 0) >= 2000 THEN 'High Revenue Day'
                    WHEN COALESCE(SUM(oi.quantity * oi.price), 0) >= 1000 THEN 'Good Revenue Day'
                    WHEN COALESCE(SUM(oi.quantity * oi.price), 0) >= 500  THEN 'Average Revenue Day'
                    ELSE 'Low Revenue Day'
                END as revenue_category
            FROM generate_series(
                CURRENT_DATE - (? || ' days')::interval,
                CURRENT_DATE,
                '1 day'::interval
            ) dates(operation_date)
            LEFT JOIN reservations r ON DATE(r.datetime) = dates.operation_date
            LEFT JOIN orders o       ON DATE(o.datetime) = dates.operation_date
            LEFT JOIN order_items oi ON o.id = oi.order_id
            LEFT JOIN tab_orders to2 ON o.id = to2.order_id
            LEFT JOIN front_staff fs ON to2.front_staff_id = fs.employee_id
            GROUP BY dates.operation_date
            ORDER BY dates.operation_date DESC
            """;
        return jdbc.query(sql, ps -> ps.setInt(1, daysBack), (rs, i) -> new DailyOpsDto(
                rs.getDate("operation_date").toLocalDate(),
                getLong(rs, "total_reservations"),
                getLong(rs, "total_orders"),
                getLong(rs, "unique_customers"),
                getLong(rs, "active_employees"),
                rs.getBigDecimal("daily_revenue"),
                rs.getBoolean("meets_conversion_target"),
                rs.getString("revenue_category")
        ));
    }

    public List<TopProductDto> topProducts(int daysBack, int limit) {
        String sql = """
            SELECT
                p.id as product_id,
                p.name as product_name,
                c.name as category_name,
                SUM(oi.quantity) as total_quantity_sold,
                SUM(oi.quantity * oi.price) as total_revenue,
                ROUND(100.0 * SUM(oi.quantity * oi.price) / SUM(SUM(oi.quantity * oi.price)) OVER (), 2) as revenue_share_percent
            FROM products p
            JOIN categories c ON p.category_id = c.id
            JOIN order_items oi ON p.id = oi.product_id
            JOIN orders o ON o.id = oi.order_id
            WHERE o.datetime >= CURRENT_DATE - (? || ' days')::interval
            GROUP BY p.id, p.name, c.name
            ORDER BY total_revenue DESC
            LIMIT ?
            """;
        return jdbc.query(sql, ps -> { ps.setInt(1, daysBack); ps.setInt(2, limit); }, (rs, i) -> new TopProductDto(
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getString("category_name"),
                getLong(rs, "total_quantity_sold"),
                rs.getBigDecimal("total_revenue"),
                rs.getBigDecimal("revenue_share_percent")
        ));
    }

    public List<Map<String, Object>> revenueByShiftPeriodView() {
        // dynamic columns -> return as list of maps
        return jdbc.queryForList("SELECT * FROM v_revenue_by_shift_period");
    }

    public List<RevenueSplitDto> revenueSplit(LocalDate from, LocalDate to) {
        return jdbc.query("SELECT * FROM get_revenue_split(?, ?)",
                ps -> { ps.setObject(1, from); ps.setObject(2, to); },
                (rs, i) -> new RevenueSplitDto(
                        rs.getString("order_type"),
                        rs.getBigDecimal("total_revenue")
                ));
    }

    public List<ManagerShiftAboveAvgDto> managersShiftsAboveMonthlyAvgCurrentYear() {
        String sql = """
        WITH manager_worked_shifts AS (
          SELECT DISTINCT
                 s.id AS shift_id,
                 s.date,
                 s.start_time,
                 s.end_time,
                 a.employee_id AS manager_id
          FROM assignments a
          JOIN shifts s   ON s.id = a.shift_id
          JOIN managers m ON m.employee_id = a.employee_id
          WHERE s.date >= date_trunc('year', CURRENT_DATE)::date
            AND s.date <  (date_trunc('year', CURRENT_DATE) + INTERVAL '1 year')::date
        ),
        shift_revenue AS (
          SELECT
            mws.shift_id,
            date_trunc('month', mws.date)::date AS month_start,
            mws.date AS shift_date,
            mws.start_time,
            mws.end_time,
            mws.manager_id,
            COALESCE(SUM(oi.quantity * oi.price), 0)::numeric(14,2) AS shift_revenue
          FROM manager_worked_shifts mws
          LEFT JOIN orders o
            ON o.datetime::date = mws.date
           AND o.datetime::time >= mws.start_time
           AND o.datetime::time <  mws.end_time
          LEFT JOIN order_items oi ON oi.order_id = o.id
          GROUP BY mws.shift_id, month_start, mws.date, mws.start_time, mws.end_time, mws.manager_id
        ),
        monthly_avg AS (
          SELECT month_start, AVG(shift_revenue)::numeric(14,2) AS avg_revenue_per_shift
          FROM shift_revenue
          GROUP BY month_start
        )
        SELECT
          to_char(sr.month_start, 'YYYY-MM') AS period,
          sr.shift_id,
          sr.shift_date,
          sr.start_time AS shift_start_time,
          sr.end_time   AS shift_end_time,
          u.email AS manager_email,
          sr.shift_revenue,
          ma.avg_revenue_per_shift,
          (sr.shift_revenue - ma.avg_revenue_per_shift)::numeric(14,2) AS above_by
        FROM shift_revenue sr
        JOIN monthly_avg ma ON ma.month_start = sr.month_start
        JOIN managers m     ON m.employee_id = sr.manager_id
        JOIN employees e    ON e.user_id     = m.employee_id
        JOIN users u        ON u.id          = e.user_id
        WHERE sr.shift_revenue > ma.avg_revenue_per_shift
        ORDER BY period DESC, sr.shift_revenue DESC, sr.shift_date DESC
        """;

        return jdbc.query(sql, (rs, i) -> new ManagerShiftAboveAvgDto(
                rs.getString("period"),
                rs.getLong("shift_id"),
                rs.getDate("shift_date").toLocalDate(),
                rs.getObject("shift_start_time", java.time.LocalTime.class),
                rs.getObject("shift_end_time",   java.time.LocalTime.class),
                rs.getString("manager_email"),
                rs.getBigDecimal("shift_revenue"),
                rs.getBigDecimal("avg_revenue_per_shift"),
                rs.getBigDecimal("above_by")
        ));
    }

    private static Long getLong(ResultSet rs, String col) throws java.sql.SQLException {
        long v = rs.getLong(col);
        return rs.wasNull() ? null : v;
    }
}
