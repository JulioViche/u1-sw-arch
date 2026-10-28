---
tipo: deliverable
taller: TI.1.3
protocolo: RESTful
plataforma: Java
tags: [ecud, rest, java, use-cases, jersey, jax-rs, sinbdd]
---

# Casos de Uso Detallados (ECUD) - TI.1.3 (RESTful Java)

> [!NOTE]
> Especificación de Casos de Uso Detallados para la variante **RESTful en Java (Jakarta EE / Jersey)** bajo arquitectura **SINBDD**.
> Documento general de referencia: [ecud.md](../../03-deliverables/ecud.md).

---

## 1. Mapeo de Casos de Uso a Endpoints REST (JSON)

| Caso de Uso | Método HTTP | Endpoint URI | Payload Entrada (JSON) | Respuesta Exitosa (JSON) |
| :--- | :---: | :--- | :--- | :--- |
| **CU-01: Iniciar Sesión** | `POST` | `/auth/login` | `{"username":"MONSTER","password":"MONSTER9"}` | `{"token":"uuid","success":true,"message":"Login exitoso"}` |
| **CU-02: Convertir Longitud** | `POST` | `/conversor/longitud` | `{"token":"...","value":100.0,"from":"metro","to":"pie"}` | `{"value":100.0,"result":328.08,"success":true}` |
| **CU-03: Convertir Masa** | `POST` | `/conversor/masa` | `{"token":"...","value":50.0,"from":"kilogramo","to":"libra"}` | `{"value":50.0,"result":110.23,"success":true}` |
| **CU-04: Convertir Temperatura**| `POST` | `/conversor/temperatura`| `{"token":"...","value":100.0,"from":"celsius","to":"fahrenheit"}`| `{"value":100.0,"result":212.00,"success":true}` |
| **CU-05: Validar Token** | Interno | Interceptado por `ServicioAuth` en memoria RAM. Retorna HTTP 401 si expiró. |
| **CU-06: Validar Unidades** | Interno | Verificado contra catálogo estricto de 5 unidades. Retorna HTTP 400 si es desconocida. |

---

## 2. Diagrama de Casos de Uso (RESTful Java)

```mermaid
flowchart TD
    User(["fa:fa-user Usuario"])

    subgraph REST_Server ["Servidor REST (Jersey / Payara)"]
        AUTH_CTRL["AuthController (/auth/login)"]
        CONV_CTRL["ConversorController (/conversor/*)"]
        CU01(["CU-01: Login (POST /auth/login)"])
        CU02(["CU-02: Convertir Longitud (POST)"])
        CU03(["CU-03: Convertir Masa (POST)"])
        CU04(["CU-04: Convertir Temperatura (POST)"])
        CU05(["CU-05: Validar Token (RAM)"])
        CU06(["CU-06: Validar Unidades (5 por categoría)"])
    end

    User -->|HTTP POST JSON| AUTH_CTRL
    User -->|HTTP POST JSON| CONV_CTRL

    AUTH_CTRL --> CU01
    CONV_CTRL --> CU02
    CONV_CTRL --> CU03
    CONV_CTRL --> CU04

    CU02 -.->|<< include >>| CU05
    CU02 -.->|<< include >>| CU06
    CU03 -.->|<< include >>| CU05
    CU03 -.->|<< include >>| CU06
    CU04 -.->|<< include >>| CU05
    CU04 -.->|<< include >>| CU06
    CU05 -.->|<< extend (HTTP 401) >>| CU01
```

---

## 3. Detalle de Fichas de Casos de Uso
Ver especificación completa con todas las pre/postcondiciones y flujos básicos y alternativos en [ecud](../../03-deliverables/ecud.md).
