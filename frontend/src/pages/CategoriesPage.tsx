// src/pages/CategoriesPage.tsx
import { useEffect, useState } from 'react';
import type { CategoryDto } from '../types/api';
import { categoryRepository } from '../api/categoryRepository';
import { Modal } from '../components/Modal';
import { CreateCategoryForm } from '../components/forms/CreateCategoryForm';

export const CategoriesPage = () => {
    const [categories, setCategories] = useState<CategoryDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchCategories = () => {
        setLoading(true);
        categoryRepository.getAllCategories()
            .then(setCategories)
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleFormSuccess = () => {
        setIsModalOpen(false);
        fetchCategories();
    };

    const handleDelete = async (categoryId: number) => {
        if (window.confirm('Are you sure you want to delete this category? This may affect existing products.')) {
            try {
                await categoryRepository.deleteCategory(categoryId);
                alert('Category deleted successfully!');
                fetchCategories();
            } catch (error) {
                console.error('Failed to delete category:', error);
                alert('Failed to delete category.');
            }
        }
    };

    if (loading) return <div>Loading categories...</div>;

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Categories</h1>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Add Category
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create New Category">
                <CreateCategoryForm onSuccess={handleFormSuccess} />
            </Modal>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full leading-normal">
                    <thead>
                        <tr>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Name</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Status</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {categories.map(cat => (
                            <tr key={cat.id}>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm font-semibold">{cat.name}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                                    {cat.is_available ?
                                        <span className="text-green-600">Available</span> :
                                        <span className="text-red-600">Unavailable</span>
                                    }
                                </td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm text-right">
                                    <button onClick={() => handleDelete(cat.id)} className="text-red-600 hover:text-red-900">Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
