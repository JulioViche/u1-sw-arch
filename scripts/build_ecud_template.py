#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
build_ecud_template.py - Reconstruye y optimiza la plantilla oficial de ECUD
a partir de examples/ECUD_CONVERSION_UNIDADES_REST_JAVA_GR05.docx con control
estructural estricto de Word (OpenXML):
  1. Convierte dibujos de casos de uso (wp:anchor) a dibujos en línea (wp:inline) centrados.
  2. Elimina los 12 párrafos vacíos artificiales dentro de las celdas de gráficos.
  3. Escala los diagramas al tamaño óptimo de celda (~82%).
  4. Agrega <w:cantSplit/> a todas las filas de tabla para evitar cortes entre páginas.
  5. Agrega <w:keepWithNext/> a títulos y encabezados de casos de uso.
  6. Estandariza el catálogo a exactamente 5 unidades por magnitud (Vault).
  7. Normaliza validaciones de entrada (máx 16 caracteres, 2 decimales, MONSTER/MONSTER9).
  8. Corrige la postcondición de CU-04 (Temperatura).
  9. Deja marcadores limpios para parametrización rápida por variante.
  10. Preserva al 100% los namespaces OpenXML de Word (sin corromper con ns0).
"""

import zipfile
import re
import shutil
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent.parent
SRC_FILE = ROOT_DIR / "examples" / "ECUD_CONVERSION_UNIDADES_REST_JAVA_GR05.docx"
DST_FILE = ROOT_DIR / "vault" / "03-deliverables" / "templates" / "word" / "PLANTILLA_ECUD.docx"

def anchor_to_inline(match):
    anchor_xml = match.group(0)
    ext_m = re.search(r'<wp:extent cx="(\d+)" cy="(\d+)"\s*/>', anchor_xml)
    if not ext_m:
        return anchor_xml
    cx, cy = int(ext_m.group(1)), int(ext_m.group(2))
    new_cx, new_cy = int(cx * 0.82), int(cy * 0.82)
    
    docpr_m = re.search(r'<wp:docPr [^>]*/>', anchor_xml)
    docpr = docpr_m.group(0) if docpr_m else '<wp:docPr id="100" name="Picture"/>'
    
    cnv_m = re.search(r'<wp:cNvGraphicFramePr>.*?</wp:cNvGraphicFramePr>', anchor_xml, re.DOTALL)
    cnv = cnv_m.group(0) if cnv_m else '<wp:cNvGraphicFramePr><a:graphicFrameLocks xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" noChangeAspect="1"/></wp:cNvGraphicFramePr>'
    
    graphic_m = re.search(r'<a:graphic [^>]*>.*?</a:graphic>', anchor_xml, re.DOTALL)
    if not graphic_m:
        return anchor_xml
    graphic = graphic_m.group(0)
    
    inline = (
        f'<wp:inline distT="0" distB="0" distL="0" distR="0">'
        f'<wp:extent cx="{new_cx}" cy="{new_cy}"/>'
        f'<wp:effectExtent l="0" t="0" r="0" b="0"/>'
        f'{docpr}'
        f'{cnv}'
        f'{graphic}'
        f'</wp:inline>'
    )
    return f'<w:drawing>{inline}</w:drawing>'

def process_tc(tc_match):
    tc = tc_match.group(0)
    if '<w:drawing>' in tc and ('Ilustraci' in tc or 'Caso de Uso' in tc):
        # Celda de gráfico de caso de uso
        # 1. Transformar dibujo a inline
        tc = re.sub(r'<w:drawing>.*?</w:drawing>', anchor_to_inline, tc, flags=re.DOTALL)
        
        # 2. Filtrar párrafos: mantener solo el dibujo centrado y la leyenda de la ilustración centrada
        paras = re.findall(r'<w:p\b[^>]*>.*?</w:p>', tc, re.DOTALL)
        clean_paras = []
        for p in paras:
            txt = ''.join(re.findall(r'<w:t[^>]*>(.*?)</w:t>', p)).strip()
            has_d = '<w:drawing>' in p
            if has_d:
                if '<w:jc' in p:
                    p = re.sub(r'<w:jc [^>]*/>', '<w:jc w:val="center"/>', p)
                elif '<w:pPr>' in p:
                    p = p.replace('<w:pPr>', '<w:pPr><w:jc w:val="center"/>')
                else:
                    p = p.replace('>', '><w:pPr><w:jc w:val="center"/></w:pPr>', 1)
                clean_paras.append(p)
            elif txt:
                if '<w:jc' in p:
                    p = re.sub(r'<w:jc [^>]*/>', '<w:jc w:val="center"/>', p)
                elif '<w:pPr>' in p:
                    p = p.replace('<w:pPr>', '<w:pPr><w:jc w:val="center"/>')
                clean_paras.append(p)
        
        tcPr_m = re.search(r'<w:tcPr>.*?</w:tcPr>', tc, re.DOTALL)
        tcPr = tcPr_m.group(0) if tcPr_m else ''
        tc = f'<w:tc>{tcPr}{"".join(clean_paras)}</w:tc>'
    return tc

def process_tr(tr_match):
    tr = tr_match.group(0)
    if '<w:trPr>' in tr:
        if '<w:cantSplit' not in tr:
            tr = tr.replace('<w:trPr>', '<w:trPr><w:cantSplit/>')
    else:
        tr = tr.replace('>', '><w:trPr><w:cantSplit/></w:trPr>', 1)
    return tr

def build():
    print(f"Leyendo origen base: {SRC_FILE}")
    temp_dst = DST_FILE.with_suffix(".tmp.docx")

    with zipfile.ZipFile(SRC_FILE, 'r') as zin, zipfile.ZipFile(temp_dst, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
        for item in zin.infolist():
            data = zin.read(item.filename)

            if item.filename == "word/document.xml":
                xml = data.decode("utf-8")

                # 1. Quitar todos los resaltados amarillos
                xml = re.sub(r'<w:highlight [^>]*/>', '', xml)

                # 2. Carátula institucional
                xml = xml.replace("COLOMA ERAZO KEVIN BLADIMIR", "VICHE CASTILLO JULIO ENRIQUE")
                xml = xml.replace("GRANDA VELASQUEZ CARLOS JAVIER", "MURILLO MEDINA GABRIEL OMAR")
                xml = xml.replace("MOREIRA VINUEZA ERICK PATRICIO", "NRC: 36884 | GRUPO: GR09")

                # 3. Autores en tablas de casos de uso (primer nombre y primer apellido)
                xml = xml.replace("Coloma Kevin, Granda Carlos, Moreira Erick.", "{{AUTORES_CU}}")
                xml = xml.replace("Viche Castillo, Julio Enrique; Murillo Medina, Gabriel Omar.", "{{AUTORES_CU}}")
                xml = xml.replace("Julio Viche, Gabriel Murillo", "{{AUTORES_CU}}")

                # 4. Credenciales fijas
                xml = xml.replace('&quot;Monster&quot;', '&quot;MONSTER&quot;')
                xml = xml.replace('&quot;Monster9&quot;', '&quot;MONSTER9&quot;')
                xml = xml.replace('"Monster"', '"MONSTER"')
                xml = xml.replace('"Monster9"', '"MONSTER9"')

                # 5. Formato numérico a 2 decimales
                xml = xml.replace("4 decimales", "2 decimales")

                # 6. Catálogo estricto de 5 unidades por magnitud (Vault / additional-requirements.md)
                xml = xml.replace("Metros, Kilómetros, Pulgadas, Pies o Centímetros", "Metros, Centímetros, Pies, Yardas o Millas")
                xml = xml.replace("Metros, Kilometros, Pulgadas, Pies o Centimetros", "Metros, Centímetros, Pies, Yardas o Millas")
                xml = xml.replace("Kilogramos, Gramos, Libras, Onzas y Toneladas", "Gramos, Kilogramos, Libras, Onzas y Quintales")
                xml = xml.replace("(Kg, g, lb, oz, t)", "(g, kg, lb, oz, q)")
                xml = xml.replace("Celsius, Fahrenheit, Kelvin, Rankine y Reaumur", "Celsius, Fahrenheit, Kelvin, Newton y Reaumur")
                xml = xml.replace("Celsius, Fahrenheit, Kelvin, Rankine o Reaumur", "Celsius, Fahrenheit, Kelvin, Newton o Reaumur")

                # 7. Corrección de postcondición en CU-04 (Temperatura)
                xml = xml.replace(
                    "El sistema ha actualizado la base de usuarios centralizada, permitiendo o denegando accesos según los cambios realizados.",
                    "Visualización del resultado de la conversión térmica en la interfaz del cliente (Móvil, Web, Escritorio o Consola)."
                )

                # 8. Validación de entrada en flujos alternativos (Regla 10: máx 16 caracteres)
                xml = xml.replace("datos no numéricos, </w:t>", "datos no numéricos o mayores a 16 caracteres, </w:t>")
                xml = xml.replace("datos no numéricos,</w:t>", "datos no numéricos o mayores a 16 caracteres,</w:t>")

                # 9. Marcadores estandarizados para parametrización por variante
                xml = xml.replace("recursos y endpoints de la API REST", "{{VAR_META_SERVICIO}}")
                xml = xml.replace("recursos de la API REST", "{{VAR_DESC_SERVICIO}}")
                xml = xml.replace("1. El servidor Payara 7 debe estar activo y el cliente debe tener conexión a la red local.",
                                  "1. El {{VAR_SERVIDOR_NOMBRE}} debe estar activo y el cliente debe tener conexión a la red local.")
                xml = xml.replace("Servidor Payara en estado operativo", "{{VAR_SERVIDOR_ESTADO}}")
                xml = xml.replace("servidor Payara", "{{VAR_SERVIDOR_NOMBRE}}")
                xml = xml.replace("Servidor Payara", "{{VAR_SERVIDOR_NOMBRE}}")
                xml = xml.replace("en formato JSON", "{{VAR_FORMATO_RESPUESTA}}")

                xml = xml.replace("<w:t>HTTP a la API REST.</w:t>", "<w:t>{{VAR_CANAL_PETICION}}</w:t>")
                xml = xml.replace("1. El sistema recibe la petición y valida los datos con los valores estáticos definidos en memoria.",
                                  "{{VAR_LOGIN_VALIDACION}}")

                # 10. Control estructural estricto de OpenXML (sin alterar namespaces)
                # a. Celda de gráficos -> inline, centrados y sin párrafos vacíos
                xml = re.sub(r'<w:tc\b.*?</w:tc>', process_tc, xml, flags=re.DOTALL)

                # b. Filas de tabla -> cantSplit
                xml = re.sub(r'<w:tr\b.*?</w:tr>', process_tr, xml, flags=re.DOTALL)

                # c. Títulos de tablas -> keepWithNext
                def add_keep_with_next(p_match):
                    p = p_match.group(0)
                    if any(k in p for k in ['Tabla 1', 'Tabla 2', 'Tabla 3', 'Tabla 4', 'ECUD_']):
                        if '<w:keepWithNext' not in p:
                            if '<w:pPr>' in p:
                                p = p.replace('<w:pPr>', '<w:pPr><w:keepWithNext/>')
                            else:
                                p = p.replace('>', '><w:pPr><w:keepWithNext/></w:pPr>', 1)
                    return p
                xml = re.sub(r'<w:p\b[^>]*>.*?</w:p>', add_keep_with_next, xml, flags=re.DOTALL)

                data = xml.encode('utf-8')

            elif item.filename == "docProps/core.xml":
                core_xml = data.decode("utf-8")
                core_xml = re.sub(r'<dc:creator>.*?</dc:creator>', '<dc:creator>Julio Enrique Viche Castillo / Gabriel Omar Murillo Medina</dc:creator>', core_xml)
                core_xml = re.sub(r'<cp:lastModifiedBy>.*?</cp:lastModifiedBy>', '<cp:lastModifiedBy>Julio Enrique Viche Castillo</cp:lastModifiedBy>', core_xml)
                core_xml = re.sub(r'<dc:title>.*?</dc:title>', '<dc:title>Especificación de Casos de Uso Detallados - Conversor de Unidades (SINBDD)</dc:title>', core_xml)
                data = core_xml.encode("utf-8")

            zout.writestr(item, data)

    shutil.move(str(temp_dst), str(DST_FILE))
    print(f"[OK] Plantilla maestra ECUD estructurada: {DST_FILE} ({DST_FILE.stat().st_size / 1024:.1f} KB)")

if __name__ == "__main__":
    build()
