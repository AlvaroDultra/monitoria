import api from './api';

export const alunoService = {
  async getAll() {
    const response = await api.get('/professor/alunos');
    return response.data;
  },

  async getByDisciplina(disciplinaId) {
    const response = await api.get(`/professor/alunos/disciplina/${disciplinaId}`);
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/professor/alunos/${id}`);
    return response.data;
  },

  async create(alunoData) {
    const response = await api.post('/professor/alunos', alunoData);
    return response.data;
  },

  async update(id, alunoData) {
    const response = await api.put(`/professor/alunos/${id}`, alunoData);
    return response.data;
  },

  async inativar(id) {
    const response = await api.patch(`/professor/alunos/${id}/inativar`);
    return response.data;
  },
};
