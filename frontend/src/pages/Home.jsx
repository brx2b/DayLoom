import { useContext } from 'react'
import { Link } from 'react-router-dom'
import { AuthContext } from '../context/AuthContext.jsx'

export default function Home() {
  const { user, logout } = useContext(AuthContext)
  return (
    <main className="home">
      <h1>Hoy</h1>
      <p>Hola, {user?.name} ({user?.email}) [{user?.role}]</p>
      <nav>
        <Link to="/tiempo">Tiempo</Link> | <Link to="/comidas">Comidas</Link> | <Link to="/notas">Notas</Link> | <Link to="/gastos">Gastos</Link>
        {user?.role === 'ADMIN' && <> | <Link to="/admin">Admin</Link></>}
      </nav>
      <p>Aquí irá el resumen del día: tiempo, comidas, notas y gastos.</p>
      <button onClick={logout}>Salir</button>
    </main>
  )
}
