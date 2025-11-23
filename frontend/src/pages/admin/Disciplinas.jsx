import { useState, useEffect } from 'react';
import { Plus, Edit, PowerOff } from 'lucide-react';
import Card from '../../components/common/Card';
import Table from '../../components/common/Table';
import Button from '../../components/common/Button';
import Modal from '../../components/common/Modal';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Loading from '../../components/common/Loading';
import { disciplinaService } from '../../services/disciplinaService';
import { escolaService } from '../../services/escolaService';
import { professorService } from '../../services/professorService';

const Disciplinas = () => {
  const [disciplinas, setDisciplinas] = useState([]);
  const [escolas, setEscolas] = useState([]);
  const [professores, setProfessores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingDisciplina, setEditingDisciplina] = useState(null);
  const [formData, setFormData] = useState({
    sigla: '',
    nome: '',
    descricao: '',
    cargaHoraria: '',
    curso: '',
    matrizVinculada: '',
    escolaId: '',
    professorId: '',
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [disciplinasData, escolasData, professoresData] = await Promise.all([
        disciplinaService.getAll(),
        escolaService.getAtivas(),
        professorService.getAtivos(),
      ]);
      setDisciplinas(disciplinasData);
      setEscolas(escolasData);
      setProfessores(professoresData);
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingDisciplina) {
        await disciplinaService.update(editingDisciplina.id, formData);
      } else {
        await disciplinaService.create(formData);
      }
      loadData();
      handleCloseModal();
    } catch (error) {
      console.error('Erro ao salvar disciplina:', error);
    }
  };

  const handleEdit = (disciplina) => {
    setEditingDisciplina(disciplina);
    setFormData({
      sigla: disciplina.sigla,
      nome: disciplina.nome,
      descricao: disciplina.descricao || '',
      cargaHoraria: disciplina.cargaHoraria,
      curso: disciplina.curso,
      matrizVinculada: disciplina.matrizVinculada,
      escolaId: disciplina.escolaId,
      professorId: disciplina.professorId,
    });
    setIsModalOpen(true);
  };

  const handleInativar = async (id) => {
    try {
      await disciplinaService.inativar(id);
      loadData();
    } catch (error) {
      console.error('Erro ao inativar disciplina:', error);
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingDisciplina(null);
    setFormData({
      sigla: '',
      nome: '',
      descricao: '',
      cargaHoraria: '',
      curso: '',
      matrizVinculada: '',
      escolaId: '',
      professorId: '',
    });
  };

  const columns = [
    { key: 'sigla', label: 'Sigla' },
    { key: 'nome', label: 'Nome' },
    { key: 'curso', label: 'Curso' },
    { key: 'cargaHoraria', label: 'Carga Horária' },
    { key: 'professorNome', label: 'Professor' },
    {
      key: 'ativo',
      label: 'Status',
      render: (value) => (
        <span
          className={`px-2 py-1 rounded-full text-xs font-semibold ${
            value ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
          }`}
        >
          {value ? 'Ativo' : 'Inativo'}
        </span>
      ),
    },
    {
      key: 'actions',
      label: 'Ações',
      render: (_, disciplina) => (
        <div className="flex gap-2">
          <Button
            size="sm"
            variant="outline"
            onClick={() => handleEdit(disciplina)}
          >
            <Edit className="w-4 h-4" />
          </Button>
          {disciplina.ativo && (
            <Button
              size="sm"
              variant="danger"
              onClick={() => handleInativar(disciplina.id)}
            >
              <PowerOff className="w-4 h-4" />
            </Button>
          )}
        </div>
      ),
    },
  ];

  const escolaOptions = escolas.map((escola) => ({
    value: escola.id,
    label: escola.nome,
  }));

  const professorOptions = professores.map((professor) => ({
    value: professor.id,
    label: professor.nomeCompleto,
  }));

  if (loading) return <Loading />;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Disciplinas</h1>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="w-5 h-5 mr-2" />
          Nova Disciplina
        </Button>
      </div>

      <Card>
        <Table columns={columns} data={disciplinas} />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingDisciplina ? 'Editar Disciplina' : 'Nova Disciplina'}
        size="lg"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Sigla"
              value={formData.sigla}
              onChange={(e) => setFormData({ ...formData, sigla: e.target.value })}
              required
            />

            <Input
              label="Nome"
              value={formData.nome}
              onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
              required
            />

            <Input
              label="Carga Horária"
              value={formData.cargaHoraria}
              onChange={(e) =>
                setFormData({ ...formData, cargaHoraria: e.target.value })
              }
              required
            />

            <Input
              label="Curso"
              value={formData.curso}
              onChange={(e) => setFormData({ ...formData, curso: e.target.value })}
              required
            />

            <Input
              label="Matriz Vinculada"
              value={formData.matrizVinculada}
              onChange={(e) =>
                setFormData({ ...formData, matrizVinculada: e.target.value })
              }
              required
            />

            <Select
              label="Escola"
              value={formData.escolaId}
              onChange={(e) =>
                setFormData({ ...formData, escolaId: e.target.value })
              }
              options={escolaOptions}
              required
            />

            <Select
              label="Professor"
              value={formData.professorId}
              onChange={(e) =>
                setFormData({ ...formData, professorId: e.target.value })
              }
              options={professorOptions}
              required
            />
          </div>

          <div className="col-span-2">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descrição
            </label>
            <textarea
              value={formData.descricao}
              onChange={(e) =>
                setFormData({ ...formData, descricao: e.target.value })
              }
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none"
              rows="4"
            />
          </div>

          <div className="flex justify-end gap-2 pt-4">
            <Button type="button" variant="secondary" onClick={handleCloseModal}>
              Cancelar
            </Button>
            <Button type="submit">
              {editingDisciplina ? 'Atualizar' : 'Criar'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Disciplinas;
