# Changelog

Formato de commit: `<version> - <descripcion>`
Ejemplo: `1.0.1 - bug fix en backend`, `1.1.0 - nueva funcion listar personas para admin`.

## [Unreleased]

## [0.8.0] - 2026-10-10
- admin solo por /api/auth/admin/login; login publico rechaza cuentas ADMIN.
- gateway exige Tailnet tambien en /api/auth/admin/**. front: pagina /admin/login.

## [0.7.0] - 2026-10-10
- frontend: panel admin /admin (listar, editar, eliminar, solo rol ADMIN).

## [0.6.0] - 2026-10-10
- ms-planner: notas CRUD + upcoming?days=, gastos CRUD por mes + summary por categoria.
- frontend: paginas Notas y Gastos basicas. dev.ps1 incluye planner.

## [0.5.0] - 2026-10-09
- ms-habits: actividades/rutinas/comidas CRUD por fecha, apply-today, frecuentes, GET single, 401 sin token.
- frontend: paginas Tiempo y Comidas basicas. dev.ps1 incluye habits.

## [0.4.1] - 2026-10-09
- dev.ps1 en raiz: levanta mongo + identity, espera health y parte el frontend.

## [0.4.0] - 2026-10-09
- frontend: login/registro/home con React Router + AuthContext, api contra gateway.
- identity: CORS explicito (origenes por CORS_ORIGINS); gateway: globalcors.

## [0.3.0] - 2026-10-09
- ms-gateway: JWT global, TailnetFilter bloquea /api/admin/** fuera de 100.64.0.0/10, fail-fast rol ADMIN.
- ms-identity-admin: CRUD admin (list/get/patch/delete, auditLogs, seed ADMIN_*), fix /error permitAll.
- compose: ADMIN_EMAIL/PASSWORD passthrough.

## [0.2.1] - 2026-10-09
- compose: MONGODB_URI por servicio overridible (MONGODB_URI_*DB) para Atlas, default local.

## [0.2.0] - 2026-10-09
- ms-identity-admin: registro, login con JWT + BCrypt, GET /api/users/me.
- Conexion MongoDB por MONGODB_URI (authdb). Rutas con prefijo /api para gateway.

## [0.1.0] - 2026-10-09
- Esqueleto inicial: frontend React + 4 MS Spring Boot + docker-compose.
- Versionamiento SemVer inicial.
