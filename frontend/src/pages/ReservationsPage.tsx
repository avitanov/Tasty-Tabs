// src/pages/ReservationsPage.tsx
import { useEffect, useState } from 'react';
import type { ReservationDto } from '../types/api';
import { reservationRepository } from '../api/reservationRepository';
import { Modal } from '../components/Modal';
import { AcceptReservationForm } from '../components/forms/AcceptReservationForm';
import { CreateReservationForm } from '../components/forms/CreateReservationForm'; // Import the new form
import { useAuth } from '../hooks/useAuth';

export const ReservationsPage = () => {
    const { user } = useAuth();
    const [reservations, setReservations] = useState<ReservationDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedReservation, setSelectedReservation] = useState<ReservationDto | null>(null);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false); // State for the creation modal

    const fetchReservations = () => {
        setLoading(true);
        reservationRepository.getTodayReservations()
            .then(data => setReservations(data))
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchReservations();
    }, []);

    const handleOpenAcceptModal = (reservation: ReservationDto) => {
        setSelectedReservation(reservation);
    };

    const handleCloseAcceptModal = () => {
        setSelectedReservation(null);
    };

    const handleFormSuccess = () => {
        // This function can now be used by both forms
        handleCloseAcceptModal();
        setIsCreateModalOpen(false);
        fetchReservations(); // Refresh data
    };

    if (loading) return <div>Loading reservations...</div>;

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Today's Reservations</h1>
                <button
                    onClick={() => setIsCreateModalOpen(true)}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Create Reservation
                </button>
            </div>

            {/* Modal for creating a reservation */}
            <Modal
                isOpen={isCreateModalOpen}
                onClose={() => setIsCreateModalOpen(false)}
                title="Create New Reservation"
            >
                <CreateReservationForm onSuccess={handleFormSuccess} />
            </Modal>

            {/* Modal for accepting a reservation */}
            {selectedReservation && (
                <Modal
                    isOpen={!!selectedReservation}
                    onClose={handleCloseAcceptModal}
                    title={`Accept Reservation for ${selectedReservation.email}`}
                >
                    <AcceptReservationForm
                        reservationId={selectedReservation.id}
                        onSuccess={handleFormSuccess}
                    />
                </Modal>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {reservations.map(res => (
                    <div key={res.id} className="bg-white p-4 rounded-lg shadow">
                        <p className="font-bold">{res.email}</p>
                        <p>Time: {new Date(res.datetime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</p>
                        <p>Guests: {res.number_of_people}</p>
                        {user?.user_type === "FRONT_STAFF" && <div className="mt-4">
                            <button
                                onClick={() => handleOpenAcceptModal(res)}
                                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded text-sm"
                            >
                                Accept
                            </button>
                        </div>}
                    </div>
                ))}
            </div>
        </div>
    );
};
