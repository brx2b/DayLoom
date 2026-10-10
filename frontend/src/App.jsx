import { Route, Routes, Navigate } from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Home from './pages/Home.jsx'
import Tiempo from './pages/Tiempo.jsx'
import Comidas from './pages/Comidas.jsx'
import Notas from './pages/Notas.jsx'
import Gastos from './pages/Gastos.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/" element={<ProtectedRoute><Home /></ProtectedRoute>} />
      <Route path="/tiempo" element={<ProtectedRoute><Tiempo /></ProtectedRoute>} />
      <Route path="/comidas" element={<ProtectedRoute><Comidas /></ProtectedRoute>} />
      <Route path="/notas" element={<ProtectedRoute><Notas /></ProtectedRoute>} />
      <Route path="/gastos" element={<ProtectedRoute><Gastos /></ProtectedRoute>} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
