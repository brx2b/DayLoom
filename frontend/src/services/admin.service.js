import api from './api.js'

export const listUsers = () =>
  api.get('/api/admin/users').then((r) => r.data)
export const updateUser = (id, body) =>
  api.patch(`/api/admin/users/${id}`, body).then((r) => r.data)
export const deleteUser = (id) =>
  api.delete(`/api/admin/users/${id}`).then((r) => r.data)

export function errorMessage(err) {
  const res = err?.response
  if (!res) return 'sin conexion con el backend'
  if (res.status === 403 && res.data?.error === 'admin solo disponible en Tailnet') {
    return 'admin solo disponible en Tailnet: conectate a la VPN para gestionar usuarios'
  }
  return res.data?.error || `error ${res.status}`
}
