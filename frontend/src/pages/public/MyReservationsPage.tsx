// src/pages/public/MyReservationsPage.tsx
import { useEffect, useState } from 'react';
import type { ReservationDto } from '../../types/api';
import { reservationRepository } from '../../api/reservationRepository';
import { Modal } from '../../components/Modal';
import { CreateReservationForm } from '../../components/forms/CreateReservationForm';

export const MyReservationsPage = () => {
    const [reservations, setReservations] = useState<ReservationDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchReservations = () => {
        setLoading(true);
        reservationRepository.getMyReservations()
            .then(setReservations)
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchReservations();
    }, []);

    const handleCancel = async (id: number) => {
        if (window.confirm("Are you sure you want to cancel this reservation?")) {
            try {
                await reservationRepository.deleteReservation(id);
                alert("Reservation canceled.");
                fetchReservations();
            } catch (error) {
                alert("Failed to cancel reservation.");
            }
        }
    };

    const handleFormSuccess = () => {
        setIsModalOpen(false);
        fetchReservations();
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">My Reservations</h1>
                <button onClick={() => setIsModalOpen(true)} className="bg-green-500 text-white font-bold py-2 px-4 rounded">
                    + Make a Reservation
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Make a New Reservation">
                <CreateReservationForm onSuccess={handleFormSuccess} />
            </Modal>

            {loading ? <p>Loading...</p> : reservations.length === 0 ? (
                <p>You have no upcoming reservations.</p>
            ) : (
                <div className="space-y-4">
                    {reservations.map(res => (
                        <div key={res.id} className="bg-white p-4 rounded-lg shadow-md flex justify-between items-center">
                            <div>
                                <p><strong>Date:</strong> {new Date(res.datetime).toLocaleDateString()}</p>
                                <p><strong>Time:</strong> {new Date(res.datetime).toLocaleTimeString()}</p>
                                <p><strong>Guests:</strong> {res.number_of_people}</p>
                            </div>
                            <button onClick={() => handleCancel(res.id)} className="bg-red-500 text-white font-bold py-1 px-3 rounded">
                                Cancel
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};
