// src/pages/public/MenuPage.tsx
import { useEffect, useState } from 'react';
import type { CategoryDto, ProductDto, CreateOrderDto, CreateOrderItemDto } from '../../types/api';
import { categoryRepository } from '../../api/categoryRepository';
import { productRepository } from '../../api/productRepository';
import { orderRepository } from '../../api/orderRepository';
import { CartSidebar } from '../../components/public/CartSidebar';
import { Modal } from '../../components/Modal';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import { CheckoutForm } from '../../components/public/CheckoutForm';

interface CartItem extends ProductDto {
    quantity: number;
}

export const MenuPage = () => {
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();
    const [categories, setCategories] = useState<CategoryDto[]>([]);
    const [products, setProducts] = useState<ProductDto[]>([]);
    const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(null);
    const [cart, setCart] = useState<Record<number, CartItem>>({});
    const [isCheckoutModalOpen, setIsCheckoutModalOpen] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        Promise.all([
            categoryRepository.getAllCategories(),
            productRepository.getAllProducts(),
        ]).then(([catData, prodData]) => {
            const availableCats = catData.filter(c => c.is_available);
            setCategories(availableCats);
            setProducts(prodData);
            if (availableCats.length > 0) {
                setSelectedCategoryId(availableCats[0].id);
            }
        }).catch(console.error);
    }, []);

    const updateCart = (product: ProductDto, change: 1 | -1) => {
        setCart(prevCart => {
            const existingItem = prevCart[product.id];
            const newQuantity = (existingItem?.quantity || 0) + change;
            if (newQuantity <= 0) {
                const { [product.id]: _, ...rest } = prevCart;
                return rest;
            }
            return { ...prevCart, [product.id]: { ...product, quantity: newQuantity } };
        });
    };

    const handleCheckout = () => {
        if (!isAuthenticated) {
            alert("Please log in or register to place an order.");
            navigate('/login');
            return;
        }
        setIsCheckoutModalOpen(true);
    };

    const handleConfirmOrder = async (data: { delivery_address: string }) => {
        setIsSubmitting(true);
        const orderItems: CreateOrderItemDto[] = Object.values(cart).map(item => ({
            product_id: item.id,
            quantity: item.quantity,
            price: item.price,
            is_processed: false,
        }));

        const orderData: CreateOrderDto = {
            order_items: orderItems,
            status: "PENDING",
            type: "ONLINE",
            delivery_address: data.delivery_address,
        };

        try {
            await orderRepository.createOnlineOrder(orderData);
            alert("Order placed successfully!");
            setCart({});
            setIsCheckoutModalOpen(false);
        } catch (error) {
            alert("Failed to place order.");

            console.error(error);
        } finally {
            setIsSubmitting(false);
        }
    };

    const filteredProducts = products.filter(p => p.category.id === selectedCategoryId);

    return (
        <div className="flex flex-col md:flex-row p-4 gap-4 bg-gray-50 h-[calc(100vh-68px)]">
            {/* Menu */}
            <div className="flex-grow">
                <div className="flex space-x-2 overflow-x-auto pb-2">
                    {categories.map(cat => (
                        <button key={cat.id} onClick={() => setSelectedCategoryId(cat.id)}
                            className={`px-4 py-2 rounded-full font-semibold whitespace-nowrap ${selectedCategoryId === cat.id ? 'bg-blue-500 text-white' : 'bg-white'}`}>
                            {cat.name}
                        </button>
                    ))}
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
                    {filteredProducts.map(prod => (
                        <div key={prod.id} className="bg-white p-4 rounded-lg shadow">
                            <h4 className="font-bold">{prod.name}</h4>
                            <p className="text-sm text-gray-500">{prod.description}</p>
                            <div className="flex justify-between items-center mt-2">
                                <span className="font-semibold">${prod.price.toFixed(2)}</span>
                                <button onClick={() => updateCart(prod, 1)} className="bg-blue-100 text-blue-800 font-bold py-1 px-3 rounded">Add</button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            {/* Cart */}
            <div className="w-full md:w-80 lg:w-96 flex-shrink-0">
                <CartSidebar cart={cart} onUpdateQuantity={updateCart} onCheckout={handleCheckout} />
            </div>

            <Modal isOpen={isCheckoutModalOpen} onClose={() => setIsCheckoutModalOpen(false)} title="Complete Your Order">
                <CheckoutForm onSubmit={handleConfirmOrder} isSubmitting={isSubmitting} />
            </Modal>
        </div>
    );
};
