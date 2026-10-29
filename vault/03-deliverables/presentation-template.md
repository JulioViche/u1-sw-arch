---
tipo: plantilla
tags: [plantilla, presentacion, diapositivas, defensa, pptx]
---

# Plantilla Maestra: Presentación Ejecutiva y Defensa Técnica

Esta plantilla define el guion y contenido de las 12 diapositivas maestras del archivo `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.pptx`.

---

## Estructura de las Diapositivas

### Diapositiva 1: Portada Institucional
* **Título:** CONVERSOR DE UNIDADES {{PROTOCOLO}} {{PLATAFORMA}}
* **Subtítulo:** Arquitectura de Software · Tarea {{CODIGO_TAREA}} (PAO 2026-51)
* **Datos Académicos:**
  * **NRC:** {{NRC}} | **Grupo:** {{GRUPO}}
  * **Integrantes:** {{INTEGRANTES}}
  * **Docente Tutor:** Ing. Eduardo Mauricio Campaña Ortega

### Diapositiva 2: Planteamiento del Problema y Objetivos
* Necesidad de interoperabilidad entre plataformas heterogéneas.
* Implementación de una arquitectura orientada a servicios sin persistencia en base de datos (`SINBDD`).
* Objetivo: Demostrar el desacoplamiento entre servidor centralizado y 4 clientes consumidores independientes.

### Diapositiva 3: Arquitectura General de la Solución
* Diagrama de bloques de comunicación:
  * Backend Centralizado en puerto {{PUERTO}} ({{PLATAFORMA}}).
  * Protocolo de transporte: {{PROTOCOLO}} (SOAP XML Envelope / REST JSON).
  * Consumidores: Consola (`CLICON`), Escritorio (`CLIESC`), Web (`CLIWEB`), Móvil (`CLIMOV`).

### Diapositiva 4: Seguridad y Control de Acceso (Autenticación Quemada)
* Requisito obligatorio: Ningún cliente realiza conversiones sin autenticación previa.
* Credenciales maestras fijas: Usuario `MONSTER` / Clave `MONSTER9`.
* Mecanismo de sesión: Token criptográfico en memoria validado en cada petición.

### Diapositiva 5: Catálogo Estricto y Reglas de Negocio
* Catálogo cerrado de 5 unidades por magnitud ("ni una más, ni una menos"):
  * **Longitud:** Metro, Centímetro, Pie, Yarda, Milla.
  * **Masa:** Gramo, Kilogramo, Libra, Onza, Quintal.
  * **Temperatura:** Celsius, Fahrenheit, Kelvin, Newton, Réaumur.
* Restricción numérica: Entrada de máximo 16 caracteres, expresión regular `^-?\d+(\.\d+)?$`.
* Salida: Formateo obligatorio a 2 decimales (`%.2f`) con punto decimal (`.`).

### Diapositiva 6: Diseño de Servicios Web en el Servidor
* Endpoints expuestos y su contrato de interfaz.
* Manejo de concurrencia y validación de tokens.
* Tiempos de respuesta y diseño sin estado (*Stateless*).

### Diapositiva 7: Cliente de Consola (`CLICON`) y Escritorio (`CLIESC`)
* **CLICON:** Interfaz en terminal con menú guiado y manejo de reintentos.
* **CLIESC:** Ventana gráfica interactiva con contenedor adaptable (`ScrollViewer`), retroalimentación visual de errores y selector de unidades.

### Diapositiva 8: Cliente Web (`CLIWEB`)
* Despliegue en navegador (puerto local).
* Formulario responsivo con validaciones JavaScript del lado cliente y control de sesión HTTP.

### Diapositiva 9: Cliente Móvil Android (`CLIMOV`)
* Arquitectura Android (Java nativo).
* **Conectividad híbrida inteligente:** Auto-detección de IP para Emulador Android Studio (`10.0.2.2`) y dispositivos físicos vía Hotspot Wi-Fi (`192.168.137.1`).
* Llamadas asíncronas en hilo secundario con timeout ágil (3 segundos).

### Diapositiva 10: Pruebas y Verificación de Resultados
* Casos de prueba de autenticación exitosa vs rechazo de credenciales.
* Pruebas de conversión con cifras de 16 caracteres y verificación de precisión a 2 decimales.
* Comportamiento ante desconexión de red o caída del servicio.

### Diapositiva 11: Conclusiones
* Cumplimiento total de los requerimientos funcionales y no funcionales SINBDD.
* Demostración de interoperabilidad real consumiendo el servicio web desde 4 entornos radicalmente distintos.
* Robustez en el diseño de red adaptativo en el cliente móvil.

### Diapositiva 12: Preguntas y Cierre
* Espacio para consultas del jurado evaluador.
* Repositorio de código fuente y documentación técnica.
