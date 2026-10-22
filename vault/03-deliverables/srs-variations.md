---
type: deliverable
tags: [deliverable, ers, srs, variations, architecture, comparison]
---

# Variaciones del ERS / SRS según el Taller (TI.1.1 a TI.1.4)

Este documento detalla **qué secciones, tablas y textos del ERS/SRS cambian en cada taller** y cuáles se mantienen constantes, sirviendo como guía de referencia para adaptar rápidamente la plantilla base [[vault/03-deliverables/srs-template|srs-template.md]] a cualquiera de las 4 entregas de la unidad.

---

## 1. Resumen de las Cuatro Variantes

| Taller | Protocolo | Plataforma | Carpeta en Repositorio | Estilo de Servicio |
| :--- | :--- | :--- | :--- | :--- |
| **TI.1.1** | SOAP | Java | `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09` | Contratos WSDL, XML SOAP Envelope |
| **TI.1.2** | SOAP | .NET | `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09` | CoreWCF / WCF, XML SOAP Envelope |
| **TI.1.3** | REST | Java | `TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09` | Jakarta REST / Spring Boot, JSON sobre HTTP |
| **TI.1.4** | REST | .NET | `TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09` | ASP.NET Core Web API, JSON sobre HTTP |

---

## 2. Lo que Permanece INVARIABLE (Común a los 4 Talleres)

No importa la tecnología ni el protocolo, los siguientes puntos se conservan exactamente iguales en los 4 documentos:

1. **Datos del Estudiante y Académicos:**
   - **Institución:** Universidad de las Fuerzas Armadas ESPE
   - **Carrera:** Tecnologías de la Información / Ingeniería de Software
   - **NRC:** 36884 | **Grupo:** GR09 | **Periodo:** PAO 2026-51
   - **Integrantes:** `Viche Castillo, Julio Enrique` (solo él)
   - **Docente:** Ing. Eduardo Mauricio Campaña Ortega
2. **Caso de Estudio y Alcance Funcional:**
   - Convertidor de unidades de 3 magnitudes físicas: **Longitud**, **Masa (peso)** y **Temperatura**.
   - Despliegue multi-canal en 4 clientes: **Consola**, **Escritorio**, **Web** y **Móvil (Android)**.
3. **Credenciales y Autenticación:**
   - Credenciales quemadas en código (hardcoded): Usuario **`MONSTER`**, Contraseña **`MONSTER9`**.
   - Generación y validación de un token de seguridad / sesión en memoria.
4. **Restricción de Persistencia:**
   - **`SINBDD`**: No se utiliza base de datos relacional, NoSQL ni ORMs. El catálogo de unidades y las sesiones activas residen en memoria.
5. **Requisitos Funcionales (RQF):**
   - `RQF-001`: Autenticar Cliente.
   - `RQF-002`: Convertir Longitud.
   - `RQF-003`: Convertir Masa.
   - `RQF-004`: Convertir Temperatura.

---

## 3. Matriz de Contenidos Variables por Taller

### A. Portada y Título Principal

| Sección | TI.1.1 (SOAP Java) | TI.1.2 (SOAP .NET) | TI.1.3 (REST Java) | TI.1.4 (REST .NET) |
| :--- | :--- | :--- | :--- | :--- |
| **Tema del documento** | ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON SOAP EN JAVA SIN BDD | ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON SOAP EN .NET SIN BDD | ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON REST EN JAVA SIN BDD | ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON REST EN .NET SIN BDD |
| **Tarea** | TI.1.1 | TI.1.2 | TI.1.3 | TI.1.4 |
| **Nombre archivo Word** | `ERS_SOAP_JAVA_SINBDD_GR09.docx` | `ERS_SOAP_DOTNET_SINBDD_GR09.docx` | `ERS_RESTFUL_JAVA_SINBDD_GR09.docx` | `ERS_RESTFUL_DOTNET_SINBDD_GR09.docx` |

---

### B. Requisitos No Funcionales (RNF)

#### Tabla 26. RNF-001: Protocolo de Arquitectura
- **TI.1.1 (SOAP Java) / TI.1.2 (SOAP .NET):**
  - Protocolo: **SOAP 1.1 / 1.2**.
  - Formato de intercambio: Mensajes XML estructurados bajo el estándar SOAP Envelope (`<soapenv:Envelope>`, `<soapenv:Header>`, `<soapenv:Body>`).
  - Definición del contrato: Contrato formal **WSDL** (*Web Services Description Language*) y esquema XSD para tipos de datos.
- **TI.1.3 (REST Java) / TI.1.4 (REST .NET):**
  - Protocolo: **RESTful** sobre HTTP/1.1 y HTTP/2.
  - Formato de intercambio: Cargas ligeras en formato **JSON** (*JavaScript Object Notation*).
  - Métodos HTTP utilizados: `POST` para autenticación y conversiones seguras; `GET` para consulta de catálogos y unidades disponibles.
  - Endpoints: URIs unificadas (ej. `/api/v1/auth/login`, `/api/v1/convert/length`).

---

#### Tabla 27. RNF-002: Especificación y Lenguaje
- **TI.1.1 (SOAP Java):**
  - Lenguaje: Java SE (JDK 17 o 21 LTS).
  - API / Estándar: Jakarta XML Web Services / JAX-WS (`@WebService`, `@WebMethod`, `@WebParam`).
