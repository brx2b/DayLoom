import { useCallback, useEffect, useState } from 'react'
import * as svc from '../services/planner.service.js'

const nowLocal = () => {
  const d = new Date()
  const p = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`
}

export default function Notas() {
  const [items, setItems] = useState([])
  const [upcoming, setUpcoming] = useState([])
  const [form, setForm] = useState({ title: '', body: '', expiresAt: '', done: false })
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    setError('')
    try {
      const [all, up] = await Promise.all([svc.listNotes(), svc.upcomingNotes(7)])
      setItems(all)
      setUpcoming(up)
    } catch {
      setError('no se pudo cargar')
    }
  }, [])

  useEffect(() => { load() }, [load])

  async function add(e) {
    e.preventDefault()
    try {
      await svc.createNote(form)
      setForm({ title: '', body: '', expiresAt: '', done: false })
      load()
    } catch {
      setError('revisa los datos (fecha formato YYYY-MM-DDTHH:mm)')
    }
  }

  async function toggle(n) {
    await svc.updateNote(n.id, { title: n.title, body: n.body, expiresAt: n.expiresAt, done: !n.done })
    load()
  }

  const upIds = new Set(upcoming.map((n) => n.id))

  return (
    <main className="page">
      <h1>Notas</h1>
      {error && <p className="error">{error}</p>}
      <h2>Vencen pronto (7 días)</h2>
      <ul>
        {upcoming.map((n) => <li key={n.id}>{n.expiresAt} - {n.title}</li>)}
      </ul>
      <h2>Todas</h2>
      <ul>
        {items.map((n) => (
          <li key={n.id} className={upIds.has(n.id) && !n.done ? 'warn' : ''}>
            <input type="checkbox" checked={n.done} onChange={() => toggle(n)} />
            {n.expiresAt} - {n.title} {n.body && `- ${n.body}`}
            <button onClick={() => svc.deleteNote(n.id).then(load)}>x</button>
          </li>
        ))}
      </ul>
      <h3>Nueva nota</h3>
      <form onSubmit={add}>
        <input placeholder="titulo" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required maxLength={120} />
        <input placeholder="detalle" value={form.body} onChange={(e) => setForm({ ...form, body: e.target.value })} />
        <input type="datetime-local" value={form.expiresAt} onChange={(e) => setForm({ ...form, expiresAt: e.target.value })} required />
        <button>Agregar</button>
      </form>
      <p><small>Hoy: {nowLocal()}</small></p>
    </main>
  )
}
