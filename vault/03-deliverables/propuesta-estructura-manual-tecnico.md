---
tipo: analisis-propuesta
tags: [manual-tecnico, documentacion, arquitectura, plan-trabajo]
fecha: 2026-10-29
autores: Julio Enrique Viche Castillo, Gabriel Omar Murillo Medina
---

# Análisis y Propuesta de Estructura para el Manual Técnico (GR09)

Este documento recopila el contexto, el análisis de los manuales técnicos de referencia (`examples/`) y la propuesta de homogeneización para la documentación técnica (tanto en Markdown como en Word `.docx`) de las 4 variantes del proyecto (**TI.1.1** a **TI.1.4**).

---

## 1. Contexto de la Revisión

Se realizó una auditoría de todos los manuales técnicos entregados por grupos anteriores en la carpeta de referencia `examples/`:
1. `examples/soap-java/.../CONVUNI_SOAP_JAVA_GR69.docx` (SOAP Java - Grupo 69)
2. `examples/soap-java/.../CONVUNI_MANUAL_SOAP_JAVA_GR03.docx` (SOAP Java - Grupo 03)
3. `examples/restful-java/.../CONVUNI_MANUAL_REST_JAVA_GR03.docx` (RESTful Java - Grupo 03)
4. `examples/restful-java/.../MANUAL_CONVERISIÓN_UNIDADES_REST_JAVA_GRUPO4.docx` (RESTful Java - Grupo 04)
5. `examples/restful-java/.../WS_ConUni_Java_Rest_GR5-main/.../MANUAL_RESTFUL_JAVA_SINBDD_GR5.docx` (RESTful Java - Grupo 05)

### Situación Detectada en el Repositorio (`vault/`)

Al contrastar los documentos de referencia con los archivos Markdown del repositorio, se identificó una disparidad estructural:

| Archivo en `vault/` | Estructura Actual | Estado de Alineación |
| :--- | :--- | :--- |
| `vault/03-deliverables/technical-manual-template.md` | Estructura abstracta/conceptual numerada de 8 puntos. | ⚠️ No coincide con la estructura práctica de los documentos Word oficiales. |
| `vault/06-projects/ti-1-1-soap-java/technical-manual.md` | `PARTE PRÁCTICA` $\rightarrow$ `APLICACIONES SERVIDOR` $\rightarrow$ `CODIFICACIÓN DE LOS CLIENTES` (Consola, Escritorio, Web, Móvil). | ✅ Coincide al 100% con los ejemplos Word de GR69 y GR03. |
| `vault/06-projects/ti-1-3-rest-java/technical-manual.md` | Idéntica estructura simétrica de laboratorio que TI.1.1 adaptada a REST. | ✅ Coincide al 100% con los ejemplos Word de GR69 y GR03. |
| `vault/06-projects/ti-1-4-rest-dotnet/technical-manual.md` | Esquema formal numerado de 9 capítulos (Marco Teórico, Desarrollo, Despliegue, etc.). | ⚠️ No sigue la convención práctica de bloques de servidor y clientes. |
| `vault/06-projects/ti-1-2-soap-dotnet/technical-manual.md` | Contenido incompleto / preliminar (solo bloque de ejecución). | ❌ Requiere completado y alineación estructural. |

---

## 2. Patrones Comunes en los Mejores Ejemplos (GR69 y GR03)

Los manuales mejor calificados y más completos comparten una estructura didáctica tipo "guía paso a paso de laboratorio":

1. **Marco Institucional:** Carátula formal con borde doble (`twistedLines1`), datos de la asignatura (NRC 36884, Periodo PAO 2026-51, Integrantes, Tutor), e Índices automatizados (Contenido, Figuras y Tablas).
2. **Bloque 1: Aplicación Servidor (`04. SERVIDOR`):**
   - Creación del proyecto y arquitectura interna.
   - Definición de paquetes y namespaces.
   - Creación de modelos/DTOs.
   - Codificación del Servicio Web (Controladores REST o Endpoints SOAP).
   - Lógica de autenticación en memoria RAM (`SINBDD`) con credenciales fijas `MONSTER`/`MONSTER9`.
   - Lógica de conversión de magnitudes (Longitud, Masa, Temperatura).
   - Despliegue y pruebas del servidor (WSDL / Swagger / Postman).
3. **Bloque 2: Aplicaciones Clientes (`CLICON`, `CLIESC`, `CLIWEB`, `CLIMOV`):**
   Para cada uno de los 4 clientes se replica rigurosamente el mismo cuarteto de subsecciones:
   - *Creación y configuración del proyecto.*
   - *Estructura de archivos y paquetes.*
   - *Codificación* (Modelos, Controladores/Adaptadores que consumen el servicio, y Vistas).
   - *Ejecución y pruebas* (capturas obligatorias del Login y de las conversiones de magnitudes).
4. **Cierre Formal:** Conclusiones técnicas, Recomendaciones de despliegue y Referencias bibliográficas.

---

## 3. Estructura Estándar Recomendada para GR09

