// src/components/analytics/StatCard.tsx
import type { ReactNode } from 'react';

interface StatCardProps {
    title: string;
    value: string | number;
    icon: ReactNode;
}

export const StatCard = ({ title, value, icon }: StatCardProps) => (
    <div className="bg-white p-4 rounded-lg shadow-md flex items-center">
        <div className="bg-blue-100 text-blue-600 rounded-full p-3 mr-4">
            {icon}
        </div>
        <div>
            <p className="text-sm text-gray-500">{title}</p>
            <p className="text-2xl font-bold">{value}</p>
        </div>
    </div>
);
