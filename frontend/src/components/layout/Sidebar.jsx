import { NavLink } from 'react-router-dom';
import {
  Home,
  School,
  Users,
  BookOpen,
  GraduationCap,
  UserCheck,
  FileText,
  ClipboardList
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const Sidebar = () => {
  const { isAdmin, isProfessor } = useAuth();

  const adminLinks = [
    { to: '/admin', icon: Home, label: 'Dashboard' },
    { to: '/admin/escolas', icon: School, label: 'Escolas' },
    { to: '/admin/professores', icon: Users, label: 'Professores' },
    { to: '/admin/disciplinas', icon: BookOpen, label: 'Disciplinas' },
  ];

  const professorLinks = [
    { to: '/professor', icon: Home, label: 'Dashboard' },
    { to: '/professor/monitorias', icon: GraduationCap, label: 'Monitorias' },
    { to: '/professor/alunos', icon: Users, label: 'Alunos' },
    { to: '/professor/frequencias', icon: UserCheck, label: 'Frequências' },
    { to: '/professor/assuntos', icon: FileText, label: 'Assuntos' },
  ];

  const links = isAdmin() ? adminLinks : isProfessor() ? professorLinks : [];

  return (
    <aside className="w-64 bg-white shadow-lg border-r border-gray-200 min-h-[calc(100vh-4rem)]">
      <nav className="p-4 space-y-2">
        {links.map((link) => {
          const Icon = link.icon;
          return (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.to === '/admin' || link.to === '/professor'}
              className={({ isActive }) =>
                `flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive
                    ? 'bg-primary-600 text-white'
                    : 'text-gray-700 hover:bg-gray-100'
                }`
              }
            >
              <Icon className="w-5 h-5" />
              <span className="font-medium">{link.label}</span>
            </NavLink>
          );
        })}
      </nav>
    </aside>
  );
};

export default Sidebar;
