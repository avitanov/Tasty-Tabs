// src/pages/ReservationsPage.tsx
import { useEffect, useState } from 'react';
import type { ReservationDto } from '../types/api';
import { reservationRepository } from '../api/reservationRepository';
import { Modal } from '../components/Modal';
import { AcceptReservationForm } from '../components/forms/AcceptReservationForm';
import { CreateReservationForm } from '../components/forms/CreateReservationForm'; // Import the new form
import { useAuth } from '../hooks/useAuth';
import ReservationCard from '../components/reservations/ReservationCard';

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

    const handleDeleteReservation = async (reservationId: number) => {
        if (window.confirm('Are you sure you want to decline this reservation?')) {
            try {
                await reservationRepository.deleteReservation(reservationId);
                fetchReservations();
            } catch (error) {
                alert('Failed to decline reservation.');
            }
        }
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
                <h1 className="text-3xl font-bold">All Reservations</h1>
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

            <div className="min-h-screen p-4 sm:p-6 lg:p-8">
                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
                    {reservations.map(res => (
                        <ReservationCard
                            key={res.id}
                            reservation={res}
                            user={user}
                            onAccept={handleOpenAcceptModal}
                            onDecline={() => handleDeleteReservation(res.id)}
                        />
                    ))}
                </div>
            </div>

        </div>
    );
};
