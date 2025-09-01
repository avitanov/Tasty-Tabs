// src/App.tsx
import { BrowserRouter, Routes, Route, Outlet } from 'react-router-dom';
import { Navbar } from './components/Navbar';
import { LoginPage } from './pages/LoginPage';
import { OrdersPage } from './pages/OrdersPage';
import { ReservationsPage } from './pages/ReservationsPage';
import { AssignmentsPage } from './pages/AssignmentsPage';
import { ProtectedRoute } from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';
import { ROLES } from './utils/roles';
import { EmployeesPage } from './pages/EmployeesPage';
import { ShiftsPage } from './pages/ShiftsPage';
import { CategoriesPage } from './pages/CategoriesPage';
import { ProductsPage } from './pages/ProductsPage';
import { OrderDetailsPage } from './pages/OrderDetailsPage';
import DashboardPage from './pages/DashboardPage';

// A layout component to wrap pages that have the Navbar
const AppLayout = () => (
    <div className="bg-gray-50 min-h-screen">
        <Navbar />
        <main>
            <Outlet />
        </main>
    </div>
);

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/login" element={<LoginPage />} />
                    <Route element={<AppLayout />}>
                        <Route path="/" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER, ROLES.FRONT_STAFF, ROLES.BACK_STAFF]} />}>
                            <Route index element={<DashboardPage />} />
                        </Route>

                        <Route path="/orders" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER, ROLES.FRONT_STAFF, ROLES.BACK_STAFF]} />}>
                            <Route index element={<OrdersPage />} />
                            {/* Add the new route for viewing a single order */}
                            <Route path=":orderId" element={<OrderDetailsPage />} />
                        </Route>

                        <Route path="/reservations" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER, ROLES.FRONT_STAFF]} />}>
                            <Route index element={<ReservationsPage />} />
                        </Route>

                        {/* Manager-only routes */}
                        <Route path="/categories" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}>
                            <Route index element={<CategoriesPage />} />
                        </Route>

                        <Route path="/products" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}>
                            <Route index element={<ProductsPage />} />
                        </Route>
                        <Route path="/shifts" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}>
                            <Route index element={<ShiftsPage />} />
                        </Route>

                        <Route path="/employees" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}>
                            <Route index element={<EmployeesPage />} />
                        </Route>

                        <Route path="/assignments" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}>
                            <Route index element={<AssignmentsPage />} />
                        </Route>

                        <Route path="*" element={<div>404 Not Found</div>} />
                    </Route>
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
