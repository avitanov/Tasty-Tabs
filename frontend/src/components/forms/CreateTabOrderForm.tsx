// src/components/forms/CreateTabOrderForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { orderRepository } from '../../api/orderRepository';
import type { CreateOrderDto } from '../../types/api';

const createOrderSchema = z.object({
    table_number: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(1, 'Table number is required')
    ),
    // For MVP, we'll create an empty tab. Items can be added later.
});

type CreateOrderFormData = z.infer<typeof createOrderSchema>;

interface CreateTabOrderFormProps {
    onSuccess: () => void;
}

export const CreateTabOrderForm = ({ onSuccess }: CreateTabOrderFormProps) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<CreateOrderFormData>({
        resolver: zodResolver(createOrderSchema),
    });

    const onSubmit = async (data: CreateOrderFormData) => {
        try {
            const newOrderData: CreateOrderDto = {
                table_number: data.table_number,
                type: 'TAB',
                status: 'PENDING',
                order_items: [], // Start with an empty order
            };
            await orderRepository.createTabOrder(newOrderData);
            onSuccess();
        } catch (error) {
            console.error('Failed to create tab order:', error);
            alert('Failed to create tab. Please try again.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="mb-4">
                <label htmlFor="table_number" className="block text-sm font-medium text-gray-700">Table Number</label>
                <input
                    id="table_number"
                    type="number"
                    {...register('table_number')}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                />
                {errors.table_number && <p className="text-red-500 text-xs mt-1">{errors.table_number.message}</p>}
            </div>

            <div className="flex justify-end">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded disabled:bg-gray-400"
                >
                    {isSubmitting ? 'Creating...' : 'Create Tab'}
                </button>
            </div>
        </form>
    );
};
