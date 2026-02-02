import axios from 'axios'

const client = axios.create({
  baseURL: import.meta.env.DEV ? '/api' : 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
})

export const employees = {
  list: (activeOnly = false) => client.get('/employees', { params: { activeOnly } }),
  get: (id) => client.get(`/employees/${id}`),
  create: (data) => client.post('/employees', data),
  update: (id, data) => client.put(`/employees/${id}`, data),
  delete: (id) => client.delete(`/employees/${id}`),
}

export const shifts = {
  list: (params = {}) => client.get('/shifts', { params }),
  get: (id) => client.get(`/shifts/${id}`),
  create: (data) => client.post('/shifts', data),
  update: (id, data) => client.put(`/shifts/${id}`, data),
  delete: (id) => client.delete(`/shifts/${id}`),
}

export const availabilities = {
  list: (employeeId) => client.get('/availabilities', employeeId != null ? { params: { employeeId } } : {}),
  get: (id) => client.get(`/availabilities/${id}`),
  create: (data) => client.post('/availabilities', data),
  update: (id, data) => client.put(`/availabilities/${id}`, data),
  delete: (id) => client.delete(`/availabilities/${id}`),
}

export const vacationRequests = {
  list: (employeeId) => client.get('/vacation-requests', employeeId != null ? { params: { employeeId } } : {}),
  get: (id) => client.get(`/vacation-requests/${id}`),
  create: (data) => client.post('/vacation-requests', data),
  updateStatus: (id, body) => client.patch(`/vacation-requests/${id}/status`, body),
  delete: (id) => client.delete(`/vacation-requests/${id}`),
}

export const workLogs = {
  list: (employeeId) => client.get('/work-logs', employeeId != null ? { params: { employeeId } } : {}),
  get: (id) => client.get(`/work-logs/${id}`),
  create: (data) => client.post('/work-logs', data),
  update: (id, data) => client.put(`/work-logs/${id}`, data),
  delete: (id) => client.delete(`/work-logs/${id}`),
}

export default client
