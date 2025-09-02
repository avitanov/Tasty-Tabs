// src/components/analytics/MonthlyRevenueLaborChart.tsx
import { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import type { MonthlyRevenueVsLaborDto } from '../../types/api';
import { analyticsRepository } from '../../api/analyticsRepository';

export const MonthlyRevenueLaborChart = () => {
    const [data, setData] = useState<MonthlyRevenueVsLaborDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        analyticsRepository.getMonthlyRevenueVsLabor()
            .then(setData)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading monthly performance...</p>;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md h-[400px]">
            <h3 className="text-xl font-semibold mb-4">Monthly Revenue vs. Labor Cost</h3>
            <ResponsiveContainer width="100%" height="90%">
                <BarChart data={data}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="period" />
                    <YAxis />
                    <Tooltip formatter={(value: number) => `$${value.toFixed(2)}`} />
                    <Legend />
                    <Bar dataKey="total_revenue" fill="#8884d8" name="Revenue" />
                    <Bar dataKey="total_labor_cost" fill="#82ca9d" name="Labor Cost" />
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
};
