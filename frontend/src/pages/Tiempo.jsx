import { useCallback, useEffect, useState } from 'react'
import * as svc from '../services/habits.service.js'

const today = () => new Date().toISOString().slice(0, 10)
const DAYS = [1, 2, 3, 4, 5, 6, 7]

export default function Tiempo() {
  const [date, setDate] = useState(today())
  const [items, setItems] = useState([])
  const [routines, setRoutines] = useState([])
  const [form, setForm] = useState({ title: '', category: '', start: '', end: '' })
  const [rform, setRform] = useState({ title: '', category: '', daysOfWeek: [] })
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    setError('')
    try {
      const [a, r] = await Promise.all([svc.listActivities(date), svc.listRoutines()])
      setItems(a)
      setRoutines(r)
    } catch {
      setError('no se pudo cargar')
    }
  }, [date])

  useEffect(() => { load() }, [load])

  async function add(e) {
    e.preventDefault()
    try {
      await svc.createActivity({ ...form, date })
      setForm({ title: '', category: '', start: '', end: '' })
      load()
    } catch {
      setError('revisa los datos (fin mayor que inicio)')
    }
  }

  function toggleDay(d) {
    setRform((f) => ({
      ...f,
      daysOfWeek: f.daysOfWeek.includes(d)
        ? f.daysOfWeek.filter((x) => x !== d)
        : [...f.daysOfWeek, d],
    }))
  }

  async function addRoutine(e) {
    e.preventDefault()
    try {
      await svc.createRoutine(rform)
      setRform({ title: '', category: '', daysOfWeek: [] })
      load()
    } catch {
      setError('rutina invalida (dias 1-7)')
    }
  }

  return (
    <main className="page">
      <h1>Tiempo</h1>
      {error && <p className="error">{error}</p>}
      <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
      <ul>
        {items.map((a) => (
          <li key={a.id}>
            {a.start}-{a.end} {a.title} {a.category && `(${a.category})`}
            <button onClick={() => svc.deleteActivity(a.id).then(load)}>x</button>
          </li>
        ))}
      </ul>
      <h3>Agregar bloque</h3>
      <form onSubmit={add}>
        <input placeholder="titulo" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required />
        <input placeholder="categoria" value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} />
        <input type="time" value={form.start} onChange={(e) => setForm({ ...form, start: e.target.value })} required />
        <input type="time" value={form.end} onChange={(e) => setForm({ ...form, end: e.target.value })} required />
        <button>Agregar</button>
      </form>
      <h2>Rutinas</h2>
      <ul>
        {routines.map((r) => (
          <li key={r.id}>
            {r.title} [{(r.daysOfWeek || []).join(',')}]
            <button onClick={() => svc.applyRoutine(r.id).then(load)}>hoy</button>
            <button onClick={() => svc.deleteRoutine(r.id).then(load)}>x</button>
          </li>
        ))}
      </ul>
      <h3>Nueva rutina</h3>
      <form onSubmit={addRoutine}>
        <input placeholder="titulo" value={rform.title} onChange={(e) => setRform({ ...rform, title: e.target.value })} required />
        <input placeholder="categoria" value={rform.category} onChange={(e) => setRform({ ...rform, category: e.target.value })} />
        <div>
          {DAYS.map((d) => (
            <label key={d}><input type="checkbox" checked={rform.daysOfWeek.includes(d)} onChange={() => toggleDay(d)} />{d}</label>
          ))}
        </div>
        <button>Guardar</button>
      </form>
    </main>
  )
}
