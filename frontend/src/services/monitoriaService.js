import api from './api';

export const monitoriaService = {
  async getAll() {
    const response = await api.get('/professor/monitorias');
    return response.data;
  },

  async getEmAndamento() {
    const response = await api.get('/professor/monitorias/em-andamento');
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/professor/monitorias/${id}`);
    return response.data;
  },

  async create(monitoriaData) {
    const response = await api.post('/professor/monitorias', monitoriaData);
    return response.data;
  },

  async update(id, monitoriaData) {
    const response = await api.put(`/professor/monitorias/${id}`, monitoriaData);
    return response.data;
  },

  async finalizar(id) {
    const response = await api.patch(`/professor/monitorias/${id}/finalizar`);
    return response.data;
  },

  async associarAluno(alunoId, monitoriaId) {
    const response = await api.post('/professor/monitorias/associar-aluno', {
      alunoId,
      monitoriaId,
    });
    return response.data;
  },

  async removerAluno(monitorId) {
    const response = await api.delete(`/professor/monitorias/monitor/${monitorId}`);
    return response.data;
  },

  async getQuantidadeAlunos(id) {
    const response = await api.get(`/professor/monitorias/${id}/quantidade-alunos`);
    return response.data;
  },
};
