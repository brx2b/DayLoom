import { useCallback, useEffect, useState } from 'react'
import * as svc from '../services/habits.service.js'

const today = () => new Date().toISOString().slice(0, 10)
const TYPES = ['DESAYUNO', 'ALMUERZO', 'CENA', 'SNACK']

export default function Comidas() {
  const [date, setDate] = useState(today())
  const [items, setItems] = useState([])
  const [frequent, setFrequent] = useState([])
  const [form, setForm] = useState({ time: '', name: '', quantity: '', type: 'DESAYUNO' })
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    setError('')
    try {
      const [f, q] = await Promise.all([svc.listFood(date), svc.frequentFood()])
      setItems(f)
      setFrequent(q)
    } catch {
      setError('no se pudo cargar')
    }
  }, [date])

  useEffect(() => { load() }, [load])

  async function add(e) {
    e.preventDefault()
    try {
      await svc.createFood({ ...form, date })
      setForm({ time: '', name: '', quantity: '', type: 'DESAYUNO' })
      load()
    } catch {
      setError('revisa los datos')
    }
  }

  return (
    <main className="page">
      <h1>Comidas</h1>
      {error && <p className="error">{error}</p>}
      <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
      <ul>
        {items.map((f) => (
          <li key={f.id}>
            {f.time} {f.name} {f.quantity && `(${f.quantity})`} [{f.type}]
            <button onClick={() => svc.deleteFood(f.id).then(load)}>x</button>
          </li>
        ))}
      </ul>
      <h3>Agregar comida</h3>
      <form onSubmit={add}>
        <input type="time" value={form.time} onChange={(e) => setForm({ ...form, time: e.target.value })} required />
        <input placeholder="comida" list="freq" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
        <datalist id="freq">
          {frequent.map((f) => <option key={f.name} value={f.name} />)}
        </datalist>
        <input placeholder="cantidad" value={form.quantity} onChange={(e) => setForm({ ...form, quantity: e.target.value })} />
        <select value={form.type} onChange={(e) => setForm({ ...form, type: e.target.value })}>
          {TYPES.map((t) => <option key={t} value={t}>{t}</option>)}
        </select>
        <button>Agregar</button>
      </form>
    </main>
  )
}
