#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
validate_deliverables.py - Validador de estructura y contenido de entregables.
Repositorio: u1-arch (Arquitectura de Software - ESPE)
"""

import os
import sys
import zipfile
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent.parent

VARIANTS = [
    "TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09",
    "TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09",
    "TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09",
    "TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09"
]

REQUIRED_SUBDIRS = [
    "01. UML/01. ERS",
    "01. UML/02. ECUD",
    "01. UML/03. DIAGRAMAS UML",
    "02. MER",
    "03. BDD",
    "04. CLICON",
    "04. CLIESC",
    "04. CLIMOV",
    "04. CLIWEB",
    "04. SERVIDOR",
    "05. DOCUMENTACION"
]

def check_file_text_in_zip(file_path, search_terms):
    """Verifica si los términos de búsqueda están presentes dentro de los archivos XML del paquete ZIP/Office."""
    missing = list(search_terms)
    try:
        with zipfile.ZipFile(file_path, 'r') as z:
            for name in z.namelist():
                if name.endswith('.xml'):
                    try:
                        content = z.read(name).decode('utf-8', errors='ignore')
                        missing = [t for t in missing if t.lower() not in content.lower()]
                        if not missing:
                            break
                    except Exception:
                        pass
    except Exception as e:
        return False, f"Error al abrir archivo: {e}"

    if missing:
        return False, f"Faltan términos: {missing}"
    return True, "OK"

def validate_variant(variant_name):
    print(f"\n--- Validando {variant_name} ---")
    variant_path = ROOT_DIR / variant_name
    errors = 0

    if not variant_path.exists():
        print(f"  [ERROR] No existe la carpeta del proyecto: {variant_name}")
        return 1

    # 1. Validar subcarpetas requeridas
    for sub in REQUIRED_SUBDIRS:
        sub_path = variant_path / sub
        if not sub_path.exists():
            print(f"  [ERROR] Subcarpeta faltante: {sub}")
            errors += 1
        else:
            # En 02. MER y 03. BDD verificar README.md explicativo (SINBDD)
            if sub in ["02. MER", "03. BDD"]:
                readme = sub_path / "README.md"
                if not readme.exists():
                    print(f"  [WARN] Falta README.md justificativo en: {sub}")

    # 2. Validar documentos de entrega
    doc_checks = [
        ("01. UML/01. ERS", "*.docx", ["Murillo Medina", "Viche Castillo"]),
        ("01. UML/02. ECUD", "*.docx", ["Murillo Medina", "Viche Castillo"]),
        ("05. DOCUMENTACION", "*.docx", ["Murillo Medina", "Viche Castillo"]),
        ("05. DOCUMENTACION", "*.pptx", ["MURILLO MEDINA", "VICHE CASTILLO"])
    ]

    for folder_sub, pattern, terms in doc_checks:
        folder = variant_path / folder_sub
        files = [f for f in folder.glob(pattern) if not f.name.startswith("~$")]
        if not files:
            print(f"  [ERROR] No se encontró {pattern} en {folder_sub}")
            errors += 1
            continue
        for f in files:
            size_kb = f.stat().st_size / 1024
            if size_kb < 1:
                print(f"  [ERROR] Archivo sospechosamente pequeño ({size_kb:.1f} KB): {f.name}")
                errors += 1
            ok, msg = check_file_text_in_zip(f, terms)
            if not ok:
                print(f"  [ERROR] Validación fallida en {f.name}: {msg}")
                errors += 1
            else:
                print(f"  [PASS] {f.name} ({size_kb:.1f} KB) - Autores validados")

    return errors

def main():
    print("=" * 60)
    print("Validador de Entregables TI.1 (SOAP / RESTful - Java / .NET)")
    print("=" * 60)

    total_errors = 0
    for v in VARIANTS:
        total_errors += validate_variant(v)

    print("\n" + "=" * 60)
    if total_errors == 0:
        print("RESULTADO: TODOS LOS ENTREGABLES VALIDARON CON EXITO (0 ERRORES)")
        print("=" * 60)
        sys.exit(0)
    else:
        print(f"RESULTADO: SE ENCONTRARON {total_errors} ADVERTENCIAS/ERRORES")
        print("=" * 60)
        sys.exit(1)

if __name__ == "__main__":
    main()
