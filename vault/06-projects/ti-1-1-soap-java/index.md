---
tipo: taller
taller: TI.1.1
protocolo: SOAP
plataforma: Java
tags: [taller, soap, java, ti-1-1]
---

# Taller TI.1.1 · SOAP Java (SINBDD)

Carpeta de trabajo y documentación maestra para la variante **SOAP en Java** del sistema de Conversión de Unidades.

---

## 1. Ficha Técnica

| Parámetro | Detalle |
| :--- | :--- |
| **Código Tarea** | TI.1.1 |
| **Estudiante** | Julio Enrique Viche Castillo |
| **Grupo / NRC** | GR09 / NRC 36884 |
| **Periodo** | PAO 2026-51 |
| **Estilo de Servicio** | SOAP (Simple Object Access Protocol) con contratos WSDL y envelopes XML |
| **Servidor de Aplicaciones** | Payara Server Community 6.x / GlassFish |
| **Lenguaje / JDK** | Java 17+ |
| **Persistencia** | `SINBDD` (Sin Base de Datos). Credenciales fijas `MONSTER`/`MONSTER9`, tokens activos en memoria |
| **Carpeta del Proyecto** | `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/` |
| **Espacio de Trabajo UML** | `CON_UNI_GR09.sws` (en la raíz del proyecto) |

---

## 2. Documentos Maestros en Markdown

Los siguientes archivos son las fuentes editables para compilar la documentación oficial:

- 📄 **[srs.md](srs.md)**: Especificación de Requisitos de Software (IEEE-830). Fuente de `ERS_SOAP_JAVA_SINBDD_GR09.docx`.
- 📐 **[diagrams.md](diagrams.md)**: Modelos UML interactivos en sintaxis Mermaid (Casos de Uso, Actividades con swimlanes, Secuencia multicapa y Arquitectura).
- 📘 **[technical-manual.md](technical-manual.md)**: Manual técnico del sistema, arquitectura, despliegue del servidor y guía de ejecución de los 4 clientes. Fuente de `CONVUNI_SOAP_JAVA_GR09.docx`.
- 📋 **`use-cases.md`**: Especificación formal de Casos de Uso Detallados (Login y Conversión). Fuente de `ECUD_CONVERSION_UNIDADES_GR09.pdf`.
- 📊 **`presentation.md`**: Guion y estructura de diapositivas para la defensa del proyecto. Fuente de `CONVUNI_SOAP_JAVA_GR09.pptx`.

---

## 3. Estado de los Componentes

| Componente | Carpeta en Proyecto | Tecnología | Estado |
| :--- | :--- | :--- | :--- |
| **Servidor SOAP** | `04. SERVIDOR/` | Java JAX-WS / Maven | ✅ Implementado y en ejecución (puerto 8080) |
| **Cliente Consola** | `04. CLICON/` | Java CLI / JAX-WS / Maven | ✅ Implementado (autenticación obligatoria + conversor) |
| **Cliente Escritorio** | `04. CLIESC/` | Java Swing / NetBeans GUI | Pendiente de implementación |
| **Cliente Móvil** | `04. CLIMOV/` | Android (Java / Gradle) | Pendiente de implementación |
| **Cliente Web** | `04. CLIWEB/` | Java JSP / Servlets | Pendiente de implementación |

---

## 4. Modelos y Diagramas UML

Los modelos nativos de PowerDesigner se encuentran en:
`TI.1.1.../01. UML/03. DIAGRAMAS UML/` y se pueden abrir simultáneamente mediante `CON_UNI_GR09.sws`.

Las representaciones gráficas interactivas en Mermaid están documentadas en:
👉 **[diagrams.md](diagrams.md)** y en el catálogo general **[01-uml](../../03-deliverables/01-uml.md)**.

---

> [!info] Enlaces relacionados
> - [06-projects](../index.md): Índice de todos los proyectos.
> - [srs-variations](../../03-deliverables/srs-variations.md): Diferencias entre las 4 variantes.
> - [authentication](../../01-architecture/authentication.md): Reglas del login obligatorio.
