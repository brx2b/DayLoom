# Changelog

Formato de commit: `<version> - <descripcion>`
Ejemplo: `1.0.1 - bug fix en backend`, `1.1.0 - nueva funcion listar personas para admin`.

## [Unreleased]

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
