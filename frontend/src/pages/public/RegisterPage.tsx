// src/pages/public/RegisterPage.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { authRepository } from '../../api/authRepository';

const registerSchema = z.object({
    first_name: z.string().min(1, "First name is required"),
    last_name: z.string().min(1, "Last name is required"),
    email: z.string().email(),
    password: z.string().min(6, "Password must be at least 6 characters"),
    password_confirmation: z.string()
}).refine(data => data.password === data.password_confirmation, {
    message: "Passwords do not match",
    path: ["password_confirmation"],
});

type RegisterFormData = z.infer<typeof registerSchema>;

export const RegisterPage = () => {
    const navigate = useNavigate();
    const { login } = useAuth(); // We'll use this to log the user in after registration
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<RegisterFormData>({
        resolver: zodResolver(registerSchema),
    });

    const onSubmit = async (data: RegisterFormData) => {
        try {
            await authRepository.register(data);
            // Automatically log in the user after successful registration
            await login({ username: data.email, password: data.password });
            navigate('/');
        } catch (error) {
            console.error(error);
            alert('Registration failed. The email might already be in use.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50">
            <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
                <h2 className="text-2xl font-bold mb-6 text-center">Create an Account</h2>
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    {/* Form fields for first_name, last_name, email, password, password_confirmation */}
                    <input {...register('first_name')} placeholder="First Name" className="w-full p-2 border rounded" />
                    {errors.first_name && <p className="text-red-500 text-xs">{errors.first_name.message}</p>}
                    <input {...register('last_name')} placeholder="Last Name" className="w-full p-2 border rounded" />
                    {errors.last_name && <p className="text-red-500 text-xs">{errors.last_name.message}</p>}
                    <input {...register('email')} placeholder="Email" className="w-full p-2 border rounded" />
                    {errors.email && <p className="text-red-500 text-xs">{errors.email.message}</p>}
                    <input type="password" {...register('password')} placeholder="Password" className="w-full p-2 border rounded" />
                    {errors.password && <p className="text-red-500 text-xs">{errors.password.message}</p>}
                    <input type="password" {...register('password_confirmation')} placeholder="Confirm Password" className="w-full p-2 border rounded" />
                    {errors.password_confirmation && <p className="text-red-500 text-xs">{errors.password_confirmation.message}</p>}
                    <button type="submit" disabled={isSubmitting} className="w-full bg-blue-500 text-white p-3 rounded">
                        {isSubmitting ? 'Registering...' : 'Register'}
                    </button>
                </form>
            </div>
        </div>
    );
};
