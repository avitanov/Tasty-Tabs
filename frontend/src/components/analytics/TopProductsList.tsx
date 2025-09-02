// src/components/analytics/TopProductsList.tsx
import { useEffect, useState } from 'react';
import type { TopProductDto } from '../../types/api';
import { analyticsRepository } from '../../api/analyticsRepository';

export const TopProductsList = () => {
    const [products, setProducts] = useState<TopProductDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        analyticsRepository.getTopProducts(90, 5)
            .then(setProducts)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading top products...</p>;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h3 className="text-xl font-semibold mb-4">Top Selling Products (Last 90 Days)</h3>
            <ul className="space-y-3">
                {products.map(product => (
                    <li key={product.product_name} className="flex justify-between items-center border-b pb-2">
                        <div>
                            <p className="font-semibold">{product.product_name}</p>
                            <p className="text-sm text-gray-500">{product.category_name}</p>
                        </div>
                        <div className="text-right">
                            <p className="font-bold">${product.total_revenue.toFixed(2)}</p>
                            <p className="text-sm text-gray-500">{product.total_quantity_sold} sold</p>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};
