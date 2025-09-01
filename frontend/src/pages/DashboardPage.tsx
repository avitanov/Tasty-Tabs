// src/pages/DashboardPage.tsx
import { useAuth } from '../hooks/useAuth';
import { ROLES } from '../utils/roles';
import { CurrentShiftCard } from '../components/employee/CurrentShiftCard';

// This is the Manager's dashboard view
const ManagerDashboard = () => (
    <>
        <h1 className="text-3xl font-bold">Manager Dashboard</h1>
        <p className="mt-2">Welcome back! Here's a summary of today's operations.</p>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
            <div className="bg-white p-6 rounded-lg shadow-md">
                <h2 className="text-xl font-semibold">Open Orders</h2>
                <p className="mt-2 text-gray-600">View and manage all incoming and in-progress orders.</p>
            </div>
            <div className="bg-white p-6 rounded-lg shadow-md">
                <h2 className="text-xl font-semibold">Today's Reservations</h2>
                <p className="mt-2 text-gray-600">See all scheduled reservations for today.</p>
            </div>
        </div>
    </>
);

// This is the Employee's dashboard view
const EmployeeDashboard = () => (
    <>
        <h1 className="text-3xl font-bold mb-6">Dashboard</h1>
        <CurrentShiftCard />
    </>
);

const DashboardPage = () => {
    const { user } = useAuth();

    // Determine which dashboard to show based on user role
    const isManager = user?.user_type === ROLES.MANAGER;

    return (
        <div className="p-6">
            {isManager ? <ManagerDashboard /> : <EmployeeDashboard />}
        </div>
    );
};

export default DashboardPage;
