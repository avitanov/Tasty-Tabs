// src/components/forms/UpdateTableNumberForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type { OrderDto } from '../../types/api';
import { orderRepository } from '../../api/orderRepository';

const updateTableSchema = z.object({
    table_number: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(1, 'Invalid table number')
    ),
});

type UpdateTableFormData = z.infer<typeof updateTableSchema>;

interface UpdateTableNumberFormProps {
    order: OrderDto;
    onSuccess: () => void;
}

export const UpdateTableNumberForm = ({ order, onSuccess }: UpdateTableNumberFormProps) => {
    const { register, handleSubmit, formState: { isSubmitting } } = useForm<UpdateTableFormData>({
        resolver: zodResolver(updateTableSchema),
        defaultValues: { table_number: order.table_number },
    });

    const onSubmit = async (data: UpdateTableFormData) => {
        try {
            const updatedOrderData = { ...order, table_number: data.table_number };
            await orderRepository.updateOrder(updatedOrderData);
            alert('Table number updated successfully!');
            onSuccess();
        } catch (error) {
            console.error('Failed to update table number:', error);
            alert('Failed to update table number.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="flex items-center space-x-2">
            <input
                type="number"
                {...register('table_number')}
                className="p-1 border rounded w-20"
            />
            <button type="submit" disabled={isSubmitting} className="bg-gray-200 hover:bg-gray-300 text-black text-xs font-bold py-1 px-2 rounded">
                {isSubmitting ? '...' : 'Save'}
            </button>
        </form>
    );
};
