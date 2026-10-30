# Arquitectura de Software — Talleres de Servicios Web (TI.1)

Repositorio institucional que consolida los **cuatro talleres individuales (TI.1.1, TI.1.2, TI.1.3 y TI.1.4)** de la Unidad 1 de Arquitectura de Software. Cada taller implementa de forma independiente una solución completa de servicios web y sus clientes multi-canal para un **Conversor de Unidades** bajo la modalidad **`SINBDD` (Sin Base de Datos)**.

| Parámetro | Detalle Institucional |
| :--- | :--- |
| **Institución** | Universidad de las Fuerzas Armadas ESPE |
| **Asignatura** | Arquitectura de Software |
| **Unidad Académica** | Unidad 1 — Arquitecturas Basadas en Servicios Web |
| **Talleres Consolidados** | TI.1.1 · TI.1.2 · TI.1.3 · TI.1.4 |
| **Periodo Académico** | PAO 2026-51 |
| **NRC** | 36884 |
| **Grupo** | GR09 |
| **Integrantes** | Julio Enrique Viche Castillo, Gabriel Omar Murillo Medina |

> [!INFO] Documentación y Navegación
> * Si interactúas como agente de IA o desarrollador, revisa **[`AGENTS.md`](AGENTS.md)** para conocer las directrices operativas.
> * La fuente de verdad documental se gestiona como bóveda de Obsidian en **[`vault/index.md`](vault/index.md)**.

---

## 1. Talleres Individuales (TI.1.1 a TI.1.4)

El repositorio está organizado en cuatro proyectos independientes, cada uno abordando un estilo de servicio web y una plataforma tecnológica específica:

| Taller | Protocolo | Plataforma | Servidor Web | Clientes Multiplataforma | Directorio del Taller |
| :---: | :---: | :---: | :--- | :--- | :--- |
| **TI.1.1** | **SOAP 1.2** | **Java** | JAX-WS en Apache Tomcat | Consola (CLI), Escritorio (Swing), Móvil (Android), Web (JSP) | [`TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09`](TI.1.1.%20PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09) |
| **TI.1.2** | **SOAP 1.2** | **.NET** | CoreWCF en Kestrel | Consola (CLI), Escritorio (WinForms), Móvil (Android .NET), Web (Blazor) | [`TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09`](TI.1.2.%20PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09) |
| **TI.1.3** | **RESTful** | **Java** | Jakarta REST / Tomcat | Consola (CLI), Escritorio (Swing), Móvil (Android), Web (HTML/JS) | [`TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09`](TI.1.3.%20PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09) |
| **TI.1.4** | **RESTful** | **.NET** | ASP.NET Core Web API / Kestrel | Consola (CLI), Escritorio (WinForms), Móvil (Android .NET), Web (Blazor) | [`TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09`](TI.1.4.%20PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09) |

---

## 2. Caso de Estudio: Convertidor de Unidades Multi-Canal

Cada uno de los talleres resuelve el mismo caso de estudio integral:
* **Autenticación Obligatoria:** Antes de acceder a las operaciones de conversión, el usuario debe autenticarse en el servidor.
* **Modalidad `SINBDD`:** Sin base de datos relacional ni ORM. Credenciales maestras fijas: usuario **`MONSTER`**, contraseña **`MONSTER9`**.
* **Gestión de Sesión:** Manejo de tokens de autenticación volátiles en memoria con expiración.
* **Catálogo Estricto de Unidades:** Exactamente 5 unidades por cada magnitud física soportada (Longitud, Masa, Tiempo, etc.).
* **Reglas de Formato Numérico:**
  * Entrada: Expresión regular `^-?\d+(\.\d+)?$`, signo negativo opcional solo al inicio, longitud máxima de 16 caracteres.
  * Salida: Presentación estricta con **2 decimales** (`%.2f` / `.toFixed(2)`) y **punto decimal (`.`)**.

---

