// src/components/modals/PaymentModal.tsx
import { useState, useMemo } from 'react';
import type { OrderDto, CreatePaymentDto } from '../../types/api';
import { paymentRepository } from '../../api/paymentRepository';
import { Modal } from '../Modal';

interface PaymentModalProps {
    isOpen: boolean;
    onClose: () => void;
    order: OrderDto;
    onSuccess: () => void;
}

const TIP_OPTIONS = [5, 10, 15];

export const PaymentModal = ({ isOpen, onClose, order, onSuccess }: PaymentModalProps) => {
    const [paymentType, setPaymentType] = useState<'CASH' | 'CARD' | null>(null);
    const [tipOption, setTipOption] = useState<number | 'custom' | null>(null);
    const [customTip, setCustomTip] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');

    const subtotal = useMemo(() =>
        order.order_items.reduce((sum, item) => sum + (item.price * item.quantity), 0),
        [order.order_items]
    );

    const tipAmount = useMemo(() => {
        if (tipOption === 'custom') {
            return parseFloat(customTip) || 0;
        }
        if (tipOption) {
            return subtotal * (tipOption / 100);
        }
        return 0;
    }, [tipOption, customTip, subtotal]);

    const total = subtotal + tipAmount;

    const handleSubmit = async () => {
        setError('');
        if (!paymentType) {
            setError('Please select a payment method.');
            return;
        }

        setIsSubmitting(true);
        const paymentData: CreatePaymentDto = {
            order_id: order.id,
            amount: total,
            payment_type: paymentType,
            tip_amount: tipAmount,
        };

        try {
            await paymentRepository.createPayment(paymentData);
            alert('Payment successful!');
            onSuccess();
        } catch (err) {
            console.error("Payment failed:", err);
            setError('An error occurred during payment. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title={`Payment for Order #${order.id}`}>
            <div className="space-y-4">
                {/* Payment Method */}
                <div>
                    <h4 className="font-semibold mb-2">Payment Method</h4>
                    <div className="flex gap-4">
                        <button onClick={() => setPaymentType('CASH')} className={`w-full p-3 rounded-lg border-2 ${paymentType === 'CASH' ? 'border-blue-500 bg-blue-50' : 'border-gray-300'}`}>Cash</button>
                        <button onClick={() => setPaymentType('CARD')} className={`w-full p-3 rounded-lg border-2 ${paymentType === 'CARD' ? 'border-blue-500 bg-blue-50' : 'border-gray-300'}`}>Card</button>
                    </div>
                </div>

                {/* Tip Selection */}
                <div>
                    <h4 className="font-semibold mb-2">Add a Tip</h4>
                    <div className="flex gap-2">
                        {TIP_OPTIONS.map(opt => (
                            <button key={opt} onClick={() => setTipOption(opt)} className={`flex-1 p-2 rounded border ${tipOption === opt ? 'bg-blue-500 text-white' : ''}`}>{opt}%</button>
                        ))}
                        <button onClick={() => setTipOption('custom')} className={`flex-1 p-2 rounded border ${tipOption === 'custom' ? 'bg-blue-500 text-white' : ''}`}>Custom</button>
                    </div>
                    {tipOption === 'custom' && (
                        <input type="number" placeholder="Enter tip amount" value={customTip} onChange={e => setCustomTip(e.target.value)} className="w-full mt-2 p-2 border rounded" />
                    )}
                </div>

                {/* Summary */}
                <div className="border-t pt-4 space-y-2">
                    <div className="flex justify-between"><span className="text-gray-600">Subtotal</span><span>${subtotal.toFixed(2)}</span></div>
                    <div className="flex justify-between"><span className="text-gray-600">Tip</span><span>${tipAmount.toFixed(2)}</span></div>
                    <div className="flex justify-between font-bold text-lg"><span >Grand Total</span><span>${total.toFixed(2)}</span></div>
                </div>

                {error && <p className="text-red-500 text-sm">{error}</p>}

                {/* Action Button */}
                <button onClick={handleSubmit} disabled={isSubmitting} className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-3 rounded-lg disabled:bg-gray-400">
                    {isSubmitting ? 'Processing...' : 'Confirm Payment'}
                </button>
            </div>
        </Modal>
    );
};
