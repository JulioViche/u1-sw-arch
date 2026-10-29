# Plantillas Reutilizables Oficiales (Obtenidas de `examples/`)

Este directorio contiene las **plantillas maestras oficiales** (Word, PowerPoint y SAP PowerDesigner) extraídas y consolidadas a partir de la solución de referencia académica provista por la cátedra en `examples/`.

---

## Trazabilidad y Origen Exacto en `examples/`

| Archivo en `templates/` | Ruta de Origen Exacta en `examples/` | Rol del Documento |
| :--- | :--- | :--- |
| **`word/PLANTILLA_ERS.docx`** | `examples/ERS_SOAP_JAVA_SINBDD_GR69_PLANTILLA.docx` | Plantilla institucional IEEE 830 provista por el docente para el ERS. |
| **`word/PLANTILLA_MANUAL_TECNICO.docx`** | `examples/.../GR69/.../05. DOCUMENTACION/02. MANUAL TECNICO/CONVUNI_SOAP_JAVA_GR69.docx` | Manual técnico de referencia con estructura y diseño institucional completo. |
| **`word/REFERENCIA_ECUD_ORIGINAL.pdf`** | `examples/.../GR03/.../01. UML/02. ECUD/ECUD_CONVERSION_UNIDADES_G03.pdf` | Documento de Casos de Uso Detallados original entregado por la referencia. |
| **`word/PLANTILLA_ECUD.docx`** | Replicación fiel y editable en Word del PDF original de referencia de la cátedra. | Plantilla editable `.docx` para generar los ECUD de las 4 variantes. |
| **`powerpoint/PLANTILLA_PRESENTACION.pptx`** | `examples/.../GR69/.../05. DOCUMENTACION/02. MANUAL TECNICO/CONVUNI_SOAP_JAVA_GR69.pptx` | Presentación oficial de sustentación de referencia (diapositivas, estilos y colores). |
| **`powerdesigner/CON_UNI_PLANTILLA.sws`** | `examples/.../GR03/SOAP_EN_JAVA/CON_UNI_GR03.sws` | Espacio de trabajo (.sws) que vincula todos los proyectos UML en SAP PowerDesigner. |
| **`powerdesigner/01_CASOS_DE_USO/*`** | `examples/.../GR69/.../01. UML/03. DIAGRAMAS UML/01_CASOS_DE_USO/` | Modelos `.oom` (`OOm_Casos_De_Uso.oom`, `CU_Ingresar_Sistema.oom`) y proyecto `.prj`. |
| **`powerdesigner/02_ACTIVIDAD/*`** | `examples/.../GR69/.../01. UML/03. DIAGRAMAS UML/02_ACTIVIDAD/` | Modelos `.oom` (`ActividadIniciarSesión.oom`, `ActividadConvertirUnidades.oom`) y `.prj`. |
| **`powerdesigner/03_SECUENCIA/*`** | `examples/.../GR69/.../01. UML/03. DIAGRAMAS UML/03_SECUENCIA/` | Modelos `.oom` (`Secuencia.oom`, `SecuenciaConvertirUnidades.oom`) y `.prj`. |
| **`powerdesigner/04_CLASES/*`** | `examples/.../GR69/.../01. UML/03. DIAGRAMAS UML/04_CLASES/README.txt` | Documento que ratifica la condición `SINBDD` del proyecto. |
| **`powerdesigner/05_ARQUITECTURA/*`** | `examples/.../GR69/.../01. UML/03. DIAGRAMAS UML/05_ARQUITECTURA/` | Modelo de arquitectura empresarial `.eam` (`Arquitectura.eam`) y `.prj`. |

---

## Mapeo con la Fuente de Verdad en Markdown (`vault/`)

| Plantilla en `templates/` | Fuente Markdown en `vault/` | Salida en las 4 Variantes (`TI.1.X...`) |
| :--- | :--- | :--- |
| `word/PLANTILLA_ERS.docx` | [`srs-template.md`](../srs-template.md) | `01. UML/01. ERS/ERS_*.docx` |
| `word/PLANTILLA_ECUD.docx` | [`ecud-template.md`](../ecud-template.md) | `01. UML/02. ECUD/ECUD_*.docx` |
| `word/PLANTILLA_MANUAL_TECNICO.docx` | [`technical-manual-template.md`](../technical-manual-template.md) | `05. DOCUMENTACION/CONVUNI_*.docx` |
| `powerpoint/PLANTILLA_PRESENTACION.pptx` | [`presentation-template.md`](../presentation-template.md) | `05. DOCUMENTACION/CONVUNI_*.pptx` |
| `powerdesigner/*` | [`01-uml.md`](../01-uml.md) | `01. UML/03. DIAGRAMAS UML/` |
