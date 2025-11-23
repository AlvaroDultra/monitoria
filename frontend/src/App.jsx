import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './components/layout/Layout';
import Login from './pages/auth/Login';

// Admin Pages
import AdminDashboard from './pages/admin/Dashboard';
import Escolas from './pages/admin/Escolas';
import Professores from './pages/admin/Professores';
import Disciplinas from './pages/admin/Disciplinas';

// Professor Pages
import ProfessorDashboard from './pages/professor/Dashboard';
import Monitorias from './pages/professor/Monitorias';
import Alunos from './pages/professor/Alunos';

// Protected Route Component
const ProtectedRoute = ({ children, requiredRole }) => {
  const { isAuthenticated, user, loading } = useAuth();

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="w-16 h-16 border-4 border-primary-200 border-t-primary-600 rounded-full animate-spin" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && user?.role !== requiredRole) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

// Public Route Component
const PublicRoute = ({ children }) => {
  const { isAuthenticated, user } = useAuth();

  if (isAuthenticated) {
    if (user?.role === 'ROLE_ADMIN') {
      return <Navigate to="/admin" replace />;
    } else if (user?.role === 'ROLE_PROFESSOR') {
      return <Navigate to="/professor" replace />;
    }
  }

  return children;
};

function AppRoutes() {
  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={
            <PublicRoute>
              <Login />
            </PublicRoute>
          }
        />

        {/* Admin Routes */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute requiredRole="ROLE_ADMIN">
              <Layout />
            </ProtectedRoute>
          }
        >
          <Route index element={<AdminDashboard />} />
          <Route path="escolas" element={<Escolas />} />
          <Route path="professores" element={<Professores />} />
          <Route path="disciplinas" element={<Disciplinas />} />
        </Route>

        {/* Professor Routes */}
        <Route
          path="/professor"
          element={
            <ProtectedRoute requiredRole="ROLE_PROFESSOR">
              <Layout />
            </ProtectedRoute>
          }
        >
          <Route index element={<ProfessorDashboard />} />
          <Route path="monitorias" element={<Monitorias />} />
          <Route path="alunos" element={<Alunos />} />
        </Route>

        {/* Default Route */}
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}

export default App;
