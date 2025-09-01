// src/pages/OrdersPage.tsx
import { useEffect, useState } from 'react';
import type { OrderDto } from '../types/api';
import { orderRepository } from '../api/orderRepository';
import { Modal } from '../components/Modal';
import { CreateTabOrderForm } from '../components/forms/CreateTabOrderForm';
import { Link } from 'react-router-dom';

export const OrdersPage = () => {
    const [openOrders, setOpenOrders] = useState<OrderDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchOrders = async () => {
        try {
            setLoading(true);
            const orders = await orderRepository.getOpenOrders();
            setOpenOrders(orders);
        } catch (err) {
            setError('Failed to fetch open orders.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    const handleFormSuccess = () => {
        setIsModalOpen(false);
        fetchOrders(); // Refresh the list after creating a new order
    };

    if (loading) return <div>Loading orders...</div>;
    if (error) return <div className="text-red-500">{error}</div>;

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Open Orders</h1>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Create Tab
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create New Tab">
                <CreateTabOrderForm onSuccess={handleFormSuccess} />
            </Modal>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full leading-normal">
                    <thead>
                        <tr>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Order ID</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Type / Table</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Status</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Timestamp</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {openOrders.map(order => (
                            <tr key={order.id}>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{order.id}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                                    {order.type === 'TAB' ? `Tab - Table ${order.table_number}` : 'Online'}
                                </td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                                    <span className="relative inline-block px-3 py-1 font-semibold text-green-900 leading-tight">
                                        <span aria-hidden className="absolute inset-0 bg-green-200 opacity-50 rounded-full"></span>
                                        <span className="relative">{order.status}</span>
                                    </span>
                                </td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{new Date(order.timestamp).toLocaleString()}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm text-right">
                                    {/* Update this button to a Link */}
                                    <Link to={`/orders/${order.id}`} className="text-indigo-600 hover:text-indigo-900 font-semibold">
                                        View Details
                                    </Link>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
