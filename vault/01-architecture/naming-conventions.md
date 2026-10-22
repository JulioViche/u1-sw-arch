---
tipo: arquitectura
tags: [arquitectura, nomenclatura]
---

# Convenciones de nombres

Patrones tomados de la solución de referencia en `examples/`. Sustituye `<GRUPO>` por
`GR09`, `<PROTOCOLO>` por `RESTFUL`/`SOAP` y `<PLATAFORMA>` por `JAVA`/`DOTNET`.

## Archivos de entregables

| Artefacto | Patrón | Ejemplo de referencia |
| --- | --- | --- |
| ERS | `ERS_<PROTOCOLO>_<PLATAFORMA>_SINBDD_<GRUPO>.docx` | `ERS_SOAP_JAVA_SINBDD_GR69.docx` |
| Plantilla ERS | `ERS_<...>_PLANTILLA.docx` | `ERS_SOAP_JAVA_SINBDD_GR69_PLANTILLA.docx` |
| ECUD | `ECUD_<PROYECTO>_<GRUPO>.pdf` | `ECUD_CONVERSION_UNIDADES_G03.pdf` |
| Manual técnico | `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.docx` | `CONVUNI_SOAP_JAVA_GR69.docx` |
| Presentación | `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.pptx` | `CONVUNI_SOAP_JAVA_GR69.pptx` |

## Carpetas

- Entregables numerados: `NN. NOMBRE` → `04. SERVIDOR`.
- Subcarpetas de diagramas: `NN_NOMBRE` → `01_CASOS_DE_USO`.
- Carpetas de variante: `TI.1.1. PAO202651_<PROTOCOLO>_<PLATAFORMA>_SINBDD_NRC36884_GR09`.

## Reglas

- No uses espacios sueltos al final del nombre.
- Evita caracteres inválidos en Windows: `: * ? " < > |`.
- Un `README.txt` breve por carpeta es aceptable para dejar notas de contexto.

> [!tip]
> Cuando el nombre del artefacto dependa del protocolo/plataforma, usa **mayúsculas** y
> separa con guion bajo (`_`), igual que en el ejemplo.

> [!info] Relacionado
> - [[vault/03-deliverables/01-uml]]
> - [[vault/03-deliverables/05-documentation]]
