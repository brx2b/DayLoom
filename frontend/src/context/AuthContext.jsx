import { createContext, useCallback, useState } from 'react'
import * as authService from '../services/auth.service.js'

export const AuthContext = createContext(null)

function loadSession() {
  const token = localStorage.getItem('token')
  if (!token) return { token: null, user: null }
  return {
    token,
    user: {
      email: localStorage.getItem('email'),
      name: localStorage.getItem('name'),
      role: localStorage.getItem('role'),
    },
  }
}

function saveSession(data) {
  localStorage.setItem('token', data.token)
  localStorage.setItem('email', data.email)
  localStorage.setItem('name', data.name)
  localStorage.setItem('role', data.role)
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(loadSession)

  const apply = useCallback((data) => {
    saveSession(data)
    setSession({ token: data.token, user: { email: data.email, name: data.name, role: data.role } })
  }, [])

  const register = useCallback(async (email, password, name) => {
    apply(await authService.register(email, password, name))
  }, [apply])

  const login = useCallback(async (email, password) => {
    apply(await authService.login(email, password))
  }, [apply])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('email')
    localStorage.removeItem('name')
    localStorage.removeItem('role')
    setSession({ token: null, user: null })
  }, [])

  return (
    <AuthContext.Provider value={{ ...session, register, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}
