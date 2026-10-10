import api from './api.js'

export const listNotes = () =>
  api.get('/api/notes').then((r) => r.data)
export const upcomingNotes = (days = 7) =>
  api.get('/api/notes/upcoming', { params: { days } }).then((r) => r.data)
export const createNote = (body) =>
  api.post('/api/notes', body).then((r) => r.data)
export const updateNote = (id, body) =>
  api.put(`/api/notes/${id}`, body).then((r) => r.data)
export const deleteNote = (id) =>
  api.delete(`/api/notes/${id}`).then((r) => r.data)

export const listExpenses = (month) =>
  api.get('/api/expenses', { params: { month } }).then((r) => r.data)
export const summaryExpenses = (month) =>
  api.get('/api/expenses/summary', { params: { month } }).then((r) => r.data)
export const createExpense = (body) =>
  api.post('/api/expenses', body).then((r) => r.data)
export const deleteExpense = (id) =>
  api.delete(`/api/expenses/${id}`).then((r) => r.data)
