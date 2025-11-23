import { useState, useEffect } from 'react';
import { Plus, Edit, PowerOff } from 'lucide-react';
import Card from '../../components/common/Card';
import Table from '../../components/common/Table';
import Button from '../../components/common/Button';
import Modal from '../../components/common/Modal';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Loading from '../../components/common/Loading';
import { alunoService } from '../../services/alunoService';
import { disciplinaService } from '../../services/disciplinaService';

const Alunos = () => {
  const [alunos, setAlunos] = useState([]);
  const [disciplinas, setDisciplinas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingAluno, setEditingAluno] = useState(null);
  const [formData, setFormData] = useState({
    matricula: '',
    nomeCompleto: '',
    disciplinaId: '',
    semestre: '',
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [alunosData, disciplinasData] = await Promise.all([
        alunoService.getAll(),
        disciplinaService.getAll(),
      ]);
      setAlunos(alunosData);
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
      if (editingAluno) {
        await alunoService.update(editingAluno.id, formData);
      } else {
        await alunoService.create(formData);
      }
      loadData();
      handleCloseModal();
    } catch (error) {
      console.error('Erro ao salvar aluno:', error);
    }
  };

  const handleEdit = (aluno) => {
    setEditingAluno(aluno);
    setFormData({
      matricula: aluno.matricula,
      nomeCompleto: aluno.nomeCompleto,
      disciplinaId: aluno.disciplinaId,
      semestre: aluno.semestre,
    });
    setIsModalOpen(true);
  };

  const handleInativar = async (id) => {
    if (window.confirm('Tem certeza que deseja inativar este aluno?')) {
      try {
        await alunoService.inativar(id);
        loadData();
      } catch (error) {
        console.error('Erro ao inativar aluno:', error);
      }
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingAluno(null);
    setFormData({
      matricula: '',
      nomeCompleto: '',
      disciplinaId: '',
      semestre: '',
    });
  };

  const columns = [
    { key: 'matricula', label: 'Matrícula' },
    { key: 'nomeCompleto', label: 'Nome Completo' },
    { key: 'disciplinaNome', label: 'Disciplina' },
    { key: 'semestre', label: 'Semestre' },
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
      render: (_, aluno) => (
        <div className="flex gap-2">
          <Button
            size="sm"
            variant="outline"
            onClick={() => handleEdit(aluno)}
          >
            <Edit className="w-4 h-4" />
          </Button>
          {aluno.ativo && (
            <Button
              size="sm"
              variant="danger"
              onClick={() => handleInativar(aluno.id)}
            >
              <PowerOff className="w-4 h-4" />
            </Button>
          )}
        </div>
      ),
    },
  ];

  const disciplinaOptions = disciplinas.map((disciplina) => ({
    value: disciplina.id,
    label: disciplina.nome,
  }));

  if (loading) return <Loading />;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Meus Alunos</h1>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="w-5 h-5 mr-2" />
          Novo Aluno
        </Button>
      </div>

      <Card>
        <Table columns={columns} data={alunos} />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingAluno ? 'Editar Aluno' : 'Novo Aluno'}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Matrícula"
            value={formData.matricula}
            onChange={(e) =>
              setFormData({ ...formData, matricula: e.target.value })
            }
            required
          />

          <Input
            label="Nome Completo"
            value={formData.nomeCompleto}
            onChange={(e) =>
              setFormData({ ...formData, nomeCompleto: e.target.value })
            }
            required
          />

          <Select
            label="Disciplina"
            value={formData.disciplinaId}
            onChange={(e) =>
              setFormData({ ...formData, disciplinaId: e.target.value })
            }
            options={disciplinaOptions}
            required
          />

          <Input
            label="Semestre"
            type="number"
            value={formData.semestre}
            onChange={(e) =>
              setFormData({ ...formData, semestre: e.target.value })
            }
          />

          <div className="flex justify-end gap-2 pt-4">
            <Button type="button" variant="secondary" onClick={handleCloseModal}>
              Cancelar
            </Button>
            <Button type="submit">
              {editingAluno ? 'Atualizar' : 'Criar'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Alunos;
