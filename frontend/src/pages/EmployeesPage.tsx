// src/pages/EmployeesPage.tsx
import { useEffect, useState } from 'react';
import type { EmployeeDto } from '../types/api';
import { employeeRepository } from '../api/employeeRepository';
import { Modal } from '../components/Modal';
import { CreateEmployeeForm } from '../components/forms/CreateEmployeeForm';

export const EmployeesPage = () => {
    const [employees, setEmployees] = useState<EmployeeDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchEmployees = () => {
        setLoading(true);
        employeeRepository.getAllEmployees()
            .then(setEmployees)
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchEmployees();
    }, []);

    const handleFormSuccess = () => {
        setIsModalOpen(false);
        fetchEmployees();
    };

    if (loading) return <div>Loading employees...</div>;

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Employees</h1>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Add Employee
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create New Employee">
                <CreateEmployeeForm onSuccess={handleFormSuccess} />
            </Modal>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full leading-normal">
                    <thead>
                        <tr>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Email</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Role</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Phone Number</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Gross/Net Salary</th>
                        </tr>
                    </thead>
                    <tbody>
                        {employees.map(emp => (
                            <tr key={emp.id}>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{emp.email}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{emp.user_type}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{emp.phone_number}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">${emp.gross_salary} / ${emp.net_salary}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
