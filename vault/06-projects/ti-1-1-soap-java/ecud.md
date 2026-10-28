---
tipo: deliverable
taller: TI.1.1
protocolo: SOAP
plataforma: Java
tags: [ecud, soap, java, use-cases, jax-ws, sinbdd]
---

# Casos de Uso Detallados (ECUD) - TI.1.1 (SOAP Java)

> [!NOTE]
> Especificación de Casos de Uso Detallados para la variante **SOAP en Java (JAX-WS)** bajo arquitectura **SINBDD**.
> Documento general de referencia: [ecud.md](../../03-deliverables/ecud.md).

---

## 1. Mapeo de Casos de Uso a Servicios SOAP

| Caso de Uso | Operación SOAP | Endpoint WSDL | Mensaje de Petición (XML Body) |
| :--- | :--- | :--- | :--- |
| **CU-01: Iniciar Sesión** | `login(usuario, contrasenia)` | `http://localhost:8080/ServidorSOAP/ServicioAutenticacion?wsdl` | `<usuario>MONSTER</usuario><contrasenia>MONSTER9</contrasenia>` |
| **CU-02: Convertir Longitud** | `convertirLongitud(token, valor, origen, destino)` | `http://localhost:8080/ServidorSOAP/ServicioConversion?wsdl` | `<token>uuid</token><valor>100.0</valor><origen>metro</origen><destino>pie</destino>` |
| **CU-03: Convertir Masa** | `convertirMasa(token, valor, origen, destino)` | `http://localhost:8080/ServidorSOAP/ServicioConversion?wsdl` | `<token>uuid</token><valor>10.0</valor><origen>kilogramo</origen><destino>libra</destino>` |
| **CU-04: Convertir Temperatura**| `convertirTemperatura(token, valor, origen, destino)` | `http://localhost:8080/ServidorSOAP/ServicioConversion?wsdl` | `<token>uuid</token><valor>100.0</valor><origen>celsius</origen><destino>fahrenheit</destino>` |
| **CU-05: Validar Token** | Validación interna en memoria RAM (`ConcurrentHashMap`) antes de despachar cada operación SOAP. |
| **CU-06: Validar Unidades** | Validación en `ConversorUtil` contra el catálogo estricto de 5 unidades. |

---

## 2. Diagrama de Casos de Uso (SOAP Java)

```mermaid
flowchart TD
    User(["fa:fa-user Usuario"])

    subgraph SOAP_Server ["Servidor SOAP (JAX-WS / Payara)"]
        WS_AUTH["ServicioAutenticacion (/ServicioAutenticacion)"]
        WS_CONV["ServicioConversion (/ServicioConversion)"]
        CU01(["CU-01: Login (MONSTER / MONSTER9)"])
        CU02(["CU-02: Convertir Longitud"])
        CU03(["CU-03: Convertir Masa"])
        CU04(["CU-04: Convertir Temperatura"])
        CU05(["CU-05: Validar Token (RAM)"])
        CU06(["CU-06: Validar Unidades (5 por catálogo)"])
    end

    User -->|SOAP Request| WS_AUTH
    User -->|SOAP Request| WS_CONV

    WS_AUTH --> CU01
    WS_CONV --> CU02
    WS_CONV --> CU03
    WS_CONV --> CU04

    CU02 -.->|<< include >>| CU05
    CU02 -.->|<< include >>| CU06
    CU03 -.->|<< include >>| CU05
    CU03 -.->|<< include >>| CU06
    CU04 -.->|<< include >>| CU05
    CU04 -.->|<< include >>| CU06
    CU05 -.->|<< extend (error) >>| CU01
```

---

## 3. Detalle de Fichas de Casos de Uso
Ver especificación completa con todas las pre/postcondiciones y flujos básicos y alternativos en [ecud](../../03-deliverables/ecud.md).
