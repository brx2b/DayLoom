import { useContext, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { AuthContext } from '../context/AuthContext.jsx'
import { errorMessage } from '../services/auth.service.js'

export default function AdminLogin() {
  const { adminLogin } = useContext(AuthContext)
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function onSubmit(e) {
    e.preventDefault()
    setError('')
    setBusy(true)
    try {
      await adminLogin(email.trim(), password)
      navigate('/admin', { replace: true })
    } catch (err) {
      setError(errorMessage(err))
    } finally {
      setBusy(false)
    }
  }

  return (
    <main className="auth">
      <h1>DayLoom</h1>
      <h2>Acceso admin (solo Tailnet)</h2>
      <form onSubmit={onSubmit}>
        <input type="email" placeholder="correo admin" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <input type="password" placeholder="contraseña" value={password} onChange={(e) => setPassword(e.target.value)} required />
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={busy}>{busy ? '...' : 'Entrar como admin'}</button>
      </form>
      <p><Link to="/login">Volver al login</Link></p>
    </main>
  )
}
