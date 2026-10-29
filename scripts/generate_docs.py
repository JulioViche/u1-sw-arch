#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
generate_docs.py - Generador y compilador de documentación oficial de los talleres TI.1.
Repositorio: u1-arch (Arquitectura de Software - ESPE)
Autores: Julio Enrique Viche Castillo / Gabriel Omar Murillo Medina
NRC: 36884 | Periodo: PAO 2026-51 | Grupo: GR09
"""

import os
import re
import sys
import shutil
import zipfile
import tempfile
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent.parent
TEMPLATES_DIR = ROOT_DIR / "vault" / "03-deliverables" / "templates"

VARIANTS_CONFIG = [
    {
        "folder": "TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09",
        "taller": "TI.1.1",
        "protocolo": "SOAP",
        "plataforma": "JAVA",
        "stack_label": "SOAP 1.2 / Java 17-21 (JAX-WS, Tomcat)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON SOAP EN JAVA SIN BDD",
        "ers_file": "ERS_SOAP_JAVA_SINBDD_GR09.docx",
        "ecud_file": "ECUD_SOAP_JAVA_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_SOAP_JAVA_GR09.docx",
        "ppt_file": "CONVUNI_SOAP_JAVA_GR09.pptx"
    },
    {
        "folder": "TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09",
        "taller": "TI.1.2",
        "protocolo": "SOAP",
        "plataforma": "DOTNET",
        "stack_label": "SOAP 1.2 / C# .NET 8 (CoreWCF, Kestrel)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON SOAP EN .NET SIN BDD",
        "ers_file": "ERS_SOAP_DOTNET_SINBDD_GR09.docx",
        "ecud_file": "ECUD_SOAP_DOTNET_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_SOAP_DOTNET_GR09.docx",
        "ppt_file": "CONVUNI_SOAP_DOTNET_GR09.pptx"
    },
    {
        "folder": "TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09",
        "taller": "TI.1.3",
        "protocolo": "RESTFUL",
        "plataforma": "JAVA",
        "stack_label": "RESTful JSON / Java 17-21 (Spring Boot, Tomcat)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON REST EN JAVA SIN BDD",
        "ers_file": "ERS_RESTFUL_JAVA_SINBDD_GR09.docx",
        "ecud_file": "ECUD_RESTFUL_JAVA_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_RESTFUL_JAVA_GR09.docx",
        "ppt_file": "CONVUNI_RESTFUL_JAVA_GR09.pptx"
    },
    {
        "folder": "TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09",
        "taller": "TI.1.4",
        "protocolo": "RESTFUL",
        "plataforma": "DOTNET",
        "stack_label": "RESTful JSON / C# .NET 8 (ASP.NET Core Web API, Kestrel)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON REST EN .NET SIN BDD",
        "ers_file": "ERS_RESTFUL_DOTNET_SINBDD_GR09.docx",
        "ecud_file": "ECUD_RESTFUL_DOTNET_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_RESTFUL_DOTNET_GR09.docx",
        "ppt_file": "CONVUNI_RESTFUL_DOTNET_GR09.pptx"
    }
]

METADATA = {
    "NRC": "36884",
    "PERIODO": "PAO 2026-51",
    "GRUPO": "GR09",
    "AUTORES_LINEA": "Julio Enrique Viche Castillo / Gabriel Omar Murillo Medina",
    "AUTOR_1_APELLIDOS": "Viche Castillo",
    "AUTOR_1_NOMBRES": "Julio Enrique",
    "AUTOR_2_APELLIDOS": "Murillo Medina",
    "AUTOR_2_NOMBRES": "Gabriel Omar",
    "LOMO_TECNICO": "VICHE / MURILLO",
}

def update_xml_in_zip(zip_path, xml_filename, transform_fn):
    """Aplica una función de transformación sobre un archivo XML dentro de un archivo Office (ZIP)."""
    temp_dir = tempfile.mkdtemp()
    temp_zip = Path(temp_dir) / zip_path.name
    modified = False

    try:
        with zipfile.ZipFile(zip_path, 'r') as zin, zipfile.ZipFile(temp_zip, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
            for item in zin.infolist():
                data = zin.read(item.filename)
                if item.filename == xml_filename:
                    text = data.decode('utf-8', errors='replace')
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

def apply_metadata_to_office(file_path):
    """Actualiza metadatos Dublin Core de cualquier archivo Office."""
    def transform_core(text):
        new_text = re.sub(r'<dc:creator>.*?</dc:creator>', f'<dc:creator>{METADATA["AUTORES_LINEA"]}</dc:creator>', text)
        new_text = re.sub(r'<cp:lastModifiedBy>.*?</cp:lastModifiedBy>', f'<cp:lastModifiedBy>{METADATA["AUTORES_LINEA"]}</cp:lastModifiedBy>', new_text)
        return new_text, (new_text != text)

    update_xml_in_zip(file_path, 'docProps/core.xml', transform_core)

def customize_word_document(docx_path, config):
    """Personaliza autores, lomo, título y encabezados de un documento Word."""
    def transform_doc(text):
        changed = False
        new_text = text

        # 1. Corrección estricta y limpia del Título del ERS (evita duplicaciones en carátula y TOC)
        if docx_path.name.startswith("ERS_") and "ers_title" in config:
            expected_title = config["ers_title"]

            # Reemplazar cualquier párrafo de carátula con paraId="1A71C88B" por un único run limpio
            clean_cover_para = (
                '<w:p w14:paraId="1A71C88B" w14:textId="15B5DFC4" w:rsidR="00B6784F" w:rsidRDefault="00B6784F" w:rsidP="00B6784F">'
                '<w:pPr><w:jc w:val="center"/><w:rPr><w:sz w:val="44"/><w:szCs w:val="44"/></w:rPr></w:pPr>'
                f'<w:r w:rsidRPr="00850009"><w:rPr><w:sz w:val="44"/><w:szCs w:val="44"/></w:rPr><w:t>{expected_title}</w:t></w:r>'
                '</w:p>'
            )
            sub_text, n_cover = re.subn(r'<w:p [^>]*paraId="1A71C88B"[^>]*>.*?</w:p>', clean_cover_para, new_text, flags=re.DOTALL)
            if n_cover > 0:
                new_text = sub_text
                changed = True

            # Reemplazar el Título 1 del cuerpo (P#30)
            pattern_h1 = r'(<w:p [^>]*paraId="60B8FD53"[^>]*>.*?<w:t[^>]*>).*?(</w:t>.*?</w:p>)'
            if re.search(pattern_h1, new_text, flags=re.DOTALL):
                sub_text = re.sub(pattern_h1, rf'\g<1>{expected_title}\g<2>', new_text, flags=re.DOTALL)
                if sub_text != new_text:
                    new_text = sub_text
                    changed = True

            # Reemplazar texto en el hipervínculo de la tabla de contenidos (TOC)
            pattern_toc = r'(<w:hyperlink w:anchor="_Toc229513488"[^>]*>.*?<w:t>).*?(</w:t>)'
            if re.search(pattern_toc, new_text, flags=re.DOTALL):
                sub_text = re.sub(pattern_toc, rf'\g<1>{expected_title}\g<2>', new_text, flags=re.DOTALL)
                if sub_text != new_text:
                    new_text = sub_text
                    changed = True

            # Quitar resaltados (highlights)
            if "<w:highlight" in new_text:
                new_text = re.sub(r'<w:highlight [^>]*/>', '', new_text)
                changed = True

            # Unificar estilos de tablas RF y RNF a Tablaconcuadrcula4-nfasis1
            if 'w:val="Tablaconcuadrcula5oscura-nfasis4"' in new_text:
                new_text = new_text.replace('w:val="Tablaconcuadrcula5oscura-nfasis4"', 'w:val="Tablaconcuadrcula4-nfasis1"')
                changed = True

            # Unificar fuentes de tablas a Calibri
            if 'w:ascii="Verdana"' in new_text:
                new_text = new_text.replace('w:ascii="Verdana"', 'w:ascii="Calibri"').replace('w:hAnsi="Verdana"', 'w:hAnsi="Calibri"')
                changed = True

            # Ajustar pie de tabla 26 (RNF-001) según protocolo
            proto_label = "SOAP" if "SOAP" in config.get("protocolo", "SOAP").upper() else "RESTful"
            new_text = re.sub(
                r'Tabla 26\. Requerimiento no funcional Protocolo de Arquitectura \((SOAP|RESTful)\)',
                f'Tabla 26. Requerimiento no funcional Protocolo de Arquitectura ({proto_label})',
                new_text
            )
            new_text = re.sub(
                r'Tabla 26\. RNF-001: Protocolo de Arquitectura \((SOAP|RESTful)\)',
                f'Tabla 26. RNF-001: Protocolo de Arquitectura ({proto_label})',
                new_text
            )
            # RNF-007: Mantenibilidad
            new_text = re.sub(
                r'(<w:t>RNF-00</w:t></w:r><w:r[^>]*><w:t>7</w:t></w:r>.*?</w:tc><w:tc>.*?<w:r[^>]*><w:t>)Accesibilidad(</w:t>)',
                r'\g<1>Mantenibilidad\g<2>',
                new_text,
                flags=re.DOTALL
            )

        # 2. Autores en carátula
        if "Murillo Medina" not in new_text and "Viche Castillo, Julio Enrique" in new_text:
            target = "Viche Castillo, Julio Enrique"
            replacement = 'Viche Castillo, Julio Enrique</w:t></w:r></w:p><w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Calibri" w:hAnsi="Calibri"/><w:b/><w:sz w:val="24"/></w:rPr><w:t>Murillo Medina, Gabriel Omar'
            new_text = new_text.replace(target, replacement)
            changed = True

        # 3. Lomo técnico
        if METADATA["LOMO_TECNICO"] not in new_text:
            for old_lomo in ["VICHE CASTILLO JULIO ENRIQUE", "JULIO VICHE"]:
                if old_lomo in new_text:
                    new_text = new_text.replace(old_lomo, METADATA["LOMO_TECNICO"])
                    changed = True

        # 4. NRC y Grupo
        if "30741" in new_text:
            new_text = new_text.replace("30741", METADATA["NRC"])
            changed = True
        if "GR69" in new_text:
            new_text = new_text.replace("GR69", METADATA["GRUPO"])
            changed = True
        if "GR03" in new_text:
            new_text = new_text.replace("GR03", METADATA["GRUPO"])
            changed = True

        return new_text, changed

    update_xml_in_zip(docx_path, 'word/document.xml', transform_doc)
    apply_metadata_to_office(docx_path)

def customize_pptx_document(pptx_path, config):
    """Personaliza autores, portada y láminas de la presentación PowerPoint."""
    def transform_slide(text):
        changed = False
        new_text = text

        if "GABRIEL OMAR MURILLO MEDINA" not in new_text:
            target = "INTEGRANTES: JULIO ENRIQUE VICHE CASTILLO"
            replacement = f"INTEGRANTES: {METADATA['AUTORES_LINEA'].upper()}"
            if target in new_text:
                new_text = new_text.replace(target, replacement)
                changed = True

        if "30741" in new_text:
            new_text = new_text.replace("30741", METADATA["NRC"])
            changed = True
        if "GR69" in new_text:
            new_text = new_text.replace("GR69", METADATA["GRUPO"])
            changed = True

        return new_text, changed

    update_xml_in_zip(pptx_path, 'ppt/slides/slide1.xml', transform_slide)
    apply_metadata_to_office(pptx_path)

def patch_ecud_images_for_dotnet(docx_path):
    """Reemplaza 'Servidor Payara - Seguridad' por 'Servidor .NET - Seguridad' en las imagenes incrustadas del docx."""
    try:
        from PIL import Image, ImageDraw, ImageFont
        import io

        temp_dir = tempfile.mkdtemp()
        temp_zip = Path(temp_dir) / docx_path.name
        modified = False

        with zipfile.ZipFile(docx_path, 'r') as zin, zipfile.ZipFile(temp_zip, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
            for item in zin.infolist():
                data = zin.read(item.filename)
                if item.filename.startswith("word/media/image") and item.filename.endswith(".png") and any(f"image{n}.png" in item.filename for n in [2, 3, 4, 5]):
                    im = Image.open(io.BytesIO(data)).convert("RGB")
                    w, h = im.size
                    pixels = im.load()
                    gray_pixels = [
                        (x, y) for y in range(h) for x in range(w)
                        if abs(pixels[x, y][0] - pixels[x, y][1]) < 5 and abs(pixels[x, y][1] - pixels[x, y][2]) < 5 and 150 < pixels[x, y][0] < 200
                    ]
                    if gray_pixels:
                        min_x = min(p[0] for p in gray_pixels)
                        max_x = max(p[0] for p in gray_pixels)
                        min_y = min(p[1] for p in gray_pixels)
                        center_x = (min_x + max_x) // 2

                        draw = ImageDraw.Draw(im)
                        draw.rectangle([min_x + 15, min_y + 15, max_x - 15, min_y + 70], fill=(192, 192, 192))
                        font = ImageFont.truetype("C:\\Windows\\Fonts\\arialbd.ttf", 16)
                        draw.text((center_x, min_y + 26), "Servidor .NET -", fill=(0, 0, 0), font=font, anchor="mt")
                        draw.text((center_x, min_y + 44), "Seguridad", fill=(0, 0, 0), font=font, anchor="mt")

                        buf = io.BytesIO()
                        im.save(buf, format="PNG")
                        data = buf.getvalue()
                        modified = True
                zout.writestr(item, data)

        if modified:
            shutil.move(str(temp_zip), str(docx_path))
        shutil.rmtree(temp_dir, ignore_errors=True)
    except Exception as e:
        print(f"    [WARN] No se pudieron parchear imagenes para .NET: {e}")

def customize_ecud_document(docx_path, config):
    """Personaliza la Especificación de Casos de Uso Detallados (ECUD) según protocolo y plataforma."""
    protocolo = config.get("protocolo", "SOAP").upper()
    plataforma = config.get("plataforma", "JAVA").upper()
    is_soap = "SOAP" in protocolo
    is_dotnet = "DOTNET" in plataforma

    def transform_ecud(text):
        changed = False
        new_text = text

        # 1. Quitar cualquier resaltado
        if "<w:highlight" in new_text:
            new_text = re.sub(r'<w:highlight [^>]*/>', '', new_text)
            changed = True

        # 2. Formato estricto a 2 decimales (Regla 10)
        if "4 decimales" in new_text:
            new_text = new_text.replace("4 decimales", "2 decimales")
            changed = True

        # 3. Normalizar credenciales quemadas (MONSTER / MONSTER9)
        if '&quot;Monster&quot;' in new_text:
            new_text = new_text.replace('&quot;Monster&quot;', '&quot;MONSTER&quot;')
            changed = True
        if '&quot;Monster9&quot;' in new_text:
            new_text = new_text.replace('&quot;Monster9&quot;', '&quot;MONSTER9&quot;')
            changed = True
        if '"Monster"' in new_text:
            new_text = new_text.replace('"Monster"', '"MONSTER"')
            changed = True
        if '"Monster9"' in new_text:
            new_text = new_text.replace('"Monster9"', '"MONSTER9"')
            changed = True

        # 4. Autores de los casos de uso: primer nombre y apellido
        old_authors = [
            "Coloma Kevin, Granda Carlos, Moreira Erick.",
            "Viche Castillo, Julio Enrique; Murillo Medina, Gabriel Omar."
        ]
        for oa in old_authors:
            if oa in new_text:
                new_text = new_text.replace(oa, "Julio Viche, Gabriel Murillo")
                changed = True

        # 5. Adaptaciones por protocolo (SOAP vs REST)
        if is_soap:
            replacements = [
                ("recursos y endpoints de la API REST", "operaciones del servicio web SOAP"),
                ("recursos de la API REST", "operaciones del servicio web SOAP"),
                ("endpoints de la API REST", "operaciones del servicio web SOAP"),
                ("API REST", "servicio web SOAP"),
                ("petición HTTP a la API REST", "petición SOAP sobre HTTP al servicio web"),
                ("en formato JSON", "en envoltorio SOAP (XML)")
            ]
            for old, new in replacements:
                if old in new_text:
                    new_text = new_text.replace(old, new)
                    changed = True

        # 6. Adaptaciones por plataforma (Java vs .NET)
        if is_dotnet:
            replacements_plat = [
                ("servidor Payara 7", "servidor .NET (Kestrel)"),
                ("servidor Payara", "servidor .NET"),
                ("Servidor Payara", "Servidor .NET")
            ]
            for old, new in replacements_plat:
                if old in new_text:
                    new_text = new_text.replace(old, new)
                    changed = True

        return new_text, changed

    update_xml_in_zip(docx_path, 'word/document.xml', transform_ecud)
    if is_dotnet:
        patch_ecud_images_for_dotnet(docx_path)
    apply_metadata_to_office(docx_path)

def generate_variant_documents(cfg, fresh=False):
    """Genera o sincroniza los 4 entregables para una variante específica."""
    variant_root = ROOT_DIR / cfg["folder"]
    print(f"\n[{cfg['taller']}] Procesando {cfg['protocolo']} en {cfg['plataforma']}...")

    # Carpetas destino
    ers_dir = variant_root / "01. UML" / "01. ERS"
    ecud_dir = variant_root / "01. UML" / "02. ECUD"
    doc_dir = variant_root / "05. DOCUMENTACION"

    ers_dir.mkdir(parents=True, exist_ok=True)
    ecud_dir.mkdir(parents=True, exist_ok=True)
    doc_dir.mkdir(parents=True, exist_ok=True)

    # 1. ERS Word
    target_ers = ers_dir / cfg["ers_file"]
    if fresh or not target_ers.exists():
        src_ers = TEMPLATES_DIR / "word" / "PLANTILLA_ERS.docx"
        shutil.copy2(src_ers, target_ers)
        print(f"  + Inicializado ERS desde plantilla: {cfg['ers_file']}")
    customize_word_document(target_ers, cfg)
    print(f"  [OK] ERS actualizado: {cfg['ers_file']} ({target_ers.stat().st_size / 1024:.1f} KB)")

    # 2. ECUD Word
    target_ecud = ecud_dir / cfg["ecud_file"]
    if fresh or not target_ecud.exists():
        src_ecud = TEMPLATES_DIR / "word" / "PLANTILLA_ECUD.docx"
        shutil.copy2(src_ecud, target_ecud)
        print(f"  + Inicializado ECUD desde plantilla: {cfg['ecud_file']}")
    customize_ecud_document(target_ecud, cfg)
    print(f"  [OK] ECUD actualizado: {cfg['ecud_file']} ({target_ecud.stat().st_size / 1024:.1f} KB)")

    # 3. Manual Técnico Word
    target_manual = doc_dir / cfg["manual_file"]
    if fresh or not target_manual.exists():
        src_manual = TEMPLATES_DIR / "word" / "PLANTILLA_MANUAL_TECNICO.docx"
        shutil.copy2(src_manual, target_manual)
        print(f"  + Inicializado Manual Técnico desde plantilla: {cfg['manual_file']}")
    customize_word_document(target_manual, cfg)
    print(f"  [OK] Manual Técnico actualizado: {cfg['manual_file']} ({target_manual.stat().st_size / 1024:.1f} KB)")

    # 4. Presentación PPTX
    target_ppt = doc_dir / cfg["ppt_file"]
    if fresh or not target_ppt.exists():
        src_ppt = TEMPLATES_DIR / "powerpoint" / "PLANTILLA_PRESENTACION.pptx"
        shutil.copy2(src_ppt, target_ppt)
        print(f"  + Inicializada Presentación desde plantilla: {cfg['ppt_file']}")
    customize_pptx_document(target_ppt, cfg)
    print(f"  [OK] Presentación actualizada: {cfg['ppt_file']} ({target_ppt.stat().st_size / 1024:.1f} KB)")

def main():
    print("=" * 70)
    print("GENERADOR Y COMPILADOR DE DOCUMENTACION - TI.1 (ESPE GR09)")
    print(f"Autores: {METADATA['AUTORES_LINEA']}")
    print(f"NRC: {METADATA['NRC']} | Grupo: {METADATA['GRUPO']} | Periodo: {METADATA['PERIODO']}")
    print("=" * 70)

    fresh_mode = "--fresh" in sys.argv

    for cfg in VARIANTS_CONFIG:
        generate_variant_documents(cfg, fresh=fresh_mode)

    print("\n" + "=" * 70)
    print("[EXITO] Proceso de compilación y sincronización documental concluido.")
    print("=" * 70)

if __name__ == "__main__":
    main()
