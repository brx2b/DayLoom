import { useContext, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { AuthContext } from '../context/AuthContext.jsx'
import { errorMessage } from '../services/auth.service.js'

export default function Register() {
  const { register } = useContext(AuthContext)
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function onSubmit(e) {
    e.preventDefault()
    setError('')
    if (password.length < 8) {
      setError('la contraseña debe tener al menos 8 caracteres')
      return
    }
    setBusy(true)
    try {
      await register(email.trim(), password, name.trim())
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
      <h2>Crear cuenta</h2>
      <form onSubmit={onSubmit}>
        <input placeholder="nombre" value={name} onChange={(e) => setName(e.target.value)} required maxLength={100} />
        <input type="email" placeholder="correo" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <input type="password" placeholder="contraseña (min 8)" value={password} onChange={(e) => setPassword(e.target.value)} required />
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={busy}>{busy ? '...' : 'Registrarse'}</button>
      </form>
      <p>¿Ya tienes cuenta? <Link to="/login">Entra</Link></p>
    </main>
  )
}
