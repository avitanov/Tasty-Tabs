BEGIN;


CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) UNIQUE,
    street VARCHAR(255),
    city VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS employees (
                                         user_id BIGSERIAL PRIMARY KEY,
                                         net_salary DECIMAL(10,2),
    gross_salary DECIMAL(10,2),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS customers (
                                         user_id BIGSERIAL PRIMARY KEY,
                                         FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS staff_roles (
                                           id BIGSERIAL PRIMARY KEY,
                                           name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS front_staff (
                                           employee_id BIGSERIAL PRIMARY KEY,
                                           tip_percent DECIMAL(10,2),
    staff_role_id BIGINT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (staff_role_id) REFERENCES staff_roles (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS back_staff (
                                          employee_id BIGSERIAL PRIMARY KEY,
                                          staff_role_id BIGINT NOT NULL,
                                          FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (staff_role_id) REFERENCES staff_roles (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS managers (
                                        employee_id BIGSERIAL PRIMARY KEY,
                                        FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS shifts (
                                      id BIGSERIAL PRIMARY KEY,
                                      date DATE NOT NULL,
                                      start_time TIME NOT NULL,
                                      end_time TIME NOT NULL,
                                      manager_id BIGINT NOT NULL,
                                      FOREIGN KEY (manager_id) REFERENCES managers (employee_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS assignments (
                                           id BIGSERIAL PRIMARY KEY,
                                           clock_in_time TIME,
                                           clock_out_time TIME,
                                           manager_id BIGINT NOT NULL,
                                           employee_id BIGINT NOT NULL,
                                           shift_id BIGINT NOT NULL,
                                           FOREIGN KEY (manager_id) REFERENCES managers (employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (shift_id) REFERENCES shifts (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS tables (
                                      table_number BIGSERIAL PRIMARY KEY,
                                      seat_capacity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
                                            id BIGSERIAL PRIMARY KEY,
                                            user_id BIGINT NOT NULL,
                                            creation_timestamp TIMESTAMP NOT NULL,
                                            datetime TIMESTAMP NOT NULL,
                                            number_of_people BIGINT NOT NULL,
                                            stay_length DECIMAL(10,2) NULL, --hours
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS frontstaff_managed_reservations (
                                                               id BIGSERIAL PRIMARY KEY,
                                                               reservation_id BIGINT NOT NULL,
                                                               front_staff_id BIGINT NOT NULL,
                                                               table_number BIGINT NOT NULL,
                                                               FOREIGN KEY (reservation_id) REFERENCES reservations (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (front_staff_id) REFERENCES front_staff (employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (table_number) REFERENCES tables (table_number) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS categories (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS products (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category_id BIGINT NOT NULL,
    manage_inventory BOOLEAN NOT NULL DEFAULT FALSE,
    tax_class VARCHAR(4) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS inventories (
                                           product_id BIGSERIAL PRIMARY KEY,
                                           quantity INT NOT NULL,
                                           restock_level  INT NULL,
                                           FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           product_id BIGINT NOT NULL,
                                           is_processed BOOLEAN NOT NULL DEFAULT FALSE,
                                           quantity INT NOT NULL,
                                           price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS tab_orders (
                                          order_id BIGSERIAL PRIMARY KEY,
                                          front_staff_id BIGINT NOT NULL,
                                          table_number BIGINT NOT NULL,
                                          FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (table_number) REFERENCES tables (table_number) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (front_staff_id) REFERENCES front_staff (employee_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS online_orders (
                                             order_id BIGSERIAL PRIMARY KEY,
                                             delivery_address VARCHAR(255) NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        order_id BIGINT NOT NULL,
                                        amount DECIMAL(10,2) NOT NULL,
    payment_type VARCHAR(32) NOT NULL,
    tip_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- ASSERTIONS

ALTER TABLE employees
    ADD CONSTRAINT employees_net_salary_nonneg   CHECK (net_salary   IS NULL OR net_salary   >= 0),
  ADD CONSTRAINT employees_gross_salary_nonneg CHECK (gross_salary IS NULL OR gross_salary >= 0);

ALTER TABLE inventories
    ADD CONSTRAINT inventories_qty_nonneg CHECK (quantity >= 0),
  ADD CONSTRAINT inventories_restock_nonneg CHECK (restock_level IS NULL OR restock_level >= 0);

ALTER TABLE products
    ADD CONSTRAINT products_price_nonneg CHECK (price >= 0);

ALTER TABLE order_items
    ADD CONSTRAINT order_items_qty_pos CHECK (quantity > 0),
  ADD CONSTRAINT order_items_price_nonneg CHECK (price >= 0);

ALTER TABLE payments
    ADD CONSTRAINT payments_amount_nonneg CHECK (amount >= 0),
  ADD CONSTRAINT payments_tip_nonneg    CHECK (tip_amount >= 0);

ALTER TABLE tables
    ADD CONSTRAINT tables_capacity_pos CHECK (seat_capacity > 0);

ALTER TABLE reservations
    ADD CONSTRAINT reservations_people_pos CHECK (number_of_people > 0);

ALTER TABLE front_staff
    ADD CONSTRAINT front_staff_tip_pct CHECK (tip_percent IS NULL OR (tip_percent >= 0 AND tip_percent <= 100));


CREATE OR REPLACE FUNCTION enforce_no_double_booking()
RETURNS trigger AS $$
DECLARE
new_span tsrange;
  conflict_exists boolean;
BEGIN
SELECT tsrange(
               r.datetime,
               r.datetime + (COALESCE(r.stay_length, 2) * INTERVAL '1 hour'),
               '[)'
           )
INTO new_span
FROM reservations r
WHERE r.id = NEW.reservation_id;

IF new_span IS NULL THEN
    RAISE EXCEPTION 'Reservation % not found or invalid', NEW.reservation_id;
END IF;

SELECT EXISTS (
    SELECT 1
    FROM frontstaff_managed_reservations fmr
             JOIN reservations r2 ON r2.id = fmr.reservation_id
    WHERE fmr.table_number = NEW.table_number
      AND (NEW.id IS NULL OR fmr.id <> NEW.id)
      AND tsrange(
            r2.datetime,
            r2.datetime + (COALESCE(r2.stay_length, 2) * INTERVAL '1 hour'),
            '[)'
        ) && new_span
) INTO conflict_exists;

IF conflict_exists THEN
    RAISE EXCEPTION 'Double booking prevented: table % has overlapping reservations', NEW.table_number;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_no_double_booking_ins ON frontstaff_managed_reservations;
CREATE CONSTRAINT TRIGGER trg_no_double_booking_ins
AFTER INSERT ON frontstaff_managed_reservations
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION enforce_no_double_booking();

DROP TRIGGER IF EXISTS trg_no_double_booking_upd ON frontstaff_managed_reservations;
CREATE CONSTRAINT TRIGGER trg_no_double_booking_upd
AFTER UPDATE ON frontstaff_managed_reservations
                 DEFERRABLE INITIALLY DEFERRED
                 FOR EACH ROW EXECUTE FUNCTION enforce_no_double_booking();

CREATE INDEX IF NOT EXISTS fmr_table_idx ON frontstaff_managed_reservations (table_number);
CREATE INDEX IF NOT EXISTS reservations_span_expr_gist
    ON reservations USING gist (
    tsrange(
    datetime,
    datetime + (COALESCE(stay_length, 2) * INTERVAL '1 hour'),
    '[)'
    )
    );
CREATE INDEX IF NOT EXISTS idx_payments_order      ON payments(order_id);
CREATE INDEX IF NOT EXISTS idx_payments_created_at ON payments(created_at);


CREATE OR REPLACE FUNCTION fmr_capacity_guard() RETURNS trigger AS $$
DECLARE
cap int;
  party bigint;
BEGIN
SELECT seat_capacity INTO cap FROM tables WHERE table_number = NEW.table_number;
SELECT number_of_people INTO party FROM reservations WHERE id = NEW.reservation_id;

IF cap IS NULL OR party IS NULL THEN
    RAISE EXCEPTION 'Invalid reservation % or table %', NEW.reservation_id, NEW.table_number;
END IF;

  IF party > cap THEN
    RAISE EXCEPTION 'Party size % exceeds capacity % for table %',
      party, cap, NEW.table_number;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_fmr_capacity_ins ON frontstaff_managed_reservations;
CREATE TRIGGER trg_fmr_capacity_ins
    BEFORE INSERT ON frontstaff_managed_reservations
    FOR EACH ROW EXECUTE FUNCTION fmr_capacity_guard();

DROP TRIGGER IF EXISTS trg_fmr_capacity_upd ON frontstaff_managed_reservations;
CREATE TRIGGER trg_fmr_capacity_upd
    BEFORE UPDATE ON frontstaff_managed_reservations
    FOR EACH ROW EXECUTE FUNCTION fmr_capacity_guard();


CREATE OR REPLACE FUNCTION payments_mark_order_paid() RETURNS trigger AS $$
BEGIN
UPDATE orders
SET status = 'PAID'
WHERE id = NEW.order_id;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_payments_mark_order_paid ON payments;
CREATE TRIGGER trg_payments_mark_order_paid
    AFTER INSERT ON payments
    FOR EACH ROW EXECUTE FUNCTION payments_mark_order_paid();


CREATE MATERIALIZED VIEW IF NOT EXISTS mv_payments_daily_channel AS
WITH orders_channel AS (
  SELECT
    o.id AS order_id,
    CASE
      WHEN EXISTS (SELECT 1 FROM tab_orders t WHERE t.order_id = o.id) THEN 'TAB'
      WHEN EXISTS (SELECT 1 FROM online_orders oo WHERE oo.order_id = o.id) THEN 'ONLINE'
      ELSE 'UNKNOWN'
    END AS channel
  FROM orders o
)
SELECT
    (date_trunc('day', p.created_at))::date AS day,
  oc.channel,
  COUNT(DISTINCT p.order_id)            AS paid_orders_cnt,
  SUM(p.amount)::numeric(14,2)          AS revenue,
  SUM(p.tip_amount)::numeric(14,2)      AS tip_total
FROM payments p
    JOIN orders_channel oc ON oc.order_id = p.order_id
GROUP BY (date_trunc('day', p.created_at))::date, oc.channel;

CREATE UNIQUE INDEX IF NOT EXISTS ux_mv_payments_daily_channel
    ON mv_payments_daily_channel (day, channel);



-- DML
INSERT INTO users(id, email, password, phone_number, street, city) -- password1
VALUES
    (1, 'test@hotmail.com', '$2a$10$rliIgXfgZgT1ljzxf.3NjeF1hx63s30xKfUsKjeUYA8jL/GXA1Jsy', '070003005', 'Mladinska 3', 'Strumica'),
    (2, 'test2@hotmail.com', '$2a$10$rliIgXfgZgT1ljzxf.3NjeF1hx63s30xKfUsKjeUYA8jL/GXA1Jsy', '070001002', 'Marsal Tito 10', 'Strumica'),
    (3, 'test3@hotmail.com', '$2a$10$rliIgXfgZgT1ljzxf.3NjeF1hx63s30xKfUsKjeUYA8jL/GXA1Jsy', '070003003', 'Mladinska 5', 'Strumica'),
    (4, 'test4@hotmail.com', '$2a$10$rliIgXfgZgT1ljzxf.3NjeF1hx63s30xKfUsKjeUYA8jL/GXA1Jsy', '070004004', 'Marsal Tito 11', 'Strumica'),
    (5, 'test5@hotmail.com', '$2a$10$rliIgXfgZgT1ljzxf.3NjeF1hx63s30xKfUsKjeUYA8jL/GXA1Jsy', '070005005', 'Mladinska 12', 'Strumica');

INSERT INTO employees(user_id, net_salary, gross_salary)
VALUES
    (1, 30000, 40000),
    (3, 50000, 62000),
    (4, 35000, 46000),
    (5, 28000, 37000);

INSERT INTO managers(employee_id)
VALUES
    (3);

INSERT INTO staff_roles(id, name)
VALUES
    (1, 'Server'),
    (2, 'Chef'),
    (3, 'Bartender'),
    (4, 'Hostess');

INSERT INTO front_staff(employee_id, tip_percent, staff_role_id)
VALUES
    (1, .4, 1),
    (5, 0.1, 4);

INSERT INTO back_staff(employee_id, staff_role_id)
VALUES
    (4, 2);

INSERT INTO customers(user_id)
VALUES
    (2);

INSERT INTO shifts (id, date, start_time, end_time, manager_id)
VALUES
    (1, current_date, '09:00:00', '17:00:00', 3);

INSERT INTO assignments(id, clock_in_time, clock_out_time, manager_id, employee_id, shift_id)
VALUES
    (1, NULL, NULL, 3, 1, 1);

INSERT INTO tables(table_number, seat_capacity)
VALUES
    (1, 4),
    (2, 8);

INSERT INTO  reservations(id, user_id, creation_timestamp, datetime, stay_length, number_of_people)
VALUES
    (1, 2, now(), now(), NULL, 4);

INSERT INTO frontstaff_managed_reservations(id, reservation_id, front_staff_id, table_number)
VALUES
    (1, 1, 5, 1);

INSERT INTO categories(id, name)
VALUES
    (1, 'Drinks'),
    (2, 'Appetizers'),
    (3, 'Entrees');

INSERT INTO products(id, name, description, price, category_id, manage_inventory, tax_class)
VALUES
    (1, 'Coca Cola', 'A classic carbonated soft drink.', 100, 1, TRUE, 'A'),
    (2, 'Pomfrit so sirenje', 'Crispy french fries topped with melted cheese.', 250, 2, FALSE, 'A');

INSERT INTO inventories(product_id, quantity)
VALUES
    (1, 100);

INSERT INTO orders(id, status, datetime)
VALUES
    (1, 'PENDING', '2025-01-05 10:00:00'),
    (2, 'ACCEPTED', '2025-01-05 10:00:00'),
    (3, 'CONFIRMED', '2025-01-05 11:00:00');

INSERT INTO order_items (id, order_id, product_id, is_processed, quantity, price)
SELECT 1, 1, 1, TRUE, 3, price FROM products WHERE id = 1
UNION ALL
SELECT 2, 1, 2, FALSE, 1, price FROM products WHERE id = 2
UNION ALL
SELECT 3, 3, 2, FALSE, 2, price FROM products WHERE id = 2
UNION ALL
SELECT 4, 3, 1, FALSE, 2, price FROM products WHERE id = 1;

INSERT INTO tab_orders(order_id, front_staff_id, table_number)
VALUES
    (1, 1, 1);

INSERT INTO online_orders(order_id, delivery_address, customer_id)
VALUES
    (3, 'Leninova 5', 2);

INSERT INTO payments(id, order_id, amount, payment_type, tip_amount)
VALUES
    (1, 3, 700, 'cash', 10);

REFRESH MATERIALIZED VIEW mv_payments_daily_channel;

-- ANALYTIC: Server performance & revenue ranking (last 3 months)
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
    CASE
    WHEN total_assignments > 0
    THEN (orders_processed::float / total_assignments)
    ELSE 0
    END as orders_per_assignment,
    CASE
    WHEN gross_salary > 0
    THEN total_revenue_generated / gross_salary
    ELSE 0
    END as revenue_to_salary_ratio,
    CASE
    WHEN orders_processed > 0
    THEN total_revenue_generated / orders_processed
    ELSE 0
    END as avg_revenue_per_order,
    CASE
    WHEN tip_percent > 0 AND total_revenue_generated > 0
    THEN (total_revenue_generated * tip_percent / 100)
    ELSE 0
    END as estimated_tips_earned
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
ORDER BY total_revenue_generated DESC, orders_processed DESC;

-- ANALYTIC: Monthly revenue vs labor cost (% of revenue)
WITH monthly_revenue AS (
    SELECT
        DATE_TRUNC('month', o.datetime) as operation_month,
        SUM(oi.quantity * oi.price) as revenue
    FROM orders o
             JOIN order_items oi ON o.id = oi.order_id
    GROUP BY DATE_TRUNC('month', o.datetime)
),
     monthly_labor_cost AS (
         SELECT
             monthly_assignments.operation_month,
             SUM(e.gross_salary) as labor_cost
         FROM (
                  SELECT DISTINCT
                      DATE_TRUNC('month', s.date) as operation_month,
                      a.employee_id
                  FROM shifts s
                           JOIN assignments a ON s.id = a.shift_id
              ) as monthly_assignments
                  JOIN employees e ON monthly_assignments.employee_id = e.user_id
         GROUP BY monthly_assignments.operation_month
     )
SELECT
    TO_CHAR(COALESCE(mr.operation_month, mlc.operation_month), 'YYYY-MM') as period,
    ROUND(COALESCE(mr.revenue, 0)::numeric, 2) as total_revenue,
    ROUND(COALESCE(mlc.labor_cost, 0)::numeric, 2) as total_labor_cost,
    ROUND(
            CASE
                WHEN COALESCE(mr.revenue, 0) > 0
                    THEN (COALESCE(mlc.labor_cost, 0) / mr.revenue * 100)
                ELSE 0
                END::numeric, 2
        ) as labor_as_percent_of_revenue
FROM monthly_revenue mr
         FULL OUTER JOIN monthly_labor_cost mlc ON mr.operation_month = mlc.operation_month
ORDER BY period DESC;

-- ANALYTIC: Daily operations summary (30 days) with conversion & revenue thresholds
SELECT
    dates.operation_date,
    COUNT(DISTINCT r.id) as total_reservations,
    COUNT(DISTINCT o.id) as total_orders,
    COUNT(DISTINCT r.user_id) as unique_customers,
    COUNT(DISTINCT fs.employee_id) as active_employees,
    COALESCE(SUM(oi.quantity * oi.price), 0) as daily_revenue,
    CASE
        WHEN COUNT(DISTINCT r.id) = 0 THEN false
        ELSE (COUNT(DISTINCT o.id) * 100.0 / COUNT(DISTINCT r.id)) >= 70
        END as meets_conversion_target,
    CASE
        WHEN COALESCE(SUM(oi.quantity * oi.price), 0) >= 2000 THEN 'High Revenue Day'
        WHEN COALESCE(SUM(oi.quantity * oi.price), 0) >= 1000 THEN 'Good Revenue Day'
        WHEN COALESCE(SUM(oi.quantity * oi.price), 0) >= 500 THEN 'Average Revenue Day'
        ELSE 'Low Revenue Day'
        END as revenue_category
FROM generate_series(
                         CURRENT_DATE - INTERVAL '30 days',
                         CURRENT_DATE,
                         '1 day'::interval
         ) dates(operation_date)
         LEFT JOIN reservations r
                   ON DATE(r.datetime) = dates.operation_date
    LEFT JOIN orders o
ON DATE(o.datetime) = dates.operation_date
    LEFT JOIN order_items oi
    ON o.id = oi.order_id
    LEFT JOIN tab_orders to2
    ON o.id = to2.order_id
    LEFT JOIN front_staff fs
    ON to2.front_staff_id = fs.employee_id
GROUP BY dates.operation_date
ORDER BY dates.operation_date DESC;

-- ANALYTIC: Top 10 products by revenue (last 90 days) + revenue share
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
WHERE o.datetime >= CURRENT_DATE - INTERVAL '90 days'
GROUP BY p.id, p.name, c.name
ORDER BY total_revenue DESC
    LIMIT 10;

-- ANALYTIC: Revenue by shift period (dynamic monthly view)


CREATE OR REPLACE VIEW v_revenue_by_shift_period AS

-- CTE to get all unique shift start and end times from your shifts table
WITH distinct_shift_periods AS (
    SELECT DISTINCT
        start_time::time AS start_t,
        end_time::time AS end_t
    FROM
        shifts
)

-- Main query to calculate revenue
SELECT
    -- 1. The month of the order, e.g., '2025-08'
    TO_CHAR(o.datetime, 'YYYY-MM') AS period,

    -- 2. The shift period string, e.g., '08:00:00-16:00:00'
    dsp.start_t::text || '-' || dsp.end_t::text AS shift_period,

    -- 3. The total revenue for that shift period within that month
    SUM(oi.price * oi.quantity) AS total_revenue
FROM
    orders o
        JOIN
    order_items oi ON o.id = oi.order_id
-- Join each order to a shift period if its timestamp's time falls within the start and end times
        JOIN
    distinct_shift_periods dsp ON o.datetime::time >= dsp.start_t AND o.datetime::time < dsp.end_t
GROUP BY
    period,
    shift_period
ORDER BY
    period DESC,
    shift_period ASC;

SELECT * FROM v_revenue_by_shift_period;


-- ANALYTIC: Revenue split (Online vs Tab) for a date range (by order date)
DROP FUNCTION IF EXISTS get_revenue_split(date, date);

CREATE OR REPLACE FUNCTION get_revenue_split(p_start_date DATE, p_end_date DATE)
RETURNS TABLE(order_type TEXT, total_revenue NUMERIC(14,2)) AS $$
BEGIN
RETURN QUERY
SELECT 'Online Orders'::text AS order_type,
        COALESCE(SUM(p.amount), 0)::numeric(14,2) AS total_revenue
FROM orders o
         JOIN payments p ON o.id = p.order_id
         JOIN online_orders oo ON o.id = oo.order_id
WHERE o.datetime::date BETWEEN p_start_date AND p_end_date

UNION ALL

SELECT 'Tab Orders'::text AS order_type,
        COALESCE(SUM(p.amount), 0)::numeric(14,2) AS total_revenue
FROM orders o
         JOIN payments p ON o.id = p.order_id
         JOIN tab_orders tord ON o.id = tord.order_id
WHERE o.datetime::date BETWEEN p_start_date AND p_end_date;
END;
$$ LANGUAGE plpgsql;

-- ANALYTIC: Managers' shifts above monthly average revenue (current calendar year)
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
GROUP BY
    mws.shift_id, month_start, mws.date, mws.start_time, mws.end_time, mws.manager_id
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
    u.email       AS manager_email,
    sr.shift_revenue,
    ma.avg_revenue_per_shift,
    (sr.shift_revenue - ma.avg_revenue_per_shift)::numeric(14,2) AS above_by
FROM shift_revenue sr
         JOIN monthly_avg ma ON ma.month_start = sr.month_start
         JOIN managers m     ON m.employee_id = sr.manager_id
         JOIN employees e    ON e.user_id     = m.employee_id
         JOIN users u        ON u.id          = e.user_id
WHERE sr.shift_revenue > ma.avg_revenue_per_shift
ORDER BY period DESC, sr.shift_revenue DESC, sr.shift_date DESC;


SELECT setval('public.users_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.users;
SELECT setval('public.staff_roles_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.staff_roles;
SELECT setval('public.shifts_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.shifts;
SELECT setval('public.assignments_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.assignments;
SELECT setval('public.reservations_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.reservations;
SELECT setval('public.frontstaff_managed_reservations_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.frontstaff_managed_reservations;
SELECT setval('public.categories_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.categories;
SELECT setval('public.products_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.products;
SELECT setval('public.orders_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.orders;
SELECT setval('public.order_items_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.order_items;
SELECT setval('public.payments_id_seq', COALESCE(MAX(id + 1), 1), false) FROM public.payments;

COMMIT;