Esta es la estructura definitiva propuesta para unificar los 4 manuales en Markdown y los 4 documentos Word oficiales (`CONVUNI_<PROTOCOLO>_<PLATAFORMA>_GR09.docx`):

```markdown
CARÁTULA INSTITUCIONAL
ÍNDICE DE CONTENIDOS
ÍNDICE DE FIGURAS
ÍNDICE DE TABLAS

PARTE PRÁCTICA

1. INTRODUCCIÓN
   1.1. Contexto del Proyecto y Arquitectura de Servicios Web
   1.2. Principio SINBDD y Autenticación en Memoria (Credenciales MONSTER / MONSTER9)

2. OBJETIVOS
   2.1. Objetivo General
   2.2. Objetivos Específicos

3. INSTALACIÓN Y CONFIGURACIÓN DE HERRAMIENTAS
   3.1. SDKs y Runtimes (JDK 17-21 / .NET 8 SDK / Android SDK)
   3.2. Entornos de Desarrollo (Apache NetBeans / Visual Studio / Android Studio)
   3.3. Servidor de Ejecución (Apache Tomcat / Payara / Kestrel)

4. ESTRUCTURA GLOBAL DEL PROYECTO
   4.1. Organización del Repositorio y Carpetas de Entregables
   4.2. Topología de Red y Conectividad (Localhost, Emulador 10.0.2.2, Red LAN)

5. APLICACIÓN SERVIDOR (04. SERVIDOR)
   5.1. Creación y Configuración del Proyecto Backend
   5.2. Estructura de Paquetes / Namespaces
   5.3. Modelos y Objetos de Transferencia de Datos (DTOs)
   5.4. Servicio de Seguridad y Autenticación (Login con MONSTER/MONSTER9)
   5.5. Servicio de Conversión de Unidades
        - Magnitud Longitud: Metros, Centímetros, Pies, Yardas, Millas
        - Magnitud Masa: Gramos, Kilogramos, Libras, Onzas, Quintales
        - Magnitud Temperatura: Celsius, Fahrenheit, Kelvin, Newton, Reaumur
   5.6. Publicación del Servicio y Verificación del Contrato (WSDL / Swagger OpenAPI)
   5.7. Pruebas Unitarias o de Integración (curl / Postman)

6. CODIFICACIÓN DE LOS CLIENTES
   6.1. CLIENTE DE CONSOLA (04. CLICON)
        6.1.1. Creación y Estructura del Proyecto
        6.1.2. Consumo del Servicio Web (SOAP / REST)
        6.1.3. Modelos, Controladores y Menú de Usuario
        6.1.4. Ejecución y Pruebas (Login y Conversiones con límite a 2 decimales)
   6.2. CLIENTE DE ESCRITORIO (04. CLIESC)
        6.2.1. Creación y Estructura del Proyecto (Java Swing / WPF)
        6.2.2. Consumo del Servicio Web
        6.2.3. Vistas Gráficas (Formulario de Login y Pantalla de Conversiones)
        6.2.4. Ejecución y Pruebas
   6.3. CLIENTE WEB (04. CLIWEB)
        6.3.1. Creación y Estructura del Proyecto (JSP/Servlets / Blazor / HTML+JS)
        6.3.2. Consumo del Servicio Web
        6.3.3. Vistas Web Responsivas
        6.3.4. Ejecución y Pruebas en Navegador
   6.4. CLIENTE MÓVIL (04. CLIMOV)
        6.4.1. Creación y Configuración del Proyecto Android (Permisos INTERNET)
        6.4.2. Consumo Asíncrono del Servicio Web
        6.4.3. Diseño de Vistas (Activities / XML Layouts)
        6.4.4. Ejecución y Pruebas en Emulador / Dispositivo Físico

7. CONCLUSIONES
8. RECOMENDACIONES
9. REFERENCIAS BIBLIOGRÁFICAS
```

---

## 4. Plan de Acción Recomendado (Siguientes Pasos)

1. **Alinear la plantilla maestra `vault/03-deliverables/technical-manual-template.md`:**  
   Actualizarla para que adopte formalmente esta estructura de dos grandes bloques (Servidor + 4 Clientes).
2. **Homogeneizar los manuales `.md` de proyectos:**  
   - Completar `vault/06-projects/ti-1-2-soap-dotnet/technical-manual.md` adaptando el contenido de SOAP Java a C# / CoreWCF / Kestrel.
   - Reestructurar `vault/06-projects/ti-1-4-rest-dotnet/technical-manual.md` para que adopte la misma organización modular de clientes que TI.1.1 y TI.1.3.
3. **Construir la plantilla maestra en Word (`PLANTILLA_MANUAL_TECNICO.docx`):**  
   Similar al proceso ejecutado para el ECUD:
   - Dejar marcadores limpios (`{{VAR_SERVIDOR_NOMBRE}}`, `{{VAR_CANAL_PETICION}}`, etc.).
   - Asegurar que todas las tablas de código y capturas tengan estilos homogéneos (`w:cantSplit`, sin párrafos vacíos innecesarios).
   - Parametrizar la compilación automática mediante `scripts/generate_docs.py`.
