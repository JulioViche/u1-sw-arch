# Plan para corregir los manuales técnicos y presentaciones

## Objetivo

Revisar los cuatro manuales técnicos DOCX y sus cuatro presentaciones PPTX de los proyectos GR09 sin base de datos. Corregir capturas que no correspondan a la aplicación propia, completar evidencias faltantes y conservar el código como texto editable.

## Alcance

| Variante | Manual DOCX | Presentación PPTX |
| --- | --- | --- |
| SOAP + Java (TI.1.1) | `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_SOAP_JAVA_GR09.docx` | `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_SOAP_JAVA_GR09.pptx` |
| SOAP + .NET (TI.1.2) | `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_SOAP_DOTNET_GR09.docx` | `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_SOAP_DOTNET_GR09.pptx` |
| REST + Java (TI.1.3) | `TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_RESTFUL_JAVA_GR09.docx` | `TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_RESTFUL_JAVA_GR09.pptx` |
| REST + .NET (TI.1.4) | `TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_RESTFUL_DOTNET_GR09.docx` | `TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_RESTFUL_DOTNET_GR09.pptx` |

## Pasos

1. Auditar las figuras, tablas, rótulos y marcadores pendientes en los ocho archivos. Identificar capturas ajenas, repetidas, borrosas o que no prueben el flujo descrito.
2. Contrastar cada imagen con el código de su propia variante. No reutilizar una captura SOAP para REST ni una captura Java para .NET.
3. Ejecutar los clientes de consola, escritorio, web y móvil cuando el entorno disponible lo permita. Capturar inicio de sesión y conversiones representativas de cada cliente; registrar si faltan servidor, emulador o dispositivo.
4. Reemplazar capturas incorrectas en DOCX y PPTX. Mantener las imágenes vinculadas a la variante, con numeración, pie de figura y tamaño legibles.
5. Mantener todo fragmento de código como texto editable en Word y PowerPoint. No convertir clases, tablas de código ni comandos en imágenes. Corregir saltos de línea, ajuste, tamaño y paginación.
6. Confirmar en portadas y diapositivas los dos autores, en orden alfabético por apellidos: **Murillo Medina, Gabriel Omar; Viche Castillo, Julio Enrique**.
7. Renderizar cada DOCX y PPTX. Revisar visualmente todas las páginas y diapositivas, verificar que no haya recortes, solapamientos, rótulos separados ni marcadores de captura pendiente.
8. Comprobar que los documentos abran en Word y PowerPoint, que las capturas sean propias de cada cliente y que los enlaces de las versiones Markdown apunten a archivos existentes.
9. Mantener el alcance SINBDD: no agregar capturas ni contenido de variantes con base de datos. Preservar los demás archivos y cambios del repositorio.

## Criterios de aceptación

- Los cuatro DOCX y los cuatro PPTX corresponden a sus propias aplicaciones y tecnologías.
- Las capturas prueban los clientes de consola, escritorio, web y móvil; no hay evidencia ajena a la variante.
- El código de los informes y las presentaciones permanece seleccionable, copiable y editable como texto.
- Los ocho artefactos incluyen ambos autores en el orden indicado.
- No quedan marcadores pendientes ni figuras con rótulos incorrectos.
- Los ocho archivos abren y sus renders se revisan antes de cerrar la tarea.

## Estado

- [x] TI.1.1 SOAP + Java: manual y presentación revisados; código editable y capturas corregidas.
- [ ] TI.1.2 SOAP + .NET: avance parcial; quedan capturas, pies y explicaciones que no coinciden. Continuar desde [CONTINUAR_DOCUMENTACION_TI12.md](CONTINUAR_DOCUMENTACION_TI12.md).
- [x] TI.1.3 REST + Java: manual y presentación revisados; capturas de ejecución incrustadas sin pendientes.
- [x] TI.1.4 REST + .NET: manual y presentación revisados; capturas de ejecución incrustadas sin pendientes.
- [ ] Revisión final cruzada de los cuatro pares DOCX/PPTX pendiente hasta resolver TI.1.2 y comprobar cada variante por separado.

Los estados de TI.1.1, TI.1.3 y TI.1.4 se conservan del registro anterior; no representan una nueva auditoría completa realizada al guardar el contexto del 30 de octubre de 2026.
