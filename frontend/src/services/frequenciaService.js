import api from './api';

export const frequenciaService = {
  async registrar(frequenciaData) {
    const response = await api.post('/professor/frequencias', frequenciaData);
    return response.data;
  },

  async atualizar(frequenciaId, presente) {
    const response = await api.patch(`/professor/frequencias/${frequenciaId}?presente=${presente}`);
    return response.data;
  },
};
