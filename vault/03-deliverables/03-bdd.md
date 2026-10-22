---
tipo: entregable
tags: [entregable, bdd]
---

# 03 · BDD

Carpeta `03. BDD/` de cada variante.

> [!danger] El proyecto NO usa base de datos
> La variante es explícitamente **SINBDD**. No se crea esquema, no hay scripts SQL como
> requisito y no se usa ORM. Por eso esta carpeta queda **vacía**.

## Qué deja el scaffold

`03. BDD/` permanece **vacía**: solo contiene su archivo `.gitkeep` (para que git conserve
la carpeta). No se agregan esquemas ni scripts SQL.

Las credenciales y los factores de conversión son **fijos / en memoria**. Ver
[[vault/03-deliverables/02-mer|02 · MER]], que también queda vacío por el mismo motivo.

## Credenciales quemadas (referencia)

Como no hay base de datos, el login valida contra valores **quemados en el código**:

| Dato | Valor |
| --- | --- |
| Usuario | `MONSTER` |
| Contraseña | `MONSTER9` |

Ver [[vault/01-architecture/authentication|Autenticación (login)]].

> [!warning]
> Verifica que la documentación (manual, ERS) **no** prometa persistencia. Si necesitas
> datos, defínelos como constantes en el código.

> [!info] Relacionado
> - [[vault/03-deliverables/02-mer]]
> - [[vault/05-reference/academic-data]]
