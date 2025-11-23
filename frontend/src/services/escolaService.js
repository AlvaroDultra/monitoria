import api from './api';

export const escolaService = {
  async getAll() {
    const response = await api.get('/admin/escolas');
    return response.data;
  },

  async getAtivas() {
    const response = await api.get('/admin/escolas/ativas');
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/admin/escolas/${id}`);
    return response.data;
  },

  async create(escolaData) {
    const response = await api.post('/admin/escolas', escolaData);
    return response.data;
  },

  async update(id, escolaData) {
    const response = await api.put(`/admin/escolas/${id}`, escolaData);
    return response.data;
  },

  async inativar(id) {
    const response = await api.patch(`/admin/escolas/${id}/inativar`);
    return response.data;
  },

  async ativar(id) {
    const response = await api.patch(`/admin/escolas/${id}/ativar`);
    return response.data;
  },

  async deletar(id) {
    const response = await api.delete(`/admin/escolas/${id}`);
    return response.data;
  },
};
