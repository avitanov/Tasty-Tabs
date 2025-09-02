// src/components/analytics/RevenueByShiftPeriodTable.tsx
import { useEffect, useState } from 'react';
import { analyticsRepository } from '../../api/analyticsRepository';

export const RevenueByShiftPeriodTable = () => {
    const [data, setData] = useState<Record<string, any>[]>([]);
    const [loading, setLoading] = useState(true);
    const [headers, setHeaders] = useState<string[]>([]);

    useEffect(() => {
        analyticsRepository.getRevenueByShiftPeriod()
            .then(rawData => {
                if (rawData.length > 0) {
                    setHeaders(Object.keys(rawData[0]));
                }
                setData(rawData);
            })
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading revenue by shift period...</p>;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h3 className="text-xl font-semibold mb-4">Revenue by Shift Period</h3>
            <div className="overflow-x-auto">
                <table className="min-w-full">
                    <thead>
                        <tr>
                            {headers.map(header => (
                                <th key={header} className="py-2 text-left capitalize">{header.replace(/_/g, ' ')}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {data.map((row, i) => (
                            <tr key={i} className="border-t">
                                {headers.map(header => (
                                    <td key={header} className="py-2">
                                        {typeof row[header] === 'number' ? `$${row[header].toFixed(2)}` : row[header]}
                                    </td>
                                ))}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
};
