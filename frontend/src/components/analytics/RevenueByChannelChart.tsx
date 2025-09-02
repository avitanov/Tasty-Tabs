// src/components/analytics/RevenueByChannelChart.tsx
import { useEffect, useState, useMemo, useCallback } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import type { AnalyticsByChannelResponse } from '../../types/api';
import { analyticsRepository } from '../../api/analyticsRepository';

// Helper to get date strings for API params
const getISODateString = (date: Date) => date.toISOString().split('T')[0];

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#FF8042'];

export const RevenueByChannelChart = () => {
    const [data, setData] = useState<AnalyticsByChannelResponse | null>(null);
    const [loading, setLoading] = useState(true);

    // State for the date inputs
    const [dateRange, setDateRange] = useState(() => {
        const to = new Date();
        const from = new Date();
        from.setDate(to.getDate() - 29); // Default to last 30 days
        return {
            from: getISODateString(from),
            to: getISODateString(to),
        };
    });

    const fetchData = useCallback(() => {
        setLoading(true);
        analyticsRepository.getRevenueByChannel(dateRange)
            .then(setData)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, [dateRange]);

    // Fetch data on initial load and when the date range is submitted
    useEffect(() => {
        fetchData();
    }, [dateRange, fetchData]);

    // Transform the API data into a format that Recharts can easily use for a multi-line chart.
    const chartData = useMemo(() => {
        if (!data) return [];

        const processedData: Record<string, any> = {};
        const channels = data.channels.map(c => c.channel);

        data.channels.forEach(channel => {
            channel.data.forEach(dailyData => {
                if (!processedData[dailyData.day]) {
                    processedData[dailyData.day] = { day: dailyData.day };
                    // Initialize other channels to 0 for this day to keep the lines continuous
                    channels.forEach(c => { processedData[dailyData.day][c] = 0; });
                }
                processedData[dailyData.day][channel.channel] = dailyData.revenue;
            });
        });

        // Sort data by date to ensure the X-axis is chronological
        return Object.values(processedData).sort((a, b) => new Date(a.day).getTime() - new Date(b.day).getTime());
    }, [data]);

    const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setDateRange(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <div className="md:flex justify-between items-center mb-4">
                <h3 className="text-xl font-semibold mb-2 md:mb-0">Revenue by Channel</h3>
                <div className="flex items-center gap-2">
                    <input type="date" name="from" value={dateRange.from} onChange={handleDateChange} className="p-1 border rounded" />
                    <span>to</span>
                    <input type="date" name="to" value={dateRange.to} onChange={handleDateChange} className="p-1 border rounded" />
                </div>
            </div>

            {loading ? <p>Loading chart data...</p> : (
                <div className="h-[400px]">
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={chartData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="day" tickFormatter={(dateStr) => new Date(dateStr).toLocaleDateString([], { month: 'short', day: 'numeric' })} />
                            <YAxis tickFormatter={(value) => `$${value}`} />
                            <Tooltip formatter={(value: number) => `$${value.toFixed(2)}`} />
                            <Legend />
                            {data?.channels.map((channel, index) => (
                                <Line
                                    key={channel.channel}
                                    type="monotone"
                                    dataKey={channel.channel}
                                    stroke={COLORS[index % COLORS.length]}
                                    strokeWidth={2}
                                    dot={false}
                                />
                            ))}
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            )}
            {data && (
                <div className="grid grid-cols-3 gap-4 text-center mt-4 border-t pt-4">
                    <div>
                        <p className="text-sm text-gray-500">Grand Total Revenue</p>
                        <p className="text-xl font-bold">${data.grand_total_revenue.toLocaleString(undefined, { minimumFractionDigits: 2 })}</p>
                    </div>
                    <div>
                        <p className="text-sm text-gray-500">Total Paid Orders</p>
                        <p className="text-xl font-bold">{data.grand_total_paid_orders.toLocaleString()}</p>
                    </div>
                    <div>
                        <p className="text-sm text-gray-500">Total Tips</p>
                        <p className="text-xl font-bold">${data.grand_total_tips.toLocaleString(undefined, { minimumFractionDigits: 2 })}</p>
                    </div>
                </div>
            )}
        </div>
    );
};
