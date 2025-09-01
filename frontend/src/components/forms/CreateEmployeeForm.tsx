// src/components/forms/CreateEmployeeForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { employeeRepository } from '../../api/employeeRepository';
import type { CreateEmployeeRequest } from '../../types/api';

const staffRoles = [
    { id: 1, name: 'Server' },
    { id: 2, name: 'Chef' },
    { id: 3, name: 'Bartender' },
    { id: 4, name: 'Hostess' },
];

const createEmployeeSchema = z.object({
    email: z.string().email('Invalid email address'),
    password: z.string().min(6, 'Password must be at least 6 characters'),
    employee_type: z.enum(["MANAGER", "FRONT_STAFF", "BACK_STAFF"]),
    staff_role_id: z.preprocess(
        (a) => a ? parseInt(z.string().parse(a), 10) : undefined,
        z.number().optional()
    ),
    phone_number: z.string().optional(),
    gross_salary: z.number().min(0, 'Gross salary must be non-negative').optional().default(0),
    net_salary: z.number().min(0, 'Net salary must be non-negative').optional().default(0),
    // Add other fields from CreateEmployeeRequest as needed
});

type CreateEmployeeFormData = z.infer<typeof createEmployeeSchema>;

interface CreateEmployeeFormProps {
    onSuccess: () => void;
}

export const CreateEmployeeForm = ({ onSuccess }: CreateEmployeeFormProps) => {
    const { register, handleSubmit, watch, formState: { errors, isSubmitting } } = useForm<CreateEmployeeFormData>({
        resolver: zodResolver(createEmployeeSchema),
    });

    const employeeType = watch('employee_type');

    const onSubmit = async (data: CreateEmployeeFormData) => {
        try {
            const employeeData: CreateEmployeeRequest = { ...data };
            await employeeRepository.createEmployee(employeeData);
            onSuccess();
        } catch (error) {
            console.error('Failed to create employee:', error);
            alert('Failed to create employee. Please try again.');
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
                <label>Email</label>
                <input {...register('email')} className="w-full p-2 border rounded" />
                {errors.email && <p className="text-red-500 text-xs">{errors.email.message}</p>}
            </div>
            <div>
                <label>Password</label>
                <input type="password" {...register('password')} className="w-full p-2 border rounded" />
                {errors.password && <p className="text-red-500 text-xs">{errors.password.message}</p>}
            </div>
            <div>
                <label>Employee Type</label>
                <select {...register('employee_type')} className="w-full p-2 border rounded">
                    <option value="">Select Type</option>
                    <option value="MANAGER">Manager</option>
                    <option value="FRONT_STAFF">Front Staff</option>
                    <option value="BACK_STAFF">Back Staff</option>
                </select>
                {errors.employee_type && <p className="text-red-500 text-xs">{errors.employee_type.message}</p>}
            </div>

            {employeeType !== 'MANAGER' && (
                <div>
                    <label>Staff Role</label>
                    <select {...register('staff_role_id')} className="w-full p-2 border rounded">
                        <option value="">Select Role</option>
                        {staffRoles.map(role => <option key={role.id} value={role.id}>{role.name}</option>)}
                    </select>
                    {errors.staff_role_id && <p className="text-red-500 text-xs">{errors.staff_role_id.message}</p>}
                </div>
            )}

            <div>
                <label>Phone Number</label>
                <input {...register('phone_number')} className="w-full p-2 border rounded" />
            </div>
            <div>
                <label>Gross Salary ($)</label>
                <input type="number" {...register('gross_salary', { valueAsNumber: true })} className="w-full p-2 border rounded" />

            </div>
            <div>
                <label>Net Salary ($)</label>
                <input type="number" {...register('net_salary', { valueAsNumber: true })} className="w-full p-2 border rounded" />
            </div>

            <div className="flex justify-end">
                <button type="submit" disabled={isSubmitting} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    {isSubmitting ? 'Creating...' : 'Create Employee'}
                </button>
            </div>
        </form>
    );
};
