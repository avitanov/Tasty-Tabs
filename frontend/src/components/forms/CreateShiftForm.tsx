// src/components/forms/CreateShiftForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { shiftRepository } from '../../api/shiftRepository';
import type { CreateShiftDto } from '../../types/api';

const createShiftSchema = z.object({
    date: z.string().min(1, 'Date is required'),
    startTime: z.string().min(1, 'Start time is required'),
    endTime: z.string().min(1, 'End time is required'),
});

type CreateShiftFormData = z.infer<typeof createShiftSchema>;

interface CreateShiftFormProps {
    onSuccess: () => void;
}

export const CreateShiftForm = ({ onSuccess }: CreateShiftFormProps) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<CreateShiftFormData>({
        resolver: zodResolver(createShiftSchema),
    });

    const onSubmit = async (data: CreateShiftFormData) => {
        try {
            const shiftData: CreateShiftDto = {
                date: data.date,
                start: `${data.date}T${data.startTime}:00`,
                end: `${data.date}T${data.endTime}:00`,
            };
            await shiftRepository.createShift(shiftData);
            onSuccess();
        } catch (error) {
            console.error('Failed to create shift:', error);
            alert('Failed to create shift. Please try again.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
                <label>Date</label>
                <input type="date" {...register('date')} className="w-full p-2 border rounded" />
                {errors.date && <p className="text-red-500 text-xs">{errors.date.message}</p>}
            </div>
            <div>
                <label>Start Time</label>
                <input type="time" {...register('startTime')} className="w-full p-2 border rounded" />
                {errors.startTime && <p className="text-red-500 text-xs">{errors.startTime.message}</p>}
            </div>
            <div>
                <label>End Time</label>
                <input type="time" {...register('endTime')} className="w-full p-2 border rounded" />
                {errors.endTime && <p className="text-red-500 text-xs">{errors.endTime.message}</p>}
            </div>
            <div className="flex justify-end">
                <button type="submit" disabled={isSubmitting} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    {isSubmitting ? 'Creating...' : 'Create Shift'}
                </button>
            </div>
        </form>
    );
};
