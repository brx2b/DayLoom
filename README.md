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
```powershell
.\dev.ps1            # backend (solo gateway publica puerto) + frontend
.\dev.ps1 -Build     # reconstruye imagenes
```
Frontend:
```bash
cd frontend
npm install
npm run dev
```

## Acceso Tailnet (admin)
El gateway solo escucha en `127.0.0.1` y los MS no publican puertos.
Se expone con (solo tailnet, no publico):
```bash
tailscale serve --bg http://127.0.0.1:8090
# => https://brx.tail9dde4c.ts.net
```
Las rutas `/api/admin/*` y `/api/auth/admin/*` exigen una de:
- IP en `ADMIN_TAILNET_IPS`, o
- header `Tailscale-User-Login` en `ADMIN_TAILNET_LOGINS` llegando por loopback
  (lo pone tailscaled via serve; desde red es infalsificable porque el puerto
  no esta publicado).

## Versionamiento
SemVer con formato `<version> - descripcion`. Ver `VERSIONING.md` y `CHANGELOG.md`.
