import { useState, useEffect } from 'react';
import { Plus, Edit, CheckCircle, Users } from 'lucide-react';
import Card from '../../components/common/Card';
import Table from '../../components/common/Table';
import Button from '../../components/common/Button';
import Modal from '../../components/common/Modal';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Loading from '../../components/common/Loading';
import { monitoriaService } from '../../services/monitoriaService';
import { disciplinaService } from '../../services/disciplinaService';
import { TIPO_MONITORIA } from '../../constants/enums';

const Monitorias = () => {
  const [monitorias, setMonitorias] = useState([]);
  const [disciplinas, setDisciplinas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingMonitoria, setEditingMonitoria] = useState(null);
  const [formData, setFormData] = useState({
    disciplinaId: '',
    tipo: '',
    local: '',
    dataInicio: '',
    dataEncerramento: '',
    horaInicio: '',
    horaEncerramento: '',
    curso: '',
    semestre: '',
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [monitoriasData, disciplinasData] = await Promise.all([
        monitoriaService.getAll(),
        disciplinaService.getAll(),
      ]);
      setMonitorias(monitoriasData);
      setDisciplinas(disciplinasData.filter((d) => d.ativo));
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingMonitoria) {
        await monitoriaService.update(editingMonitoria.id, formData);
      } else {
        await monitoriaService.create(formData);
      }
      loadData();
      handleCloseModal();
    } catch (error) {
      console.error('Erro ao salvar monitoria:', error);
    }
  };

  const handleEdit = (monitoria) => {
    setEditingMonitoria(monitoria);
    setFormData({
      disciplinaId: monitoria.disciplinaId,
      tipo: monitoria.tipo,
      local: monitoria.local || '',
      dataInicio: monitoria.dataInicio,
      dataEncerramento: monitoria.dataEncerramento || '',
      horaInicio: monitoria.horaInicio,
      horaEncerramento: monitoria.horaEncerramento,
      curso: monitoria.curso,
      semestre: monitoria.semestre,
    });
    setIsModalOpen(true);
  };

  const handleFinalizar = async (id) => {
    if (window.confirm('Tem certeza que deseja finalizar esta monitoria?')) {
      try {
        await monitoriaService.finalizar(id);
        loadData();
      } catch (error) {
        console.error('Erro ao finalizar monitoria:', error);
      }
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingMonitoria(null);
    setFormData({
      disciplinaId: '',
      tipo: '',
      local: '',
      dataInicio: '',
      dataEncerramento: '',
      horaInicio: '',
      horaEncerramento: '',
      curso: '',
      semestre: '',
    });
  };

  const columns = [
    { key: 'disciplinaNome', label: 'Disciplina' },
    { key: 'curso', label: 'Curso' },
    { key: 'semestre', label: 'Semestre' },
    { key: 'tipo', label: 'Tipo' },
    { key: 'dataInicio', label: 'Data Início' },
    {
      key: 'finalizada',
      label: 'Status',
      render: (value) => (
        <span
          className={`px-2 py-1 rounded-full text-xs font-semibold ${
            !value ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
          }`}
        >
          {!value ? 'Em Andamento' : 'Finalizada'}
        </span>
      ),
    },
    {
      key: 'actions',
      label: 'Ações',
      render: (_, monitoria) => (
        <div className="flex gap-2">
          {!monitoria.finalizada && (
            <>
              <Button
                size="sm"
                variant="outline"
                onClick={() => handleEdit(monitoria)}
              >
                <Edit className="w-4 h-4" />
              </Button>
              <Button
                size="sm"
                variant="success"
                onClick={() => handleFinalizar(monitoria.id)}
              >
                <CheckCircle className="w-4 h-4" />
              </Button>
            </>
          )}
        </div>
      ),
    },
  ];

  const tipoOptions = Object.entries(TIPO_MONITORIA).map(([key, value]) => ({
    value: key,
    label: value,
  }));

  const disciplinaOptions = disciplinas.map((disciplina) => ({
    value: disciplina.id,
    label: disciplina.nome,
  }));

  if (loading) return <Loading />;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Minhas Monitorias</h1>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="w-5 h-5 mr-2" />
          Nova Monitoria
        </Button>
      </div>

      <Card>
        <Table columns={columns} data={monitorias} />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingMonitoria ? 'Editar Monitoria' : 'Nova Monitoria'}
        size="lg"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Select
              label="Disciplina"
              value={formData.disciplinaId}
              onChange={(e) =>
                setFormData({ ...formData, disciplinaId: e.target.value })
              }
              options={disciplinaOptions}
              required
            />

            <Select
              label="Tipo"
              value={formData.tipo}
              onChange={(e) =>
                setFormData({ ...formData, tipo: e.target.value })
              }
              options={tipoOptions}
              required
            />

            <Input
              label="Local"
              value={formData.local}
              onChange={(e) =>
                setFormData({ ...formData, local: e.target.value })
              }
            />

            <Input
              label="Curso"
              value={formData.curso}
              onChange={(e) =>
                setFormData({ ...formData, curso: e.target.value })
              }
              required
            />

            <Input
              label="Semestre"
              type="number"
              value={formData.semestre}
              onChange={(e) =>
                setFormData({ ...formData, semestre: e.target.value })
              }
              required
            />

            <Input
              label="Data Início"
              type="date"
              value={formData.dataInicio}
              onChange={(e) =>
                setFormData({ ...formData, dataInicio: e.target.value })
              }
              required
            />

            <Input
              label="Data Encerramento"
              type="date"
              value={formData.dataEncerramento}
              onChange={(e) =>
                setFormData({ ...formData, dataEncerramento: e.target.value })
              }
            />

            <Input
              label="Hora Início"
              type="time"
              value={formData.horaInicio}
              onChange={(e) =>
                setFormData({ ...formData, horaInicio: e.target.value })
              }
              required
            />

            <Input
              label="Hora Encerramento"
              type="time"
              value={formData.horaEncerramento}
              onChange={(e) =>
                setFormData({ ...formData, horaEncerramento: e.target.value })
              }
              required
            />
          </div>

          <div className="flex justify-end gap-2 pt-4">
            <Button type="button" variant="secondary" onClick={handleCloseModal}>
              Cancelar
            </Button>
            <Button type="submit">
              {editingMonitoria ? 'Atualizar' : 'Criar'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Monitorias;
