# Continuación de la corrección de capturas TI1.2

Fecha de corte: 30 de octubre de 2026. **Trabajo pendiente; no declarar corregido el par DOCX/PPTX de TI1.2.**

El usuario pidió guardar este estado para continuar y publicar los avances en `main` de GitHub. La corrección se interrumpió antes de sustituir los entregables oficiales por las copias de prueba.

## Petición que debe completarse

Corregir las imágenes del manual técnico y la presentación SOAP .NET: cada captura debe pertenecer al cliente implementado y corresponder al texto de su página o diapositiva. Deben verse grandes, legibles, sin deformación ni caracteres dañados. Conservar las capturas propias que ya sirven. El usuario señaló especialmente la **diapositiva 17 del PPTX**, no la página 17 del DOCX.

Leer `AGENTS.md` y `AGENTS_MANUALES_TECNICOS.md`. Los fragmentos de código deben quedar editables; las tarjetas PNG de código creadas durante las pruebas no cumplen por sí solas ese criterio.

## Entregables guardados en el repositorio

| Variante | Archivos | Estado al cortar |
| --- | --- | --- |
| TI1.1 SOAP Java | `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_SOAP_JAVA_GR09.docx` y `.pptx` | Cambios locales previos conservados. El usuario los calificó como relativamente bien; no se realizó otra auditoría visual completa durante el cierre. |
| TI1.2 SOAP .NET | `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/CONVUNI_SOAP_DOTNET_GR09.docx` y `.pptx` | Avance parcial. Persisten desajustes entre imagen, pie y explicación. Las copias experimentales descritas abajo no se copiaron sobre estos archivos. |

También estaba modificado `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/04. SERVIDOR/src/main/webapp/WEB-INF/payara-web.xml`: se añadió la declaración DTD de Payara y `error-url=""`. Es un cambio previo guardado junto al trabajo. No se recompiló ni se probó de nuevo al cerrar.

Los cuatro archivos oficiales se abrieron correctamente mediante Word y PowerPoint antes de publicar. Las dos presentaciones tienen 30 diapositivas. Abrir el archivo no demuestra que sus imágenes y textos estén corregidos.

## Cambio importante de la fuente de código

La sesión trabajaba en `docs/gr09-evidencias-20261029`, con HEAD `6e7912a947dcfd3c0a7a530074c994baf993460e`. Al preparar la publicación se obtuvo `origin/main` en `1e70fb81d53c6407c8c1d7cd041b313e9f27c245`, tres commits por delante.

Esos commits cambian el cliente móvil .NET. **Releer el código de `main` antes de reutilizar capturas o afirmaciones de esta sesión.** La versión de `main` incluye:

- `04. CLIMOV/ClienteMovilGR09/ClienteMovilGR09.csproj`
- `04. CLIMOV/ClienteMovilGR09/Vistas/PaginaPrincipal.xaml` y `.xaml.cs`
- `04. CLIMOV/ClienteMovilGR09/Controladores/ControladorConversor.cs`
- `04. CLIMOV/ClienteMovilGR09/Servicios/ClienteSoap.cs`

Las referencias anteriores a `ClienteMovilMaui/MainPage.xaml`, `MainPage.xaml.cs` y `Services/SoapClient.cs` describen la rama anterior. Las capturas móviles de esa versión deben cotejarse o repetirse con la interfaz actual. La integración debe conservar los cambios de código y APK de `main`.

## Hallazgos que motivaron la corrección

- El PPTX oficial contiene capturas de plantillas o proyectos ajenos en secciones de servidor, consola, escritorio, web y móvil.
- Las diapositivas 19 y 20 conservan referencias a JSP/Servlet que no corresponden al cliente ASP.NET Core MVC/Razor. Revisar también URL y archivo de acceso de las diapositivas 21 y 27.
- En el DOCX hay explicaciones antiguas de WCF, .NET 6, API REST, Windows Forms, `DashboardVista`, partidos y `DataGridView`. Algunas figuras muestran código propio mientras el pie todavía dice «crear proyecto» o «crear formulario».
- Hay imágenes repetidas con pies distintos. La vista de autenticación y la de conversión no pueden intercambiarse.
- Los ejemplos `LoginPage.xaml` y `SeleccionPrograma.xaml` de la documentación anterior no deben darse por existentes. Comprobar los nombres reales tras integrar `main`.

## Copias y evidencias locales

Todo lo siguiente está en `D:\temp\ti12-evidencia`; **no forma parte de la publicación de GitHub**. Si no existe al retomar, volver a obtener las capturas desde los proyectos.

