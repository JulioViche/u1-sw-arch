# AGENTS.md

Guía operativa para cualquier agente de IA que trabaje en este repositorio.
Objetivo: que un agente entienda **qué es el proyecto**, **dónde va cada cosa** y **cómo
trabajar sin romper la coherencia** entre las cuatro variantes.

---

## 1. Contexto

Repositorio académico de **Arquitectura de Software** (tarea **TI.1.1**, PAO 2026-51,
NRC **36884**, grupo **GR09**, integrante **Julio Enrique Viche Castillo**). Contiene
**cuatro proyectos independientes** que resuelven
el mismo caso —un **conversor de unidades** con inicio de sesión— usando:

- **RESTful** y **SOAP** como estilos de servicio web.
- **Java** y **.NET** como plataformas.
- **Sin base de datos** (`SINBDD`): los datos son fijos o en memoria.

> **Todo proyecto debe iniciar con un login** (caso de uso obligatorio, en los cuatro
> clientes). Para esta unidad las credenciales van **quemadas en el código** (hardcoded):
> usuario **`MONSTER`**, contraseña **`MONSTER9`**. Ver [authentication](vault/01-architecture/authentication.md).

Cada proyecto vive en su carpeta de primer nivel y comparte la **misma estructura de
entregables**.

---

## 2. Reglas para agentes

1. **No modifiques `examples/`.** Es material de referencia e ignorado por git. Solo lectura.
2. **No introduzcas base de datos.** El proyecto es `SINBDD`. Evita ORM, migraciones y
   scripts SQL como requisito. Por eso las carpetas `02. MER/` y `03. BDD/` quedan **vacías**.
3. **Trabaja en la variante correcta.** No mezcles archivos entre RESTful/SOAP ni entre Java/.NET.
4. **Respeta la estructura y la nomenclatura** (secciones 3 y 4).
5. **No hagas commits ni cambios en git** salvo que el usuario lo pida de forma explícita.
6. **Idioma: español** para documentación y artefactos. Código: identificadores en inglés o
   español según la convención que ya exista en la carpeta.
7. **Mantén la coherencia entre variantes.** Mismas entidades, casos de uso y contrato lógico
   en las cuatro, aunque cambien la tecnología y el estilo de servicio.
8. **Uso de `.gitkeep` y contenido en `01. ERS/`:**
   - No borres `.gitkeep` en carpetas vacías (ej. `02. MER/`, `03. BDD/`).
   - En las carpetas `01. UML/01. ERS/` solo va el archivo Word oficial (`.docx`), sin archivos `.md` duplicados ni `.gitkeep` innecesarios.
9. **Todo proyecto inicia con login.** Antes de convertir, el usuario debe autenticarse con
   las credenciales **quemadas** `MONSTER` / `MONSTER9`. Ver
   [authentication](vault/01-architecture/authentication.md).
10. **Requerimientos adicionales y formato visual:**
    - Formateo estricto a **2 decimales** (`%.2f` / `.toFixed(2)`) con **punto decimal (`.`)** en la presentación de todos los clientes (`CLICON`, `CLIESC`, `CLIWEB`, `CLIMOV`). En la entrada solo se permiten dígitos (`0-9`), el punto (`.`) y el signo negativo (`-`) únicamente al inicio de la cifra (`^-?\d+(\.\d+)?$`), con límite máximo de **16 caracteres** exclusivo para el campo del valor numérico a convertir.
    - Catálogo estricto de exactamente **5 unidades por magnitud** ("ni una más, ni una menos"). Ver [additional-requirements](vault/01-architecture/additional-requirements.md).

---

## 3. Mapa del repositorio

```
u1-arch/
├── README.md                     # Visión general del repositorio
├── AGENTS.md                     # Este archivo
├── .gitignore                    # Ignora examples/
├── vault/                        # Documentación Obsidian (fuente de verdad)
├── examples/                     # Solución de referencia (NO tocar)
└── TI.1.1. PAO202651_<PROTOCOLO>_<PLATAFORMA>_SINBDD_NRC36884_GR09/   # x4 variantes
```

Variantes:

| Carpeta | Protocolo | Plataforma |
| --- | --- | --- |
| `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09` | SOAP | Java |
| `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09` | SOAP | .NET |
| `TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09` | REST | Java |
| `TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09` | REST | .NET |

---

## 4. Estructura estándar de una variante

```
<TI.1.1. PAO202651_..._GR09>/
├── 01. UML/
│   ├── 01. ERS/                 # Especificación de Requisitos de Software (.docx)
│   ├── 02. ECUD/                # Casos de uso detallados (.pdf/.docx)
│   └── 03. DIAGRAMAS UML/       # 01_CASOS_DE_USO, 02_ACTIVIDAD, 03_SECUENCIA,
│                                # 04_CLASES, 05_ARQUITECTURA (modelos + exportados)
├── 02. MER/                     # Vacío (SINBDD): sin base de datos
├── 03. BDD/                     # Vacío (SINBDD): sin base de datos
├── 04. CLICON/                  # Cliente de consola
├── 04. CLIESC/                  # Cliente de escritorio
├── 04. CLIMOV/                  # Cliente móvil (Android)
├── 04. CLIWEB/                  # Cliente web
├── 04. SERVIDOR/                # Servicio web (REST o SOAP)
└── 05. DOCUMENTACION/           # Manual técnico, presentación y anexos
```

