// src/components/forms/CreateProductForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { productRepository } from '../../api/productRepository';
import { useEffect, useState } from 'react';
import type { CategoryDto } from '../../types/api';
import { categoryRepository } from '../../api/categoryRepository';

const createProductSchema = z.object({
    name: z.string().min(2, 'Name must be at least 2 characters'),
    price: z.preprocess(
        (a) => parseFloat(z.string().parse(a)),
        z.number().positive('Price must be positive')
    ),
    // description: z.string().min(10, 'Description must be at least 10 characters'),
    category_id: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(1, 'Please select a category')
    ),
    tax_class: z.string().default('A'),
    manage_inventory: z.boolean().default(false),
    description: z.string().default(''),
});

type CreateProductFormData = z.infer<typeof createProductSchema>;

interface CreateProductFormProps {
    onSuccess: () => void;
}

export const CreateProductForm = ({ onSuccess }: CreateProductFormProps) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<CreateProductFormData>({
        resolver: zodResolver(createProductSchema),
    });

    const [categories, setCategories] = useState<CategoryDto[]>([]);

    useEffect(() => {
        categoryRepository.getAllCategories().then(setCategories).catch(console.error);
    }, []);

    const onSubmit = async (data: CreateProductFormData) => {
        try {
            await productRepository.createProduct(data);
            onSuccess();
        } catch (error) {
            console.error('Failed to create product:', error);
            alert('Failed to create product.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
                <label>Product Name</label>
                <input {...register('name')} className="w-full p-2 border rounded" />
                {errors.name && <p className="text-red-500 text-xs">{errors.name.message}</p>}
            </div>
            <div>
                <label>Price ($)</label>
                <input type="number" step="0.01" {...register('price')} className="w-full p-2 border rounded" />
                {errors.price && <p className="text-red-500 text-xs">{errors.price.message}</p>}
            </div>
            <div>
                <label>Category</label>
                <select {...register('category_id')} className="w-full p-2 border rounded">
                    <option value="">Select a category</option>
                    {categories.filter(c => c.is_available).map(cat => (
                        <option key={cat.id} value={cat.id}>{cat.name}</option>
                    ))}
                </select>
                {errors.category_id && <p className="text-red-500 text-xs">{errors.category_id.message}</p>}
            </div>
            <div>
                <label>Tax Class</label>
                <select {...register('tax_class')} className="w-full p-2 border rounded">
                    <option value="A">A - 10%</option>
                    <option value="B">B - 5%</option>
                    <option value="C">C - 0%</option>
                    <option value="D">D - 18%</option>
                </select>
                {errors.category_id && <p className="text-red-500 text-xs">{errors.category_id.message}</p>}
            </div>

            <div>
                <label>Product Description</label>
                <textarea {...register('description')} className="w-full p-2 border rounded" />
                {errors.description && <p className="text-red-500 text-xs">{errors.description.message}</p>}
            </div>

            <div>
                <input type="checkbox" {...register('manage_inventory')} className="mr-2" />
                <label>Manage Inventory</label>
            </div>
            <div className="flex justify-end">
                <button type="submit" disabled={isSubmitting} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    {isSubmitting ? 'Creating...' : 'Create Product'}
                </button>
            </div>
        </form>
    );
};
