// src/components/modals/AddItemModal.tsx
import { useEffect, useState } from 'react';
import type { CategoryDto, ProductDto, CreateOrderItemDto } from '../types/api';
import { categoryRepository } from '../api/categoryRepository';
import { productRepository } from '../api/productRepository';
import { orderRepository } from '../api/orderRepository';
import { Modal } from './Modal';

interface CartItem extends ProductDto {
    quantity: number;
}

interface AddItemModalProps {
    isOpen: boolean;
    onClose: () => void;
    orderId: number;
    onSuccess: () => void; // To refresh the order details page
}

export const AddItemModal = ({ isOpen, onClose, orderId, onSuccess }: AddItemModalProps) => {
    const [view, setView] = useState<'categories' | 'products'>('categories');
    const [categories, setCategories] = useState<CategoryDto[]>([]);
    const [products, setProducts] = useState<ProductDto[]>([]);
    const [selectedCategory, setSelectedCategory] = useState<CategoryDto | null>(null);
    const [cart, setCart] = useState<Record<number, CartItem>>({});
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        if (isOpen) {
            // Reset state when modal opens
            setView('categories');
            setCart({});
            categoryRepository.getAllCategories().then(setCategories).catch(console.error);
        }
    }, [isOpen]);

    const handleCategorySelect = (category: CategoryDto) => {
        setSelectedCategory(category);
        // You might need a productRepository.getProductsByCategoryId(category.id) method here.
        // For now, we'll filter the full product list.
        productRepository.getAllProducts().then(allProducts => {
            const filteredProducts = allProducts.filter(p => p.category.id === category.id);
            setProducts(filteredProducts);
            setView('products');
        }).catch(console.error);
    };

    const updateCart = (product: ProductDto, change: 1 | -1) => {
        setCart(prevCart => {
            const existingItem = prevCart[product.id];
            const newQuantity = (existingItem?.quantity || 0) + change;

            if (newQuantity <= 0) {
                const { [product.id]: _, ...rest } = prevCart;
                return rest;
            }

            return {
                ...prevCart,
                [product.id]: { ...product, quantity: newQuantity },
            };
        });
    };

    const handleBackToCategories = () => {
        setView('categories');
        setSelectedCategory(null);
    };

    const handleSubmitItems = async () => {
        setIsSubmitting(true);
        try {
            const itemsToAdd: CreateOrderItemDto[] = Object.values(cart).map(item => ({
                product_id: item.id,
                quantity: item.quantity,
                price: item.price, // Price might be determined by the backend
                is_processed: false,
            }));

            // The API adds one item at a time, so we must loop.
            // A batch endpoint would be more efficient here.
            for (const item of itemsToAdd) {
                await orderRepository.addItemToOrder(orderId, item);
            }

            onSuccess();
            onClose();
        } catch (error) {
            console.error("Failed to add items:", error);
            alert("An error occurred while adding items.");
        } finally {
            setIsSubmitting(false);
        }
    };

    const cartTotal = Object.values(cart).reduce((sum, item) => sum + item.price * item.quantity, 0);

    return (
        <Modal isOpen={isOpen} onClose={onClose} title={view === 'categories' ? 'Select a Category' : `Products in ${selectedCategory?.name}`}>
            <div className="min-h-[400px]">
                {view === 'products' && (
                    <button onClick={handleBackToCategories} className="mb-4 text-sm text-blue-600 hover:underline">&larr; Back to Categories</button>
                )}

                {view === 'categories' && (
                    <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
                        {categories.filter(c => c.is_available).map(cat => (
                            <button key={cat.id} onClick={() => handleCategorySelect(cat)} className="p-4 bg-gray-100 hover:bg-blue-100 rounded-lg text-center font-semibold">
                                {cat.name}
                            </button>
                        ))}
                    </div>
                )}

                {view === 'products' && (
                    <div className="space-y-3">
                        {products.map(prod => (
                            <div key={prod.id} className="flex justify-between items-center p-2 border rounded-lg">
                                <div>
                                    <p className="font-semibold">{prod.name}</p>
                                    <p className="text-sm text-gray-600">${prod.price.toFixed(2)}</p>
                                </div>
                                <div className="flex items-center space-x-3">
                                    <button onClick={() => updateCart(prod, -1)} className="bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center font-bold">-</button>
                                    <span>{cart[prod.id]?.quantity || 0}</span>
                                    <button onClick={() => updateCart(prod, 1)} className="bg-green-500 text-white rounded-full w-6 h-6 flex items-center justify-center font-bold">+</button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {Object.keys(cart).length > 0 && (
                <div className="mt-4 pt-4 border-t flex justify-between items-center">
                    <p className="font-semibold">Total: ${cartTotal.toFixed(2)}</p>
                    <button onClick={handleSubmitItems} disabled={isSubmitting} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                        {isSubmitting ? 'Adding...' : `Add ${Object.values(cart).reduce((sum, item) => sum + item.quantity, 0)} Items`}
                    </button>
                </div>
            )}
        </Modal>
    );
};
