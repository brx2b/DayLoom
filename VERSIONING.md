# Versionamiento (SemVer)

Formato commit: `<MAJOR>.<MINOR>.<PATCH> - descripcion`

- `PATCH` (1.0.x): bug fix que no rompe nada.
  Ej: `1.0.1 - bug fix en backend`
- `MINOR` (1.x.0): nueva funcion compatible.
  Ej: `1.1.0 - nueva funcion listar personas registradas para admin`
- `MAJOR` (x.0.0): nuevo MS o cambio que rompe uso regular.
  Ej: `2.0.0 - nuevo ms notificaciones, rompe contrato gateway`

Reglas:
1. Actualizar `VERSION` y `CHANGELOG.md` en el mismo commit.
2. Crear tag `v<version>` despues del commit: `git tag v1.1.0 && git push --tags`.
3. Un solo bump por commit. No mezclar fix + feature en el mismo numero.
4. `0.x.y` = fase inicial inestable. `1.0.0` = primer MVP usable.
