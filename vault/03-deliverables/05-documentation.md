---
tipo: entregable
tags: [entregable, documentacion]
---

# 05 · Documentación

Carpeta `05. DOCUMENTACION/` de cada variante. Manual técnico, presentación y anexos.

| Artefacto | Patrón |
| --- | --- |
| Manual técnico | `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.docx` |
| Presentación | `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.pptx` |
| Anexos | libre (PDF, capturas) |

En el ejemplo de referencia, el manual vive en una subcarpeta `02. MANUAL TECNICO/`.

## Manuales Técnicos en Markdown (Vault)
- [Manual Técnico - SOAP Java (TI.1.1)](manual-tecnico-soap.md)
- [Manual Técnico - RESTful Java (TI.1.3)](manual-tecnico-restful.md)

## Contenido sugerido del manual

1. Introducción y objetivo del sistema.
2. Descripción del caso de estudio (conversor de unidades + login).
3. Arquitectura (estilo de servicio y plataforma).
4. Requisitos (referencia al ERS).
5. Diseño (referencia a UML; MER y BDD no aplican por ser SINBDD).
6. Guía de instalación y ejecución (ver [build-and-run](../04-guides/build-and-run.md)).
7. Pruebas y resultados.
8. Conclusiones y anexos.

> [!tip]
> Reutiliza el contenido común entre variantes (caso de estudio, requisitos, diseño) y
> adapta solo lo específico de la tecnología y el protocolo.

> [!info] Relacionado
> - [naming-conventions](../01-architecture/naming-conventions.md)
> - [04-clients-and-server](04-clients-and-server.md)
