// src/components/public/CartSidebar.tsx
// src/components/public/CartSidebar.tsx
import type { ProductDto } from '../../types/api';

interface CartItem extends ProductDto {
    quantity: number;
}

interface CartSidebarProps {
    cart: Record<number, CartItem>;
    onUpdateQuantity: (product: ProductDto, change: 1 | -1) => void;
    onCheckout: () => void;
}

export const CartSidebar = ({ cart, onUpdateQuantity, onCheckout }: CartSidebarProps) => {
    const cartItems = Object.values(cart);
    const total = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

    if (cartItems.length === 0) {
        return (
            <div className="bg-white p-4 rounded-lg shadow-lg h-full">
                <h3 className="text-xl font-bold border-b pb-2">Your Order</h3>
                <p className="text-gray-500 mt-4">Your cart is empty. Add items from the menu to get started!</p>
            </div>
        );
    }

    return (
        <div className="bg-white p-4 rounded-lg shadow-lg h-full flex flex-col">
            <h3 className="text-xl font-bold border-b pb-2">Your Order</h3>
            <div className="flex-grow my-4 space-y-3 overflow-y-auto">
                {cartItems.map(item => (
                    <div key={item.id} className="flex justify-between items-center">
                        <div>
                            <p className="font-semibold">{item.name}</p>
                            <p className="text-sm text-gray-600">${item.price.toFixed(2)}</p>
                        </div>
                        <div className="flex items-center space-x-2">
                            <button onClick={() => onUpdateQuantity(item, -1)} className="font-bold">-</button>
                            <span>{item.quantity}</span>
                            <button onClick={() => onUpdateQuantity(item, 1)} className="font-bold">+</button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="border-t pt-4">
                <div className="flex justify-between font-bold text-lg mb-4">
                    <span>Total</span>
                    <span>${total.toFixed(2)}</span>
                </div>
                <button onClick={onCheckout} className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-3 rounded-lg">
                    Checkout
                </button>
            </div>
        </div>
    );
};
