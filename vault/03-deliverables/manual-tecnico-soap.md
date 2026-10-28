---
tipo: manual-tecnico
protocolo: SOAP
plataforma: Java
taller: TI.1.1
tags: [manual-tecnico, java, sinbdd, entrega]
---

# Manual Técnico - Conversor de Unidades con SOAP en Java (SINBDD)

> [!NOTE]
> Documento extraído del archivo oficial `MANUAL_SOAP_JAVA_SINBDD_GR09.docx`.
> Tarea académica: **Arquitectura de Software** (PAO 2026-51, NRC 36884, Grupo GR09).
> Autor: **Julio Enrique Viche Castillo** | Tutor: **Ing. Eduardo Mauricio Campaña Ortega**

MANUAL TÉCNICO SOAP JAVA SINBDD

## PARTE PRÁCTICA

### INTRODUCCIÓN

Un Web Service SOAP es una interfaz de comunicación basada en protocolos estandarizados que permite el intercambio de información estructurada entre sistemas computacionales. A diferencia de otros enfoques, SOAP se fundamenta estrictamente en el uso de XML como lenguaje para el envío de mensajes y depende de un contrato formal conocido como WSDL (Web Services Description Language), que define las operaciones, parámetros y tipos de datos disponibles. Esta arquitectura es esencial en entornos empresariales donde la seguridad, la transaccionalidad y el cumplimiento estricto de protocolos son prioritarios.

En el desarrollo de servicios SOAP con Java, la lógica suele organizarse mediante la exposición de Endpoints (puntos de acceso). A diferencia del patrón MVC tradicional de aplicaciones web, SOAP se centra en la Orientación a Servicios (SOA), donde el servidor actúa como un proveedor de funciones específicas que el cliente consume tras conocer la definición del servicio. El intercambio de datos en la red se realiza a través del protocolo HTTP.

El servidor SOAP se ejecuta como una aplicación Java independiente. Publisher.java publica los endpoints ServicioAutenticacion y ServicioConversion con Metro/JAX-WS en el puerto 8080; se inicia desde la carpeta 04. SERVIDOR con mvn exec:java y no necesita un servidor de aplicaciones externo.

### OBJETIVOS

#### OBJETIVO GENERAL

Documentar la arquitectura, configuración y uso de un conversor de unidades con autenticación previa, clientes Java de consola, escritorio, web y móvil, y un servicio SOAP implementado con Metro/JAX-WS. El sistema convierte longitud, masa y temperatura y opera sin base de datos (SINBDD).

#### OBJETIVOS ESPECÍFICOS

Describir los endpoints ServicioAutenticacion y ServicioConversion, sus operaciones JAX-WS y la publicación del WSDL en las rutas ?wsdl de los endpoints.

Explicar el consumo SOAP en los clientes Java de consola, escritorio, web y móvil, incluidos el inicio de sesión, el token de sesión y las solicitudes de conversión.

Resumir cómo SOAP transporta solicitudes y respuestas XML por HTTP y cómo Metro/JAX-WS publica las operaciones definidas por los endpoints.

### INSTALACIÓN Y CONFIGURACIÓN DE HERRAMIENTAS

Para dar inicio al desarrollo de la práctica, primero se deben instalar y configurar las herramientas esenciales. Para este proyectó se utilizó Windows 11.

#### INSTALACIÓN Y CONFIGURACIÓN DE APACHE NETBEANS 24

El proyecto SOAP requiere JDK 11 o superior y Maven. Verifica Java con java --version y Maven con mvn --version desde PowerShell o la terminal integrada de NetBeans.

| Java --version |
| --- |

> [!TIP] Captura de Pantalla
> **Versión de Java JDK 11 o superior en el símbolo del sistema**

##### Figura 3. Versión de Java JDK 11 o superior en el símbolo del sistema

Para la instalación de la herramienta se accede a la página oficial de descarga de Apache https://netbeans.apache.org/front/main/download/ se selecciona la opción ‘Older Releases’, ‘Apache NetBeans 24’ y la opción para el sistema operativo.

> [!TIP] Captura de Pantalla
> **Página oficial de descarga de Apache NetBeans**

*_Figura 4. Página oficial de descarga de Apache NetBeans_*

Una vez seleccionada la opción para el sistema operativo, se inicia la descarga y se obtendrá el siguiente ejecutable.

> [!TIP] Captura de Pantalla
> **Instalador Apache NetBeans 24**

##### Figura 5. Instalador Apache NetBeans 24

Al ejecutar el instalador se muestra la siguiente pantalla, en el proceso de instalación se aceptan los términos y condiciones, se selecciona la ubicación de la descarga y se finaliza la instalación.

> [!TIP] Captura de Pantalla
> **Pantalla inicial de instalación NetBeans 24**

##### Figura 6. Pantalla inicial de instalación NetBeans 24

Una vez finalizada la descarga se ejecuta la aplicación y se iniciará Apache NetBeans 24.

> [!TIP] Captura de Pantalla
> **Aplicación Apache NetBeans 24**

##### Figura 7. Aplicación Apache NetBeans 24

#### INSTALACIÓN Y CONFIGURACIÓN DE ANDROID STUDIO

Para la instalación de la herramienta se accede a la página oficial de descarga de Android Studio https://developer.android.com/studio se selecciona la opción de descarga, se aceptan términos y condiciones e iniciará la descarga.

> [!TIP] Captura de Pantalla
> **Página oficial de descarga de Android Studio**

##### Figura 8. Página oficial de descarga de Android Studio

Al finalizar la descarga se obtendrá el instalador ejecutable.

> [!TIP] Captura de Pantalla
> **Ejecutable Android Studio**

##### Figura 9. Ejecutable Android Studio

Al ejecutar el instalador se mostrará la siguiente pantalla:

> [!TIP] Captura de Pantalla
> **Pantalla inicial de instalación de Android Studio**

##### Figura 10. Pantalla inicial de instalación de Android Studio

Durante la instalación se selecciona los componentes a instalar, ubicación y carpeta de inicio. Al finalizar la instalación se iniciará la configuración en donde se selecciona el Setup Standard se aceptan términos y condiciones y se inicia la descarga de componentes.

> [!TIP] Captura de Pantalla
> **Aplicación Android Studio**

##### Figura 11. Aplicación Android Studio

### ESTRUCTURA DE PROYECTO

Para la elaboración del proyecto, en primer lugar, es necesario crear una carpeta en la cual se alojarán los archivos necesarios para su funcionamiento.

> [!TIP] Captura de Pantalla
> **Creación de la carpeta raíz**

##### Figura 1. Creación de la carpeta raíz

Dentro de la carpeta principal del proyecto, se debe ubicar la siguiente subestructura.

> [!TIP] Captura de Pantalla
> **Estructura inicial del proyecto**

##### Figura 2. Estructura inicial del proyecto

Todo esto con la finalidad de llevar buenas prácticas y mantener organizado el proyecto.

## APLICACIONES SERVIDOR

### CREACIÓN DEL PROYECTO SERVIDOR

El servidor es un proyecto Java con Maven. En NetBeans abre la carpeta 04. SERVIDOR, revisa pom.xml y ejecuta la clase ec.edu.monster.publisher.Publisher mediante mvn exec:java; Maven descarga Jakarta XML Web Services y Metro/JAX-WS según el pom.xml.

> [!TIP] Captura de Pantalla
> **Creación del proyecto – Servidor SOAP**

##### Figura 12. Creación del proyecto – Servidor SOAP

> [!TIP] Captura de Pantalla
> **Configuración del proyecto 1 - Servidor SOAP**

##### Figura 13. Configuración del proyecto 1 - Servidor SOAP

> [!TIP] Captura de Pantalla
> **Configuración del proyecto 2 - Servidor SOAP**

##### Figura 14. Configuración del proyecto 2 - Servidor SOAP

### CODIFICACIÓN DEL PROYECTO SERVIDOR

#### CREACIÓN DE PAQUETES

El servidor separa la publicación SOAP, los endpoints, los modelos de respuesta y la lógica de conversión. Los paquetes reales son ec.edu.monster.publisher, ec.edu.monster.ws, ec.edu.monster.model y ec.edu.monster.util; no hay acceso a una base de datos.

> [!TIP] Captura de Pantalla
> **Estructura de paquetes - Servidor SOAP**

##### Figura 15. Estructura de paquetes - Servidor SOAP

#### CREACIÓN DE WEBSERVICES

AuthWebService expone el inicio de sesión, la validación y el cierre de sesión. ConversionWebService expone las operaciones de conversión para longitud, masa y temperatura; Metro genera el WSDL al publicar cada endpoint.

Los endpoints usan @WebService, @WebMethod y @WebParam. AuthWebService valida MONSTER / MONSTER9 y administra tokens en memoria; ConversionWebService verifica el token antes de delegar los cálculos en ConversionUtil.

