import { useState, useEffect } from 'react';
import { GraduationCap, Users, BookOpen } from 'lucide-react';
import Card from '../../components/common/Card';
import Loading from '../../components/common/Loading';
import { monitoriaService } from '../../services/monitoriaService';
import { alunoService } from '../../services/alunoService';
import { disciplinaService } from '../../services/disciplinaService';

const Dashboard = () => {
  const [stats, setStats] = useState({
    monitoriasEmAndamento: 0,
    totalAlunos: 0,
    totalDisciplinas: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const [monitorias, alunos, disciplinas] = await Promise.all([
        monitoriaService.getEmAndamento(),
        alunoService.getAll(),
        disciplinaService.getAll(),
      ]);

      setStats({
        monitoriasEmAndamento: monitorias.length,
        totalAlunos: alunos.filter((a) => a.ativo).length,
        totalDisciplinas: disciplinas.filter((d) => d.ativo).length,
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
        Dashboard - Professor
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="flex items-center justify-center w-16 h-16 bg-blue-100 rounded-lg">
                <GraduationCap className="w-8 h-8 text-blue-600" />
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Monitorias em Andamento</p>
              <p className="text-3xl font-bold text-gray-900">{stats.monitoriasEmAndamento}</p>
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
              <p className="text-sm font-medium text-gray-500">Total de Alunos</p>
              <p className="text-3xl font-bold text-gray-900">{stats.totalAlunos}</p>
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
              <p className="text-sm font-medium text-gray-500">Disciplinas</p>
              <p className="text-3xl font-bold text-gray-900">{stats.totalDisciplinas}</p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;
