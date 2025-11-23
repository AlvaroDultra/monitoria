import api from './api';

export const disciplinaService = {
  async getAll() {
    const response = await api.get('/admin/disciplinas');
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/admin/disciplinas/${id}`);
    return response.data;
  },

  async create(disciplinaData) {
    const response = await api.post('/admin/disciplinas', disciplinaData);
    return response.data;
  },

  async update(id, disciplinaData) {
    const response = await api.put(`/admin/disciplinas/${id}`, disciplinaData);
    return response.data;
  },

  async inativar(id) {
    const response = await api.patch(`/admin/disciplinas/${id}/inativar`);
    return response.data;
  },

  async ativar(id) {
    const response = await api.patch(`/admin/disciplinas/${id}/ativar`);
    return response.data;
  },
};
