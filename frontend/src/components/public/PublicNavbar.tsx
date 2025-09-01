// src/components/public/PublicNavbar.tsx
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

export const PublicNavbar = () => {
    const { isAuthenticated, logout, user } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <nav className="bg-white shadow-md text-gray-800 p-4 flex justify-between items-center">
            <NavLink to="/" className="text-2xl font-bold text-blue-600">TastyTabs</NavLink>
            <div className="flex items-center space-x-4">
                <NavLink to="/" className={({ isActive }) => `hover:text-blue-600 ${isActive ? 'font-semibold' : ''}`}>Menu</NavLink>
                {isAuthenticated && (
                    <>
                        <NavLink to="/my-orders" className={({ isActive }) => `hover:text-blue-600 ${isActive ? 'font-semibold' : ''}`}>My Orders</NavLink>
                        <NavLink to="/my-reservations" className={({ isActive }) => `hover:text-blue-600 ${isActive ? 'font-semibold' : ''}`}>My Reservations</NavLink>
                    </>
                )}
                {isAuthenticated ? (
                    <>
                        <span className="text-sm">Hi, {user?.email}</span>
                        <button onClick={handleLogout} className="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-3 rounded-md text-sm">
                            Logout
                        </button>
                    </>
                ) : (
                    <div className="space-x-2">
                        <button onClick={() => navigate('/login')} className="bg-gray-200 hover:bg-gray-300 text-black font-bold py-2 px-3 rounded-md text-sm">
                            Login
                        </button>
                        <button onClick={() => navigate('/register')} className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-3 rounded-md text-sm">
                            Register
                        </button>
                    </div>
                )}
            </div>
        </nav>
    );
};
