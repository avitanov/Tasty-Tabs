// src/components/employee/CurrentShiftCard.tsx
import { useEffect, useState } from 'react';
import type { AssignmentDto } from '../../types/api';
import { assignmentRepository } from '../../api/assignmentRepository';
import { useAuth } from '../../hooks/useAuth';

export const CurrentShiftCard = () => {
    const { user } = useAuth();
    const [assignment, setAssignment] = useState<AssignmentDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [isActionLoading, setIsActionLoading] = useState(false);

    useEffect(() => {
        if (user) {
            assignmentRepository.getNextShift(user.id)
                .then(setAssignment)
                .catch(() => setError('No upcoming shift found.'))
                .finally(() => setLoading(false));
        }
    }, [user]);

    const handleClockIn = async () => {
        if (!assignment) return;
        setIsActionLoading(true);
        try {
            const updatedAssignment = await assignmentRepository.clockIn(assignment.id);
            setAssignment(updatedAssignment);
        } catch (err) {
            alert('Failed to clock in.');
            console.error(err);
        } finally {
            setIsActionLoading(false);
        }
    };

    const handleClockOut = async () => {
        if (!assignment) return;
        setIsActionLoading(true);
        try {
            const updatedAssignment = await assignmentRepository.clockOut(assignment.id);
            setAssignment(updatedAssignment);
        } catch (err) {
            alert('Failed to clock out.');
            console.error(err);
        } finally {
            setIsActionLoading(false);
        }
    };

    const formatTime = (dateString?: string) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    };

    if (loading) return <div>Loading your shift details...</div>;
    if (error) return <div className="text-center text-gray-500">{error}</div>;
    if (!assignment) return null;

    const { shift, manager, clock_in_time, clock_out_time } = assignment;
    const shiftStartTime = new Date(shift.start);
    const now = new Date();
    const oneHourBeforeShift = new Date(shiftStartTime.getTime() - 60 * 60 * 1000);

    const isClockInDisabled = now < oneHourBeforeShift;
    const isClockedIn = !!clock_in_time && !clock_out_time;
    const isShiftComplete = !!clock_in_time && !!clock_out_time;

    const getActionButton = () => {
        if (isShiftComplete) {
            return <p className="text-center font-semibold text-green-600">Shift Completed</p>;
        }
        if (isClockedIn) {
            return (
                <button
                    onClick={handleClockOut}
                    disabled={isActionLoading}
                    className="w-full bg-red-500 hover:bg-red-700 text-white font-bold py-3 px-4 rounded-lg disabled:bg-gray-400"
                >
                    {isActionLoading ? 'Processing...' : 'Clock Out'}
                </button>
            );
        }
        return (
            <button
                onClick={handleClockIn}
                disabled={isClockInDisabled || isActionLoading}
                className="w-full bg-green-500 hover:bg-green-700 text-white font-bold py-3 px-4 rounded-lg disabled:bg-gray-400"
                title={isClockInDisabled ? `You can clock in after ${formatTime(oneHourBeforeShift.toISOString())}` : ''}
            >
                {isActionLoading ? 'Processing...' : 'Clock In'}
            </button>
        );
    };

    return (
        <div className="bg-white p-6 rounded-lg shadow-md max-w-md mx-auto">
            <h2 className="text-2xl font-bold text-center mb-1">Your Next Shift</h2>
            <p className="text-center text-gray-500 mb-4">{new Date(shift.date).toLocaleDateString([], { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}</p>

            <div className="grid grid-cols-2 gap-4 text-center my-6">
                <div>
                    <p className="text-sm text-gray-500">Shift Starts</p>
                    <p className="text-xl font-bold">{formatTime(shift.start)}</p>
                </div>
                <div>
                    <p className="text-sm text-gray-500">Shift Ends</p>
                    <p className="text-xl font-bold">{formatTime(shift.end)}</p>
                </div>
                <div>
                    <p className="text-sm text-gray-500">Clocked In</p>
                    <p className={`text-xl font-bold ${clock_in_time ? 'text-green-600' : ''}`}>{formatTime(clock_in_time)}</p>
                </div>
                <div>
                    <p className="text-sm text-gray-500">Clocked Out</p>
                    <p className={`text-xl font-bold ${clock_out_time ? 'text-red-600' : ''}`}>{formatTime(clock_out_time)}</p>
                </div>
            </div>

            <div className="text-center mb-6">
                <p className="text-sm text-gray-500">Manager</p>
                <p className="text-lg font-semibold">{manager.email}</p>
            </div>

            <div className="mt-4">
                {getActionButton()}
            </div>
        </div>
    );
};
