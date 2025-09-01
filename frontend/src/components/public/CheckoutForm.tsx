// src/components/forms/CheckoutForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const checkoutSchema = z.object({
    delivery_address: z.string().min(10, 'Please enter a valid address'),
});

type CheckoutFormData = z.infer<typeof checkoutSchema>;

interface CheckoutFormProps {
    onSubmit: (data: CheckoutFormData) => void;
    isSubmitting: boolean;
}

export const CheckoutForm = ({ onSubmit, isSubmitting }: CheckoutFormProps) => {
    const { register, handleSubmit, formState: { errors } } = useForm<CheckoutFormData>({
        resolver: zodResolver(checkoutSchema),
    });

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <p className="mb-4">Please provide your delivery address to complete the order.</p>
            <div className="mb-4">
                <label>Delivery Address</label>
                <textarea {...register('delivery_address')} rows={3} className="w-full p-2 border rounded" />
                {errors.delivery_address && <p className="text-red-500 text-xs">{errors.delivery_address.message}</p>}
            </div>
            <div className="flex justify-end">
                <button type="submit" disabled={isSubmitting} className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded">
                    {isSubmitting ? 'Placing Order...' : 'Confirm Order'}
                </button>
            </div>
        </form>
    );
};
