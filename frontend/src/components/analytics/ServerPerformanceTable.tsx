// src/components/analytics/ServerPerformanceTable.tsx
import { useEffect, useState } from 'react';
import type { ServerPerformanceDto } from '../../types/api';
import { analyticsRepository } from '../../api/analyticsRepository';

export const ServerPerformanceTable = () => {
    const [servers, setServers] = useState<ServerPerformanceDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        analyticsRepository.getServerPerformance()
            .then(setServers)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading server performance...</p>;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h3 className="text-xl font-semibold mb-4">Server Performance</h3>
            <div className="overflow-x-auto">
                <table className="min-w-full leading-normal">
                    <thead>
                        <tr>
                            <th className="px-5 py-3 border-b-2 text-left text-xs font-semibold uppercase">Server</th>
                            <th className="px-5 py-3 border-b-2 text-left text-xs font-semibold uppercase">Orders Processed</th>
                            <th className="px-5 py-3 border-b-2 text-left text-xs font-semibold uppercase">Avg. Order Value</th>
                            <th className="px-5 py-3 border-b-2 text-left text-xs font-semibold uppercase">Total Revenue</th>
                        </tr>
                    </thead>
                    <tbody>
                        {servers.map(server => (
                            <tr key={server.server_email}>
                                <td className="px-5 py-4 border-b text-sm">{server.server_email}</td>
                                <td className="px-5 py-4 border-b text-sm">{server.orders_processed}</td>
                                <td className="px-5 py-4 border-b text-sm">${server.avg_order_value.toFixed(2)}</td>
                                <td className="px-5 py-4 border-b text-sm font-bold">${server.total_revenue_generated.toFixed(2)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
