import { useCallback, useEffect, useState } from 'react'
import * as svc from '../services/admin.service.js'

const emptyEdit = { id: null, name: '', email: '', password: '', role: 'USER' }

export default function Admin() {
  const [users, setUsers] = useState([])
  const [edit, setEdit] = useState(emptyEdit)
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    setError('')
    try {
      setUsers(await svc.listUsers())
    } catch (err) {
      setError(svc.errorMessage(err))
    }
  }, [])

  useEffect(() => { load() }, [load])

  function startEdit(u) {
    setEdit({ id: u.id, name: u.name, email: u.email, password: '', role: u.role })
  }

  async function saveEdit(e) {
    e.preventDefault()
    const body = { name: edit.name, email: edit.email, role: edit.role }
    if (edit.password) body.password = edit.password
    try {
      await svc.updateUser(edit.id, body)
      setEdit(emptyEdit)
      load()
    } catch (err) {
      setError(svc.errorMessage(err))
    }
  }

  async function remove(id) {
    if (!window.confirm('¿Eliminar usuario?')) return
    try {
      await svc.deleteUser(id)
      load()
    } catch (err) {
      setError(svc.errorMessage(err))
    }
  }

  return (
    <main className="page">
      <h1>Admin - Usuarios</h1>
      {error && <p className="error">{error}</p>}
      <ul>
        {users.map((u) => (
          <li key={u.id}>
            {u.email} - {u.name} [{u.role}]
            <button onClick={() => startEdit(u)}>editar</button>
            <button onClick={() => remove(u.id)}>x</button>
          </li>
        ))}
      </ul>
      {edit.id && (
        <form onSubmit={saveEdit}>
          <h3>Editar {edit.email}</h3>
          <input placeholder="nombre" value={edit.name} onChange={(e) => setEdit({ ...edit, name: e.target.value })} required />
          <input type="email" placeholder="correo" value={edit.email} onChange={(e) => setEdit({ ...edit, email: e.target.value })} required />
          <input type="password" placeholder="nueva contraseña (vacío = no cambia)" value={edit.password} onChange={(e) => setEdit({ ...edit, password: e.target.value })} />
          <select value={edit.role} onChange={(e) => setEdit({ ...edit, role: e.target.value })}>
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
          </select>
          <button>Guardar</button>
          <button type="button" onClick={() => setEdit(emptyEdit)}>Cancelar</button>
        </form>
      )}
    </main>
  )
}
