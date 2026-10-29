#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
update_metadata.py - Script de sincronización y actualización de metadatos académicos.
Repositorio: u1-arch (Arquitectura de Software - ESPE)
Autores: Julio Enrique Viche Castillo / Gabriel Omar Murillo Medina
NRC: 36884 | Periodo: PAO 2026-51 | Grupo: GR09
"""

import os
import re
import sys
import zipfile
import tempfile
import shutil
from pathlib import Path

# Configuración por defecto de metadatos
DEFAULT_METADATA = {
    "NRC": "36884",
    "PERIODO": "PAO 2026-51",
    "GRUPO": "GR09",
    "AUTORES": [
        "Julio Enrique Viche Castillo",
        "Gabriel Omar Murillo Medina"
    ],
    "DOC_AUTHORS_LINE": "Julio Enrique Viche Castillo / Gabriel Omar Murillo Medina",
    "DOC_AUTHORS_BLOCK": "Viche Castillo, Julio Enrique\nMurillo Medina, Gabriel Omar"
}

ROOT_DIR = Path(__file__).resolve().parent.parent

VARIANTS = [
    "TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09",
    "TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09",
    "TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09",
    "TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09"
]

def update_xml_in_zip(zip_path, xml_filename, transform_fn):
    """Aplica transform_fn sobre el contenido de un archivo XML dentro de un archivo ZIP/Office."""
    temp_dir = tempfile.mkdtemp()
    temp_zip = Path(temp_dir) / zip_path.name

    modified = False
    try:
        with zipfile.ZipFile(zip_path, 'r') as zin, zipfile.ZipFile(temp_zip, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
            for item in zin.infolist():
                data = zin.read(item.filename)
                if item.filename == xml_filename:
                    text = data.decode('utf-8')
                    new_text, was_changed = transform_fn(text)
                    if was_changed:
                        data = new_text.encode('utf-8')
                        modified = True
                zout.writestr(item, data)

        if modified:
            shutil.move(str(temp_zip), str(zip_path))
    finally:
        shutil.rmtree(temp_dir, ignore_errors=True)

    return modified

def sync_office_metadata(file_path, authors_str):
    """Actualiza los metadatos Dublin Core (core.xml) del archivo Office."""
    def transform_core(text):
        new_text = re.sub(r'<dc:creator>.*?</dc:creator>', f'<dc:creator>{authors_str}</dc:creator>', text)
        new_text = re.sub(r'<cp:lastModifiedBy>.*?</cp:lastModifiedBy>', f'<cp:lastModifiedBy>{authors_str}</cp:lastModifiedBy>', new_text)
        return new_text, (new_text != text)

    update_xml_in_zip(file_path, 'docProps/core.xml', transform_core)

def sync_word_document(docx_path):
    """Asegura que el documento Word contenga ambos integrantes en su portada y lomo."""
    def transform_doc(text):
        changed = False
        new_text = text

        # 1. Portada autores
        if "Murillo Medina" not in new_text:
            target_single = "Viche Castillo, Julio Enrique"
            replacement_double = "Viche Castillo, Julio Enrique</w:t></w:r></w:p><w:p><w:pPr><w:jc w:val=\"center\"/></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Calibri\" w:hAnsi=\"Calibri\"/><w:b/><w:sz w:val=\"24\"/></w:rPr><w:t>Murillo Medina, Gabriel Omar"
            if target_single in new_text:
                new_text = new_text.replace(target_single, replacement_double)
                changed = True

        # 2. Lomo técnico
        if "VICHE / MURILLO" not in new_text:
            if "VICHE CASTILLO JULIO ENRIQUE" in new_text:
                new_text = new_text.replace("VICHE CASTILLO JULIO ENRIQUE", "VICHE / MURILLO")
                changed = True
            elif "JULIO VICHE" in new_text:
                new_text = new_text.replace("JULIO VICHE", "VICHE / MURILLO")
                changed = True

        return new_text, changed

    update_xml_in_zip(docx_path, 'word/document.xml', transform_doc)
    sync_office_metadata(docx_path, DEFAULT_METADATA["DOC_AUTHORS_LINE"])

def sync_pptx_document(pptx_path):
    """Asegura que la presentación PPTX contenga ambos integrantes en la lámina 1."""
    def transform_slide(text):
        changed = False
        new_text = text
        if "GABRIEL OMAR MURILLO MEDINA" not in new_text:
            target = "INTEGRANTES: JULIO ENRIQUE VICHE CASTILLO"
            replacement = "INTEGRANTES: JULIO ENRIQUE VICHE CASTILLO / GABRIEL OMAR MURILLO MEDINA"
            if target in new_text:
                new_text = new_text.replace(target, replacement)
                changed = True
        return new_text, changed

    update_xml_in_zip(pptx_path, 'ppt/slides/slide1.xml', transform_slide)
    sync_office_metadata(pptx_path, DEFAULT_METADATA["DOC_AUTHORS_LINE"])

def process_all_deliverables():
    """Recorre las 4 variantes y actualiza los 16 documentos oficiales."""
    print("==> Procesando documentos Office de las variantes...")
    count = 0
    for variant in VARIANTS:
        variant_dir = ROOT_DIR / variant
        if not variant_dir.exists():
            continue

        for ext in ["*.docx", "*.pptx"]:
            for file_path in variant_dir.rglob(ext):
                if file_path.suffix.lower() == ".docx":
                    sync_word_document(file_path)
                elif file_path.suffix.lower() == ".pptx":
                    sync_pptx_document(file_path)
                count += 1
                print(f"  [OK] Sincronizado: {file_path.relative_to(ROOT_DIR)}")

    print(f"==> Total documentos procesados: {count}")

def main():
    print("=" * 60)
    print("Sincronizador de Metadata u1-arch")
    print(f"Autores: {DEFAULT_METADATA['DOC_AUTHORS_LINE']}")
    print(f"NRC: {DEFAULT_METADATA['NRC']} | Grupo: {DEFAULT_METADATA['GRUPO']}")
    print("=" * 60)
    process_all_deliverables()
    print("\n[OK] Metadata sincronizada exitosamente.")

if __name__ == "__main__":
    main()
