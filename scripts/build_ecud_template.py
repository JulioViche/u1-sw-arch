#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
build_ecud_template.py - Reemplaza y parametriza la plantilla de ECUD
utilizando examples/ECUD_CONVERSION_UNIDADES_REST_JAVA_GR05.docx como base oficial.
"""

import zipfile
import re
import shutil
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent.parent
SRC_FILE = ROOT_DIR / "examples" / "ECUD_CONVERSION_UNIDADES_REST_JAVA_GR05.docx"
DST_FILE = ROOT_DIR / "vault" / "03-deliverables" / "templates" / "word" / "PLANTILLA_ECUD.docx"

def build():
    print(f"Leyendo origen: {SRC_FILE}")
    temp_dst = DST_FILE.with_suffix(".tmp.docx")

    with zipfile.ZipFile(SRC_FILE, 'r') as zin, zipfile.ZipFile(temp_dst, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
        for item in zin.infolist():
            data = zin.read(item.filename)
            
            if item.filename == "word/document.xml":
                xml = data.decode("utf-8")
                
                # 1. Carátula (Portador Choice + Fallback)
                xml = xml.replace("COLOMA ERAZO KEVIN BLADIMIR", "VICHE CASTILLO JULIO ENRIQUE")
                xml = xml.replace("GRANDA VELASQUEZ CARLOS JAVIER", "MURILLO MEDINA GABRIEL OMAR")
                xml = xml.replace("MOREIRA VINUEZA ERICK PATRICIO", "NRC: 36884 | GRUPO: GR09")
                
                # 2. Tablas de Casos de Uso (Autores: Primer nombre y primer apellido)
                xml = xml.replace("Coloma Kevin, Granda Carlos, Moreira Erick.", "Julio Viche, Gabriel Murillo")
                xml = xml.replace("Viche Castillo, Julio Enrique; Murillo Medina, Gabriel Omar.", "Julio Viche, Gabriel Murillo")
                
                # 3. Credenciales quemadas estandarizadas (MONSTER / MONSTER9)
                xml = xml.replace('&quot;Monster&quot;', '&quot;MONSTER&quot;')
                xml = xml.replace('&quot;Monster9&quot;', '&quot;MONSTER9&quot;')
                xml = xml.replace('"Monster"', '"MONSTER"')
                xml = xml.replace('"Monster9"', '"MONSTER9"')
                
                # 4. Quitar todos los resaltados
                xml = re.sub(r'<w:highlight [^>]*/>', '', xml)
                
                # 5. Formato de decimales estricto a 2 (Regla 10)
                xml = xml.replace("4 decimales", "2 decimales")
                
                data = xml.encode("utf-8")

            elif item.filename == "docProps/core.xml":
                core_xml = data.decode("utf-8")
                core_xml = re.sub(r'<dc:creator>.*?</dc:creator>', '<dc:creator>Julio Enrique Viche Castillo / Gabriel Omar Murillo Medina</dc:creator>', core_xml)
                core_xml = re.sub(r'<cp:lastModifiedBy>.*?</cp:lastModifiedBy>', '<cp:lastModifiedBy>Julio Enrique Viche Castillo</cp:lastModifiedBy>', core_xml)
                core_xml = re.sub(r'<dc:title>.*?</dc:title>', '<dc:title>Especificación de Casos de Uso Detallados - Conversor de Unidades (SINBDD)</dc:title>', core_xml)
                data = core_xml.encode("utf-8")
                
            zout.writestr(item, data)

    shutil.move(str(temp_dst), str(DST_FILE))
    print(f"[OK] Nueva plantilla ECUD creada: {DST_FILE} ({DST_FILE.stat().st_size / 1024:.1f} KB)")

if __name__ == "__main__":
    build()
