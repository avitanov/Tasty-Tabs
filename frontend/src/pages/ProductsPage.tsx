// src/pages/ProductsPage.tsx
import { useEffect, useState } from 'react';
import type { ProductDto } from '../types/api';
import { productRepository } from '../api/productRepository';
import { Modal } from '../components/Modal';
import { CreateProductForm } from '../components/forms/CreateProductForm';

export const ProductsPage = () => {
    const [products, setProducts] = useState<ProductDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchProducts = () => {
        setLoading(true);
        productRepository.getAllProducts()
            .then(setProducts)
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    const handleFormSuccess = () => {
        setIsModalOpen(false);
        fetchProducts();
    };

    const handleDelete = async (productId: number) => {
        if (window.confirm('Are you sure you want to delete this product?')) {
            try {
                await productRepository.deleteProduct(productId);
                fetchProducts();
            } catch (error) {
                alert('Failed to delete product.');
            }
        }
    };

    if (loading) return <div>Loading products...</div>;

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Products</h1>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Add Product
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create New Product">
                <CreateProductForm onSuccess={handleFormSuccess} />
            </Modal>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full leading-normal">
                    <thead>
                        <tr>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Product Name</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Category</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Price</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {products.map(prod => (
                            <tr key={prod.id}>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{prod.name}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{prod.category.name}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">${prod.price.toFixed(2)}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm text-right">
                                    <button onClick={() => handleDelete(prod.id)} className="text-red-600 hover:text-red-900">Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
