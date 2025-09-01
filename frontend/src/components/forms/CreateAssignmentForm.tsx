// src/components/forms/CreateAssignmentForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { assignmentRepository } from '../../api/assignmentRepository';
import { useEffect, useState } from 'react';
import type { EmployeeDto, ShiftDto } from '../../types/api';
import { employeeRepository } from '../../api/employeeRepository';
import { shiftRepository } from '../../api/shiftRepository';

const createAssignmentSchema = z.object({
    employee_id: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(1, 'Please select an employee')
    ),
    shift_id: z.preprocess(
        (a) => parseInt(z.string().parse(a), 10),
        z.number().min(1, 'Please select a shift')
    ),
});

type CreateAssignmentFormData = z.infer<typeof createAssignmentSchema>;

interface CreateAssignmentFormProps {
    onSuccess: () => void;
}

export const CreateAssignmentForm = ({ onSuccess }: CreateAssignmentFormProps) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<CreateAssignmentFormData>({
        resolver: zodResolver(createAssignmentSchema),
    });

    const [employees, setEmployees] = useState<EmployeeDto[]>([]);
    const [shifts, setShifts] = useState<ShiftDto[]>([]);

    useEffect(() => {
        // Fetch both employees and shifts when the component mounts
        Promise.all([
            employeeRepository.getAllEmployees(),
            shiftRepository.getAllShifts()
        ]).then(([empData, shiftData]) => {
            setEmployees(empData);
            setShifts(shiftData);
        }).catch(console.error);
    }, []);

    const onSubmit = async (data: CreateAssignmentFormData) => {
        try {
            await assignmentRepository.createAssignment(data);
            onSuccess();
        } catch (error) {
            console.error('Failed to create assignment:', error);
            alert('Failed to create assignment. Please try again.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="mb-4">
                <label htmlFor="employee_id" className="block text-sm font-medium text-gray-700">Employee</label>
                <select
                    id="employee_id"
                    {...register('employee_id')}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                >
                    <option value="">Select Employee</option>
                    {employees.map(emp => <option key={emp.id} value={emp.id}>{emp.email}</option>)}
                </select>
                {errors.employee_id && <p className="text-red-500 text-xs mt-1">{errors.employee_id.message}</p>}
            </div>

            <div className="mb-4">
                <label htmlFor="shift_id" className="block text-sm font-medium text-gray-700">Shift</label>
                <select
                    id="shift_id"
                    {...register('shift_id')}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                >
                    <option value="">Select Shift</option>
                    {shifts.map(shift => (
                        <option key={shift.id} value={shift.id}>
                            {new Date(shift.date).toLocaleDateString()} ({new Date(shift.start).toLocaleTimeString()} - {new Date(shift.end).toLocaleTimeString()})
                        </option>
                    ))}
                </select>
                {errors.shift_id && <p className="text-red-500 text-xs mt-1">{errors.shift_id.message}</p>}
            </div>

            <div className="flex justify-end">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded disabled:bg-gray-400"
                >
                    {isSubmitting ? 'Assigning...' : 'Create Assignment'}
                </button>
            </div>
        </form>
    );
};
