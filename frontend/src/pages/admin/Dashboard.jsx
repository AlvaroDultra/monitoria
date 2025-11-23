import { useState, useEffect } from 'react';
import { School, Users, BookOpen } from 'lucide-react';
import Card from '../../components/common/Card';
import Loading from '../../components/common/Loading';
import { escolaService } from '../../services/escolaService';
import { professorService } from '../../services/professorService';
import { disciplinaService } from '../../services/disciplinaService';

const Dashboard = () => {
  const [stats, setStats] = useState({
    escolas: 0,
    professores: 0,
    disciplinas: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const [escolas, professores, disciplinas] = await Promise.all([
        escolaService.getAtivas(),
        professorService.getAtivos(),
        disciplinaService.getAll(),
      ]);

      setStats({
        escolas: escolas.length,
        professores: professores.length,
        disciplinas: disciplinas.filter((d) => d.ativo).length,
      });
    } catch (error) {
      console.error('Erro ao carregar estatísticas:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-900 mb-8">
        Dashboard - Administrador
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="flex items-center justify-center w-16 h-16 bg-blue-100 rounded-lg">
                <School className="w-8 h-8 text-blue-600" />
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Escolas Ativas</p>
              <p className="text-3xl font-bold text-gray-900">{stats.escolas}</p>
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="flex items-center justify-center w-16 h-16 bg-green-100 rounded-lg">
                <Users className="w-8 h-8 text-green-600" />
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Professores Ativos</p>
              <p className="text-3xl font-bold text-gray-900">{stats.professores}</p>
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="flex items-center justify-center w-16 h-16 bg-purple-100 rounded-lg">
                <BookOpen className="w-8 h-8 text-purple-600" />
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Disciplinas Ativas</p>
              <p className="text-3xl font-bold text-gray-900">{stats.disciplinas}</p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;
