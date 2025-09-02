// src/pages/OrderDetailsPage.tsx
import { useCallback, useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import type { OrderDto } from '../types/api';
import { orderRepository } from '../api/orderRepository';
import { AddItemModal } from '../components/AddItemModal';
import { UpdateOrderStatusActions } from '../components/order/UpdateOrderStatusActions';
import { PaymentModal } from '../components/modals/PaymentModal';

export const OrderDetailsPage = () => {
    const { orderId } = useParams<{ orderId: string }>();
    const [order, setOrder] = useState<OrderDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [isAddItemModalOpen, setIsAddItemModalOpen] = useState(false);
    const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false); // New state for payment modal

    const fetchOrderDetails = useCallback(() => {
        if (orderId) {
            setLoading(true);
            orderRepository.getOrderById(parseInt(orderId, 10))
                .then(setOrder)
                .catch(console.error)
                .finally(() => setLoading(false));
        }
    }, [orderId]);

    useEffect(() => {
        fetchOrderDetails();
    }, [orderId, fetchOrderDetails]);

    const handleSuccess = () => {
        setIsAddItemModalOpen(false);
        setIsPaymentModalOpen(false); // Close payment modal on success too
        fetchOrderDetails(); // Generic success handler for all updates
    };

    if (loading) return <div>Loading order details...</div>;
    if (!order) return <div>Order not found.</div>;

    const totalPrice = order.order_items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const isPayable = order.status !== 'COMPLETED' && order.status !== 'CANCELED' && totalPrice > 0 && order.status !== 'PAID';

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'COMPLETED': return 'bg-green-100 text-green-800';
            case 'CANCELED': return 'bg-red-100 text-red-800';
            case 'CONFIRMED': return 'bg-yellow-100 text-yellow-800';
            case 'PAID': return 'bg-purple-100 text-purple-800';
            default: return 'bg-blue-100 text-blue-800';
        }
    }

    return (
        <div className="p-6">
            <Link to="/admin/orders" className="text-blue-600 hover:underline mb-4 block">&larr; Back to All Orders</Link>

            {/* Modals */}
            {orderId && <AddItemModal isOpen={isAddItemModalOpen} onClose={() => setIsAddItemModalOpen(false)} orderId={parseInt(orderId, 10)} onSuccess={handleSuccess} />}
            {order && <PaymentModal isOpen={isPaymentModalOpen} onClose={() => setIsPaymentModalOpen(false)} order={order} onSuccess={handleSuccess} />}

            <div className="flex justify-between items-start mb-6">
                <div>
                    <h1 className="text-3xl font-bold">Order #{order.id}</h1>
                    <p className="text-gray-500">Placed on: {new Date(order.timestamp).toLocaleString()}</p>
                </div>
                <div className="flex gap-4">
                    <button onClick={() => setIsAddItemModalOpen(true)} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                        + Add Items
                    </button>
                    {isPayable && (
                        <button onClick={() => setIsPaymentModalOpen(true)} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                            Pay
                        </button>
                    )}
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* Order Items */}
                <div className="md:col-span-2 bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4 border-b pb-2">Items</h2>
                    <div className="space-y-3">
                        {order.order_items.map(item => (
                            <div key={item.id} className="flex justify-between items-center">
                                <div>
                                    <p className="font-semibold">{item.product_name} <span className="text-gray-500 font-normal">x {item.quantity}</span></p>
                                    <p className="text-sm text-gray-600">@ ${item.price.toFixed(2)} each</p>
                                </div>
                                <p className="font-bold">${(item.price * item.quantity).toFixed(2)}</p>
                            </div>
                        ))}
                    </div>
                    <div className="mt-6 pt-4 border-t flex justify-end">
                        <p className="text-lg font-bold">Total: ${totalPrice.toFixed(2)}</p>
                    </div>
                </div>

                {/* Order Details */}
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4 border-b pb-2">Details</h2>
                    <div className="space-y-3">
                        <p><strong>Status:</strong> <span className={`px-2 py-1 rounded-full text-sm font-semibold ${getStatusColor(order.status)}`}>{order.status.replace('_', ' ')}</span></p>
                        <p><strong>Type:</strong> {order.type}</p>
                        {order.type === 'TAB' && <p><strong>Table:</strong> {order.table_number}</p>}
                        {order.front_staff_name && <p><strong>Staff:</strong> {order.front_staff_name}</p>}
                        {order.type === 'ONLINE' && (
                            <>
                                <p><strong>Customer:</strong> {order.customer_name}</p>
                                <p><strong>Address:</strong> {order.delivery_address}</p>
                            </>
                        )}
                    </div>
                </div>

                <UpdateOrderStatusActions order={order} onSuccess={handleSuccess} />

            </div>
        </div>
    );
};
