# Scripts de Automatización y Validación

Este directorio contiene herramientas en Python para la sincronización de metadatos, validación de coherencia y generación documental de las cuatro variantes del taller **TI.1**.

---

## 1. Requisitos

- Python 3.10+
- Dependencias (instalación con pip):

```bash
pip install -r scripts/requirements.txt
```

---

## 2. Herramientas Disponibles

### `generate_docs.py`
Generador y compilador de documentación oficial (`ERS`, `ECUD`, `Manual Técnico` y `Presentación`) para las cuatro variantes a partir de las plantillas de `vault/03-deliverables/templates/` y metadatos del proyecto.

**Uso:**
```bash
# Modo sincronización segura (actualiza metadatos y portadas preservando capturas técnicas)
python scripts/generate_docs.py

# Modo inicialización limpia (reemplaza con plantillas base frescas)
python scripts/generate_docs.py --fresh
```

### `update_metadata.py`
Sincroniza y actualiza la metadata académica en los 16 documentos oficiales Office (`.docx` y `.pptx`) de las cuatro variantes del proyecto:
- Autores: **Julio Enrique Viche Castillo** / **Gabriel Omar Murillo Medina**.
- NRC: **36884**.
- Periodo: **PAO 2026-51**.
- Grupo: **GR09**.

**Uso:**
```bash
python scripts/update_metadata.py
```

### `validate_deliverables.py`
Realiza un escaneo integral y verificación de integridad:
- Comprueba la existencia de todas las subcarpetas requeridas según la arquitectura estándar del repositorio (`01. UML`, `02. MER`, `03. BDD`, `04. CLICON`, `04. CLIESC`, `04. CLIMOV`, `04. CLIWEB`, `04. SERVIDOR`, `05. DOCUMENTACION`).
- Valida la presencia de `.gitkeep` en las carpetas vacías (`02. MER/` y `03. BDD/`).
- Inspecciona los 16 archivos Office (`.docx` y `.pptx`) para verificar que no estén vacíos y contengan a ambos autores.

**Uso:**
```bash
python scripts/validate_deliverables.py
```

---

## 3. Relación con la Fuente de Verdad (`vault/`)

Los modelos y plantillas base de los documentos se gestionan en:
- `vault/03-deliverables/document-mapping.md`: Matriz de trazabilidad global.
- `vault/03-deliverables/templates/`: Plantillas físicas Word, PowerPoint y PowerDesigner.
- `vault/05-reference/academic-data.md`: Datos de autores, NRC y grupo.
