import { useContext, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { AuthContext } from '../context/AuthContext.jsx'
import { errorMessage } from '../services/auth.service.js'

export default function Login() {
  const { login } = useContext(AuthContext)
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
      await login(email.trim(), password)
      navigate('/', { replace: true })
    } catch (err) {
      setError(errorMessage(err))
    } finally {
      setBusy(false)
    }
  }

  return (
    <main className="auth">
      <h1>DayLoom</h1>
      <h2>Entrar</h2>
      <form onSubmit={onSubmit}>
        <input type="email" placeholder="correo" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <input type="password" placeholder="contraseña" value={password} onChange={(e) => setPassword(e.target.value)} required />
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={busy}>{busy ? '...' : 'Entrar'}</button>
      </form>
      <p>¿Sin cuenta? <Link to="/register">Regístrate</Link></p>
    </main>
  )
}
