// src/components/analytics/RevenueSplitChart.tsx
import { useEffect, useState } from 'react';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import type { RevenueSplitDto } from '../../types/api';
import { analyticsRepository } from '../../api/analyticsRepository';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

export const RevenueSplitChart = () => {
    const [data, setData] = useState<RevenueSplitDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        analyticsRepository.getRevenueSplit()
            .then(setData)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading revenue split...</p>;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md h-[400px]">
            <h3 className="text-xl font-semibold mb-4">Revenue by Order Type (Last 30 Days)</h3>
            <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                    <Pie
                        data={data}
                        dataKey="total_revenue"
                        nameKey="order_type"
                        cx="50%"
                        cy="50%"
                        outerRadius={120}
                        fill="#8884d8"
                        label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                    >
                        {data.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip formatter={(value: number) => `$${value.toFixed(2)}`} />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
};
