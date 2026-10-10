import api from './api.js'

export async function register(email, password, name) {
  const { data } = await api.post('/api/auth/register', { email, password, name })
  return data
}

export async function login(email, password) {
  const { data } = await api.post('/api/auth/login', { email, password })
  return data
}

export async function adminLogin(email, password) {
  const { data } = await api.post('/api/auth/admin/login', { email, password })
  return data
}

export async function me() {
  const { data } = await api.get('/api/users/me')
  return data
}

export function errorMessage(err) {
  const res = err?.response
  if (!res) return 'sin conexion con el backend'
  if (res.status === 409) return 'email ya registrado'
  if (res.status === 401) return 'credenciales invalidas'
  return res.data?.error || res.data?.message || `error ${res.status}`
}
