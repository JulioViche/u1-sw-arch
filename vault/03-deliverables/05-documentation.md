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

## Entregables Generados (.docx)

| Variante | Carpeta | Archivo Oficial | Estado |
| :--- | :--- | :--- | :--- |
| **TI.1.1** (SOAP Java) | `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/05. DOCUMENTACION/` | `CONVUNI_SOAP_JAVA_GR09.docx` | Generado |
| **TI.1.2** (SOAP .NET) | `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/` | `CONVUNI_SOAP_DOTNET_GR09.docx` | Generado |
| **TI.1.3** (RESTful Java) | `TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09/05. DOCUMENTACION/` | `CONVUNI_RESTFUL_JAVA_GR09.docx` | Generado |
| **TI.1.4** (RESTful .NET) | `TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/` | `CONVUNI_RESTFUL_DOTNET_GR09.docx` | Generado |

## Manuales Técnicos en Markdown (Vault)
- [Manual Técnico - SOAP Java (TI.1.1)](../06-projects/ti-1-1-soap-java/technical-manual.md)
- [Manual Técnico - SOAP .NET (TI.1.2)](../06-projects/ti-1-2-soap-dotnet/technical-manual.md)
- [Manual Técnico - RESTful Java (TI.1.3)](../06-projects/ti-1-3-rest-java/technical-manual.md)
- [Manual Técnico - RESTful .NET (TI.1.4)](../06-projects/ti-1-4-rest-dotnet/technical-manual.md)

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
