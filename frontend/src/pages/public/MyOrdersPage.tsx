// src/pages/public/MyOrdersPage.tsx
import { useEffect, useState } from 'react';
import type { OrderDto } from '../../types/api';
import { orderRepository } from '../../api/orderRepository';
import { useAuth } from '../../hooks/useAuth';

const getStatusColor = (status: string) => {
    switch (status) {
        case 'COMPLETED': return 'bg-green-100 text-green-800';
        case 'CANCELED': return 'bg-red-100 text-red-800';
        case 'IN_PROGRESS': return 'bg-yellow-100 text-yellow-800';
        default: return 'bg-blue-100 text-blue-800';
    }
};

export const MyOrdersPage = () => {
    const { user } = useAuth();
    const [orders, setOrders] = useState<OrderDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (user) {
            setLoading(true);
            orderRepository.getOnlineOrdersByCustomer(user.id)
                .then(setOrders)
                .catch(console.error)
                .finally(() => setLoading(false));
        }
    }, [user]);

    if (loading) return <p className="p-6">Loading your orders...</p>;

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <h1 className="text-3xl font-bold mb-4">My Orders</h1>

            {orders.length === 0 ? (
                <p>You haven't placed any orders yet. Visit our menu to get started!</p>
            ) : (
                <div className="space-y-4">
                    {orders.map(order => {
                        const totalPrice = order.order_items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
                        return (
                            <div key={order.id} className="bg-white p-4 rounded-lg shadow-md">
                                <div className="flex justify-between items-start">
                                    <div>
                                        <p className="font-bold text-lg">Order #{order.id}</p>
                                        <p className="text-sm text-gray-500">
                                            Placed on: {new Date(order.timestamp).toLocaleDateString()}
                                        </p>
                                    </div>
                                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${getStatusColor(order.status)}`}>
                                        {order.status.replace('_', ' ')}
                                    </span>
                                </div>
                                <div className="mt-4 border-t pt-2">
                                    <p><strong>Address:</strong> {order.delivery_address}</p>
                                    <p><strong>Total:</strong> ${totalPrice.toFixed(2)}</p>
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
};
