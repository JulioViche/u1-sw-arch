---
tipo: variantes
tags: [variantes, rest, soap]
---

# RESTful vs SOAP

Ambos estilos exponen las **mismas operaciones** de negocio (autenticación + conversión de
unidades); cambia la forma de exponerlas y transportarlas.

| Criterio | RESTful | SOAP |
| --- | --- | --- |
| Estilo | Arquitectónico (recursos) | Protocolo (mensajería) |
| Transporte típico | HTTP | HTTP, SMTP, etc. |
| Formato | JSON (habitual) | XML |
| Contrato | OpenAPI (opcional) | WSDL (obligatorio) |
| Operaciones | Verbos HTTP + rutas | Métodos remotos |

## Operaciones mínimas del servicio

| Operación | REST (ejemplo) | SOAP (ejemplo) |
| --- | --- | --- |
| Iniciar sesión | `POST /auth/login` | `iniciarSesion(usuario, clave)` |
| Convertir | `GET /convertir?valor=&de=&a=` | `convertir(valor, origen, destino)` |

> [!danger] Todo proyecto inicia con login
> El login es obligatorio antes de convertir. Las credenciales van **quemadas**:
> `MONSTER` / `MONSTER9`. Ver [[vault/01 - Arquitectura/Autenticación|Autenticación (login)]].

> [!note] Sin base de datos
> La autenticación valida credenciales **quemadas en el código**; no hay persistencia.
> Ver [[vault/05 - Referencia/Datos académicos|Datos académicos]].

> [!info] Relacionado
> - [[vault/03 - Entregables/04 Clientes y servidor]]
> - [[vault/02 - Variantes/Java vs .NET]]
