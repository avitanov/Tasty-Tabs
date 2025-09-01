// src/context/AuthContext.tsx
import { createContext, useState, useEffect, type ReactNode } from 'react';
import { type UserDto, type AuthRequest, UserType } from '../types/api';
import { authRepository } from '../api/authRepository';

interface AuthContextType {
    user: UserDto | null;
    token: string | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    login: (credentials: AuthRequest) => Promise<void>;
    logout: () => void;
    hasRole: (roles: UserType[]) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
    const [user, setUser] = useState<UserDto | null>(null);
    const [token, setToken] = useState<string | null>(localStorage.getItem('authToken'));
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const storedToken = localStorage.getItem('authToken');
        const storedUser = localStorage.getItem('user');

        if (storedToken && storedUser) {
            setToken(storedToken);
            setUser(JSON.parse(storedUser));
        }
        setIsLoading(false);

    }, []);

    const login = async (credentials: AuthRequest) => {
        const response = await authRepository.login(credentials);
        localStorage.setItem('authToken', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
        setToken(response.token);
        setUser(response.user);
    };

    const logout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
    };

    const hasRole = (roles: UserType[]): boolean => {
        console.log("Checking roles:", roles, "for user:", user);
        return user ? roles.includes(user.user_type) : false;
    };

    return (
        <AuthContext.Provider value={{
            user,
            token,
            isAuthenticated: !!token,
            isLoading,
            login,
            logout,
            hasRole,
        }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;

