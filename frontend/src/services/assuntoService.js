import api from './api';

export const assuntoService = {
  async registrar(assuntoData) {
    const response = await api.post('/professor/assuntos', assuntoData);
    return response.data;
  },

  async getByMonitoria(monitoriaId) {
    const response = await api.get(`/professor/assuntos/monitoria/${monitoriaId}`);
    return response.data;
  },

  async excluir(assuntoId) {
    const response = await api.delete(`/professor/assuntos/${assuntoId}`);
    return response.data;
  },
};
