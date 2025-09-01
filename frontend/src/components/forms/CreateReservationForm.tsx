// src/components/forms/CreateReservationForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { reservationRepository } from '../../api/reservationRepository';
import type { CreateReservationDto } from '../../types/api';

const createReservationSchema = z.object({
    number_of_people: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(1, 'At least one person is required')
    ),
    stay_length: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(30, 'Minimum stay is 30 minutes')
    ),
    date: z.string().min(1, 'Date is required'),
    time: z.string().min(1, 'Time is required'),
});

type CreateReservationFormData = z.infer<typeof createReservationSchema>;

interface CreateReservationFormProps {
    onSuccess: () => void;
}

export const CreateReservationForm = ({ onSuccess }: CreateReservationFormProps) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<CreateReservationFormData>({
        resolver: zodResolver(createReservationSchema),
    });

    const onSubmit = async (data: CreateReservationFormData) => {
        try {
            const reservationData: CreateReservationDto = {
                number_of_people: data.number_of_people,
                stay_length: data.stay_length,
                datetime: `${data.date}T${data.time}:00`,
            };
            await reservationRepository.createReservation(reservationData);
            onSuccess();
        } catch (error) {
            console.error('Failed to create reservation:', error);
            alert('Failed to create reservation. Please try again.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
                <label>Number of People</label>
                <input type="number" {...register('number_of_people')} className="w-full p-2 border rounded" />
                {errors.number_of_people && <p className="text-red-500 text-xs">{errors.number_of_people.message}</p>}
            </div>
            <div>
                <label>Stay Length (in minutes)</label>
                <input type="number" step="15" {...register('stay_length')} className="w-full p-2 border rounded" />
                {errors.stay_length && <p className="text-red-500 text-xs">{errors.stay_length.message}</p>}
            </div>
            <div>
                <label>Date</label>
                <input type="date" {...register('date')} className="w-full p-2 border rounded" />
                {errors.date && <p className="text-red-500 text-xs">{errors.date.message}</p>}
            </div>
            <div>
                <label>Time</label>
                <input type="time" {...register('time')} className="w-full p-2 border rounded" />
                {errors.time && <p className="text-red-500 text-xs">{errors.time.message}</p>}
            </div>
            <div className="flex justify-end">
                <button type="submit" disabled={isSubmitting} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    {isSubmitting ? 'Creating...' : 'Create Reservation'}
                </button>
            </div>
        </form>
    );
};