> [!TIP] Captura de Pantalla
> **Estructura de paquetes - WebServices**

##### Figura 16. Estructura de paquetes - WebServices

**Tabla 1. Método iniciarSesion - AuthWebService.java**

public LoginResponse iniciarSesion(

@WebParam(name = "usuario") String usuario,

@WebParam(name = "clave") String clave) {

if (USUARIO_REQUERIDO.equals(usuario) && CLAVE_REQUERIDA.equals(clave)) {

String nuevoToken = UUID.randomUUID().toString();

TOKENS_ACTIVOS.add(nuevoToken);

return new LoginResponse(true, nuevoToken, "Inicio de sesión exitoso. Bienvenido al Conversor de Unidades.");

} else {

return new LoginResponse(false, null, "Error de autenticación: Credenciales inválidas.");

}

}

**Tabla 2. Método convertirLongitud - ConversionWebService.java**

public ConversionResponse convertirLongitud(

@WebParam(name = "token") String token,

@WebParam(name = "valor") double valor,

@WebParam(name = "unidadOrigen") String unidadOrigen,

@WebParam(name = "unidadDestino") String unidadDestino) {

if (!AuthWebService.isTokenValido(token)) {

return new ConversionResponse(false, 0.0, ERROR_TOKEN_INVALIDO);

}

try {

double resultado = ConversionUtil.convertirLongitud(valor, unidadOrigen, unidadDestino);

return new ConversionResponse(true, resultado, null);

} catch (IllegalArgumentException e) {

return new ConversionResponse(false, 0.0, "Error en unidades de longitud: " + e.getMessage());

} catch (Exception e) {

return new ConversionResponse(false, 0.0, "Error interno al calcular longitud: " + e.getMessage());

}

}

**Tabla 3. Método convertirLongitud - ConversionUtil.java**

public static double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) {

String uOrigen = normalizar(unidadOrigen);

String uDestino = normalizar(unidadDestino);

if (!FACTORES_LONGITUD.containsKey(uOrigen)) {

throw new IllegalArgumentException("Unidad de origen no reconocida para longitud: " + unidadOrigen +

". Las unidades permitidas son: metros, centimetros, pies, yardas, millas.");

}

if (!FACTORES_LONGITUD.containsKey(uDestino)) {

throw new IllegalArgumentException("Unidad de destino no reconocida para longitud: " + unidadDestino +

". Las unidades permitidas son: metros, centimetros, pies, yardas, millas.");

}

double factorOrigen = FACTORES_LONGITUD.get(uOrigen);

double factorDestino = FACTORES_LONGITUD.get(uDestino);

// Convertir origen a metros y luego metros a destino

double valorEnMetros = valor * factorOrigen;

return valorEnMetros / factorDestino;

}

**Tabla 4. Método main - Publisher.java**

public static void main(String[] args) {

System.out.println("==========================================================");

System.out.println(" INICIANDO SERVIDOR SOAP - CONVERSOR DE UNIDADES (SINBDD)");

System.out.println(" Asignatura: Arquitectura de Software | Tarea: TI.1.1");

System.out.println(" Grupo: GR09 | Autor: Julio Enrique Viche Castillo");

System.out.println("==========================================================");

try {

// Publicar Servicio de Autenticación

System.out.println("\n[1/2] Publicando Servicio de Autenticacion...");

Endpoint.publish(URL_AUTH, new AuthWebService());

System.out.println("-> Endpoint activo en: " + URL_AUTH);

System.out.println("-> WSDL disponible en: " + URL_AUTH + "?wsdl");

// Publicar Servicio de Conversión

System.out.println("\n[2/2] Publicando Servicio de Conversion...");

Endpoint.publish(URL_CONV, new ConversionWebService());

System.out.println("-> Endpoint activo en: " + URL_CONV);

System.out.println("-> WSDL disponible en: " + URL_CONV + "?wsdl");

System.out.println("\n[OK] Servidor SOAP listo y a la escucha de peticiones en el puerto 8080.");

System.out.println("Presione Ctrl + C para detener el servicio.\n");

} catch (Exception e) {

System.err.println("\n[ERROR] No se pudo publicar el servicio SOAP: " + e.getMessage());

e.printStackTrace();

}

}

#### CREACIÓN DE SERVICIO

En esta sección se implementa la "capa de servicio", que es el cerebro de la aplicación. Aquí se separa la lógica matemática y de seguridad del protocolo de comunicación. Al no contar con una base de datos, toda la gestión de datos se realiza en memoria, optimizando la velocidad de respuesta.

Servicio de Seguridad: Encargado de la validación de credenciales "hardcoded" y la generación/verificación de tokens de sesión para proteger los puntos de entrada del servidor.

Servicio de Conversiones: Contiene los algoritmos y factores de conversión para las magnitudes de longitud, masa y temperatura. Está diseñado para ser escalable, permitiendo añadir nuevas unidades sin afectar la estructura principal.

> [!TIP] Captura de Pantalla
> **Estructura de paquetes - servicios**

##### Figura 17. Estructura de paquetes - servicios

**Tabla 5. Método convertirLongitud - ConversionUtil.java**

