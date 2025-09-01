// src/pages/ShiftsPage.tsx
import { useEffect, useState } from 'react';
import type { ShiftDto } from '../types/api';
import { shiftRepository } from '../api/shiftRepository';
import { Modal } from '../components/Modal';
import { CreateShiftForm } from '../components/forms/CreateShiftForm';

export const ShiftsPage = () => {
    const [shifts, setShifts] = useState<ShiftDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchShifts = () => {
        setLoading(true);
        shiftRepository.getAllShifts()
            .then(setShifts)
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchShifts();
    }, []);

    const handleFormSuccess = () => {
        setIsModalOpen(false);
        fetchShifts();
    };

    if (loading) return <div>Loading shifts...</div>;

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Shifts</h1>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Create Shift
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create New Shift">
                <CreateShiftForm onSuccess={handleFormSuccess} />
            </Modal>

            <div className="bg-white shadow-md rounded-lg p-4">
                <h2 className="text-xl font-semibold mb-2">Scheduled Shifts</h2>
                <ul>
                    {shifts.map(shift => (
                        <li key={shift.id} className="border-b py-2">
                            Shift on <span className="font-semibold">{new Date(shift.date).toLocaleDateString()}</span> from{' '}
                            <span className="font-semibold">{new Date(shift.start).toLocaleTimeString()}</span> to{' '}
                            <span className="font-semibold">{new Date(shift.end).toLocaleTimeString()}</span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};