- **TI.1.2 (SOAP .NET):**
  - Lenguaje: C# 12 / .NET 8 o 9 LTS.
  - Framework / Librería: CoreWCF / Windows Communication Foundation (WCF) (`[ServiceContract]`, `[OperationContract]`).
- **TI.1.3 (REST Java):**
  - Lenguaje: Java SE (JDK 17 o 21 LTS).
  - API / Framework: Jakarta RESTful Web Services (JAX-RS) con Jersey / RESTEasy o Spring Boot Web.
- **TI.1.4 (REST .NET):**
  - Lenguaje: C# 12 / .NET 8 o 9 LTS.
  - Framework: ASP.NET Core Web API / Minimal APIs (`[ApiController]`, `[Route]`).

---

#### Tabla 28. RNF-003: Entorno de Desarrollo (IDE)
- **TI.1.1 (SOAP Java):** Apache NetBeans 19+, Eclipse IDE for Enterprise Java Developers o IntelliJ IDEA Ultimate.
- **TI.1.2 (SOAP .NET):** Microsoft Visual Studio 2022 o Visual Studio Code con C# Dev Kit.
- **TI.1.3 (REST Java):** IntelliJ IDEA, Apache NetBeans o Eclipse con Maven.
- **TI.1.4 (REST .NET):** Microsoft Visual Studio 2022 o Visual Studio Code con SDK de .NET.

---

#### Tabla 29. RNF-004: Servidor de Aplicaciones / Runtime
- **TI.1.1 (SOAP Java):** Servidor embebido JAX-WS (`Endpoint.publish()`) o contenedor de servlets (Apache Tomcat 10, GlassFish, Payara).
- **TI.1.2 (SOAP .NET):** Servidor Kestrel embebido configurado con Middleware CoreWCF.
- **TI.1.3 (REST Java):** Apache Tomcat embebido (Spring Boot) o servidor standalone JAX-RS.
- **TI.1.4 (REST .NET):** Servidor Kestrel de alto rendimiento integrado en ASP.NET Core.

---

#### Tabla 30. RNF-005: Mecanismo de Seguridad y Token
- **Variantes SOAP (TI.1.1 y TI.1.2):**
  - El token de seguridad puede transmitirse en el encabezado SOAP (`<soapenv:Header><auth:token>...</auth:token></soapenv:Header>`) o como parámetro obligatorio en el cuerpo de la operación.
- **Variantes REST (TI.1.3 y TI.1.4):**
  - El token se transmite en la cabecera estándar HTTP `Authorization: Bearer <token>` o como un encabezado personalizado `X-Auth-Token`.

---

### C. Especificaciones de los Clientes Multi-canal

| Componente | TI.1.1 (SOAP Java) | TI.1.2 (SOAP .NET) | TI.1.3 (REST Java) | TI.1.4 (REST .NET) |
| :--- | :--- | :--- | :--- | :--- |
| **04. CLICON (Consola)** | Aplicación Java de línea de comandos consumiendo cliente JAX-WS generado. | Aplicación de consola C# utilizando `ServiceReference` o `ChannelFactory`. | Aplicación Java de consola utilizando `HttpClient` nativo de Java 11+. | Aplicación C# de consola utilizando `System.Net.Http.HttpClient`. |
| **04. CLIESC (Escritorio)** | Java Swing / JavaFX consumiendo el stub SOAP generado. | C# Windows Presentation Foundation (WPF) o Windows Forms. | Java Swing / JavaFX consumiendo la API REST mediante JSON (`Jackson`/`Gson`). | C# WPF / WinForms consumiendo la API REST con `System.Text.Json`. |
| **04. CLIWEB (Web)** | Interfaz Web (HTML5/CSS3/JavaScript) comunicándose mediante llamadas AJAX / Fetch al backend o página JSP. | Aplicación Web ASP.NET Core MVC / Razor Pages o SPA estática con Fetch API. | Frontend Web (HTML/JS Fetch API o cliente JSP/Thymeleaf) consumiendo REST. | Frontend Web (HTML5/JS Fetch API o Blazor WebAssembly) consumiendo endpoints REST. |
| **04. CLIMOV (Móvil Android)** | Aplicación Android (Java/Kotlin) utilizando librería **Ksoap2-android** o cliente HTTP con XML parser. | Aplicación Android (Java/Kotlin) comunicándose por SOAP mediante Ksoap2. | Aplicación Android utilizando **Retrofit 2** / **OkHttp** con conversor Gson/Moshi. | Aplicación Android utilizando **Retrofit 2** / **OkHttp** o cliente .NET MAUI si aplica. |

---

## 4. Checklist para Adaptar el ERS a un Taller Específico

Al preparar el documento de entrega para un taller concreto:

1. [ ] Modificar el título y tema de la carátula con el protocolo y plataforma correspondientes.
2. [ ] Validar que la tabla del integrante contenga: **`Viche Castillo, Julio Enrique`**.
3. [ ] Ajustar la redacción de la sección **1. Introducción** para citar el protocolo adecuado (SOAP o REST).
4. [ ] Verificar que las tablas **RNF-001 a RNF-005** reflejen la tecnología del taller (lenguaje, framework, runtime y formato de token).
5. [ ] Asegurar que la descripción de las interfaces de los 4 clientes (Consola, Escritorio, Web, Móvil) mencione las librerías cliente correctas.
6. [ ] Mantener sin cambios el catálogo de conversión, los RQF-001 a RQF-004 y la restricción **SINBDD** (credenciales fijas `MONSTER`/`MONSTER9`).
