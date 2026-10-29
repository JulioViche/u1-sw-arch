# TI.1.1 — Arquitectura de Servicios Web (PAO 2026-51)

Repositorio académico con **cuatro variantes** del mismo sistema: un **conversor de
unidades** multi-canal construido como servicio web **RESTful** y **SOAP**, cada uno en **Java** y
**.NET**, y explícitamente **sin base de datos** (`SINBDD`).

| Dato | Valor |
| --- | --- |
| Asignatura | Arquitectura de Software |
| Tarea | TI.1 (TI.1.1 a TI.1.4) |
| Periodo | PAO 2026-51 |
| NRC | 36884 |
| Grupo | GR09 |
| Integrantes | Julio Enrique Viche Castillo, Gabriel Omar Murillo Medina |

> [!info] Documentación para agentes de IA
> Lee **[AGENTS.md](AGENTS.md)** antes de interactuar con el repositorio.
> La fuente de verdad documental vive en el vault de Obsidian: **[vault/index.md](vault/index.md)**.

---

## 1. Variantes del Proyecto

Cada variante es un **proyecto independiente** con su propia estructura de clientes y servidor:

| Variante | Protocolo | Plataforma | Servidor | Clientes | Carpeta |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TI.1.1** | SOAP 1.2 | Java 17/21 | JAX-WS / Tomcat | Consola, Swing, Android, Web (JSP) | [`TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09`](TI.1.1.%20PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09) |
| **TI.1.2** | SOAP 1.2 | C# .NET 8 | CoreWCF / Kestrel | Consola, WinForms, Android, Blazor | [`TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09`](TI.1.2.%20PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09) |
| **TI.1.3** | RESTful | Java 17/21 | Jakarta REST / Tomcat | Consola, Swing, Android, Web | [`TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09`](TI.1.3.%20PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09) |
| **TI.1.4** | RESTful | C# .NET 8 | ASP.NET Core Web API | Consola, WinForms, Android, Blazor | [`TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09`](TI.1.4.%20PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09) |

---

## 2. Estructura Estándar de cada Variante

```text
TI.1.X. PAO202651_<PROTOCOLO>_<PLATAFORMA>_SINBDD_NRC36884_GR09/
├── 01. UML/
│   ├── 01. ERS/              # Especificación de Requisitos de Software (.docx)
│   ├── 02. ECUD/             # Especificación de Casos de Uso Detallados (.docx)
│   └── 03. DIAGRAMAS UML/    # Modelos PowerDesigner y diagramas exportados
├── 02. MER/                  # Vacío con .gitkeep (SINBDD: sin base de datos)
├── 03. BDD/                  # Vacío con .gitkeep (SINBDD: sin base de datos)
├── 04. CLICON/               # Cliente de consola
├── 04. CLIESC/               # Cliente de escritorio
├── 04. CLIMOV/               # Cliente móvil (Android)
├── 04. CLIWEB/               # Cliente web
├── 04. SERVIDOR/             # Servicio web (REST o SOAP)
└── 05. DOCUMENTACION/        # Manual técnico (.docx) y Presentación (.pptx)
```

---

## 3. Scripts de Automatización (`scripts/`)

El repositorio incluye herramientas en Python para validar y compilar la documentación oficial:

```bash
# Instalar dependencias requeridas
pip install -r scripts/requirements.txt

# Generar y sincronizar los 16 documentos oficiales desde las plantillas del Vault
python scripts/generate_docs.py

# Validar integridad estructural y verificar metadatos de autores en los 16 entregables
python scripts/validate_deliverables.py

# Actualizar metadatos académicos en todo el proyecto
python scripts/update_metadata.py
```

Más información en la guía de [scripts/README.md](scripts/README.md).

---

## 4. Inicio Rápido de Ejecución

Cada variante se ejecuta con las herramientas nativas de su stack:

| Stack | Compilar / Ejecutar |
| :--- | :--- |
| **Java + Maven** | `mvn clean package` · `mvn exec:java` |
| **.NET SDK** | `dotnet build` · `dotnet run` |
| **Android (CLIMOV)** | `gradlew.bat assembleDebug` |

> [!WARNING]
> El **servidor debe estar en ejecución** antes de lanzar cualquier cliente.
> Consulta [Compilar y ejecutar](vault/04-guides/build-and-run.md) para el flujo detallado paso a paso.

---

## 5. Reglas Clave y Restricciones Técnicas

1. **Autenticación Obligatoria:** Todo cliente inicia con una pantalla o prompt de login antes de permitir la conversión.
2. **Credenciales Quemadas (`SINBDD`):** Usuario **`MONSTER`**, Contraseña **`MONSTER9`**.
3. **Formateo Numérico Estricto:** Presentación a **2 decimales** (`%.2f` / `.toFixed(2)`) con punto decimal (`.`), entrada numérica con regex `^-?\d+(\.\d+)?$` y longitud máxima de 16 caracteres.
4. **Catálogo de Unidades:** Exactamente 5 unidades por magnitud (Longitud, Masa, Tiempo, etc.).

---

## 6. Documentación y Bóveda Obsidian

| Recurso | Propósito |
| :--- | :--- |
| **[AGENTS.md](AGENTS.md)** | Reglas operativas y convenciones de arquitectura para agentes de IA |
| **[vault/index.md](vault/index.md)** | Índice maestro de la base de conocimiento en Obsidian |
| **[vault/03-deliverables/document-mapping.md](vault/03-deliverables/document-mapping.md)** | Matriz de trazabilidad y plantillas del proyecto |
| **`examples/`** | Solución de referencia institucional (solo lectura, ignorado en git) |

### Abrir en Obsidian
Abre la **raíz del repositorio** como bóveda en Obsidian (*Open folder as vault*) para navegar fluidamente entre todas las notas y enlaces internos.
