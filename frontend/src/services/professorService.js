import api from './api';

export const professorService = {
  async getAll() {
    const response = await api.get('/admin/professores');
    return response.data;
  },

  async getAtivos() {
    const response = await api.get('/admin/professores/ativos');
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/admin/professores/${id}`);
    return response.data;
  },

  async create(professorData) {
    const response = await api.post('/admin/professores', professorData);
    return response.data;
  },

  async inativar(id) {
    const response = await api.patch(`/admin/professores/${id}/inativar`);
    return response.data;
  },

  async ativar(id) {
    const response = await api.patch(`/admin/professores/${id}/ativar`);
    return response.data;
  },
};
