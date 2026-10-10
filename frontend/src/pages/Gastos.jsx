import { useCallback, useEffect, useState } from 'react'
import * as svc from '../services/planner.service.js'

const thisMonth = () => new Date().toISOString().slice(0, 7)

export default function Gastos() {
  const [month, setMonth] = useState(thisMonth())
  const [items, setItems] = useState([])
  const [summary, setSummary] = useState(null)
  const [form, setForm] = useState({ date: new Date().toISOString().slice(0, 10), amount: '', category: '', note: '' })
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    setError('')
    try {
      const [l, s] = await Promise.all([svc.listExpenses(month), svc.summaryExpenses(month)])
      setItems(l)
      setSummary(s)
    } catch {
      setError('no se pudo cargar')
    }
  }, [month])

  useEffect(() => { load() }, [load])

  async function add(e) {
    e.preventDefault()
    try {
      await svc.createExpense({ ...form, amount: Number(form.amount) })
      setForm({ date: new Date().toISOString().slice(0, 10), amount: '', category: '', note: '' })
      load()
    } catch {
      setError('revisa los datos (monto mayor que 0)')
    }
  }

  return (
    <main className="page">
      <h1>Gastos</h1>
      {error && <p className="error">{error}</p>}
      <input type="month" value={month} onChange={(e) => setMonth(e.target.value)} />
      {summary && (
        <p>Total {month}: <strong>{summary.total}</strong> en {summary.count} gastos</p>
      )}
      {summary && summary.byCategory.length > 0 && (
        <ul>
          {summary.byCategory.map((c) => <li key={c.category}>{c.category}: {c.total}</li>)}
        </ul>
      )}
      <ul>
        {items.map((g) => (
          <li key={g.id}>
            {g.date} {g.amount} [{g.category}] {g.note}
            <button onClick={() => svc.deleteExpense(g.id).then(load)}>x</button>
          </li>
        ))}
      </ul>
      <h3>Agregar gasto</h3>
      <form onSubmit={add}>
        <input type="date" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} required />
        <input type="number" step="0.01" min="0.01" placeholder="monto" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} required />
        <input placeholder="categoria" value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} required />
        <input placeholder="nota" value={form.note} onChange={(e) => setForm({ ...form, note: e.target.value })} />
        <button>Agregar</button>
      </form>
    </main>
  )
}