| public static double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) { String uOrigen = normalizar(unidadOrigen); String uDestino = normalizar(unidadDestino); if (!FACTORES_LONGITUD.containsKey(uOrigen)) { throw new IllegalArgumentException("Unidad de origen no reconocida para longitud: " + unidadOrigen + ". Las unidades permitidas son: metros, centimetros, pies, yardas, millas."); } if (!FACTORES_LONGITUD.containsKey(uDestino)) { throw new IllegalArgumentException("Unidad de destino no reconocida para longitud: " + unidadDestino + ". Las unidades permitidas son: metros, centimetros, pies, yardas, millas."); } double factorOrigen = FACTORES_LONGITUD.get(uOrigen); double factorDestino = FACTORES_LONGITUD.get(uDestino); // Convertir origen a metros y luego metros a destino double valorEnMetros = valor * factorOrigen; return valorEnMetros / factorDestino; } |
| --- |

**Tabla 6. Método convertirMasa - ConversionUtil.java**

| public static double convertirMasa(double valor, String unidadOrigen, String unidadDestino) { String uOrigen = normalizar(unidadOrigen); String uDestino = normalizar(unidadDestino); if (!FACTORES_MASA.containsKey(uOrigen)) { throw new IllegalArgumentException("Unidad de origen no reconocida para masa: " + unidadOrigen + ". Las unidades permitidas son: gramos, kilogramos, onzas, libras, quintales."); } if (!FACTORES_MASA.containsKey(uDestino)) { throw new IllegalArgumentException("Unidad de destino no reconocida para masa: " + unidadDestino + ". Las unidades permitidas son: gramos, kilogramos, onzas, libras, quintales."); } double factorOrigen = FACTORES_MASA.get(uOrigen); double factorDestino = FACTORES_MASA.get(uDestino); // Convertir origen a kilogramos y luego a destino double valorEnKilos = valor * factorOrigen; return valorEnKilos / factorDestino; } |
| --- |

**Tabla 7. Método convertirTemperatura - ConversionUtil.java**

| public static double convertirTemperatura(double valor, String unidadOrigen, String unidadDestino) { String uOrigen = normalizar(unidadOrigen); String uDestino = normalizar(unidadDestino); // Paso 1: Convertir de origen a Celsius (base) double celsius; switch (uOrigen) { case "celsius": case "c": case "centigrados": celsius = valor; break; case "fahrenheit": case "f": celsius = (valor - 32.0) * 5.0 / 9.0; break; case "kelvin": case "k": celsius = valor - 273.15; break; case "newton": case "n": celsius = valor * 100.0 / 33.0; break; case "reaumur": case "re": celsius = valor * 5.0 / 4.0; break; default: throw new IllegalArgumentException("Escala de temperatura de origen no válida: " + unidadOrigen + ". Las escalas permitidas son: Celsius, Fahrenheit, Kelvin, Newton, Reaumur."); } // Paso 2: Convertir de Celsius a destino switch (uDestino) { case "celsius": case "c": case "centigrados": return celsius; case "fahrenheit": case "f": return (celsius * 9.0 / 5.0) + 32.0; case "kelvin": case "k": return celsius + 273.15; case "newton": case "n": return celsius * 33.0 / 100.0; case "reaumur": case "re": return celsius * 4.0 / 5.0; default: throw new IllegalArgumentException("Escala de temperatura de destino no válida: " + unidadDestino + ". Las escalas permitidas son: Celsius, Fahrenheit, Kelvin, Newton, Reaumur."); } } |
| --- |

### PRUEBAS DE LOS SERVICIOS

Para comprobar el servidor, inicia Publisher y abre http://localhost:8080/ServidorSOAP/ServicioAutenticacion?wsdl y http://localhost:8080/ServidorSOAP/ServicioConversion?wsdl. El WSDL permite revisar las operaciones publicadas; las conversiones requieren un token obtenido mediante el inicio de sesión.

El servidor publica dos endpoints SOAP: ServicioAutenticacion para iniciar, validar y cerrar sesión, y ServicioConversion para convertir longitud, masa y temperatura.

WSLogin: Punto de entrada para el módulo de seguridad. Recibe la petición del cliente y procesa la validación de credenciales para permitir o denegar el acceso a las funciones del servidor.

WSLongitud: Expone las operaciones matemáticas para las conversiones de medidas de longitud soportadas por el sistema (centímetros, metros, pies, yardas y millas).

WSMasa: Se encarga de recibir los parámetros y retornar las transformaciones entre las distintas unidades de masa (gramos, kilogramos, onzas, libras y quintales).

WSTemperatura: Maneja las peticiones referentes a las variaciones térmicas, permitiendo convertir entre Fahrenheit, Kelvin, Celsius, Newton y Réaumur.

Abre las URL ?wsdl en un navegador para comprobar que Metro publicó ambos endpoints. Después ejecuta un cliente Java, autentícate con MONSTER / MONSTER9 y prueba una conversión en cada magnitud.

> [!TIP] Captura de Pantalla
> **WSDL publicado por Metro/JAX-WS para consultar las operaciones de los endpoints SOAP.**

##### Figura 18. WSDL publicado por Metro/JAX-WS para consultar las operaciones de los endpoints SOAP.

> [!TIP] Captura de Pantalla
> **Prueba 1: Login.**

##### Figura 19. Prueba 1: Login.

> [!TIP] Captura de Pantalla
> **Prueba 2: Conversor de Longitud.**

##### Figura 20. Prueba 2: Conversor de Longitud.

> [!TIP] Captura de Pantalla
> **Prueba 3: Conversor de masa.**

##### Figura 21. Prueba 3: Conversor de masa.

> [!TIP] Captura de Pantalla
> **Prueba 4: Conversor de temperatura.**

##### Figura 22. Prueba 4: Conversor de temperatura.

## CODIFICACIÓN DE LOS CLIENTES

### CLIENTE CONSOLA

#### CREACIÓN DEL PROYECTO

El proyecto se crea usando NetBeans seleccionando ‘Nuevo Proyecto’, ‘Java with Maven’ y ‘Java Application’.

> [!TIP] Captura de Pantalla
> **Creación del proyecto - Cliente consola**

##### Figura 23. Creación del proyecto - Cliente consola

En este paso se define el nombre del proyecto y la ubicación.

> [!TIP] Captura de Pantalla
> **Configuración del proyecto - Cliente consola**

##### Figura 24. Configuración del proyecto - Cliente consola

#### ESTRUCTURA DEL PROYECTO

Una vez creado el proyecto se crea la estructura de paquetes basada en el patrón MVC

> [!TIP] Captura de Pantalla
> **Estructura del proyecto - Cliente consola**

##### Figura 25. Estructura del proyecto - Cliente consola

### CODIFICACIÓN DEL PROYECTO CLIENTE CONSOLA

#### CREACIÓN DE MODELOS Y VISTAS

Dado que no se requiere persistencia local ni mapeo de entidades complejas, el modelo se enfoca en la organización lógica de las opciones. Se crea la clase CatalogoUnidades dentro del paquete modelo para gestionar paramétricamente los tipos de unidades de medida disponibles.

En la capa de interacción, dentro del paquete vista, se implementan las clases InterfazConsola y el enumerador OpcionMenu. Estas clases se encargan de abstraer la clase Scanner, capturar los datos por teclado de forma segura y desplegar los menús interactivos al usuario sin mezclar la lógica de negocio.

> [!TIP] Captura de Pantalla
> **Creación de modelos y vistas - Cliente consola**

##### Figura 26. Creación de modelos y vistas - Cliente consola

**Tabla 8. Clase ConversionRequest - Cliente consola**

| package ec.edu.monster.client.model;import java.io.Serializable;/** * [MODELO] Encapsula los datos de entrada para solicitar una conversión de unidades. */public class ConversionRequest implements Serializable { private static final long serialVersionUID = 1L; private String magnitud; // Longitud, Masa, Temperatura private String unidadOrigen; private String unidadDestino; private double valor; public ConversionRequest() { } public ConversionRequest(String magnitud, String unidadOrigen, String unidadDestino, double valor) { this.magnitud = magnitud; this.unidadOrigen = unidadOrigen; this.unidadDestino = unidadDestino; this.valor = valor; } public String getMagnitud() { return magnitud; } public void setMagnitud(String magnitud) { this.magnitud = magnitud; } public String getUnidadOrigen() { return unidadOrigen; } public void setUnidadOrigen(String unidadOrigen) { this.unidadOrigen = unidadOrigen; } public String getUnidadDestino() { return unidadDestino; } public void setUnidadDestino(String unidadDestino) { this.unidadDestino = unidadDestino; } public double getValor() { return valor; } public void setValor(double valor) { this.valor = valor; } @Override public String toString() { return "ConversionRequest{" + "magnitud='" + magnitud + '\'' + ", unidadOrigen='" + unidadOrigen + '\'' + ", unidadDestino='" + unidadDestino + '\'' + ", valor=" + valor + '}'; }} |
| --- |

**Tabla 9. Clase ConsoleView - Cliente consola**

| package ec.edu.monster.client.view;import ec.edu.monster.client.model.ConversionRequest;import ec.edu.monster.client.model.ConversionResponse;import ec.edu.monster.client.model.SessionModel;import java.util.Scanner;/** * [VISTA] Interfaz de usuario para la consola de comandos. * Responsable exclusivo de la entrada/salida por terminal, formateo de texto * y presentación visual de datos. No contiene lógica de negocio ni llamadas directas a red. */public class ConsoleView { public static final String[] UNIDADES_LONGITUD = { "metro", "centimetro", "pie", "yarda", "milla" }; public static final String[] UNIDADES_MASA = { "gramo", "kilogramo", "libra", "onza", "quintal" }; public static final String[] UNIDADES_TEMPERATURA = { "celsius", "fahrenheit", "kelvin", "newton", "reaumur" }; private final Scanner scanner; public ConsoleView() { this.scanner = new Scanner(System.in); } public void mostrarBanner() { System.out.println("=================================================================="); System.out.println(" UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE - EXTENSIÓN LATACUNGA"); System.out.println(" DEPARTAMENTO DE ENERGÍA Y MECÁNICA"); System.out.println(" ARQUITECTURA DE SOFTWARE"); System.out.println(" TAREA TI.1.1 (PAO 2026-51)"); System.out.println("------------------------------------------------------------------"); System.out.println(" CLIENTE DE CONSOLA: CONVERSOR DE UNIDADES (SOAP JAVA - SINBDD) "); System.out.println(" NRC: 36884 / Grupo: GR09 / Autor: Julio Enrique Viche Castillo "); System.out.println(" Patrón Arquitectónico: Modelo - Vista - Controlador (MVC)"); System.out.println("==================================================================\n"); } public String[] pedirCredenciales() { System.out.println("------------------------------------------------------------------"); System.out.println(" INICIO DE SESIÓN OBLIGATORIO (AUTENTICACIÓN) "); System.out.println("------------------------------------------------------------------"); System.out.println(" [Nota]: Credenciales del sistema:"); System.out.println(" Usuario: MONSTER / Contraseña: MONSTER9"); System.out.println("------------------------------------------------------------------"); System.out.print("Ingrese Usuario: "); String usuario = scanner.nextLine().trim(); System.out.print("Ingrese Contraseña: "); String clave = scanner.nextLine().trim(); return new String[]{usuario, clave}; } public boolean preguntarReintento() { System.out.print("\n¿Desea reintentar el inicio de sesión? (s/n): "); String resp = scanner.nextLine().trim().toLowerCase(); return resp.equals("s") // resp.equals("si") // resp.equals("y"); } public String mostrarMenuPrincipal(SessionModel session) { System.out.println("\n=========================================================="); System.out.println(" MENÚ PRINCIPAL DE CONVERSIONES (MVC) "); System.out.println("=========================================================="); System.out.printf(" Sesión activa: %s / Token: %s\n", session.getUsuario(), session.getTokenAbreviado()); System.out.println("----------------------------------------------------------"); System.out.println(" 1. Conversión de Longitud (metro, centimetro, pie, yarda, milla)"); System.out.println(" 2. Conversión de Masa (gramo, kilogramo, libra, onza, quintal)"); System.out.println(" 3. Conversión de Temperatura (Celsius, Fahrenheit, Kelvin, Newton, Reaumur)"); System.out.println(" 4. Verificar estado del token de sesión"); System.out.println(" 5. Cerrar sesión y Salir"); System.out.println("=========================================================="); System.out.print("Seleccione una opción (1-5): "); return scanner.nextLine().trim(); } public ConversionRequest solicitarDatosConversion(String magnitud, String[] unidades) { System.out.println("\n--- [CONVERSIÓN DE " + magnitud.toUpperCase() + "] ---"); String origen = seleccionarUnidad("origen", unidades); if (origen == null) return null; String destino = seleccionarUnidad("destino", unidades); if (destino == null) return null; double valor = pedirValorNumerico(); return new ConversionRequest(magnitud, origen, destino, valor); } private String seleccionarUnidad(String tipo, String[] unidades) { System.out.println("Seleccione unidad de " + tipo + ":"); for (int i = 0; i < unidades.length; i++) { System.out.printf(" %d. %s\n", (i + 1), capitalizar(unidades[i])); } System.out.print("Elija una opción (1-" + unidades.length + "): "); String input = scanner.nextLine().trim(); try { int idx = Integer.parseInt(input) - 1; if (idx >= 0 && idx < unidades.length) { return unidades[idx]; } else { mostrarError("Opción de unidad fuera de rango."); return null; } } catch (NumberFormatException e) { mostrarError("Entrada no numérica."); return null; } } public double pedirValorNumerico() { while (true) { System.out.print("Ingrese el valor numérico a convertir (solo números y '.'): "); String input = scanner.nextLine().trim(); if (input.length() > 16) { mostrarError("Longitud excedida. El valor no puede superar los 16 caracteres."); continue; } if (!input.matches("^\\d+(\\.\\d+)?$")) { mostrarError("Entrada no válida. Solo se permiten números y el punto decimal '.' (ejemplo: 25.5 o 100)."); continue; } try { return Double.parseDouble(input); } catch (NumberFormatException e) { mostrarError("Ingrese un número válido (ejemplo: 25.5 o 100)."); } } } public void mostrarResultadoConversion(ConversionRequest req, ConversionResponse resp) { if (resp != null && resp.isExito()) { System.out.println("\n=========================================================="); System.out.println(" RESULTADO DE LA CONVERSIÓN "); System.out.println("=========================================================="); System.out.printf(java.util.Locale.US, " %.2f %s ---> %.2f %s\n", req.getValor(), capitalizar(req.getUnidadOrigen()), resp.getValorConvertido(), capitalizar(req.getUnidadDestino())); System.out.println("=========================================================="); } else { String err = (resp != null) ? resp.getMensajeError() : "Sin respuesta del servidor"; mostrarError("En la conversión: " + err); } } public void mostrarMensaje(String mensaje) { System.out.println(mensaje); } public void mostrarExito(String mensaje) { System.out.println("\n[OK] " + mensaje); } public void mostrarError(String mensaje) { System.out.println("\n[ERROR] " + mensaje); } public void mostrarDespedida() { System.out.println("\n=========================================================="); System.out.println(" Gracias por utilizar el Conversor de Unidades SOAP (GR09)"); System.out.println("==========================================================\n"); } private String capitalizar(String str) { if (str == null // str.isEmpty()) return ""; return str.substring(0, 1).toUpperCase() + str.substring(1); }} |
| --- |

**Tabla 10. Clase ConsoleAppController - Cliente consola**

| package ec.edu.monster.client.controller;import ec.edu.monster.client.model.ConversionRequest;import ec.edu.monster.client.model.ConversionResponse;import ec.edu.monster.client.model.LoginResponse;import ec.edu.monster.client.view.ConsoleView;/** * [CONTROLADOR PRINCIPAL] Orquestador de la aplicación de consola. * Coordina la Vista (ConsoleView) y los Controladores de Dominio (AuthController, ConversionController) * según el patrón arquitectónico Modelo-Vista-Controlador (MVC). */public class ConsoleAppController { private final ConsoleView view; private final AuthController authController; private final ConversionController conversionController; public ConsoleAppController( ConsoleView view, AuthController authController, ConversionController conversionController) { this.view = view; this.authController = authController; this.conversionController = conversionController; } /** * Inicia el ciclo de vida de la aplicación: banner, autenticación obligatoria y menú principal. */ public void iniciar() { view.mostrarBanner(); // 1. Caso de Uso obligatorio: Iniciar Sesión (Autenticación) boolean autenticado = false; while (!autenticado) { String[] credenciales = view.pedirCredenciales(); try { view.mostrarMensaje("\n[+] Enviando solicitud de autenticación SOAP..."); LoginResponse loginResp = authController.login(credenciales[0], credenciales[1]); if (loginResp != null && loginResp.isExito()) { view.mostrarExito(loginResp.getMensaje()); view.mostrarMensaje("[*] Token de sesión obtenido: " + loginResp.getToken()); autenticado = true; } else { String msg = (loginResp != null) ? loginResp.getMensaje() : "Respuesta vacía del servidor"; view.mostrarError(msg); if (!view.preguntarReintento()) { view.mostrarMensaje("\n[!] Saliendo de la aplicación. Hasta luego."); return; } } } catch (Exception e) { view.mostrarError("No se pudo conectar al servidor SOAP: " + e.getMessage()); view.mostrarMensaje("Verifique que el servidor (Publisher.java) esté activo en el puerto 8080."); if (!view.preguntarReintento()) { return; } } } // 2. Bucle principal de operaciones de conversión boolean ejecutando = true; while (ejecutando) { String opcion = view.mostrarMenuPrincipal(authController.getSessionModel()); switch (opcion) { case "1": ejecutarConversion("longitud", ConsoleView.UNIDADES_LONGITUD); break; case "2": ejecutarConversion("masa", ConsoleView.UNIDADES_MASA); break; case "3": ejecutarConversion("temperatura", ConsoleView.UNIDADES_TEMPERATURA); break; case "4": verificarToken(); break; case "5": cerrarSesion(); ejecutando = false; break; default: view.mostrarError("Opción no válida. Seleccione un número del 1 al 5."); } } view.mostrarDespedida(); } private void ejecutarConversion(String magnitud, String[] unidades) { ConversionRequest req = view.solicitarDatosConversion(magnitud, unidades); if (req == null) return; try { view.mostrarMensaje("\n[+] Enviando petición SOAP al servidor..."); ConversionResponse resp = conversionController.convertir(req); view.mostrarResultadoConversion(req, resp); } catch (Exception e) { view.mostrarError("Falla en la comunicación SOAP: " + e.getMessage()); } } private void verificarToken() { try { boolean valido = authController.validarToken(); if (valido) { view.mostrarExito("El token de sesión sigue ACTIVO y válido en el servidor."); } else { view.mostrarError("El token de sesión ya NO es válido en el servidor."); } } catch (Exception e) { view.mostrarError("Error al verificar el token: " + e.getMessage()); } } private void cerrarSesion() { view.mostrarMensaje("\n[+] Cerrando sesión en el servidor SOAP..."); boolean res = authController.logout(); if (res) { view.mostrarExito("Sesión cerrada satisfactoriamente en el servidor."); } else { view.mostrarMensaje("[*] Sesión finalizada localmente."); } }} |
| --- |

CONSUMO DEL SERVICIO SOAP (CONTROLADOR)

Los clientes Java usan SoapClientManager para comunicarse con ServicioAutenticacion y ServicioConversion. El gestor prepara las solicitudes SOAP y conserva el token de sesión; el cliente no genera clases mediante el asistente de NetBeans.

> [!TIP] Captura de Pantalla
> **Creación de controlador - Cliente consola**

##### Figura 27. Creación de controlador - Cliente consola

ConversionController coordina la selección de magnitud y unidades con SoapClientManager. Valida la sesión, invoca la operación SOAP correspondiente y entrega el resultado a la vista.

**Tabla 11. Clase ConversionController - Cliente consola**

| package ec.edu.monster.client.controller;import ec.edu.monster.client.model.ConversionRequest;import ec.edu.monster.client.model.ConversionResponse;import ec.edu.monster.client.model.SessionModel;import ec.edu.monster.client.ws.IServicioConversion;import ec.edu.monster.client.ws.SoapClientManager;/** * [CONTROLADOR] Gestiona el flujo del caso de uso de Conversión de Unidades. * Despacha las solicitudes hacia el servicio web SOAP ServicioConversion previa verificación del token. */public class ConversionController { private final SoapClientManager clientManager; private final SessionModel sessionModel; public ConversionController(SoapClientManager clientManager, SessionModel sessionModel) { this.clientManager = clientManager; this.sessionModel = sessionModel; } /** * Ejecuta la conversión de la magnitud correspondiente en el servidor SOAP. */ public ConversionResponse convertir(ConversionRequest request) throws Exception { if (!sessionModel.isActivo() // sessionModel.getToken() == null) { return new ConversionResponse(false, 0.0, "Acceso no autorizado: No existe una sesión activa."); } IServicioConversion convService = clientManager.getConversionService(); String token = sessionModel.getToken(); String magnitud = request.getMagnitud().toLowerCase(); switch (magnitud) { case "longitud": return convService.convertirLongitud( token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino()); case "masa": return convService.convertirMasa( token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino()); case "temperatura": return convService.convertirTemperatura( token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino()); default: return new ConversionResponse(false, 0.0, "Magnitud física desconocida: " + request.getMagnitud()); } }} |
| --- |

CLASE PRINCIPAL (EJECUCIÓN)

Finalmente, se codifica la clase ClienteConsola.java que orquesta el flujo completo de la aplicación. El sistema inicia solicitando credenciales al usuario para invocar el servicio de seguridad; únicamente si la autenticación es exitosa, se instancia la interfaz y el controlador para permitir el uso de las operaciones de conversión.

> [!TIP] Captura de Pantalla
> **Creación de clase principal - Cliente consola**

##### Figura 28. Creación de clase principal - Cliente consola

**Tabla 12. Clase ConsoleClientApplication - Cliente consola**

| package ec.edu.monster.client;import ec.edu.monster.client.controller.AuthController;import ec.edu.monster.client.controller.ConsoleAppController;import ec.edu.monster.client.controller.ConversionController;import ec.edu.monster.client.model.SessionModel;import ec.edu.monster.client.view.ConsoleView;import ec.edu.monster.client.ws.SoapClientManager;/** * Punto de entrada principal (Bootstrapper) del Cliente de Consola SOAP. * Instancia y enlaza los componentes según el patrón arquitectónico * Modelo - Vista - Controlador (MVC). * * Asignatura: Arquitectura de Software / Tarea: TI.1.1 * Grupo: GR09 / Autor: Julio Enrique Viche Castillo */public class ConsoleClientApplication { public static void main(String[] args) { // 1. Instanciación del Modelo y Gestor de Servicios Web SOAP SessionModel sessionModel = new SessionModel(); SoapClientManager clientManager = new SoapClientManager(); // 2. Instanciación de los Controladores de Dominio AuthController authController = new AuthController(clientManager, sessionModel); ConversionController conversionController = new ConversionController(clientManager, sessionModel); // 3. Instanciación de la Vista ConsoleView view = new ConsoleView(); // 4. Inyección en el Controlador Orquestador y arranque ConsoleAppController appController = new ConsoleAppController(view, authController, conversionController); appController.iniciar(); }} |
| --- |

#### EJECUCIÓN DEL CLIENTE DE CONSOLA

### CAPTURA PENDIENTELogin - Cliente consola

##### Figura 29. Login - Cliente consola

> [!TIP] Captura de Pantalla
> **Conversión de Longitud - Cliente consola**

##### Figura 30. Conversión de Longitud - Cliente consola

> [!TIP] Captura de Pantalla
> **Conversión de Masa - Cliente consola**

##### Figura 31. Conversión de Masa - Cliente consola

> [!TIP] Captura de Pantalla
> **Conversión de Temperatura - Cliente consola**

##### Figura 32. Conversión de Temperatura - Cliente consola

### CLIENTE ESCRITORIO

#### CREACIÓN DEL PROYECTO

El proyecto se crea de igual manera que el cliente de consola Java Application

#### ESTRUCTURA DEL PROYECTO

El cliente de escritorio con interfaz gráfica se estructuró bajo el patrón MVC. Se crearon los paquetes modelo, vista y controlador para separar el diseño visual de la lógica de consumo del servicio web.

> [!TIP] Captura de Pantalla
> **Estructura del proyecto - Cliente escritorio**

##### Figura 33. Estructura del proyecto - Cliente escritorio

### CODIFICACIÓN DEL PROYECTO CLIENTE ESCRITORIO

#### CREACIÓN DE MODELOS

Al no requerir base de datos, en el paquete modelo solo se manejan clases auxiliares y enumeradores. Estos sirven exclusivamente para llenar las opciones de listas desplegables en las ventanas.

> [!TIP] Captura de Pantalla
> **Creación de modelo - Cliente escritorio**

##### Figura 34. Creación de modelo - Cliente escritorio

CREACIÓN DE CONTROLADOR

El cliente de escritorio usa SoapClientManager para invocar los endpoints SOAP. Sus controladores reciben los datos de las ventanas, verifican la sesión y presentan la respuesta de conversión.

> [!TIP] Captura de Pantalla
> **Creación de servicio - Cliente consola**

##### Figura 35. Creación de servicio - Cliente consola

**Tabla 13. Clase ConversionController - Cliente escritorio**

| package ec.edu.monster.client.controller;import ec.edu.monster.client.model.ConversionRequest;import ec.edu.monster.client.model.ConversionResponse;import ec.edu.monster.client.model.SessionModel;import ec.edu.monster.client.ws.IServicioConversion;import ec.edu.monster.client.ws.SoapClientManager;/** * [CONTROLADOR] Gestiona el cómputo de conversiones invocando el servicio web SOAP. */public class ConversionController { private final SoapClientManager clientManager; private final SessionModel sessionModel; public ConversionController(SoapClientManager clientManager, SessionModel sessionModel) { this.clientManager = clientManager; this.sessionModel = sessionModel; } public ConversionResponse convertir(ConversionRequest request) throws Exception { if (!sessionModel.isActivo() // sessionModel.getToken() == null) { return new ConversionResponse(false, 0.0, "Acceso no autorizado: No existe una sesión activa."); } IServicioConversion convService = clientManager.getConversionService(); String token = sessionModel.getToken(); String magnitud = request.getMagnitud().toLowerCase(); switch (magnitud) { case "longitud": return convService.convertirLongitud( token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino()); case "masa": return convService.convertirMasa( token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino()); case "temperatura": return convService.convertirTemperatura( token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino()); default: return new ConversionResponse(false, 0.0, "Magnitud desconocida: " + request.getMagnitud()); } }} |
| --- |

CREACIÓN DE VISTAS

En el paquete vista se diseñaron las interfaces gráficas utilizando los formularios (JFrames) de NetBeans. Estas ventanas contienen los componentes visuales necesarios para interactuar con el usuario. Su función es estrictamente visual: capturan los datos de entrada y muestran los resultados obtenidos, delegando todo el procesamiento al controlador.

> [!TIP] Captura de Pantalla
> **Creación de vistas - Cliente escritorio**

##### Figura 36. Creación de vistas - Cliente escritorio

CLASE PRINCIPAL (EJECUCIÓN)

La clase principal del proyecto funciona como el punto de entrada de la aplicación. Su única responsabilidad es instanciar la ventana inicial del sistema (la interfaz de inicio de sesión) y hacerla visible en la pantalla para iniciar el flujo gráfico del programa.

> [!TIP] Captura de Pantalla
> **Creación de vistas - Cliente escritorio**

##### Figura 37. Creación de vistas - Cliente escritorio

#### EJECUCIÓN DEL CLIENTE DE ESCRITORIO

> [!TIP] Captura de Pantalla
> **Login - Cliente escritorio**

##### Figura 38. Login - Cliente escritorio

> [!TIP] Captura de Pantalla
> **Pantalla de Conversiones - Cliente escritorio**

##### Figura 39. Pantalla de Conversiones - Cliente escritorio

### CLIENTE WEB

#### CREACIÓN DEL PROYECTO

El proyecto se crea de igual manera que el webservice con ‘Web Application’.

#### ESTRUCTURA DEL PROYECTO

El cliente web se desarrolló siguiendo el patrón MVC utilizando Servlets y JavaServer Pages (JSP). La estructura separa la lógica de control (en el paquete controlador) de las interfaces de usuario (páginas web).

> [!TIP] Captura de Pantalla
> **Estructura del proyecto - Cliente web**

##### Figura 40. Estructura del proyecto - Cliente web

### CODIFICACIÓN DEL PROYECTO CLIENTE WEB

CONSUMO DEL SERVICIO SOAP (SERVLETS)

El cliente web usa LoginServlet y ConversionServlet para gestionar solicitudes del navegador y llamar a SoapClientManager. Jetty 11 embebido escucha en el puerto 8081; las vistas disponibles incluyen index.html, conversor.html y páginas JSP.

> [!TIP] Captura de Pantalla
> **Creación de servlets - Cliente web**

##### Figura 41. Creación de servlets - Cliente web

**Tabla 14. Clase ConversionServlet - Cliente web**

| package ec.edu.monster.client.controller;import ec.edu.monster.client.model.ConversionRequest;import ec.edu.monster.client.model.ConversionResponse;import ec.edu.monster.client.model.SessionModel;import ec.edu.monster.client.ws.IServicioConversion;import ec.edu.monster.client.ws.SoapClientManager;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.HttpServlet;import jakarta.servlet.http.HttpServletRequest;import jakarta.servlet.http.HttpServletResponse;import jakarta.servlet.http.HttpSession;import java.io.IOException;/** * [CONTROLADOR] Servlet para el procesamiento del cómputo de conversiones vía SOAP. */@WebServlet(name = "ConversionServlet", urlPatterns = {"/convertir"})public class ConversionServlet extends HttpServlet { private final SoapClientManager clientManager = new SoapClientManager(); @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { request.setCharacterEncoding("UTF-8"); response.setCharacterEncoding("UTF-8"); HttpSession session = request.getSession(false); SessionModel sessionUser = (session != null) ? (SessionModel) session.getAttribute("sessionUser") : null; // Validación estricta de sesión activa (SINBDD) if (sessionUser == null // !sessionUser.isActivo() // sessionUser.getToken() == null) { response.sendRedirect(request.getContextPath() + "/index.html?error=SesionInvalida"); return; } String magnitud = request.getParameter("magnitud"); String unidadOrigen = request.getParameter("unidadOrigen"); String unidadDestino = request.getParameter("unidadDestino"); String valorStr = request.getParameter("valor"); if (magnitud == null // unidadOrigen == null // unidadDestino == null // valorStr == null) { request.setAttribute("error", "Faltan parámetros obligatorios para la conversión."); request.getRequestDispatcher("conversor.jsp").forward(request, response); return; } double valor; try { String valorLimpio = valorStr.trim(); if (valorLimpio.length() > 16) { request.setAttribute("error", "El valor numérico no puede superar los 16 caracteres."); request.getRequestDispatcher("conversor.jsp").forward(request, response); return; } if (!valorLimpio.matches("^\\d+(\\.\\d+)?$")) { request.setAttribute("error", "Solo se permiten números y el punto decimal '.' (ejemplo: 25.5 o 100)."); request.getRequestDispatcher("conversor.jsp").forward(request, response); return; } valor = Double.parseDouble(valorLimpio); } catch (NumberFormatException e) { request.setAttribute("error", "El valor numérico ingresado no es válido."); request.getRequestDispatcher("conversor.jsp").forward(request, response); return; } ConversionRequest convReq = new ConversionRequest(magnitud, unidadOrigen, unidadDestino, valor); String token = sessionUser.getToken(); try { IServicioConversion convService = clientManager.getConversionService(); ConversionResponse convResp; switch (magnitud.toLowerCase()) { case "longitud": convResp = convService.convertirLongitud(token, valor, unidadOrigen, unidadDestino); break; case "masa": convResp = convService.convertirMasa(token, valor, unidadOrigen, unidadDestino); break; case "temperatura": convResp = convService.convertirTemperatura(token, valor, unidadOrigen, unidadDestino); break; default: convResp = new ConversionResponse(false, 0.0, "Magnitud física desconocida: " + magnitud); } if (convResp != null && convResp.isExito()) { String params = String.format(java.util.Locale.US, "valor=%.2f&origen=%s&destino=%s&resultado=%.2f&magnitud=%s&usuario=%s&token=%s", convReq.getValor(), java.net.URLEncoder.encode(convReq.getUnidadOrigen(), java.nio.charset.StandardCharsets.UTF_8), java.net.URLEncoder.encode(convReq.getUnidadDestino(), java.nio.charset.StandardCharsets.UTF_8), convResp.getValorConvertido(), java.net.URLEncoder.encode(convReq.getMagnitud(), java.nio.charset.StandardCharsets.UTF_8), java.net.URLEncoder.encode(sessionUser.getUsuario(), java.nio.charset.StandardCharsets.UTF_8), sessionUser.getTokenAbreviado()); response.sendRedirect(request.getContextPath() + "/conversor.html?" + params); } else { String err = (convResp != null) ? convResp.getMensajeError() : "Error en el cálculo"; String params = String.format("error=%s&magnitud=%s&usuario=%s&token=%s", java.net.URLEncoder.encode(err, java.nio.charset.StandardCharsets.UTF_8), java.net.URLEncoder.encode(convReq.getMagnitud(), java.nio.charset.StandardCharsets.UTF_8), java.net.URLEncoder.encode(sessionUser.getUsuario(), java.nio.charset.StandardCharsets.UTF_8), sessionUser.getTokenAbreviado()); response.sendRedirect(request.getContextPath() + "/conversor.html?" + params); } } catch (Exception e) { String errorMsg = "Falla de comunicación con el servicio de conversión: " + e.getMessage(); response.sendRedirect(request.getContextPath() + "/conversor.html?error=" + java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8)); } } @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { response.sendRedirect(request.getContextPath() + "/conversor.html"); }} |
| --- |

**Tabla 15. Clase LoginServlet - Cliente web**

| package ec.edu.monster.client.controller;import ec.edu.monster.client.model.LoginResponse;import ec.edu.monster.client.model.SessionModel;import ec.edu.monster.client.ws.IServicioAutenticacion;import ec.edu.monster.client.ws.SoapClientManager;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.HttpServlet;import jakarta.servlet.http.HttpServletRequest;import jakarta.servlet.http.HttpServletResponse;import jakarta.servlet.http.HttpSession;import java.io.IOException;/** * [CONTROLADOR] Servlet para el procesamiento del inicio de sesión SOAP. */@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})public class LoginServlet extends HttpServlet { private final SoapClientManager clientManager = new SoapClientManager(); @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { request.setCharacterEncoding("UTF-8"); response.setCharacterEncoding("UTF-8"); String usuario = request.getParameter("usuario"); String clave = request.getParameter("clave"); if (usuario == null // usuario.trim().isEmpty() // clave == null // clave.trim().isEmpty()) { response.sendRedirect(request.getContextPath() + "/index.html?error=" + java.net.URLEncoder.encode("Por favor, complete todos los campos de acceso.", java.nio.charset.StandardCharsets.UTF_8)); return; } try { IServicioAutenticacion authService = clientManager.getAuthService(); LoginResponse loginResp = authService.iniciarSesion(usuario.trim(), clave.trim()); if (loginResp != null && loginResp.isExito()) { HttpSession session = request.getSession(true); SessionModel sessionModel = new SessionModel(usuario.trim(), loginResp.getToken()); session.setAttribute("sessionUser", sessionModel); response.sendRedirect(request.getContextPath() + "/conversor.html?usuario=" + java.net.URLEncoder.encode(usuario.trim(), java.nio.charset.StandardCharsets.UTF_8) + "&token=" + sessionModel.getTokenAbreviado()); } else { String errorMsg = (loginResp != null) ? loginResp.getMensaje() : "Credenciales inválidas."; response.sendRedirect(request.getContextPath() + "/index.html?error=" + java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8)); } } catch (Exception e) { String errorMsg = "Error de comunicación con el Servidor SOAP: " + e.getMessage(); response.sendRedirect(request.getContextPath() + "/index.html?error=" + java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8)); } } @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { response.sendRedirect(request.getContextPath() + "/index.html"); }} |
| --- |

CREACIÓN DE VISTAS

Las interfaces de usuario se construyeron utilizando HTML y la tecnología JSP (JavaServer Pages) dentro del directorio Web Pages. Se diseñaron dos vistas principales: index.jsp, que actúa como la pantalla de inicio de sesión interactuando con el LoginServlet, y conversor.jsp, que contiene los formularios dinámicos para seleccionar las unidades de medida, capturar los valores y mostrar los resultados procesados por el ConversionServlet.

> [!TIP] Captura de Pantalla
> **Creación de vistas - Cliente web**

##### Figura 42. Creación de vistas - Cliente web

**Tabla 16. Archivo conversor.html - Cliente web**

| <!DOCTYPE html><html lang="es"><head> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Conversor de Unidades SOAP - Panel Web (GR09)</title> <link rel="stylesheet" href="css/style.css"></head><body> <!-- Encabezado con sesión activa --> <header class="header-bar"> <div class="header-brand"> <h1>CONVERSOR DE UNIDADES (SOAP JAVA · SINBDD)</h1> <p>ESPE · Arquitectura de Software (TI.1.1) / Autor: Julio Enrique Viche Castillo</p> </div> <div class="header-user"> <span class="badge-session"> <span class="status-dot">●</span> <strong id="lblUsuario">MONSTER</strong> <span style="opacity: 0.8; font-size: 0.75rem;" id="lblToken">(Sesión Activa)</span> </span> <a href="logout" class="btn-logout">Cerrar Sesión</a> </div> </header> <!-- Panel Central del Conversor --> <main class="main-wrapper"> <div class="card-box"> <h2 class="card-title">Conversor de Unidades Multimagnitud</h2> <p class="card-subtitle">Cálculos procesados mediante servicios web SOAP protegidos por token</p> <div id="alertBox" class="alert alert-error" style="display: none;"></div> <!-- 1. Pestañas de Magnitud --> <label class="form-label">1. Seleccione la Magnitud Física:</label> <div class="segmented-group"> <button type="button" class="segment-btn active" id="btnLongitud" onclick="cambiarMagnitud('Longitud')"> Longitud </button> <button type="button" class="segment-btn" id="btnMasa" onclick="cambiarMagnitud('Masa')"> Masa </button> <button type="button" class="segment-btn" id="btnTemperatura" onclick="cambiarMagnitud('Temperatura')"> Temperatura </button> </div> <!-- Formulario de Conversión --> <form action="convertir" method="POST" id="formConversion"> <input type="hidden" id="inputMagnitud" name="magnitud" value="Longitud"> <!-- 2. Valor Numérico --> <div class="form-group"> <label class="form-label" for="txtValor">2. Ingrese el Valor a Convertir:</label> <input type="text" id="txtValor" name="valor" class="form-input" autocomplete="off" inputmode="decimal" maxlength="16" pattern="^\d+(\.\d+)?$" title="Solo se permiten números y el punto decimal '.' (ej. 100 o 25.5)" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) // (event.charCode === 46 && this.value.indexOf('.') === -1)" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*?)\..*/g, '$1').slice(0, 16)" value="100.00" required style="font-size: 1.15rem; font-weight: bold;"> </div> <!-- 3. Unidades Origen -> Destino con Intercambio --> <div class="conversion-units-row"> <div> <label class="form-label" for="cmbOrigen">De (Unidad de Origen):</label> <select id="cmbOrigen" name="unidadOrigen" class="form-select" required></select> </div> <div> <button type="button" class="btn-swap" onclick="intercambiarUnidades()" title="Intercambiar unidades (Origen ⇄ Destino)"> &#8644; </button> </div> <div> <label class="form-label" for="cmbDestino">A (Unidad de Destino):</label> <select id="cmbDestino" name="unidadDestino" class="form-select" required></select> </div> </div> <button type="submit" class="btn-primary" id="btnCalcular">Calcular Conversión SOAP</button> </form> <!-- 4. Tarjeta de Resultado Dinámica --> <div class="result-card" id="cardResultado" style="display: none;"> <div class="result-value" id="resultadoTexto"></div> <div class="result-subtext" id="resultadoSubtext"> Cálculo procesado exitosamente por el Servidor SOAP en memoria (SINBDD). </div> <button type="button" class="btn-copy" onclick="copiarResultado()">Copiar Resultado</button> </div> </div> </main> <!-- Barra de Estado Inferior --> <footer class="footer-bar"> <span>Servicio SOAP: <code>http://localhost:8080/ServidorSOAP/ServicioConversion?wsdl</code></span> <span>Arquitectura: Modelo - Vista - Controlador (HTML5 / Servlet / JAX-WS)</span> </footer> <!-- Catálogo de JavaScript para unidades dinámicas --> <script> const CATALOGO = { 'Longitud': ['Metro', 'Centimetro', 'Pie', 'Yarda', 'Milla'], 'Masa': ['Gramo', 'Kilogramo', 'Libra', 'Onza', 'Quintal'], 'Temperatura': ['Celsius', 'Fahrenheit', 'Kelvin', 'Newton', 'Reaumur'] }; function cambiarMagnitud(magnitud) { document.getElementById('inputMagnitud').value = magnitud; // Actualizar estilo visual de los botones de pestaña document.querySelectorAll('.segment-btn').forEach(btn => { btn.classList.remove('active'); if (btn.innerText.trim().toLowerCase() === magnitud.toLowerCase()) { btn.classList.add('active'); } }); // Actualizar selectores de unidades const cmbOrigen = document.getElementById('cmbOrigen'); const cmbDestino = document.getElementById('cmbDestino'); cmbOrigen.innerHTML = ''; cmbDestino.innerHTML = ''; const unidades = CATALOGO[magnitud] // []; unidades.forEach(u => { const opt1 = new Option(u, u); const opt2 = new Option(u, u); cmbOrigen.add(opt1); cmbDestino.add(opt2); }); if (unidades.length > 1) { cmbDestino.selectedIndex = 1; } } function intercambiarUnidades() { const cmbOrigen = document.getElementById('cmbOrigen'); const cmbDestino = document.getElementById('cmbDestino'); const temp = cmbOrigen.value; cmbOrigen.value = cmbDestino.value; cmbDestino.value = temp; } function copiarResultado() { const texto = document.getElementById('resultadoTexto').innerText.trim(); navigator.clipboard.writeText(texto).then(() => { const btn = event.target; const orig = btn.innerText; btn.innerText = '¡Copiado!'; setTimeout(() => { btn.innerText = orig; }, 1500); }); } window.onload = function() { const params = new URLSearchParams(window.location.search); const user = params.get('usuario'); const token = params.get('token'); if (user) document.getElementById('lblUsuario').innerText = user; if (token) document.getElementById('lblToken').innerText = '(Token: ' + token + ')'; const mag = params.get('magnitud') // 'Longitud'; cambiarMagnitud(mag); const valor = params.get('valor'); if (valor) document.getElementById('txtValor').value = valor; const origen = params.get('origen'); const destino = params.get('destino'); if (origen) document.getElementById('cmbOrigen').value = origen; if (destino) document.getElementById('cmbDestino').value = destino; const resultado = params.get('resultado'); const error = params.get('error'); if (resultado) { const card = document.getElementById('cardResultado'); card.style.display = 'block'; document.getElementById('resultadoTexto').innerText = parseFloat(valor).toFixed(2) + " " + origen + " = " + parseFloat(resultado).toFixed(2) + " " + destino; } else if (error) { const alertBox = document.getElementById('alertBox'); alertBox.style.display = 'block'; alertBox.innerText = decodeURIComponent(error); } }; </script></body></html> |
| --- |

**Tabla 17. Archivo index.html - Cliente web**

| <!DOCTYPE html><html lang="es"><head> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Acceso al Sistema - Conversor de Unidades SOAP (GR09)</title> <link rel="stylesheet" href="css/style.css"></head><body> <!-- Encabezado Institucional --> <header class="header-bar"> <div class="header-brand"> <h1>UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE</h1> <p>Arquitectura de Software (TI.1.1) · Grupo GR09 / Julio Enrique Viche Castillo</p> </div> <div class="header-user"> <span class="badge-session"> <span class="status-dot">●</span> Servidor SOAP: 8080 </span> </div> </header> <!-- Contenedor del Formulario de Login --> <main class="main-wrapper"> <div class="card-box card-login"> <h2 class="card-title">Iniciar Sesión</h2> <p class="card-subtitle">Autenticación requerida para acceder al Conversor SOAP (SINBDD)</p> <div id="alertBox" class="alert alert-error" style="display: none;"></div> <div id="infoBox" class="alert alert-info" style="display: none;"></div> <form action="login" method="POST"> <div class="form-group"> <label class="form-label" for="txtUsuario">Usuario:</label> <input type="text" id="txtUsuario" name="usuario" class="form-input" value="MONSTER" required autofocus> </div> <div class="form-group"> <label class="form-label" for="txtClave">Contraseña:</label> <input type="password" id="txtClave" name="clave" class="form-input" value="MONSTER9" required> </div> <div class="alert alert-info" style="font-size: 0.8rem; padding: 8px 12px; margin-bottom: 20px;"> <strong>Credenciales del curso:</strong> Usuario: <code>MONSTER</code> / Clave: <code>MONSTER9</code> </div> <button type="submit" class="btn-primary">Ingresar al Conversor</button> </form> </div> </main> <!-- Barra de Estado Inferior --> <footer class="footer-bar"> <span>Servidor Backend: <code>http://localhost:8080/ServidorSOAP</code> (Modalidad SINBDD)</span> <span>Patrón Arquitectónico: Modelo - Vista - Controlador (JSP / Servlet)</span> </footer> <script> // Leer parámetros de la URL para mostrar alertas const params = new URLSearchParams(window.location.search); const error = params.get('error'); const mensaje = params.get('mensaje'); if (error) { const alertBox = document.getElementById('alertBox'); alertBox.style.display = 'block'; if (error === 'CredencialesInvalidas' // error === 'Invalido') { alertBox.innerText = 'Usuario o contraseña incorrectos. Verifique sus credenciales.'; } else if (error === 'SesionInvalida') { alertBox.innerText = 'Su sesión ha expirado o no es válida. Inicie sesión nuevamente.'; } else { alertBox.innerText = decodeURIComponent(error); } } if (mensaje) { const infoBox = document.getElementById('infoBox'); infoBox.style.display = 'block'; if (mensaje === 'SesionCerrada') { infoBox.innerText = 'Sesión cerrada satisfactoriamente en el servidor SOAP.'; } else { infoBox.innerText = decodeURIComponent(mensaje); } } </script></body></html> |
| --- |

EJECUCIÓN DEL CLIENTE DE WEB

### CAPTURA PENDIENTELogin - Cliente web

##### Figura 43. Login - Cliente web

> [!TIP] Captura de Pantalla
> **Pantalla de Conversiones - Cliente web**

##### Figura 44. Pantalla de Conversiones - Cliente web

### CLIENTE MÓVIL

#### CREACIÓN DEL PROYECTO

El proyecto se debe crear en Android Studio. Se selecciona la opción ‘New Project’, en la pantalla siguiente ‘Empty Activity’ y finalmente se configura el proyecto con nombre, ubicación y la opción ‘Java’ en Build Configuration Laguaje. Finalizamos haciendo click en el botón ‘Finish’.

> [!TIP] Captura de Pantalla
> **Configuración del proyecto - Cliente móvil**

##### Figura 45. Configuración del proyecto - Cliente móvil

#### ESTRUCTURA DEL PROYECTO

El desarrollo de la aplicación móvil en Android Studio se organizó bajo el patrón arquitectónico MVC (Modelo-Vista-Controlador). Para asegurar el desacoplamiento de la lógica, la vista y el consumo de red, se estructuró el proyecto en los paquetes model, view y controller.

> [!TIP] Captura de Pantalla
> **Estructura del proyecto - Cliente móvil**

##### Figura 46. Estructura del proyecto - Cliente móvil

### CODIFICACIÓN DEL PROYECTO CLIENTE MÓVIL

#### CREACIÓN DE MODELOS

En el paquete model se crearon las clases necesarias para el intercambio de información: ConversionRequest, ConversionResult, LoginResult y la enumeración ServiceType.

La aplicación Android usa SoapClient con ksoap2-android para enviar solicitudes SOAP/XML al servidor. Los controladores procesan inicio de sesión y conversiones de forma asíncrona y actualizan las actividades.

> [!TIP] Captura de Pantalla
> **Creación de modelos - Cliente móvil**

##### Figura 47. Creación de modelos - Cliente móvil

CREACIÓN DE CONTROLADORES

LoginController y ConversionController coordinan las actividades Android con SoapClient. Ejecutan las llamadas de red fuera del hilo de interfaz y devuelven el resultado a las vistas.

> [!TIP] Captura de Pantalla
> **Creación de controladores - Cliente móvil**

##### Figura 48. Creación de controladores - Cliente móvil

**Tabla 18. Clase LoginController - Cliente móvil**

| package ec.edu.monster.client.controller;import android.os.Handler;import android.os.Looper;import ec.edu.monster.client.model.LoginResponse;import ec.edu.monster.client.model.SessionModel;import ec.edu.monster.client.model.SoapClient;import java.util.concurrent.Future;/** * [CONTROLADOR] Gestiona el flujo de autenticación para la vista móvil. * Coordina llamadas asíncronas y publica resultados en el hilo principal (MainLooper). */public class LoginController { public interface LoginCallback { void onSuccess(SessionModel session); void onError(String mensaje); } private final SoapClient soapClient; private final Handler mainHandler; public LoginController() { this.soapClient = new SoapClient(); this.mainHandler = new Handler(Looper.getMainLooper()); } public void procesarLogin(String baseUrl, String usuario, String clave, LoginCallback callback) { new Thread(() -> { try { Future<LoginResponse> future = soapClient.iniciarSesion(baseUrl, usuario, clave); LoginResponse resp = future.get(); mainHandler.post(() -> { if (resp != null && resp.isExito() && resp.getToken() != null) { SessionModel session = new SessionModel(usuario, resp.getToken()); callback.onSuccess(session); } else { String err = (resp != null && resp.getMensaje() != null) ? resp.getMensaje() : "Error al iniciar sesión."; callback.onError(err); } }); } catch (Exception e) { mainHandler.post(() -> callback.onError("Error de conexión: " + e.getMessage())); } }).start(); }} |
| --- |

**Tabla 19. Clase ConversionController - Cliente móvil**

| package ec.edu.monster.client.controller;import android.os.Handler;import android.os.Looper;import ec.edu.monster.client.model.ConversionRequest;import ec.edu.monster.client.model.ConversionResponse;import ec.edu.monster.client.model.SoapClient;import java.util.concurrent.Future;/** * [CONTROLADOR] Gestiona el despacho de conversiones de unidades SOAP. */public class ConversionController { public interface ConversionCallback { void onSuccess(ConversionResponse response); void onError(String mensaje); } private final SoapClient soapClient; private final Handler mainHandler; public ConversionController() { this.soapClient = new SoapClient(); this.mainHandler = new Handler(Looper.getMainLooper()); } public void procesarConversion(String baseUrl, String token, ConversionRequest req, ConversionCallback callback) { new Thread(() -> { try { Future<ConversionResponse> future; String magnitud = req.getMagnitud().toLowerCase(); switch (magnitud) { case "longitud": future = soapClient.convertirLongitud(baseUrl, token, req.getValor(), req.getUnidadOrigen(), req.getUnidadDestino()); break; case "masa": future = soapClient.convertirMasa(baseUrl, token, req.getValor(), req.getUnidadOrigen(), req.getUnidadDestino()); break; case "temperatura": future = soapClient.convertirTemperatura(baseUrl, token, req.getValor(), req.getUnidadOrigen(), req.getUnidadDestino()); break; default: mainHandler.post(() -> callback.onError("Magnitud física no soportada: " + req.getMagnitud())); return; } ConversionResponse resp = future.get(); mainHandler.post(() -> { if (resp != null && resp.isExito()) { callback.onSuccess(resp); } else { String err = (resp != null && resp.getMensajeError() != null) ? resp.getMensajeError() : "Error al procesar la conversión."; callback.onError(err); } }); } catch (Exception e) { mainHandler.post(() -> callback.onError("Error de red: " + e.getMessage())); } }).start(); }} |
| --- |

CREACIÓN DE VISTAS

Se crean los archivos de interfaz gráfica en el paquete views.

> [!TIP] Captura de Pantalla
> **Creación de vistas - Cliente móvil**

##### Figura 49. Creación de vistas - Cliente móvil

#### EJECUCIÓN DEL CLIENTE DE MÓVIL

#### CAPTURA PENDIENTELogin - Cliente móvil

##### Figura 50. Login - Cliente móvil

> [!TIP] Captura de Pantalla
> **Pantalla de Conversiones - Cliente móvil**

*_Figura 51. Pantalla de Conversiones - Cliente móvil_*

#### CONCLUSIONES

El proyecto integra Publisher en Java, Metro/JAX-WS, los endpoints de autenticación y conversión, y cuatro clientes Java. El servidor se ejecuta como proceso independiente, publica el WSDL de cada endpoint y conserva credenciales y sesiones en memoria, sin base de datos.

La implementación de los módulos de seguridad y conversiones evidenció la importancia de separar correctamente la lógica de negocio y la capa de exposición del servicio web. A pesar de no contar con conexión a una base de datos, el uso de DTOs, validaciones en memoria y manejo de sesiones simuladas dio como resultado soluciones mantenibles, escalables y con un nivel adecuado de desacoplamiento.

Los clientes de consola, escritorio, web con Servlets/JSP y móvil Android consumen los endpoints SOAP. SoapClientManager gestiona las llamadas desde los clientes Java y ksoap2-android permite el intercambio SOAP/XML en Android; todos usan el token de sesión para las conversiones.

La solución conecta clientes Java con dos endpoints SOAP publicados por Metro/JAX-WS. El WSDL describe las operaciones y los tipos de datos; Publisher inicia el servicio y los clientes manejan el flujo de autenticación y conversión.

#### RECOMENDACIONES

Mantener la estructura modular y el patrón MVC utilizados en el proyecto para futuros desarrollos, ya que permite escalar nuevas categorías de conversión sin comprometer la arquitectura inicial del servidor SOAP.

Implementar un manejo de excepciones más detallado (como SOAPFaults) en el servidor, de modo que cuando el cliente envíe unidades no soportadas o datos erróneos, los mensajes de error XML sean estandarizados y faciliten el diagnóstico.

Añadir validaciones más estrictas en los servicios, especialmente en el control del tiempo de vida de las sesiones del usuario y en el bloqueo de peticiones con valores numéricos inválidos (como medidas de masa negativas).

Como mejora futura, se puede integrar un sistema de logs en el proceso Publisher para registrar solicitudes SOAP, errores y operaciones de conversión, facilitando el diagnóstico del servicio.

Implementar mecanismos de caché o estructuras de datos optimizadas en memoria para los catálogos de unidades, en caso de que el sistema escale a un volumen masivo de peticiones concurrentes.

Desarrollar pruebas unitarias en la capa de servicios para asegurar la exactitud de las fórmulas matemáticas utilizadas en las transformaciones de temperatura, longitud y masa ante futuras modificaciones.

Mejorar la interfaz de usuario en los clientes web y móvil incorporando retroalimentación visual más precisa, controles de validación en los formularios y manejo asíncrono avanzado para no bloquear las pantallas durante la espera de respuesta del servidor.

Documentar el contrato WSDL detalladamente con comentarios estructurados, lo que permitirá a futuros desarrolladores entender rápidamente los tipos de datos requeridos y facilitar su consumo desde sistemas construidos en otros lenguajes de programación.
