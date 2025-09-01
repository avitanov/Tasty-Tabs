// src/components/forms/CreateCategoryForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { categoryRepository } from '../../api/categoryRepository';

const createCategorySchema = z.object({
    name: z.string().min(2, 'Name must be at least 2 characters'),
    is_available: z.boolean(),
});

type CreateCategoryFormData = z.infer<typeof createCategorySchema>;

interface CreateCategoryFormProps {
    onSuccess: () => void;
}

export const CreateCategoryForm = ({ onSuccess }: CreateCategoryFormProps) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<CreateCategoryFormData>({
        resolver: zodResolver(createCategorySchema),
        defaultValues: { is_available: true },
    });

    const onSubmit = async (data: CreateCategoryFormData) => {
        try {
            await categoryRepository.createCategory(data);
            onSuccess();
        } catch (error) {
            console.error('Failed to create category:', error);
            alert('Failed to create category.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
                <label>Category Name</label>
                <input {...register('name')} className="w-full p-2 border rounded" />
                {errors.name && <p className="text-red-500 text-xs">{errors.name.message}</p>}
            </div>
            <div className="flex items-center">
                <input type="checkbox" {...register('is_available')} className="h-4 w-4 rounded" />
                <label className="ml-2">Is Available</label>
            </div>
            <div className="flex justify-end">
                <button type="submit" disabled={isSubmitting} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    {isSubmitting ? 'Creating...' : 'Create Category'}
                </button>
            </div>
        </form>
    );
};
