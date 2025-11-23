import { useState, useEffect } from 'react';
import { Plus, Edit, Power, PowerOff, Trash2 } from 'lucide-react';
import Card from '../../components/common/Card';
import Table from '../../components/common/Table';
import Button from '../../components/common/Button';
import Modal from '../../components/common/Modal';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Loading from '../../components/common/Loading';
import { escolaService } from '../../services/escolaService';
import { TIPO_ESCOLA } from '../../constants/enums';

const Escolas = () => {
  const [escolas, setEscolas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingEscola, setEditingEscola] = useState(null);
  const [formData, setFormData] = useState({
    nome: '',
    tipo: '',
  });

  useEffect(() => {
    loadEscolas();
  }, []);

  const loadEscolas = async () => {
    try {
      const data = await escolaService.getAll();
      setEscolas(data);
    } catch (error) {
      console.error('Erro ao carregar escolas:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingEscola) {
        await escolaService.update(editingEscola.id, formData);
      } else {
        await escolaService.create(formData);
      }
      loadEscolas();
      handleCloseModal();
    } catch (error) {
      console.error('Erro ao salvar escola:', error);
    }
  };

  const handleEdit = (escola) => {
    setEditingEscola(escola);
    setFormData({
      nome: escola.nome,
      tipo: escola.tipo,
    });
    setIsModalOpen(true);
  };

  const handleToggleStatus = async (escola) => {
    try {
      if (escola.ativo) {
        await escolaService.inativar(escola.id);
      } else {
        await escolaService.ativar(escola.id);
      }
      loadEscolas();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  };

  const handleDeletar = async (id, nome) => {
    if (window.confirm(`Tem certeza que deseja deletar a escola "${nome}"? Esta ação não pode ser desfeita.`)) {
      try {
        await escolaService.deletar(id);
        loadEscolas();
      } catch (error) {
        console.error('Erro ao deletar escola:', error);
        alert('Erro ao deletar escola. Ela pode estar vinculada a outras entidades.');
      }
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingEscola(null);
    setFormData({ nome: '', tipo: '' });
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'tipo', label: 'Tipo' },
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
      render: (_, escola) => (
        <div className="flex gap-2">
          <Button
            size="sm"
            variant="outline"
            onClick={() => handleEdit(escola)}
            title="Editar"
          >
            <Edit className="w-4 h-4" />
          </Button>
          <Button
            size="sm"
            variant={escola.ativo ? 'danger' : 'success'}
            onClick={() => handleToggleStatus(escola)}
            title={escola.ativo ? 'Inativar' : 'Ativar'}
          >
            {escola.ativo ? (
              <PowerOff className="w-4 h-4" />
            ) : (
              <Power className="w-4 h-4" />
            )}
          </Button>
          <Button
            size="sm"
            variant="danger"
            onClick={() => handleDeletar(escola.id, escola.nome)}
            title="Deletar"
          >
            <Trash2 className="w-4 h-4" />
          </Button>
        </div>
      ),
    },
  ];

  const tipoOptions = Object.entries(TIPO_ESCOLA).map(([key, value]) => ({
    value: key,
    label: value,
  }));

  if (loading) return <Loading />;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Escolas</h1>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="w-5 h-5 mr-2" />
          Nova Escola
        </Button>
      </div>

      <Card>
        <Table columns={columns} data={escolas} />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingEscola ? 'Editar Escola' : 'Nova Escola'}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Nome da Escola"
            value={formData.nome}
            onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
            required
          />

          <Select
            label="Tipo de Escola"
            value={formData.tipo}
            onChange={(e) => setFormData({ ...formData, tipo: e.target.value })}
            options={tipoOptions}
            required
          />

          <div className="flex justify-end gap-2 pt-4">
            <Button type="button" variant="secondary" onClick={handleCloseModal}>
              Cancelar
            </Button>
            <Button type="submit">
              {editingEscola ? 'Atualizar' : 'Criar'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Escolas;
