import api from "./api";

export const adminService = {
  list: async (endpoint) => (await api.get(endpoint)).data,
  getOne: async (endpoint, id) => (await api.get(`${endpoint}/${id}`)).data,
  create: async (endpoint, payload) => (await api.post(endpoint, payload)).data,
  update: async (endpoint, id, payload) => (await api.put(`${endpoint}/${id}`, payload)).data,
  remove: async (endpoint, id) => (await api.delete(`${endpoint}/${id}`)).data,
  patch: async (url, payload) => (await api.patch(url, payload)).data,
};
