---
tipo: referencia
tags: [referencia, academico]
---

# Datos académicos

| Campo | Valor |
| --- | --- |
| Asignatura | Arquitectura de Software |
| Tarea | TI.1.1 |
| Periodo | PAO 2026-51 |
| NRC | 36884 |
| Grupo | GR09 |
| Integrante | Julio Enrique Viche Castillo |
| Repositorio | u1-arch |

## Caso de estudio

**Conversor de unidades** con inicio de sesión, implementado en cuatro variantes
(RESTful/SOAP × Java/.NET) y **sin base de datos**.

- Magnitudes sugeridas: longitud, masa, temperatura.
- Autenticación con credenciales fijas/en memoria.
- `02. MER/` y `03. BDD/` quedan **vacías** por ser SINBDD.

## Credenciales (quemadas)

Todo proyecto **inicia con login**. Para esta unidad las credenciales van **quemadas en el
código** (hardcoded, sin base de datos):

| Usuario | Contraseña |
| --- | --- |
| `MONSTER` | `MONSTER9` |

Ver [Autenticación (login)](../01-architecture/authentication.md).

## Solución de referencia (`examples/`)

Contiene una implementación **SOAP + Java** que sirve como modelo de estructura y
nomenclatura. Es material de **solo lectura** e ignorado por git (`.gitignore`).

> [!warning]
> No copies a ciegas ni modifiques `examples/`. Úsalo como referencia.

> [!info] Relacionado
> - [naming-conventions](../01-architecture/naming-conventions.md)
> - [03-bdd](../03-deliverables/03-bdd.md)
