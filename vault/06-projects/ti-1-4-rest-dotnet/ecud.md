---
tipo: deliverable
taller: TI.1.4
protocolo: RESTful
plataforma: .NET
tags: [ecud, rest, dotnet, use-cases, aspnetcore, sinbdd]
---

# Casos de Uso Detallados (ECUD) - TI.1.4 (RESTful .NET)

> [!NOTE]
> Especificación de Casos de Uso Detallados para la variante **RESTful en .NET (ASP.NET Core Web API)** bajo arquitectura **SINBDD**.
> Documento general de referencia: [ecud.md](../../03-deliverables/ecud.md).

---

## 1. Mapeo de Casos de Uso a Controllers ASP.NET Core

| Caso de Uso | Método HTTP | Ruta Controller | Payload Entrada (JSON) | Salida (JSON) |
| :--- | :---: | :--- | :--- | :--- |
| **CU-01: Iniciar Sesión** | `POST` | `/api/auth/login` | `{"username":"MONSTER","password":"MONSTER9"}` | `{"token":"uuid","success":true}` |
| **CU-02: Convertir Longitud** | `POST` | `/api/conversor/longitud` | `{"token":"...","value":100.0,"from":"metro","to":"pie"}` | `{"value":100.0,"result":328.08,"success":true}` |
| **CU-03: Convertir Masa** | `POST` | `/api/conversor/masa` | `{"token":"...","value":50.0,"from":"kilogramo","to":"libra"}` | `{"value":50.0,"result":110.23,"success":true}` |
| **CU-04: Convertir Temperatura**| `POST` | `/api/conversor/temperatura`| `{"token":"...","value":100.0,"from":"celsius","to":"fahrenheit"}`| `{"value":100.0,"result":212.00,"success":true}` |
| **CU-05: Validar Token** | Filtro o Middleware en memoria (`ConcurrentDictionary<string, DateTime>`). |
| **CU-06: Validar Unidades** | Validación en el servicio contra el catálogo de 5 unidades. |

---

## 2. Detalle de Fichas de Casos de Uso
Ver especificación completa con todas las pre/postcondiciones y flujos básicos y alternativos en [ecud](../../03-deliverables/ecud.md).
