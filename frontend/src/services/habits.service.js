import api from './api.js'

export const listActivities = (date) =>
  api.get('/api/time/activities', { params: { date } }).then((r) => r.data)
export const createActivity = (body) =>
  api.post('/api/time/activities', body).then((r) => r.data)
export const deleteActivity = (id) =>
  api.delete(`/api/time/activities/${id}`).then((r) => r.data)

export const listRoutines = () =>
  api.get('/api/time/routines').then((r) => r.data)
export const createRoutine = (body) =>
  api.post('/api/time/routines', body).then((r) => r.data)
export const deleteRoutine = (id) =>
  api.delete(`/api/time/routines/${id}`).then((r) => r.data)
export const applyRoutine = (id) =>
  api.post(`/api/time/routines/${id}/apply-today`).then((r) => r.data)

export const listFood = (date) =>
  api.get('/api/food', { params: { date } }).then((r) => r.data)
export const createFood = (body) =>
  api.post('/api/food', body).then((r) => r.data)
export const deleteFood = (id) =>
  api.delete(`/api/food/${id}`).then((r) => r.data)
export const frequentFood = () =>
  api.get('/api/food/frequent').then((r) => r.data)