## 3. Estructura Estándar de Entregables por Taller

Cada taller `TI.1.X` cumple con la estructura formal de entregables exigida por la cátedra:

```text
TI.1.X. PAO202651_<PROTOCOLO>_<PLATAFORMA>_SINBDD_NRC36884_GR09/
├── 01. UML/
│   ├── 01. ERS/                 # Especificación de Requisitos de Software (.docx IEEE 830)
│   ├── 02. ECUD/                # Especificación de Casos de Uso Detallados (.docx)
│   └── 03. DIAGRAMAS UML/       # Modelos PowerDesigner (.oom, .eam, .prj) y diagramas exportados
├── 02. MER/                     # README.md justificativo (SINBDD: sin modelos relacionales)
├── 03. BDD/                     # README.md justificativo (SINBDD: sin scripts SQL)
├── 04. CLICON/                  # Cliente de consola (CLI)
├── 04. CLIESC/                  # Cliente de escritorio (GUI)
├── 04. CLIMOV/                  # Cliente móvil (Android)
├── 04. CLIWEB/                  # Cliente web
├── 04. SERVIDOR/                # Servicio Web (SOAP / REST)
└── 05. DOCUMENTACION/           # Manual técnico (.docx) y Presentación de defensa (.pptx)
```

---

## 4. Automatización y Herramientas (`scripts/`)

El repositorio incluye un conjunto de utilitarios en Python para la sincronización, generación y verificación de entregables:

```bash
# 1. Instalar dependencias necesarias
pip install -r scripts/requirements.txt

# 2. Generar y sincronizar la documentación oficial en los talleres
python scripts/generate_docs.py

# 3. Validar la integridad estructural y autoría en los 16 entregables
python scripts/validate_deliverables.py

# 4. Sincronizar metadatos (integrantes, NRC, periodo) en todo el repositorio
python scripts/update_metadata.py
```

Consulta [`scripts/README.md`](scripts/README.md) para más detalles.

---

## 5. Compilación y Ejecución Rápida

Para ejecutar cualquier taller, primero se debe iniciar el **Servidor** y luego cualquiera de sus **Clientes**:

### Proyectos en Java (TI.1.1 y TI.1.3)
```bash
# Servidor / Clientes Java
cd "TI.1.X.../04. <COMPONENTE>"
mvn clean package
mvn exec:java
```

### Proyectos en .NET (TI.1.2 y TI.1.4)
```bash
# Servidor / Clientes .NET
cd "TI.1.X.../04. <COMPONENTE>"
dotnet build
dotnet run
```

### Clientes Android (CLIMOV)
```bash
cd "TI.1.X.../04. CLIMOV"
gradlew.bat assembleDebug
```

> [!TIP]
> Consulta la guía paso a paso en [`vault/04-guides/build-and-run.md`](vault/04-guides/build-and-run.md) para detalles de puertos, endpoints WSDL/Swagger y configuraciones de red.

---

## 6. Documentación y Bóveda Obsidian

| Recurso | Descripción |
| :--- | :--- |
| **[`AGENTS.md`](AGENTS.md)** | Reglas operativas, nomenclatura y flujos para agentes de IA |
| **[`vault/index.md`](vault/index.md)** | Índice MOC de toda la base de conocimiento en Obsidian |
| **[`vault/03-deliverables/document-mapping.md`](vault/03-deliverables/document-mapping.md)** | Matriz de trazabilidad documental y fuentes de verdad |
| **[`vault/03-deliverables/templates/`](vault/03-deliverables/templates/)** | Plantillas maestras de Word, PowerPoint y PowerDesigner |
| **`examples/`** | Proyectos y documentos de referencia institucional (solo lectura) |

### Uso con Obsidian
Abre la **carpeta raíz del repositorio** en Obsidian (*Open folder as vault*) para visualizar los diagramas Mermaid, tablas de trazabilidad y enlaces cruzados del proyecto.
