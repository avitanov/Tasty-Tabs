// src/components/ProtectedRoute.tsx
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { UserType } from '../types/api';

interface ProtectedRouteProps {
    allowedRoles: UserType[];
}

export const ProtectedRoute = ({ allowedRoles }: ProtectedRouteProps) => {
    const { isAuthenticated, isLoading, hasRole } = useAuth();

    if (isLoading) {
        return <div>Loading...</div>; // Or a spinner component
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (!hasRole(allowedRoles)) {
        console.log("Access denied. User does not have the required role.");
        return <Navigate to="/" replace />; // Or a dedicated "Unauthorized" page
    }

    return <Outlet />;
};
