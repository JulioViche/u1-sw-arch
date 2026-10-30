#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
generate_docs.py - Generador y compilador de documentación oficial de los talleres TI.1.
Repositorio: u1-arch (Arquitectura de Software - ESPE)
Autores: Gabriel Omar Murillo Medina / Julio Enrique Viche Castillo
NRC: 36884 | Periodo: PAO 2026-51 | Grupo: GR09
"""

import os
import re
import sys
import shutil
import zipfile
import tempfile
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent.parent
TEMPLATES_DIR = ROOT_DIR / "vault" / "03-deliverables" / "templates"

VARIANTS_CONFIG = [
    {
        "folder": "TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09",
        "taller": "TI.1.1",
        "protocolo": "SOAP",
        "plataforma": "JAVA",
        "stack_label": "SOAP 1.1 / Java 11 (Metro JAX-WS, Payara Server 7)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON SOAP EN JAVA SIN BDD",
        "ers_file": "ERS_SOAP_JAVA_SINBDD_GR09.docx",
        "ecud_file": "ECUD_SOAP_JAVA_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_SOAP_JAVA_GR09.docx",
        "ppt_file": "CONVUNI_SOAP_JAVA_GR09.pptx",
        "vault_project": "ti-1-1-soap-java"
    },
    {
        "folder": "TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09",
        "taller": "TI.1.2",
        "protocolo": "SOAP",
        "plataforma": "DOTNET",
        "stack_label": "SOAP 1.1 / C# .NET 10 (ManejadorSOAP, Kestrel)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON SOAP EN .NET SIN BDD",
        "ers_file": "ERS_SOAP_DOTNET_SINBDD_GR09.docx",
        "ecud_file": "ECUD_SOAP_DOTNET_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_SOAP_DOTNET_GR09.docx",
        "ppt_file": "CONVUNI_SOAP_DOTNET_GR09.pptx",
        "vault_project": "ti-1-2-soap-dotnet"
    },
    {
        "folder": "TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09",
        "taller": "TI.1.3",
        "protocolo": "RESTFUL",
        "plataforma": "JAVA",
        "stack_label": "RESTful JSON / Java 17 (Jersey 3.1.5, Jakarta REST, Payara Server 7)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON REST EN JAVA SIN BDD",
        "ers_file": "ERS_RESTFUL_JAVA_SINBDD_GR09.docx",
        "ecud_file": "ECUD_RESTFUL_JAVA_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_RESTFUL_JAVA_GR09.docx",
        "ppt_file": "CONVUNI_RESTFUL_JAVA_GR09.pptx",
        "vault_project": "ti-1-3-rest-java"
    },
    {
        "folder": "TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09",
        "taller": "TI.1.4",
        "protocolo": "RESTFUL",
        "plataforma": "DOTNET",
        "stack_label": "RESTful JSON / C# .NET 10 (ASP.NET Core Web API, Kestrel)",
        "ers_title": "ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON REST EN .NET SIN BDD",
        "ers_file": "ERS_RESTFUL_DOTNET_SINBDD_GR09.docx",
        "ecud_file": "ECUD_RESTFUL_DOTNET_SINBDD_GR09.docx",
        "manual_file": "CONVUNI_RESTFUL_DOTNET_GR09.docx",
        "ppt_file": "CONVUNI_RESTFUL_DOTNET_GR09.pptx",
        "vault_project": "ti-1-4-rest-dotnet"
    }
]

METADATA = {
    "NRC": "36884",
    "PERIODO": "PAO 2026-51",
    "GRUPO": "GR09",
    "AUTORES_FORMAL": "Murillo Medina, Gabriel Omar; Viche Castillo, Julio Enrique",
    "AUTORES_LINEA": "Gabriel Omar Murillo Medina / Julio Enrique Viche Castillo",
    "AUTOR_1_APELLIDOS": "Murillo Medina",
    "AUTOR_1_NOMBRES": "Gabriel Omar",
    "AUTOR_2_APELLIDOS": "Viche Castillo",
    "AUTOR_2_NOMBRES": "Julio Enrique",
    "LOMO_TECNICO": "MURILLO / VICHE",
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
        new_text = re.sub(r'<dc:creator>.*?</dc:creator>', f'<dc:creator>{METADATA["AUTORES_FORMAL"]}</dc:creator>', text)
        new_text = re.sub(r'<cp:lastModifiedBy>.*?</cp:lastModifiedBy>', f'<cp:lastModifiedBy>{METADATA["AUTORES_FORMAL"]}</cp:lastModifiedBy>', new_text)
        return new_text, (new_text != text)

    update_xml_in_zip(file_path, 'docProps/core.xml', transform_core)

def customize_word_document(docx_path, config):
    """Personaliza autores, lomo, título y encabezados de un documento Word."""
    def transform_doc(text):
        changed = False
        new_text = text

        # 1. Corrección estricta del Título del ERS
        if docx_path.name.startswith("ERS_") and "ers_title" in config:
            expected_title = config["ers_title"]

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

            pattern_h1 = r'(<w:p [^>]*paraId="60B8FD53"[^>]*>.*?<w:t[^>]*>).*?(</w:t>.*?</w:p>)'
            if re.search(pattern_h1, new_text, flags=re.DOTALL):
                sub_text = re.sub(pattern_h1, rf'\g<1>{expected_title}\g<2>', new_text, flags=re.DOTALL)
                if sub_text != new_text:
                    new_text = sub_text
                    changed = True

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

            # Unificar estilos de tablas RF y RNF
            if 'w:val="Tablaconcuadrcula5oscura-nfasis4"' in new_text:
                new_text = new_text.replace('w:val="Tablaconcuadrcula5oscura-nfasis4"', 'w:val="Tablaconcuadrcula4-nfasis1"')
                changed = True

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
            replacement = 'Murillo Medina, Gabriel Omar</w:t></w:r></w:p><w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Calibri" w:hAnsi="Calibri"/><w:b/><w:sz w:val="24"/></w:rPr><w:t>Viche Castillo, Julio Enrique'
            new_text = new_text.replace(target, replacement)
            changed = True

        # 3. Lomo técnico
        for old_lomo in ["VICHE CASTILLO JULIO ENRIQUE", "JULIO VICHE", "VICHE / MURILLO"]:
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
        for old_cred in ['&quot;Monster&quot;', '"Monster"']:
            if old_cred in new_text:
                new_text = new_text.replace(old_cred, '&quot;MONSTER&quot;')
                changed = True
        for old_pass in ['&quot;Monster9&quot;', '"Monster9"']:
            if old_pass in new_text:
                new_text = new_text.replace(old_pass, '&quot;MONSTER9&quot;')
                changed = True

        # 4. Asegurar autores formales
        if "Gabriel Murillo y Julio Viche" in new_text:
            new_text = new_text.replace("Gabriel Murillo y Julio Viche", METADATA["AUTORES_FORMAL"])
            changed = True
        if "{{AUTORES_CU}}" in new_text:
            new_text = new_text.replace("{{AUTORES_CU}}", METADATA["AUTORES_FORMAL"])
            changed = True

        # 5. Parametrización estándar mediante marcadores de plantilla
        servidor_nombre = "servidor .NET (Kestrel)" if is_dotnet else "servidor Payara 7"
        servidor_estado = "Servidor .NET en estado operativo" if is_dotnet else "Servidor Payara en estado operativo"

        if is_soap:
            if not is_dotnet:  # SOAP Java (TI.1.1)
                meta_servicio = "operaciones del servicio web SOAP (JAX-WS)"
                desc_servicio = "operaciones del servicio web SOAP (JAX-WS)"
                canal_peticion = "SOAP al servicio web (JAX-WS)."
                login_validacion = "1. El sistema recibe la solicitud SOAP login() y valida las credenciales en memoria RAM (SINBDD)."
            else:  # SOAP .NET (TI.1.2)
                meta_servicio = "contratos de operación del servicio web SOAP (CoreWCF / ManejadorSOAP)"
                desc_servicio = "contratos de operación del servicio web SOAP (CoreWCF / ManejadorSOAP)"
                canal_peticion = "SOAP al servicio web (CoreWCF / ManejadorSOAP)."
                login_validacion = "1. El sistema recibe la solicitud SOAP Login() y valida las credenciales en memoria RAM (SINBDD)."
            formato_respuesta = "en el sobre de respuesta SOAP (XML)"
        else:
            if not is_dotnet:  # RESTful Java (TI.1.3)
                meta_servicio = "recursos y endpoints de la API REST (Jersey)"
                desc_servicio = "recursos de la API REST (Jersey)"
                canal_peticion = "HTTP POST a los endpoints de la API REST (Jersey)."
                login_validacion = "1. El sistema recibe la petición HTTP POST /auth/login y valida las credenciales en memoria RAM (SINBDD)."
            else:  # RESTful .NET (TI.1.4)
                meta_servicio = "recursos y endpoints de la API REST (ASP.NET Core Web API)"
                desc_servicio = "controladores de la API REST (ASP.NET Core)"
                canal_peticion = "HTTP POST a los controladores de la API REST (ASP.NET Core)."
                login_validacion = "1. El sistema recibe la petición HTTP POST /api/auth/login y valida las credenciales en memoria RAM (SINBDD)."
            formato_respuesta = "en formato JSON"

        placeholders = {
            "{{VAR_META_SERVICIO}}": meta_servicio,
            "{{VAR_DESC_SERVICIO}}": desc_servicio,
            "{{VAR_SERVIDOR_NOMBRE}}": servidor_nombre,
            "{{VAR_SERVIDOR_ESTADO}}": servidor_estado,
            "{{VAR_CANAL_PETICION}}": canal_peticion,
            "{{VAR_LOGIN_VALIDACION}}": login_validacion,
            "{{VAR_FORMATO_RESPUESTA}}": formato_respuesta
        }
        for k, v in placeholders.items():
            if k in new_text:
                new_text = new_text.replace(k, v)
                changed = True

        if is_dotnet:
            for s_old, s_new in [("servidor Payara 7", "servidor .NET (Kestrel)"), ("servidor Payara", "servidor .NET"), ("Servidor Payara", "Servidor .NET")]:
                if s_old in new_text:
                    new_text = new_text.replace(s_old, s_new)
                    changed = True

        return new_text, changed

    update_xml_in_zip(docx_path, 'word/document.xml', transform_ecud)
    apply_metadata_to_office(docx_path)

def populate_pptx_from_docx_images(pptx_path, docx_path, slide_mapping):
    """Extrae imágenes del manual Word e inyecta en láminas PPTX con figuras pendientes."""
    try:
        temp_dir = tempfile.mkdtemp()
        temp_zip = Path(temp_dir) / Path(pptx_path).name

        extracted_images = {}
        with zipfile.ZipFile(docx_path, 'r') as z_docx:
            for item in z_docx.namelist():
                if item.startswith('word/media/'):
                    img_name = Path(item).name
                    extracted_images[img_name] = z_docx.read(item)

        with zipfile.ZipFile(pptx_path, 'r') as zin, zipfile.ZipFile(temp_zip, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
            all_files = set(zin.namelist())

            for s_num, img_list in slide_mapping.items():
                slide_file = f"ppt/slides/slide{s_num}.xml"
                rels_file = f"ppt/slides/_rels/slide{s_num}.xml.rels"

                if slide_file not in all_files or rels_file not in all_files:
                    continue

                slide_xml = zin.read(slide_file).decode('utf-8', errors='replace')
                rels_xml = zin.read(rels_file).decode('utf-8', errors='replace')

                # Check if slide has placeholder or needs images
                has_pendiente = ("CAPTURA PENDIENTE" in slide_xml)
                has_pics = ("<p:pic>" in slide_xml)

                if has_pendiente:
                    # Parse rels
                    rels_root = ET.fromstring(rels_xml)
                    existing_rids = [int(r.attrib['Id'].replace('rId', '')) for r in rels_root if r.attrib.get('Id', '').startswith('rId') and r.attrib['Id'].replace('rId', '').isdigit()]
                    next_rid = max(existing_rids) + 1 if existing_rids else 1

                    assigned_rids = []
                    for img_name in img_list:
                        target_media = f"../media/{img_name}"
                        rel_id = None
                        for r in rels_root:
                            if r.attrib.get('Target') == target_media:
                                rel_id = r.attrib['Id']
                                break
                        if not rel_id:
                            rel_id = f"rId{next_rid}"
                            next_rid += 1
                            rel_elem = ET.Element('{http://schemas.openxmlformats.org/package/2006/relationships}Relationship')
                            rel_elem.attrib['Id'] = rel_id
                            rel_elem.attrib['Type'] = 'http://schemas.openxmlformats.org/officeDocument/2006/relationships/image'
                            rel_elem.attrib['Target'] = target_media
                            rels_root.append(rel_elem)
                        assigned_rids.append(rel_id)

                    new_rels_xml = ET.tostring(rels_root, encoding='utf-8', xml_declaration=True).decode('utf-8')

                    # Parse slide
                    slide_root = ET.fromstring(slide_xml)
                    namespaces = {'p': 'http://schemas.openxmlformats.org/presentationml/2006/main',
                                  'a': 'http://schemas.openxmlformats.org/drawingml/2006/main'}
                    spTree = slide_root.find('.//p:spTree', namespaces)

                    shapes_to_replace = []
                    for sp in spTree.findall('p:sp', namespaces):
                        txBody = sp.find('p:txBody', namespaces)
                        if txBody is not None:
                            full_t = "".join(t.text for t in txBody.findall('.//a:t', namespaces) if t.text)
                            if "CAPTURA PENDIENTE" in full_t:
                                shapes_to_replace.append(sp)

                    for idx, sp in enumerate(shapes_to_replace):
                        spPr = sp.find('p:spPr', namespaces)
                        xfrm = spPr.find('a:xfrm', namespaces) if spPr is not None else None
                        off = xfrm.find('a:off', namespaces) if xfrm is not None else None
                        ext = xfrm.find('a:ext', namespaces) if xfrm is not None else None

                        x = off.attrib.get('x', '1000000') if off is not None else '1000000'
                        y = off.attrib.get('y', '2000000') if off is not None else '2000000'
                        cx = ext.attrib.get('cx', '7000000') if ext is not None else '7000000'
                        cy = ext.attrib.get('cy', '4000000') if ext is not None else '4000000'

                        r_id = assigned_rids[idx % len(assigned_rids)]
                        pic_id = 900 + idx

                        pic_xml_str = f'''<p:pic xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main" xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                            <p:nvPicPr>
                                <p:cNvPr id="{pic_id}" name="Evidencia {pic_id}"/>
                                <p:cNvPicPr><a:picLocks noChangeAspect="1"/></p:cNvPicPr>
                                <p:nvPr/>
                            </p:nvPicPr>
                            <p:blipFill>
                                <a:blip xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" r:embed="{r_id}"/>
                                <a:stretch><a:fillRect/></a:stretch>
                            </p:blipFill>
                            <p:spPr>
                                <a:xfrm><a:off x="{x}" y="{y}"/><a:ext cx="{cx}" cy="{cy}"/></a:xfrm>
                                <a:prstGeom prst="rect"><a:avLst/></a:prstGeom>
                            </p:spPr>
                        </p:pic>'''

                        pic_elem = ET.fromstring(pic_xml_str)
                        spTree_children = list(spTree)
                        pos = spTree_children.index(sp)
                        spTree.remove(sp)
                        spTree.insert(pos, pic_elem)

                    new_slide_xml = ET.tostring(slide_root, encoding='utf-8', xml_declaration=True).decode('utf-8')
                    all_files.add(slide_file)
                    all_files.add(rels_file)

            for item in zin.infolist():
                data = zin.read(item.filename)
                zout.writestr(item, data)

            # Copy any missing media
            existing_media = set(zin.namelist())
            for img_name, img_data in extracted_images.items():
                media_path = f"ppt/media/{img_name}"
                if media_path not in existing_media:
                    zout.writestr(media_path, img_data)

        shutil.move(str(temp_zip), str(pptx_path))
        shutil.rmtree(temp_dir)
    except Exception as e:
        print(f"    [WARN] Excepción al procesar PPTX con imágenes: {e}")

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
    print(f"  [OK] ERS sincronizado: {cfg['ers_file']} ({target_ers.stat().st_size / 1024:.1f} KB)")

    # 2. ECUD Word (6 Casos de Uso con PowerDesigner)
    target_ecud = ecud_dir / cfg["ecud_file"]
    if fresh or not target_ecud.exists():
        src_ecud = TEMPLATES_DIR / "word" / "PLANTILLA_ECUD.docx"
        shutil.copy2(src_ecud, target_ecud)
        print(f"  + Sincronizado ECUD desde plantilla maestra: {cfg['ecud_file']}")
    customize_ecud_document(target_ecud, cfg)
    print(f"  [OK] ECUD sincronizado: {cfg['ecud_file']} ({target_ecud.stat().st_size / 1024:.1f} KB)")

    # 3. Manual Técnico Word
    target_manual = doc_dir / cfg["manual_file"]
    if fresh or not target_manual.exists():
        src_manual = TEMPLATES_DIR / "word" / "PLANTILLA_MANUAL_TECNICO.docx"
        shutil.copy2(src_manual, target_manual)
        print(f"  + Inicializado Manual Técnico desde plantilla: {cfg['manual_file']}")
    customize_word_document(target_manual, cfg)
    print(f"  [OK] Manual Técnico sincronizado: {cfg['manual_file']} ({target_manual.stat().st_size / 1024:.1f} KB)")

    # 4. Presentación PPTX
    target_ppt = doc_dir / cfg["ppt_file"]
    if fresh or not target_ppt.exists():
        src_ppt = TEMPLATES_DIR / "powerpoint" / "PLANTILLA_PRESENTACION.pptx"
        shutil.copy2(src_ppt, target_ppt)
        print(f"  + Inicializada Presentación desde plantilla: {cfg['ppt_file']}")
    customize_pptx_document(target_ppt, cfg)
    print(f"  [OK] Presentación sincronizada: {cfg['ppt_file']} ({target_ppt.stat().st_size / 1024:.1f} KB)")

def main():
    print("=" * 70)
    print("GENERADOR Y COMPILADOR DE DOCUMENTACION - TI.1 (ESPE GR09)")
    print(f"Autores: {METADATA['AUTORES_FORMAL']}")
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
