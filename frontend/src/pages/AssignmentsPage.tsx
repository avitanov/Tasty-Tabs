// src/pages/AssignmentsPage.tsx
import { useEffect, useState } from 'react';
import type { AssignmentDto } from '../types/api';
import { assignmentRepository } from '../api/assignmentRepository';
import { Modal } from '../components/Modal';
import { CreateAssignmentForm } from '../components/forms/CreateAssignmentForm';

export const AssignmentsPage = () => {
    const [assignments, setAssignments] = useState<AssignmentDto[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchAssignments = () => {
        assignmentRepository.getAllAssignments()
            .then(setAssignments)
            .catch(console.error);
    };

    useEffect(() => {
        fetchAssignments();
    }, []);

    const handleFormSuccess = () => {
        setIsModalOpen(false);
        fetchAssignments();
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Employee Assignments</h1>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Create Assignment
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create New Assignment">
                <CreateAssignmentForm onSuccess={handleFormSuccess} />
            </Modal>

            <div className="bg-white shadow-md rounded-lg p-4">
                <h2 className="text-xl font-semibold mb-2">Current Assignments</h2>
                <ul>
                    {assignments.map(a => (
                        <li key={a.id} className="border-b py-2">
                            <span className="font-semibold">{a.employee.email}</span> is assigned to shift on{' '}
                            <span className="font-semibold">{new Date(a.shift.date).toLocaleDateString()}</span> from{' '}
                            <span className="font-semibold">{new Date(a.shift.start).toLocaleTimeString()}</span> to{' '}
                            <span className="font-semibold">{new Date(a.shift.end).toLocaleTimeString()}</span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};
