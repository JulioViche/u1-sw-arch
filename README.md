# TI.1.1 — Arquitectura de Servicios Web (PAO 2026-51)

Repositorio académico con **cuatro variantes** del mismo sistema: un **conversor de
unidades** construido como servicio web **RESTful** y **SOAP**, cada uno en **Java** y
**.NET**, y explícitamente **sin base de datos** (`SINBDD`).

| Dato | Valor |
| --- | --- |
| Asignatura | Arquitectura de Software |
| Tarea | TI.1.1 |
| Periodo | PAO 2026-51 |
| NRC | 36884 |
| Grupo | GR09 |
| Integrante | Julio Enrique Viche Castillo |

> [!info] Documentación para agentes de IA
> Lee **[AGENTS.md](AGENTS.md)** antes de tocar el repositorio.
> La documentación detallada vive en el vault de Obsidian: **[vault/index.md](vault/index.md)**.

---

## Variantes del proyecto

| Variante | Protocolo | Plataforma | Carpeta |
| --- | --- | --- | --- |
| RESTful + .NET | REST | C# / ASP.NET Core | [`TI.1.1. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09`](TI.1.1.%20PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09) |
| RESTful + Java | REST | Java (JAX-RS) | [`TI.1.1. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09`](TI.1.1.%20PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09) |
| SOAP + .NET | SOAP | C# / WCF-ASMX | [`TI.1.1. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09`](TI.1.1.%20PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09) |
| SOAP + Java | SOAP | Java (JAX-WS) | [`TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09`](TI.1.1.%20PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09) |

Cada variante es un **proyecto independiente** con su propia estructura de entregables.

> [!warning] Plataformas propuestas (por confirmar)
> Los frameworks de la columna *Plataforma* son **orientativos**. Solo el ejemplo
> **SOAP + Java** (`examples/`) está respaldado por documentación; el resto debe confirmarse.

---

## Estructura estándar de cada variante

```
TI.1.1. PAO202651_<PROTOCOLO>_<PLATAFORMA>_SINBDD_NRC36884_GR09/
├── 01. UML/
│   ├── 01. ERS/              # Especificación de Requisitos de Software
│   ├── 02. ECUD/             # Especificación de Casos de Uso Detallados
│   └── 03. DIAGRAMAS UML/    # Casos de uso, actividad, secuencia, clases, arquitectura
├── 02. MER/                  # Vacío (SINBDD): sin base de datos
├── 03. BDD/                  # Vacío (SINBDD): sin base de datos
├── 04. CLICON/               # Cliente de consola
├── 04. CLIESC/               # Cliente de escritorio
├── 04. CLIMOV/               # Cliente móvil (Android)
├── 04. CLIWEB/               # Cliente web
├── 04. SERVIDOR/             # Servicio web (REST o SOAP)
└── 05. DOCUMENTACION/        # Manual técnico y anexos
```

El código (servidor y cada cliente) va **dentro de su carpeta** de la estructura anterior.
El detalle de cada entregable está en el vault → [[vault/03 - Entregables|Entregables]].

---

## Inicio rápido

Cada variante se ejecuta con las herramientas de su stack:

| Stack | Compilar / ejecutar |
| --- | --- |
| Java + Maven | `mvn clean package` · `mvn exec:java` |
| .NET | `dotnet build` · `dotnet run` |
| Android (CLIMOV) | `gradlew.bat assembleDebug` |

> [!warning]
> El servidor debe estar **en ejecución** antes de lanzar cualquier cliente.
> Consulta [[vault/04-guides/build-and-run|Compilar y ejecutar]] para el flujo completo.

---

## Documentación

| Recurso | Propósito |
| --- | --- |
| [AGENTS.md](AGENTS.md) | Reglas y flujo de trabajo para agentes de IA |
| [vault/index.md](vault/index.md) | Índice general (MOC) del vault de Obsidian |
| `examples/` | Solución de referencia (SOAP + Java). Solo lectura |

### Abrir en Obsidian

Abre la **raíz del repositorio** como bóveda en Obsidian (*Open folder as vault*). Así los
enlaces `[[vault/…]]` de las notas y de `AGENTS.md` resuelven correctamente. Los ajustes del
vault ya están incluidos en `.obsidian/`.

## Caso de estudio

Un **conversor de unidades** (longitud, masa, temperatura, etc.) con inicio de sesión.

> [!danger] Requisitos clave
> - **Todo proyecto debe iniciar con un login.** Sin autenticación no hay conversión.
> - Para esta unidad, las credenciales van **quemadas en el código** (sin base de datos):
>   usuario **`MONSTER`** · contraseña **`MONSTER9`**.

Ver [[vault/01-architecture/authentication|Autenticación (login)]] y
[[vault/05-reference/academic-data|Datos académicos]].
