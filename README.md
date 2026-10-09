# DayLoom

App de organización diaria: tiempo/rutina, alimentos, notas con vencimiento, gastos.

## Stack
Frontend React -> Backend Spring Boot (4 MS) -> MongoDB

## Estructura
- `frontend/` React (Vite)
- `backend/ms-gateway` :8080
- `backend/ms-identity-admin` :8081
- `backend/ms-habits` :8082
- `backend/ms-planner` :8083

## Dev local
```bash
cd backend
docker compose up --build
```
Frontend:
```bash
cd frontend
npm install
npm run dev
```
