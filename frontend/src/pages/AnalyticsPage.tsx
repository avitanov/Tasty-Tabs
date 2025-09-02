// src/pages/AnalyticsPage.tsx
import { useEffect, useState } from 'react';
import type { DailyOpsDto } from '../types/api';
import { analyticsRepository } from '../api/analyticsRepository';
import { StatCard } from '../components/analytics/StatCard';
import { TopProductsList } from '../components/analytics/TopProductsList';
import { ServerPerformanceTable } from '../components/analytics/ServerPerformanceTable';
import { RevenueByShiftPeriodTable } from '../components/analytics/RevenueByShiftPeriodTable';
import { ManagerPerformanceTable } from '../components/analytics/ManagerPerformanceTable';
import { RevenueSplitChart } from '../components/analytics/RevenueSplitChart';
import { MonthlyRevenueLaborChart } from '../components/analytics/MonthlyRevenueLaborChart';
import { RevenueByChannelChart } from '../components/analytics/RevenueByChannelChart';

// Simple icons for StatCards
const RevenueIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v.01" /></svg>;
const OrdersIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" /></svg>;
const CustomersIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M15 21a6 6 0 00-9-5.197m0 0A5.975 5.975 0 0112 13a5.975 5.975 0 014.5 2.803" /></svg>;


export const AnalyticsPage = () => {
    const [dailyOps, setDailyOps] = useState<DailyOpsDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        analyticsRepository.getDailyOps(30)
            .then(setDailyOps)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    const totals = dailyOps.reduce((acc, day) => {
        acc.revenue += day.daily_revenue;
        acc.orders += day.total_orders;
        acc.customers += day.unique_customers;
        return acc;
    }, { revenue: 0, orders: 0, customers: 0 });

    if (loading) return <div className="p-6">Loading analytics...</div>;

    return (
        <div className="p-6 bg-gray-50">
            <h1 className="text-3xl font-bold mb-6">Analytics Dashboard</h1>

            {/* Stat Cards Section */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
                <StatCard title="Total Revenue (Last 30 Days)" value={`$${totals.revenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`} icon={<RevenueIcon />} />
                <StatCard title="Total Orders (Last 30 Days)" value={totals.orders.toLocaleString()} icon={<OrdersIcon />} />
                <StatCard title="Unique Customers (Last 30 Days)" value={totals.customers.toLocaleString()} icon={<CustomersIcon />} />
            </div>
            <div className="mb-8">
                <RevenueByChannelChart />
            </div>

            {/* Charts Section */}
            <div className="grid grid-cols-1 lg:grid-cols-5 gap-6 mb-8">
                <div className="lg:col-span-3">
                    <MonthlyRevenueLaborChart />
                </div>
                <div className="lg:col-span-2">
                    <RevenueSplitChart />
                </div>
            </div>

            {/* Tables Section */}
            <div className="space-y-8">
                <TopProductsList />
                <ServerPerformanceTable />
                <ManagerPerformanceTable />
                <RevenueByShiftPeriodTable />
            </div>
        </div>
    );
};
