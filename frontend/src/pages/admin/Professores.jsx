import { useState, useEffect } from 'react';
import { Plus, Power, PowerOff } from 'lucide-react';
import Card from '../../components/common/Card';
import Table from '../../components/common/Table';
import Button from '../../components/common/Button';
import Modal from '../../components/common/Modal';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Loading from '../../components/common/Loading';
import { professorService } from '../../services/professorService';
import { escolaService } from '../../services/escolaService';
import { FORMACAO_PROFESSOR } from '../../constants/enums';

const Professores = () => {
  const [professores, setProfessores] = useState([]);
  const [escolas, setEscolas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    numeroRegistro: '',
    nomeCompleto: '',
    formacao: '',
    nomeInstituicao: '',
    nomeCurso: '',
    anoConclusao: '',
    escolaId: '',
    username: '',
    email: '',
    password: '',
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [professoresData, escolasData] = await Promise.all([
        professorService.getAll(),
        escolaService.getAtivas(),
      ]);
      setProfessores(professoresData);
      setEscolas(escolasData);
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await professorService.create(formData);
      loadData();
      handleCloseModal();
    } catch (error) {
      console.error('Erro ao criar professor:', error);
    }
  };

  const handleToggleStatus = async (professor) => {
    try {
      if (professor.ativo) {
        await professorService.inativar(professor.id);
      } else {
        await professorService.ativar(professor.id);
      }
      loadData();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setFormData({
      numeroRegistro: '',
      nomeCompleto: '',
      formacao: '',
      nomeInstituicao: '',
      nomeCurso: '',
      anoConclusao: '',
      escolaId: '',
      username: '',
      email: '',
      password: '',
    });
  };

  const columns = [
    { key: 'numeroRegistro', label: 'Registro' },
    { key: 'nomeCompleto', label: 'Nome' },
    { key: 'formacao', label: 'Formação' },
    { key: 'escolaNome', label: 'Escola' },
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
      render: (_, professor) => (
        <Button
          size="sm"
          variant={professor.ativo ? 'danger' : 'success'}
          onClick={() => handleToggleStatus(professor)}
        >
          {professor.ativo ? (
            <PowerOff className="w-4 h-4" />
          ) : (
            <Power className="w-4 h-4" />
          )}
        </Button>
      ),
    },
  ];

  const formacaoOptions = Object.entries(FORMACAO_PROFESSOR).map(([key, value]) => ({
    value: key,
    label: value,
  }));

  const escolaOptions = escolas.map((escola) => ({
    value: escola.id,
    label: escola.nome,
  }));

  if (loading) return <Loading />;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Professores</h1>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="w-5 h-5 mr-2" />
          Novo Professor
        </Button>
      </div>

      <Card>
        <Table columns={columns} data={professores} />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title="Novo Professor"
        size="lg"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Número de Registro"
              value={formData.numeroRegistro}
              onChange={(e) =>
                setFormData({ ...formData, numeroRegistro: e.target.value })
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
              label="Formação"
              value={formData.formacao}
              onChange={(e) =>
                setFormData({ ...formData, formacao: e.target.value })
              }
              options={formacaoOptions}
              required
            />

            <Input
              label="Instituição"
              value={formData.nomeInstituicao}
              onChange={(e) =>
                setFormData({ ...formData, nomeInstituicao: e.target.value })
              }
              required
            />

            <Input
              label="Curso"
              value={formData.nomeCurso}
              onChange={(e) =>
                setFormData({ ...formData, nomeCurso: e.target.value })
              }
              required
            />

            <Input
              label="Ano de Conclusão"
              type="number"
              value={formData.anoConclusao}
              onChange={(e) =>
                setFormData({ ...formData, anoConclusao: e.target.value })
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

            <Input
              label="Usuário"
              value={formData.username}
              onChange={(e) =>
                setFormData({ ...formData, username: e.target.value })
              }
              required
            />

            <Input
              label="Email"
              type="email"
              value={formData.email}
              onChange={(e) =>
                setFormData({ ...formData, email: e.target.value })
              }
              required
            />

            <Input
              label="Senha"
              type="password"
              value={formData.password}
              onChange={(e) =>
                setFormData({ ...formData, password: e.target.value })
              }
              required
            />
          </div>

          <div className="flex justify-end gap-2 pt-4">
            <Button type="button" variant="secondary" onClick={handleCloseModal}>
              Cancelar
            </Button>
            <Button type="submit">Criar</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Professores;
