// src/App.tsx
import { BrowserRouter, Routes, Route, Outlet } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ROLES } from './utils/roles';

// Layouts
import { Navbar as AdminNavbar } from './components/Navbar';
import { PublicNavbar } from './components/public/PublicNavbar';

// Common Pages
import { LoginPage } from './pages/LoginPage';
import { ProtectedRoute } from './components/ProtectedRoute';

// Admin Pages
import DashboardPage from './pages/DashboardPage';
import { OrdersPage } from './pages/OrdersPage';
import { OrderDetailsPage } from './pages/OrderDetailsPage';
import { ReservationsPage as AdminReservationsPage } from './pages/ReservationsPage';
import { CategoriesPage } from './pages/CategoriesPage';
import { ProductsPage } from './pages/ProductsPage';
import { ShiftsPage } from './pages/ShiftsPage';
import { EmployeesPage } from './pages/EmployeesPage';
import { AssignmentsPage } from './pages/AssignmentsPage';

// Public Pages
import { RegisterPage } from './pages/public/RegisterPage';
import { MenuPage } from './pages/public/MenuPage';
import { MyReservationsPage } from './pages/public/MyReservationsPage';
import { MyOrdersPage } from './pages/public/MyOrdersPage';
import { AnalyticsPage } from './pages/AnalyticsPage';

// Admin Layout
const AdminLayout = () => (
    <div className="bg-gray-50 min-h-screen">
        <AdminNavbar />
        <main><Outlet /></main>
    </div>
);

// Public Layout
const PublicLayout = () => (
    <div>
        <PublicNavbar />
        <main><Outlet /></main>
    </div>
);

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    {/* Public Routes */}
                    <Route element={<PublicLayout />}>
                        <Route path="/" element={<MenuPage />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/my-orders" element={<ProtectedRoute allowedRoles={[ROLES.CUSTOMER, ROLES.MANAGER, ROLES.USER]} />}>
                            <Route index element={<MyOrdersPage />} />
                        </Route>
                        <Route path="/my-reservations" element={<ProtectedRoute allowedRoles={[ROLES.CUSTOMER, ROLES.MANAGER, ROLES.USER]} />}>
                            <Route index element={<MyReservationsPage />} />
                        </Route>
                    </Route>

                    {/* Admin Routes */}
                    <Route path="/admin" element={<AdminLayout />}>
                        <Route path="" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER, ROLES.FRONT_STAFF, ROLES.BACK_STAFF]} />}>
                            <Route index element={<DashboardPage />} />
                        </Route>
                        <Route path="analytics" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}>
                            <Route index element={<AnalyticsPage />} />
                        </Route>

                        <Route path="orders" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER, ROLES.FRONT_STAFF, ROLES.BACK_STAFF]} />}>
                            <Route index element={<OrdersPage />} />
                            <Route path=":orderId" element={<OrderDetailsPage />} />
                        </Route>
                        <Route path="reservations" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER, ROLES.FRONT_STAFF]} />}>
                            <Route index element={<AdminReservationsPage />} />
                        </Route>
                        <Route path="categories" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}><Route index element={<CategoriesPage />} /></Route>
                        <Route path="products" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}><Route index element={<ProductsPage />} /></Route>
                        <Route path="shifts" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}><Route index element={<ShiftsPage />} /></Route>
                        <Route path="employees" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}><Route index element={<EmployeesPage />} /></Route>
                        <Route path="assignments" element={<ProtectedRoute allowedRoles={[ROLES.MANAGER]} />}><Route index element={<AssignmentsPage />} /></Route>
                    </Route>

                    <Route path="*" element={<div>404 Not Found</div>} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
