import api from './api';

export const authService = {
  async login(username, password) {
    const response = await api.post('/auth/login', {
      username,
      password,
    });

    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify({
        username: response.data.username,
        email: response.data.email,
        role: response.data.role,
      }));
    }

    return response.data;
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser() {
    const userStr = localStorage.getItem('user');
    if (userStr) return JSON.parse(userStr);
    return null;
  },

  isAuthenticated() {
    return !!localStorage.getItem('token');
  },

  hasRole(role) {
    const user = this.getCurrentUser();
    return user?.role === role;
  },

  isAdmin() {
    return this.hasRole('ROLE_ADMIN');
  },

  isProfessor() {
    return this.hasRole('ROLE_PROFESSOR');
  },
};
