// src/components/forms/AcceptReservationForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { reservationRepository } from '../../api/reservationRepository';

const acceptReservationSchema = z.object({
    table_number: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(1, 'Table number is required')
    ),
});

type AcceptReservationFormData = z.infer<typeof acceptReservationSchema>;

interface AcceptReservationFormProps {
    reservationId: number;
    onSuccess: () => void;
}

export const AcceptReservationForm = ({ reservationId, onSuccess }: AcceptReservationFormProps) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<AcceptReservationFormData>({
        resolver: zodResolver(acceptReservationSchema),
    });

    const onSubmit = async (data: AcceptReservationFormData) => {
        try {
            await reservationRepository.acceptReservation(reservationId, data.table_number);
            onSuccess();
        } catch (error) {
            console.error('Failed to accept reservation:', error);
            alert('Failed to accept reservation. Please try again.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="mb-4">
                <label htmlFor="table_number" className="block text-sm font-medium text-gray-700">Assign Table Number</label>
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
                    {isSubmitting ? 'Accepting...' : 'Accept Reservation'}
                </button>
            </div>
        </form>
    );
};
