// src/components/Navbar.tsx
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ROLE_PERMISSIONS } from '../utils/roles';

const navLinks = [
    { path: '/admin/orders', label: 'Orders' },
    { path: '/admin/reservations', label: 'Reservations' },
    { path: '/admin/categories', label: 'Categories' }, // New
    { path: '/admin/products', label: 'Products' }, // New
    { path: '/admin/shifts', label: 'Shifts' },
    { path: '/admin/employees', label: 'Employees' },
    { path: '/admin/assignments', label: 'Assignments' },
];


export const Navbar = () => {
    const { isAuthenticated, logout, user } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const availableLinks = isAuthenticated && user
        ? navLinks.filter(link => ROLE_PERMISSIONS[user.user_type]?.includes(link.path))
        : [];

    return (
        <nav className="bg-gray-800 text-white p-4 flex justify-between items-center">
            <div className="flex items-center space-x-6">
                <NavLink to="/" className="text-xl font-bold">TastyTabs</NavLink>
                <div className="flex space-x-4">
                    {isAuthenticated && availableLinks.map(link => (
                        <NavLink
                            key={link.path}
                            to={link.path}
                            className={({ isActive }) =>
                                `px-3 py-2 rounded-md text-sm font-medium ${isActive ? 'bg-gray-900' : 'hover:bg-gray-700'
                                }`
                            }
                        >
                            {link.label}
                        </NavLink>
                    ))}
                </div>
            </div>
            <div className="flex items-center space-x-4">
                {isAuthenticated && user && (
                    <span className="text-sm">Welcome, {user.email}</span>
                    // TODO: Display current shift info here
                )}
                {isAuthenticated ? (
                    <button onClick={handleLogout} className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
                        Logout
                    </button>
                ) : (
                    <button onClick={() => navigate('/login')} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                        Login
                    </button>
                )}
            </div>
        </nav>
    );
};