El código de cada componente va **dentro de su carpeta** (`04. SERVIDOR/`, `04. CLICON/`, etc.).
La subestructura recomendada de `03. DIAGRAMAS UML` proviene del ejemplo de referencia y es
opcional pero recomendada. `02. MER/` y `03. BDD/` permanecen **vacías** porque el proyecto
es SINBDD (sin base de datos).

---

## 5. Convenciones de nomenclatura

Patrones observados en la solución de referencia (`examples/`). Sustituye `<GRUPO>` por `GR09`.

| Artefacto | Patrón | Ejemplo de referencia |
| --- | --- | --- |
| ERS | `ERS_<PROTOCOLO>_<PLATAFORMA>_SINBDD_<GRUPO>.docx` | `ERS_SOAP_JAVA_SINBDD_GR69.docx` |
| Plantilla ERS | `ERS_<...>_PLANTILLA.docx` | `ERS_SOAP_JAVA_SINBDD_GR69_PLANTILLA.docx` |
| ECUD | `ECUD_<PROYECTO>_<GRUPO>.pdf` | `ECUD_CONVERSION_UNIDADES_G03.pdf` |
| Manual técnico | `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.docx` | `CONVUNI_SOAP_JAVA_GR69.docx` |
| Presentación | `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.pptx` | `CONVUNI_SOAP_JAVA_GR69.pptx` |

Reglas generales:

- Carpetas de entregables numeradas: `NN. NOMBRE` (ej. `04. SERVIDOR`).
- Subcarpetas de diagramas: `NN_NOMBRE` (ej. `01_CASOS_DE_USO`).
- No uses espacios sueltos al final ni caracteres inválidos de Windows (`: * ? " < > |`).
- Mantén un `README.txt` breve por carpeta si necesitas dejar notas de contexto.

---

## 6. Flujo de trabajo por tarea

**Añadir código a una variante**
1. Identifica la variante y el componente (`SERVIDOR`, `CLICON`, `CLIWEB`, …).
2. Coloca el proyecto completo dentro de esa carpeta.
3. Verifica que compila/ejecuta con el stack correspondiente.
4. Actualiza la nota del vault si cambia el contrato del servicio.

**Añadir/ajustar diagramas**
1. Guarda el modelo editable en la subcarpeta de `03. DIAGRAMAS UML/`.
2. Exporta la imagen (PNG/PDF) junto al modelo.
3. Refleja el cambio en [01-uml](vault/03-deliverables/01-uml.md).

**Documentación**
- La fuente de verdad es `vault/`. Si cambia una convención, actualiza el vault **y**
  este `AGENTS.md`.

---

## 7. Comandos útiles

| Acción | Comando |
| --- | --- |
| Java + Maven (build) | `mvn clean package` |
| Java + Maven (run) | `mvn exec:java` |
| .NET (build) | `dotnet build` |
| .NET (run) | `dotnet run` |
| Android (build) | `gradlew.bat assembleDebug` |

> Ejecuta el **servidor** antes que los clientes. Ver
> [build-and-run](vault/04-guides/build-and-run.md).

---

## 8. Dónde está la documentación

Empieza por **[index](vault/index.md)** (índice MOC). Notas clave:

- [overview](vault/01-architecture/overview.md)
- [folder-structure](vault/01-architecture/folder-structure.md)
- [naming-conventions](vault/01-architecture/naming-conventions.md)
- [authentication](vault/01-architecture/authentication.md) · [additional-requirements](vault/01-architecture/additional-requirements.md)
- [variant-matrix](vault/02-variants/variant-matrix.md)
- [01-uml](vault/03-deliverables/01-uml.md) · [02-mer](vault/03-deliverables/02-mer.md) ·
  [03-bdd](vault/03-deliverables/03-bdd.md) · [04-clients-and-server](vault/03-deliverables/04-clients-and-server.md) ·
  [05-documentation](vault/03-deliverables/05-documentation.md)
- [build-and-run](vault/04-guides/build-and-run.md) · [generate-ers](vault/04-guides/generate-ers.md)
- [glossary](vault/05-reference/glossary.md) · [academic-data](vault/05-reference/academic-data.md)

---

## 9. Checklist antes de terminar

- [ ] El cambio está en la variante y el componente correctos.
- [ ] No se tocó `examples/`.
- [ ] No se introdujo base de datos.
- [ ] El proyecto inicia con login (credenciales quemadas `MONSTER` / `MONSTER9`).
- [ ] La estructura y la nomenclatura se respetan.
- [ ] El código compila/ejecuta en su stack.
- [ ] La documentación del vault quedó actualizada.
