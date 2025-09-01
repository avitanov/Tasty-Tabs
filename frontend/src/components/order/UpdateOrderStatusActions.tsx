// src/components/order/UpdateOrderStatusActions.tsx
import { useState } from 'react';
import type { OrderDto } from '../../types/api';
import { orderRepository } from '../../api/orderRepository';

interface UpdateOrderStatusActionsProps {
    order: OrderDto;
    onSuccess: () => void;
}

const availableStatuses = ["PENDING", "CONFIRMED", "COMPLETED", "CANCELED"];

export const UpdateOrderStatusActions = ({ order, onSuccess }: UpdateOrderStatusActionsProps) => {
    const [loadingStatus, setLoadingStatus] = useState<string | null>(null);

    const handleChangeStatus = async (newStatus: string) => {
        setLoadingStatus(newStatus);
        try {
            await orderRepository.updateOrderStatus(order.id, newStatus);
            onSuccess();
        } catch (error) {
            console.error(`Failed to update status to ${newStatus}:`, error);
            alert('Failed to update status.');
        } finally {
            setLoadingStatus(null);
        }
    };

    // Don't show actions for completed or canceled orders
    if (order.status === 'COMPLETED' || order.status === 'CANCELED') {
        return null;
    }

    return (
        <div className="mt-4 pt-4">
            <h3 className="text-md font-semibold mb-2">Actions</h3>
            <div className="flex flex-wrap gap-2">
                {availableStatuses.map(status => {
                    // Don't show a button for the current status
                    if (status === order.status) return null;

                    return (
                        <button
                            key={status}
                            onClick={() => handleChangeStatus(status)}
                            disabled={!!loadingStatus}
                            className={`text-white font-bold py-1 px-3 rounded text-sm disabled:bg-gray-400
                ${status === 'COMPLETED' ? 'bg-green-500 hover:bg-green-700' : ''}
                ${status === 'CANCELED' ? 'bg-red-500 hover:bg-red-700' : ''}
                ${status === 'CONFIRMED' ? 'bg-yellow-500 hover:bg-yellow-600' : ''}
                ${status === 'PENDING' ? 'bg-blue-500 hover:bg-blue-700' : ''}
              `}
                        >
                            {loadingStatus === status ? 'Updating...' : `Mark as ${status.replace('_', ' ')}`}
                        </button>
                    )
                })}
            </div>
        </div>
    );
};