| Recurso local | Uso y límite |
| --- | --- |
| `CONVUNI_SOAP_DOTNET_GR09.docx.original` y `.pptx.original` | Copias anteriores a los cambios de esta sesión. No restaurarlas sin comparar los avances. |
| `before-image-readability.docx`, `before-image-readability.pptx`, `CONVUNI_SOAP_DOTNET_GR09.pptx.crop` | Referencias para recuperar estructura o capturas anteriores. Algunas contienen imágenes ajenas. |
| `ppt/CONVUNI_SOAP_DOTNET_GR09-candidate.pptx` | Última prueba de PPTX. Abre y fue exportada a `ppt/candidate-v3.pdf`; contacto visual en `ppt/candidate-v3-contact.jpg`. Tiene imágenes de ejecución propias y textos corregidos, pero usa demasiadas tarjetas PNG de código. Revisar legibilidad y conservar código editable antes de adoptarla. |
| `doc/CONVUNI_SOAP_DOTNET_GR09-candidate.docx` y `-candidate-media.docx` | Word los rechazó tras guardar mediante la herramienta DOCX. No utilizarlos como entregables. |
| `doc/CONVUNI_SOAP_DOTNET_GR09-final-candidate.docx` | Se reparó el paquete y abre en Word, pero el render mostró textos nuevos en posiciones equivocadas y narrativa vieja conservada. **Descartarlo como base de contenido.** |
| `doc/candidate-final.pdf`, `doc/candidate-pages-contact.jpg`, `doc/f-*.png` | Evidencia de esos errores; no son prueba de terminación. |
| `cards/` | Tarjetas de fragmentos de código y salida de consola de la rama anterior. Sirven para localizar contenido, no para reemplazar indiscriminadamente capturas de ejecución. |

Capturas de escritorio y web tomadas durante la sesión:

- `desktop-login.png`, `desktop-result.png`, `desktop-printwindow.png`.
- `web-login-recorte.png`, `web-conversor-recorte.png`, `web-resultado-recorte.png`.
- Las capturas móviles versionadas están en `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/05. DOCUMENTACION/capturas/`; cotejarlas con el nuevo cliente de `main`.

Huellas para distinguir las copias al retomar:

| Archivo | Bytes | SHA-256 |
| --- | ---: | --- |
| DOCX oficial TI1.2 guardado | 9617584 | `7413ABCE1E0ACFFBCBCC9C113604CD20E2F660AE6D82E9760C55EF7FFC9DF919` |
| PPTX oficial TI1.2 guardado | 4288852 | `3A3CC0E77EC1B1BBE3814E21C9DCFF2A2819D86DE37C0C2560224FC96F4DF088` |
| PPTX experimental `candidate.pptx` | 3095818 | `6146FD6CE1FACD773C7D7BAF596A1A58025E29296530D7BA3EBB34E9F4C405CA` |

## Problemas de edición que no deben repetirse

1. La tanda de 151 llamadas `update_paragraph_text` devolvió éxito, pero el render conservó párrafos antiguos y colocó reemplazos junto a textos incorrectos. No confiar en esos índices ni volver a ejecutar esa tanda. El intento posterior de reconstrucción mediante Word COM no llegó a ejecutarse: el código JavaScript falló antes de lanzar herramientas.
2. El guardado DOCX experimental dejó prefijos XML sin declarar (`ns6`, `ns12` y `pic`). Reparar namespaces hizo que Word abriera el archivo, pero no corrigió los desajustes semánticos.
3. En PowerPoint COM las coordenadas y tamaños usan **puntos**, no pulgadas: multiplicar por 72 cuando las medidas se expresen en pulgadas.
4. Pasar texto con tildes por una tubería a Windows PowerShell dañó una sustitución. Se corrigió enviando JSON codificado como UTF-8/base64. Comprobar las tildes tanto en XML como en el render.
5. No volver a ejecutar a ciegas `patch_docx.py`, `patch_pptx.py` ni otros scripts de prueba de esa carpeta: varias pruebas dañaron o sustituyeron capturas útiles.

## Orden recomendado para continuar

1. Revisar `git status`, HEAD y el código móvil actual de `main`; no usar la rama anterior como fuente de verdad.
2. Partir del DOCX oficial que abre correctamente. Corregir cada explicación y pie al lado de su figura. Para sustituir texto, usar búsquedas de párrafos únicos y comprobar inmediatamente el resultado en Word; conservar estilos y tablas de código editables.
3. Auditar las 30 diapositivas del PPTX oficial y usar la copia experimental solo como referencia. Reemplazar cada captura ajena por una captura de la pantalla descrita. Conservar login, formulario y resultado reales del cliente correspondiente.
4. Ejecutar o volver a capturar los clientes cuyo aspecto cambió en `main`. Servidor primero; comprobar el contrato y las direcciones en los archivos actuales.
5. Renderizar todas las páginas y diapositivas. Revisar imagen, pie y explicación juntos, tamaño, recortes, solapamientos y tildes. Abrir de nuevo los dos archivos en Word y PowerPoint.
6. Actualizar el estado de `AGENTS_MANUALES_TECNICOS.md` solo cuando TI1.2 cumpla esos criterios. No extender esa conclusión a TI1.3 o TI1.4 sin auditarlos por separado.

No se completó la auditoría visual final ni se ejecutó una nueva compilación general al guardar este contexto. `image.png` en la raíz es la imagen de referencia adjunta por el usuario y queda fuera del commit.
