import { useContext } from 'react'
import { AuthContext } from '../context/AuthContext.jsx'

export default function Home() {
  const { user, logout } = useContext(AuthContext)
  return (
    <main className="home">
      <h1>Hoy</h1>
      <p>Hola, {user?.name} ({user?.email}) [{user?.role}]</p>
      <p>Aquí irá el resumen del día: tiempo, comidas, notas y gastos.</p>
      <button onClick={logout}>Salir</button>
    </main>
  )
}
