---
tipo: plantilla
tags: [plantilla, manual-tecnico, arquitectura, documentacion]
---

# Plantilla Maestra: Manual Técnico del Sistema

Esta plantilla define la estructura formal para compilar los manuales de ingeniería de software `CONVUNI_<PROTOCOLO>_<PLATAFORMA>_<GRUPO>.docx`.

---

## 1. Carátula y Control de Versiones

```
UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE
DEPARTAMENTO DE ENERGÍA Y MECÁNICA / CARRERA DE TI
ARQUITECTURA DE SOFTWARE · TAREA {{CODIGO_TAREA}}
PERIODO ACADÉMICO: PAO 2026-51
NRC: {{NRC}} | GRUPO: {{GRUPO}}
AUTORES: {{INTEGRANTES}}
DOCENTE TUTOR: Ing. Eduardo Mauricio Campaña Ortega
FECHA: {{FECHA}}
```

---

## 2. Índice General Requerido

1. **Introducción y Objetivos del Proyecto**
   - 1.1. Contexto académico y objetivos del conversor multi-magnitud.
   - 1.2. Justificación de la arquitectura orientada a servicios (SOA).
2. **Definición del Caso de Estudio**
   - 2.1. Alcance funcional (Autenticación y 3 magnitudes físicas).
   - 2.2. Restricción de Persistencia: Arquitectura sin base de datos (`SINBDD`).
   - 2.3. Esquema de seguridad quemada: credenciales `MONSTER` / `MONSTER9` y ciclo de vida del token.
3. **Arquitectura del Sistema y Contrato de Servicios**
   - 3.1. Estilo de servicio web: {{PROTOCOLO}} (Definición de WSDL XML o especificación OpenAPI REST JSON).
   - 3.2. Plataforma de ejecución del backend: {{PLATAFORMA}} (Payara Server / ASP.NET Core Kestrel).
   - 3.3. Topología de red: Configuración de endpoints para Localhost, Emulador (`10.0.2.2`) y Red Física/Hotspot (`192.168.137.1`).
4. **Diseño e Implementación de Clientes Multi-Canal**
   - 4.1. **Cliente de Consola (`CLICON`):** Arquitectura CLI, bucle interactivo y manejo de excepciones.
   - 4.2. **Cliente de Escritorio (`CLIESC`):** Interfaz gráfica (Swing en Java / WPF en .NET), diseño adaptativo con ScrollViewer y validación en tiempo real.
   - 4.3. **Cliente Web (`CLIWEB`):** Arquitectura cliente-servidor (JSP/Servlets en Java / Razor-HTML en .NET) con diseño responsive.
   - 4.4. **Cliente Móvil (`CLIMOV`):** Aplicación nativa Android (Java/Kotlin), lógica de autodetección de IP y consumo asíncrono con timeout de 3 segundos.
5. **Reglas de Negocio y Algoritmos de Conversión**
   - 5.1. Catálogo estricto de 5 unidades por magnitud (ni una más, ni una menos).
   - 5.2. Tablas de factores de conversión y fórmulas de pivote.
   - 5.3. Formateo estricto a 2 decimales (`%.2f`) y validación de entrada (16 caracteres, `^-?\d+(\.\d+)?$`).
6. **Guía de Puesta en Marcha y Despliegue**
   - 6.1. Requisitos de entorno (JDK, .NET SDK, Android SDK, Payara).
   - 6.2. Compilación y arranque del servidor.
   - 6.3. Ejecución de los clientes en paralelo.
7. **Pruebas y Verificación de Resultados**
   - 7.1. Casos de prueba de autenticación (éxito, usuario incorrecto, clave incorrecta).
   - 7.2. Casos de prueba de conversión y valores extremos.
8. **Conclusiones y Recomendaciones Técnicas**
