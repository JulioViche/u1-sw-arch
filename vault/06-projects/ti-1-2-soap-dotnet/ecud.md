---
tipo: deliverable
taller: TI.1.2
protocolo: SOAP
plataforma: .NET
tags: [ecud, soap, dotnet, use-cases, wcf, sinbdd]
---

# Casos de Uso Detallados (ECUD) - TI.1.2 (SOAP .NET)

> [!NOTE]
> Especificación de Casos de Uso Detallados para la variante **SOAP en .NET (WCF / CoreWCF)** bajo arquitectura **SINBDD**.
> Documento general de referencia: [ecud.md](../../03-deliverables/ecud.md).

---

## 1. Mapeo de Casos de Uso a Contratos WCF

| Caso de Uso | Contrato de Operación | Endpoint WCF | Descripción |
| :--- | :--- | :--- | :--- |
| **CU-01: Iniciar Sesión** | `Login(string usuario, string clave)` | `http://localhost:5000/ServicioAutenticacion.svc` | Valida credenciales fijas y devuelve token UUID |
| **CU-02: Convertir Longitud** | `ConvertirLongitud(string token, double valor, string from, string to)` | `http://localhost:5000/ServicioConversion.svc` | Transforma medidas de longitud (5 unidades) |
| **CU-03: Convertir Masa** | `ConvertirMasa(string token, double valor, string from, string to)` | `http://localhost:5000/ServicioConversion.svc` | Transforma medidas de masa (5 unidades) |
| **CU-04: Convertir Temperatura**| `ConvertirTemperatura(string token, double valor, string from, string to)`| `http://localhost:5000/ServicioConversion.svc` | Transforma escalas termométricas (5 escalas) |
| **CU-05: Validar Token** | Validación en memoria (`ConcurrentDictionary<string, DateTime>`). |
| **CU-06: Validar Unidades** | Validación en `ConversorUtil` contra el catálogo estricto de 5 unidades. |

---

## 2. Detalle de Fichas de Casos de Uso
Ver especificación completa con todas las pre/postcondiciones y flujos básicos y alternativos en [ecud](../../03-deliverables/ecud.md).
