// src/components/analytics/ManagerPerformanceTable.tsx
import { useEffect, useState } from 'react';
import type { ManagerShiftAboveAvgDto } from '../../types/api';
import { analyticsRepository } from '../../api/analyticsRepository';

export const ManagerPerformanceTable = () => {
    const [data, setData] = useState<ManagerShiftAboveAvgDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        analyticsRepository.getManagersShiftsAboveAvg()
            .then(setData)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading manager performance...</p>;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h3 className="text-xl font-semibold mb-4">Manager Shifts Above Average Revenue</h3>
            <div className="overflow-x-auto">
                <table className="min-w-full">
                    <thead>
                        <tr>
                            <th className="py-2 text-left">Manager</th>
                            <th className="py-2 text-left">Shift Date</th>
                            <th className="py-2 text-left">Shift Time</th>
                            <th className="py-2 text-right">Shift Revenue</th>
                            <th className="py-2 text-right">Average Revenue</th>
                        </tr>
                    </thead>
                    <tbody>
                        {data.map((shift, i) => (
                            <tr key={i} className="border-t">
                                <td className="py-2">{shift.manager_email}</td>
                                <td className="py-2">{new Date(shift.shift_date).toLocaleDateString()}</td>
                                <td className="py-2">{shift.shift_start_time} - {shift.shift_end_time}</td>
                                <td className="py-2 text-right font-bold text-green-600">${shift.shift_revenue.toFixed(2)}</td>
                                <td className="py-2 text-right text-gray-600">${shift.avg_revenue_per_shift.toFixed(2)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
