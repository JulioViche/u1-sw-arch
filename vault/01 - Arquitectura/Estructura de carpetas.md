---
tipo: arquitectura
tags: [arquitectura, estructura]
---

# Estructura de carpetas

Todas las variantes comparten la siguiente estructura de entregables:

```
TI.1.1. PAO202651_<PROTOCOLO>_<PLATAFORMA>_SINBDD_NRC36884_GR09/
├── 01. UML/
│   ├── 01. ERS/               # Especificación de Requisitos de Software
│   ├── 02. ECUD/              # Especificación de Casos de Uso Detallados
│   └── 03. DIAGRAMAS UML/
│       ├── 01_CASOS_DE_USO/
│       ├── 02_ACTIVIDAD/
│       ├── 03_SECUENCIA/
│       ├── 04_CLASES/
│       └── 05_ARQUITECTURA/
├── 02. MER/                   # Vacío (SINBDD): sin base de datos
├── 03. BDD/                   # Vacío (SINBDD): sin base de datos
├── 04. CLICON/                # Cliente de consola
├── 04. CLIESC/                # Cliente de escritorio
├── 04. CLIMOV/                # Cliente móvil (Android)
├── 04. CLIWEB/                # Cliente web
├── 04. SERVIDOR/              # Servicio web (REST o SOAP)
├── 05. DOCUMENTACION/
└── README.txt
```

> [!warning] Carpetas vacías
> Las carpetas sin contenido llevan un archivo **`.gitkeep`**. **No lo borres**: sirve
> para que git conserve la carpeta.
>
> `02. MER/` y `03. BDD/` están **vacías** en las cuatro variantes porque el proyecto es
> **SINBDD** (sin base de datos).

## Comparativa con el ejemplo de referencia

| Ruta | En el scaffold actual | En `examples/` |
| --- | --- | --- |
| `01. UML/01. ERS/` | vacío | ERS + plantilla + taller |
| `01. UML/02. ECUD/` | vacío | ECUD (PDF) |
| `01. UML/03. DIAGRAMAS UML/` | vacío | modelo + exportados por subcarpeta |
| `02. MER/` | vacío | conceptual / lógico / físico (solo README) |
| `03. BDD/` | vacío | solo README |
| `05. DOCUMENTACION/` | vacío | manual técnico + presentación |

> [!info] Relacionado
> - [[vault/01 - Arquitectura/Convenciones de nombres]]
> - [[vault/03 - Entregables/01 UML]]
