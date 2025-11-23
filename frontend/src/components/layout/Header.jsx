import { LogOut, User } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white shadow-sm border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center">
            <h1 className="text-2xl font-bold text-primary-600">
              Sistema de Monitoria Web
            </h1>
          </div>

          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2 text-gray-700">
              <User className="w-5 h-5" />
              <div className="flex flex-col">
                <span className="text-sm font-medium">{user?.username}</span>
                <span className="text-xs text-gray-500">
                  {user?.role === 'ROLE_ADMIN' ? 'Administrador' : 'Professor'}
                </span>
              </div>
            </div>

            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-gray-700 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
            >
              <LogOut className="w-4 h-4" />
              Sair
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
