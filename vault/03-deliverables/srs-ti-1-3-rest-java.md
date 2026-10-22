---
type: deliverable
tags: [deliverable, uml, ers, srs, template, requirements]
---

# Software Requirements Specification (SRS) - TI.1.3 (REST Java)

> [!NOTE]
> Especificación de Requerimientos de Software (SRS/ERS) para la entrega **TI.1.3** (REST sobre Java).
> Caso de estudio: **Conversor de Unidades** multi-canal sin base de datos (**SINBDD**). Guía de variaciones en [srs-variations](srs-variations.md).

## PORTADA Y METADATOS INSTITUCIONALES

| Campo | Detalle Institucional |
| :--- | :--- |
| **Institución** | Universidad de las Fuerzas Armadas ESPE |
| **Carrera** | Tecnologías de la Información / Ingeniería de Software |
| **Asignatura** | Arquitectura de Software |
| **Tema** | ESPECIFICACIÓN DE REQUERIMIENTOS DE SOFTWARE PARA SOA CON REST EN JAVA SIN BDD |
| **Tarea / Taller** | **TI.1.3** |
| **NRC** | 36884 |
| **Grupo** | GR09 |
| **Integrantes** | **Viche Castillo, Julio Enrique** |
| **Docente / Tutor** | Ing. Eduardo Mauricio Campaña Ortega, MIS.MDU.CCNA.CCIA . PhD. (c) Ingeniería de Software, PhD. (c) Seguridad Información |
| **Fecha** | 23/09/2026 |

---

## ÍNDICE DE CONTENIDO, ILUSTRACIONES Y TABLAS

### Índice de Ilustraciones
- **Ilustración 1.** Diagrama de casos de uso del sistema

### Índice de Tablas
- **Tabla 1.** Definiciones
- **Tabla 2.** Acrónimos y abreviaturas
- **Tabla 3.** Referencias
- **Tabla 4.** Tipo de usuario / Usuario final
- **Tabla 8.** Requisitos de interfaces de hardware
- **Tabla 9.** Interfaces de software
- **Tabla 10.** Objetivos del sistema: Autenticar el acceso de los clientes (OBJ-001)
- **Tabla 11.** Objetivos del sistema: Realizar conversiones (OBJ-002)
- **Tabla 14.** Actor: Usuario Final (ACT-001)
- **Tabla 18.** Matriz general de requisitos funcionales
- **Tabla 19.** RQF-001: Autenticar Cliente
- **Tabla 20.** RQF-002: Convertir Longitud
- **Tabla 21.** RQF-003: Convertir Masa
- **Tabla 22.** RQF-004: Convertir Temperatura
- **Tabla 26.** RNF-001: Protocolo de Arquitectura (RESTful)
- **Tabla 27.** RNF-002: Especificación de Software
- **Tabla 28.** RNF-003: Entorno de Desarrollo
- **Tabla 29.** RNF-004: Servidor de Aplicaciones
- **Tabla 30.** RNF-005: Seguridad y autorización mediante Token
- **Tabla 31.** RNF-006: Accesibilidad
- **Tabla 32.** RNF-007: Mantenimiento

---

## CASO DE ESTUDIO

Se desea desarrollar una aplicación denominada "Convertidor de Unidades" que permita a los usuarios árealizar conversiones de magnitudes físicas (longitud, peso, volumen, tiempo, temperatura, áárea) en tiempo áreal, accediendo desde múltiples canales: aplicación de consola, aplicación de escritorio, aplicación web y aplicación móvil. El sistema deberáá modernizar la forma en que los usuarios obtienen equivalencias de unidades al centralizar la lógica de conversión en una capa de serávicios robusta y permitir la interoperabilidad entre los distintos clientes.

Se desea implementar un sistema de serávicios de software basado en una Arquitectura Orientada a Servicios (SOA), la cual se atiene a las siguientes características de negocio:

- Gestáión de Usuario: El acceso al sistema estáá restáringido por un úúnico usuario debe autenticarse con sus credenciales que fueron cáreadas previamente para el acceso al sistema.

- Catálogo de Unidades: El sistema mantiene un catálogo organizado de categorías (longitud, peso, volumen, tiempo, temperatura, áárea) y para cada categoría, un conjunto de unidades con su factor de conversión a una unidad base.

- El sistema debe exponer los serávicios que permitan árealizar transformaciones precisas entre diversas unidades de medida, organizadas en Longitud, Masa y Temperatura.

- Multi-canal: La aplicación se despliega como cliente de consola, cliente de escritorio (Java Swing), aplicación Web (navegador) y Aplicación Móvil (Android), todas consumiendo el mismo serávicio backend para garantizar consistencia en los resultados independientemente del canal.

- En cada solicitud de procesamiento, el consumidor del serávicio debe proporcionar la unidad de origen, la de destáino y el valor número que se desea transformar.

- El acceso al módulo de conversión debe estáar protegido por un mecanismo de autenticación. Se requiere validar un token de seguridad antes de procesar las peticiones; en el caso de que los tokens no sean válidos o que las sesiones caduquen, el serávicio debe mostrar mensajes de error de falta de autorización.

- Independientemente del protocolo de comuúnicación utilizado, el sistema debe retornar una respuestáa estáructurada, en la cual debe estáar presente el estáado de la operación y el resultado del cálculo numérico obtenido.

- El diseño de los serávicios debe garantizar que la lógica de negocio sea consumida de la misma forma para los cuatro clientes: aplicación de consola, aplicación de escritorio, aplicación web y una aplicación móvil.

- La arquitectura del sistema debe separar la lógica de cálculo de los puntos de exposición, permitiendo que el núcleo sea reutilizable sin importar si la comuúnicación se árealiza mediante protocolos basados en texto o estáructurado en objetos.

- Se debe implementar un control de errores robusto que gestáione solicitudes con datos inconsistentes, unidades no soportadas o credenciales de sesión erróneas, garantizando que el cliente siempre tenga una respuestáa del sistema.

- El sistema seráá desarrollado con NetBeans como IDE y sin uso base de datos. La arquitectura es SOA (Service Oriented Architecture) usando Servicios Web SOAP con Java (JAX-WS)

## Introducción

Este documento de Especificación de Requerimientos de Software ha sido cáreado siguiendo la norma IEEE-830 para la especificación de requerimientos, que forma parte de la documentación generada previo al posible desarrollo e implementación de la capa de serávicios del sistema “Conversión de Unidades Monster”. El documento detalla la arquitectura, diseño de interfaces y comportamientos funcionales necesarios para construir una solución basada en el arquitectura REST, incluyendo los mecanismos de consumo por parte cuatro clientes diferentes y las restáricciones tecnológicas.

## Propósito

El propósito principal del presente documento de visión es listar, definir y detallar los requerimientos funcionales y no funcionales del sistema “Conversión de Unidades Monster”. Este documento también ayuda a recopilar y analizar ideas recogidas en el proyecto. Cabe mencionar que estáe documento no estáará sujeto a cambio en el caso de que se añadan más requisitos para el proyecto.

Es principalmente preparado para estáablecer el escenario para la fase de diseño del proyecto. El entregable que estáá siendo elaborado es la primera versión del documento de visión para el proyecto enfocado en el sistema de “Conversión de Unidades Monster”.

## Alcance

El presente trabajo se centra en la implementación del sistema de “Conversión de Unidades Monster”, mismo que facilite los procesos de cálculo matemático y transformación de medidas de manera centralizada, logrando un mayor control de las operaciones, escalabilidad y estáandarización de consumo de datos. Desarrollar un sistema considerando que el software se encuentra dividido en serávidor y cliente.

La implementación del sistema tendrá como beneficios la gestáión de los siguientes módulos:

- MÓDULO DE SEGURIDAD

- Login y autenticación del cliente.

- Generación y validación de tokens de seguridad.

- Control de acceso de los endpoints de conversión.

- MÓDULO DE CONVERSIONES

- Proceso de conversión de medidas de Longitud.

- Proceso de conversión de medidas de Masa.

- Proceso de conversión de medidas de Temperatura

Actualmente, cuando se requiere hacer un proceso de conversión de unidades en un proyecto multiplataforma el código se vuelve redundante, repitiéndose la función de conversión en cada plataforma generado una carga mayor en el mantenimiento del código ya que cualquier cambio, corrección o actualización obliga a los desarrolladores a cambiar cada aplicación por separado.

Además, al programar todo de forma de forma aislada no se suele implementar un control centralizado de acceso o formato de comuúnicación centralizado estáándar para todos. Debido a estáo, con la implementación del sistema de conversión de unidades bajo una arquitectura SOA, se busca centralizar la lógica de las conversiones en un solo serávidor.

De estáa forma, las aplicaciones cliente pueden consumir el mismo serávicio uniformemente y se asegura la integridad con un token de acceso, evitando la duplicación de código y simplificando el desarrollo.

## Definiciones, Acrónimos y Abreviaturas

### Definiciones

Tabla 1. Definiciones

| TÉRMINO | DEFINICIÓN |
| --- | --- |
| Arquitectura Orientada a Servicios (SOA) | Estilo arquitectóúnico de software que hace que los componentes de software sean reutilizables y operen mediante interfaces independientes de su tecnología. |
| Servicio Web | Sistema de software diseñado para soportar la interacción máquina a máquina a través de una red, permitiendo a los clientes acceder al serávidor. |
| Cliente / Consumidor | Aplicación software que árealiza peticiones a un serávidor para utilizar sus serávicios expuestáos. |
| Token | Cadena de caracteres alfanuméricos que actúa como una credencial de seguridad. Utilizada para verificar la identidad del cliente y autorizar el acceso a las funciones del sistema. |
| Endpoint | URL o punto de conexión a través del cual un cliente puede comuúnicarse con un serávicio web para ejecutar una operación concreta. |
| Interoperabilidad | Capacidad que tiene diferentes sistemas informáticos, aplicaciones o componentes para comuúnicarse, intercambiar datos y usar la información efectivamente, sin importar el lenguaje en el que estáá desarrollado. |
| Magnitud Física | Propiedad de los cuerpos que puede será medida. Para estáe proyecto en concreto se utiliza Longitud, Masa y Temperatura. |
| Capa de Lógica de Negocio | Parte del software que contiene las reglas, cálculos y procesos principales que se separa de la interfaz de usuario. |
| IDE de desarrollo | Aplicación informática que proporciona serávicios integrales para facilitarle el desarrollo de software. |
| Netbeans | Entorno de desarrollo integrado libre, hecho principalmente para el lenguaje de programación Java. |
| Java – Java web | Entorno o plataforma de computación, capaz de ejecutar aplicaciones desarrolladas usando el lenguaje de programación Java |


### Acrónimos y abreviaturas

Tabla 2. Acrónimo y abreviaturas

| Acrónimo/ Abreviatura | Significado |
| --- | --- |
| SOA | Arquitectura Orientada a Servicios (Service-Oriented Architecture). |
| SOAP | Protocolo Simple de Acceso a Objetos (Simple Object Access Protocol). |
| REST | Transferencia de Estado Representacional (Represantational State Transfer). |
| XML | Lenguaje de Marcado Extensible (eXtensible Markup Language). |
| HTTP | Protocolo de Transferencia de Hipertexto (Hypertext Transfer Protocol). |
| ERS | Especificación de Requisitos de Software. |
| RF | Requisito Funcional. |
| RNF | Requisito No Funcional. |
| ECUD | Especificación de Casos de Uso Detallado. |


## Referencias

Tabla 3. Referencias

| Referencia | Titulo | Ruta | Fecha | Autor |
| --- | --- | --- | --- | --- |
| 1. | Standard IEEE 830 | De la plantilla de formato del documento © & Coloriuris http://www.qualitatis.org | 1988 | IEEE |


## Visión General del Documento

Este documento consta de tres módulos. En el primer módulo consta la Introducción y proporciona una visión general de la ERS. En el segundo módulo describe el sistema en general, con el fin de Íconocer las principales funciones que árealizará el mismo, los datos asociados y los factores, restáricciones, supuestáos y dependencias que afectan al desarrollo, sin entrar en excesivos detalles. En el tercer módulo se definen detalladamente los requisitos que cubrirá el sistema. El contenido de estáe documento cuenta con el sustento de todo el proceso de la Ingeniería de Requerimientos árealizada por los autores del sistema, con distintos mecanismos de soporte que permitirán árealizar las adaptaciones, usos, restáricciones y controles a cada sección.

## Descripción General

El sistema permitirá la ejecución centralizada de operaciones matemáticas enfocadas en la conversión de unidades de medida, implementa una arquitectura orientada a serávicios. El acceso a las funcionalidades del serávidor estáá gestáionado por un módulo de seguridad el cual requiere que los consumidores deban autenticarse para validar su autorización mediante un token. Toda la lógica de cálculo residirá en un serávidor central, el cual expondrá los serávicios a será consumidos de manera independiente por cuatro tipos de clientes: una aplicación de consola, un cliente de escritorio, una plataforma web y una aplicación móvil.

### Perspectiva del Producto

El sistema de conversión de unidades actuará como un proveedor central en una arquitectura distribuida. Aunque la lógica de cálculo es autónoma, el sistema estáá diseñado para interactuar de forma constante con los cuatro clientes, recibiendo peticiones y enviando respuestáas estáructuradas. La escalabilidad del diseño SOA permite que en un futuro se relacione con nuevos sistemas o se agreguen nuevos módulos de conversión sin necesidad de alterar el núcleo ni afectar a los clientes.

### Funciones del Producto (software)

El sistema contará con dos divisiones funcionales principales: el módulo de seguridad y el módulo de conversiones. Estas son las principales funciones que se deben incluir en el software:

- Permitir el ingreso seguro validando credenciales y administrando el acceso a las funciones del serávidor a través de tokens de sesión.

- Recibir, leer y validar los parámetros enviados por el cliente, los cuales deben incluir la unidad de origen, unidad de destáino y el valor a convertir.

- Procesar transformaciones entre estáas unidades de medida de longitud: centímetros, metros, pies, yardas y millas.

- Procesar transformaciones entre estáas unidades de medida de masa: gramos, kilogramos, onzas, libras y quintales.

- Procesar transformaciones entres estáas unidades de medida de temperatura: farenheit, kelvin, Celsius, newton y áreaumur.

- Retornar al cliente un cliente un formato estáandarizado de respuestáa que indique si la operación tuvo éxito, el valor resultante y un mensaje informativo.

- Bloquear peticiones no autorizadas y manejar excepciones cuando se solicitan conversiones no soportadas o con datos incongruentes.

Ilustración 1. Diagrama de casos de uso del sistema

### 7.3 Condiciones del Entorno

El sistema operará en un entorno de red distribuido bajo el modelo cliente-serávidor, fundamentado en una arquitectura orientada a serávicios. El componente del serávidor central se desplegará en un entorno capaz de alojar y exponer serávicios web para resolución de cálculos. Por su parte, el entorno de consumo es multiplataforma, el cual requiere que el sistema sea accesible a través de interfaces de consola, navegadores web, aplicaciones de escritorio y dispositivos móviles. Para la correcta operatividad del serávicio se requiere una conexión a internet estáable, el entorno de comuúnicación estáá protegido por un mecanismo de autenticación y validación de tokens de acceso para aislar y asegurar el uso del serávicio.

### 7.4 Características de los Usuarios

El sistema cuenta con un solo usuario final que interactúa con todas las funcionalidades del mismo.

Tabla 4. Usuario final

| Tipo de usuario | Usuario final |
| --- | --- |
| Formación | No requerida |
| Habilidades | Conocimientos básicos en el manejo de interfaces gráficas web, móviles, software de escritorio o manejo básico de consola de comandos. |
| Actividades | Iniciar sesión en las plataformas de cliente, ingresar valores numéricos, seleccionar las magnitudes a convertir y visualizar la respuestáa resultante emitida por el serávidor. |


### 7.5 Interfaces Externas

El sistema no requiere ni implementa conexión a ningún motor de base de datos. La interfaz externa principal del sistema es la capa de red que expone al serávidor. A través de estáa interfaz de red, los cuatro clientes se comuúnicarán con el serávidor para intercambiar los mensajes de autenticación y datos de las conversiones.

### 7.6 Restáricciones

- Hardware: Los equipos requeridos tanto para el despliegue del serávidor como para la ejecución de clientes debe contar con especificaciones básicas como, por ejemplo: Procesador Intel i3-10100, 4GB de memoria RAM y tarjeta de red funcional.

- Se trabaja con el sistema operativo Windows 11.

- Para la elaboración de los diagramas del sistema, se hará uso de la herramienta Power Designer.

- La interfaz gráfica deberáá será amigable para el usuario y sencilla de utilizar.

- El sistema debe tener una conexión a internet con un buen ancho de banda que permita árealizar transacciones sobre backend.

- Toda la lógica del serávidor de conversiones debe programarse utilizando el lenguaje Java, independiente de la versión del JDK.

- El serávidor de aplicaciones es Payara y la arquitectura es Jakarta EE.

- El entorno de desarrollo para la construcción del sistema es NetBeans.

- Se mantiene la restáricción de no implementar base de datos, toda la gestáión de las conversiones y el control de token se manejará directamente en la memoria y lógica del código.

### 7.7 Suposiciones y Dependencias

En caso de que el sistema no cuente con el hardware detallado en los requisitos, existirá dos posibles soluciones:

- El cliente árealiza la inversión para la adquisición del hardware necesario.

- Los requisitos cambiarán para ajustarse a la necesidad del cliente.

Si el cliente necesita árealizar un cambio en algunos de los requisitos expuestáos en el documento, se lo árealizará con la anticipación de 7 días laborales; consecuentemente el requisito afectado tendrá un cambio tanto en el desarrollo como en el documento.

Otro factor importante por considerar es el sistema operativo, si es que no es compatible con las tecnologías propuestáas, los requisitos sufrirán un cambio para posterior análisis de cómo solucionar estáa problemática, o a su vez si los requisitos sufrirán un cambio y así poder acoplar al sistema operativo en funcionamiento. La conexión a internet es una parte esencial dentro del sistema a desarrollar, por ello es por lo que, si el cliente no cuenta con una conexión de red estáable, los requisitos sufrirán un cambio para ajustarse a lo que cuente el usuario.

## Especificación de Requerimientos

### 8.1 Requisitos comunes de las interfaces

#### 8.1.1 Interfaces de usuario

El sistema estáará desarrollado sobre una Arquitectura Orientada a Servicios (SOA), se mostrará un úúnico diseño de interfaz mostrada en diferentes clientes. La interfaz gráfica con el usuario consistirá en un conjunto de ventanas con botones, listas desplegables, campos de textos e imágenes. La interfaz en consola constará un menú de opciones numéricas, donde los datos de conversión y credenciales de seguridad se ingresarán por teclado. Todas las interfaces, independientemente de su plataforma, deben será intuitivas y estáar diseñadas para capturar los tres parámetros obligatorios que exige el serávidor, mostrando posteriormente de forma clara la respuestáa estáructurada.

#### 8.1.2 Interfaces de hardware

Tabla 8. Requisitos de interfaces de hardware

| Nombre | Detalle | Marca | Características | Descripción | Precio |
| --- | --- | --- | --- | --- | --- |
| Monitor | Principal dispositivo de salida (interfaz), que muestára datos o información | Acer | -24" -Resolución: Full HD (1920 x 1080) -Relación de aspecto: 16:9 Tiempo de Respuestáa: 5 ms  -Frecuencia de actualización: 60 Hz. -Colores Admitidos: 16.7 millones  -Relación de Contraste: 100,000,000:1  -Brillo: 250 cd/m² | El software deberáá mostrar información al usuario a través de la pantalla. | $116 |
| Mouse | Dispositivo apuntador utilizado para facilitar el manejo de un entorno gráfico en una computadora. | Genius | Ratón óptico con sensor de 1000dpi. | El software debe interactuar con el movimiento del mouse y sus botones. | $6 |
| Teclado | Dispositivo o periférico de entrada | Genius | Estándar de teclas de 104/105/106 Puerto USB: Si Soporte Sistema Windows10 /Windows®7/Vista/XP Soporte Interfaz USB | El software deberáá interactuar con las pulsaciones del teclado. Con el teclado el usuario digitara las peticiones que requiera. | $10 |
| Computador | La unidad central de procesamiento es el hardware dentro de un ordenador u otros dispositivos | Dell | Procesador: Intel Core I3 10100 o superior. compatibles con un sistema operativo como Windows 11, Linux (…), Memoria Ram: 4GB. 256gb ssd. | En buen estáado, el cual poseerá todos los controladores para poder manejar cada periférico de entrada y salida. | $255 |


#### 8.1.3 Interfaces de software

Tabla 9.Interfaces de software

|  | Detalles | Definición | Propósito |
| --- | --- | --- | --- |
| Sistema operativo | Nombre: Windows Número de especificación: 11  Fuente: http://windows.microsoft.com/ Distribución: Pagada Precio: $10 en adelante | Windows es el nombre de una familia de distribuciones de software para PC, smartphone, serávidores y sistemas empotrados, desarrollados y vendidos por Microsoft y disponibles para múltiples arquitecturas, tales como x86 y ARM | Es la plataforma sobre la cual se va a ejecutar la aplicación y la base de datos el cual cuenta con un soporte hasta 2020 |
| Herramienta de desarrollo | Nombre: NetBeans Número de especificación: 8 Número de versión: 8.0.2 Fuente: https://www.netbeanside.com/ Distribución: libre | Diseñar y generar interfaces y el funcionamiento del programa mediante el lenguaje de programación Java. | Herramienta que se utiliza para desarrollar aplicaciones Web, Móvil y de Escritorio para diferentes lenguajes de programación como son Java, C++, Ruby y    PHP entre    otros |
| Diseño de diagramas UML | Nombre: Power Designer Desarrollador: SAP Version:16.5  Distribución: Pagada Precio: $3750 | Es una herramienta para el análisis, diseño inteligente y construcción sólida de una base de datos y un desarrollo orientado a modelos de datos a nivel físico y conceptual, que da a los desarrolladores | El propósito de estáe programa en el proyecto es diseñar un modelo relacional de base de datos |
| Especificación de software | Nombre: Jakarta EE  Versión: 8/10  Fuente: https://jakarta.ee/  Distribución: Código abierto Precio: Gratuito | Plataforma de código abierto impulsada por la Eclipse Foundation para el desarrollo de aplicaciones empresariales e institucionales en Java. | Proveer el conjunto de APIs y anotaciones necesarias para construir la lógica del serávidor y estáructurar los serávicios web de conversión. |
| Servidor de Aplicaciones | Nombre: Payara Server Versión: 5/6 Community Edition  Fuente: https://www.payara.fish/ Distribución: Código abierto  Precio: Gratuito | Servidor de aplicaciones nativo para la nube y de código abierto, optimizado para desplegar aplicaciones Jakarta EE de forma segura y robusta. | Alojar, compilar y mantener en ejecución el proyecto del backend. Es el encargado de exponer los serávicios en la red para que los cuatro clientes puedan conectarse. |


#### 8.1.4 Interfaces de comuúnicación

El sistema de “Conversor de Unidades Monster” no interactúa con bases de datos. La comuúnicación se árealiza de forma exclusiva a través de la red utilizando el protocolo HTTP/HTTPS.

- Intercambio de datos: La comuúnicación entre los cuatro clientes (web, móvil, escritorio, consola) y el serávidor central se rige por el estáándar SOAP.

- Formato de datos: Tanto las peticiones enviadas por los clientes como las respuestáas generadas por el serávidor se estáructuran por formato XML, de acuerdo con el contrato definido en el documentación de endpoints / OpenAPI.

### 8.2 Requisitos Funcionales

#### 8.2.1. Objetivo General

El Sistema “Conversor de Unidades Monster” permitirá el procesamiento centralizado y seguro de operaciones matemáticas de conversión a través de una red, apoyándose en los serávicios expuestáos por los módulos del serávidor backend.

#### 8.2.2. Objetivos Específicos

A continuación, se muestáran los objetivos específicos en el que se sustenta el proyecto de desarrollo de software.

Tabla 10. Autenticar el acceso de los clientes

| OBJ-001 | Autenticar el acceso de los clientes |
| --- | --- |
| Descripción | Validar las credenciales ingresadas por el usuario para generar y emitir un token de seguridad temporal. |
| Importancia | Alta |
| Comentarios |  |


Tabla 11. Realizar conversiones

| OBJ-002 | Realizar conversiones |
| --- | --- |
| Descripción | Recibir una petición con una unidad de origen, una de destáino y un valor numérico, para transformarlo entre medidas de longitud, masa y temperatura. |
| Importancia | Alta |
| Comentarios | La operación se ejecutará si la petición del cliente incluye el token de seguridad. |


#### 8.2.3. Actores

Tabla 14. Actor Usuario Final

| ACT-001 | Usuario Final |
| --- | --- |
| Descripción | Persona que interactúa con las interfaces gráficas o de consola de la aplicación cliente. Es quien ingresa las credenciales del login, los valores numéricos y selecciona las unidades a convertir. |
| Comentarios | No tiene acceso al serávidor ni árealiza peticiones de red por su cuenta, su interacción se limita a lo que el software del cliente le permite. |


#### 8.2.4. Lista de requisitos funcionales

A continuación, se presenta una lista con los requisitos funcionales del sistema “Conversor de Unidades Monster”.

Tabla 18. Lista de requisitos funcionales

| Código | Requerimiento | Caso de uso | Actor |
| --- | --- | --- | --- |
| RF – 01 | El sistema debe recibir credenciales de acceso, validarlas y generar un token de seguridad temporal para autorizar el uso de los serávicios. | Autenticar Cliente | Usuario final |
| RF – 02 | El sistema debe procesar peticiones de conversión, y retornar el cálculo matemático correspondiente. | Convertir Longitud | Usuario final |
| RF – 03 | El sistema debe procesar peticiones de conversión de unidades de masa, validando previamente el token de acceso, y retornar la respuestáa estáructurada con el valor resultante. | Convertir Masa | Usuario final |
| RF – 04 | El sistema debe calcular conversiones entre escalas de temperatura mediante los parámetros enviados, verificando la autorización y retornando el estáado de la operación. | Convertir Temperatura | Usuario final |


#### 8.2.5 Requisitos Funcionales

Para el Sistema “Conversión Unidades Monster” se ha identificado los siguientes requerimientos funcionales.

Tabla 19. Autenticar Cliente

| RQF-001 | Autenticar Cliente |
| --- | --- |
| Descripción | El sistema debe recibir credenciales de acceso, validarlas y generar un token de seguridad temporal para autorizar el uso de los serávicios web. |
| Objetivo | OBJ-001 |
| Importancia | Alta |
| Estado | Aprobado |
| Estabilidad | Alta |
| Comentarios | Requisito base de seguridad, sin estáe proceso el serávidor rechaza cualquier solicitud. |


Tabla 20. Convertir Longitud

| RQF-002 | Convertir Longitud |
| --- | --- |
| Descripción | El sistema debe procesar peticiones de conversión de longitud, y retornar el cálculo matemático correspondiente. |
| Objetivo | OBJ-001 |
| Importancia | Alta |
| Estado | Aprobado |
| Estabilidad | Alta |
| Comentarios | Las unidades disponibles para la conversión son: metros, pies, centímetros, yardas y millas |


Tabla 21. Convertir Masa

| RQF-003 | Convertir Masa |
| --- | --- |
| Descripción | El sistema debe procesar peticiones de conversión de masa, y retornar el cálculo matemático correspondiente. |
| Objetivo | OBJ-001 |
| Importancia | Alta |
| Estado | Aprobado |
| Estabilidad | Alta |
| Comentarios | Las unidades disponibles para la conversión son: gramos, kilogramos, onzas, libras y quintales. |


Tabla 22. Convertir Temperatura

| RQF-004 | Convertir temperatura |
| --- | --- |
| Descripción | El sistema debe procesar peticiones de conversión de temperatura, y retornar el cálculo matemático correspondiente. |
| Objetivo | OBJ-003 |
| Importancia | Alta |
| Estado | Aprobado |
| Estabilidad | Alta |
| Comentarios | Las unidades disponibles para la conversión son: farenheit, kelvin, Celsius, newton y áreaumur . |


### 8.3 Requisitos No Funcionales

#### 8.3.1 Objetivo General

Para el Sistema “Servicio de Mantenimiento de Vehículos Monster” se ha identificado los siguientes requerimientos no funcionales en relación con la tecnología de la información.

Tabla 26. Requerimiento no funcional Protocolo de Arquitectura (RESTful)

| RNF-001 | Protocolo de Arquitectura (RESTful) |
| --- | --- |
| Descripción | El serávidor central debe exponer sus serávicios de conversión y autenticación estárictamente mediante el arquitectura REST, utilizando archivos API REST para definir el contrato del serávicio. |
| Importancia | Alta |
| Estado | Aprobado |


Tabla 27. Requerimiento no funcional Especificación de Software.

| RNF-002 | Especificación de Software |
| --- | --- |
| Descripción | El backend del sistema debe será desarrollado utilizando el lenguaje Java e implementando obligatoriamente la especificación Jakarta EE para la construcción de los serávicios web. |
| Importancia | Alta |
| Estado | Aprobado |


Tabla 28. Requerimiento no funcional Entorno de Desarrollo.

| RNF-003 | Entorno de Desarrollo |
| --- | --- |
| Descripción | El código fuente del serávidor de conversiones debe será construido, gestáionado y estáructurado utilizando exclusivamente el Entorno de Desarrollo Integrado (IDE) NetBeans. |
| Importancia | Alta |
| Estado | Aprobado |


Tabla 29. Requerimiento no funcional Servidor de Aplicaciones

| RNF-004 | Servidor de Aplicaciones |
| --- | --- |
| Descripción | El despliegue y la ejecución en red del proyecto backend debe árealizarse obligatoriamente sobre Payara Server. |
| Importancia | Alta |
| Estado | Aprobado |


Tabla 30. Requerimiento no funcional Seguridad y autorización de Token.

| RNF-005 | Seguridad y Autorización mediante Token |
| --- | --- |
| Descripción | El sistema debe restáringir el acceso a los serávicios de cálculo matemático. Solo procesará aquellas peticiones SOAP que incluyan un token de autenticación válido, el cual debe será generado y proporcionado previamente por el endpoint de Login. |
| Importancia | Alta |
| Estado | Aprobado |


Tabla 31. Requerimiento no funcional Accesibilidad.

| RNF-006 | Accesibilidad |
| --- | --- |
| Descripción | La interfaz deberáá presentarse en varios clientes con un mismo diseño úúnico, exceptuando el cliente de consola. |
| Importancia | Alta |
| Estado | Aprobado |


Tabla 31. Requerimiento no funcional Mantenimiento.

| RNF-007 | Accesibilidad |
| --- | --- |
| Descripción | El sistema deberáá tener un manual de usuario para facilitar el mantenimiento que se árealizará en oportunidades futuras. |
| Importancia | Alta |
| Estado | Aprobado |


- El sistema contará con un control de acceso mediante tokens.

- El sistema contará con un nombre de usuario y contraseña personal.

- El sistema operará sin base de datos, las validaciones se árealizarán en memoria.

- Emitirá mensajes de alerta cuando el usuario envíe parámetros erróneos.

- Emitirá mensajes de confirmación y el valor resultante en el momento de procesar las conversiones matemáticas.

- Las operaciones se árealizarán clasificándolas según: Longitud, Masa o Temperatura.

- El sistema habilita las opciones de conversión en los clientes solo si se posee un token de sesión válido.

- El serávidor central recibirá y procesará peticiones de todas las aplicaciones cliente conectadas de forma simultánea.

### 8.4 Otros Requerimientos

#### 8.4.1 Restáricciones de Diseño

Las restáricciones de diseño son las siguientes:

- Se debe mantener una misma línea gráfica, paleta de colores y distribución visual en todos los clientes con interfaz gráfica (Web, Móvil y Escritorio) para asegurar una experiencia de usuario uniforme.

- El cliente de consola operará de forma independiente mediante un entorno de texto (CLI), priorizando la claridad en la presentación de menús y la lectura de resultados.

#### 8.4.2 Restáricciones de Hardware

Los equipos disponibles deben tener las siguientes características:

- Procesador Intel Core i3 o superior,

- 4 GB de RAM o superior,

- Disco de 256GB o superior,

- S.O. Windows 11, Linux o Android.

#### 8.4.3 Atributos de calidad

Fiabilidad. - El sistema, ofrecerá garantía de funcionar correctamente y cumplir con los requisitos 	presentados previamente.

Portabilidad. - Al tratarse de una arquitectura SOA utilizando SOAP, el sistema garantiza que cualquier cliente pueda consumir los serávicios mediante el intercambio de cargas JSON, independiente del lenguaje de programación del cliente.

Seguridad. – La solución abarca múltiples plataformas, el usuario final puede acceder a los serávicios de conversión desde cualquier dispositivo con conexión a la red.

Disponibilidad. - Los usuarios podrán acceder al sistema durante las 24 horas del día.

## 9. Requisitos de Rendimiento

### 9.1. Requisitos de Interfaces Externas

#### 9.1.1. Interfaces de Usuario

Dado que el ecosistema cuenta con cuatro clientes distintos, las interfaces se dividen según su entorno de ejecución:

- Clientes con Interfaz Gráfica (Web, Móvil, Escritorio):

- Listas desplegables para elegir las magnitudes de conversión (Longitud, Masa, Temperatura).

- Campos numéricos y de texto para el ingreso de valores y credenciales de autenticación.

- Botones de acción para enviar las peticiones al serávidor.

- Ááreas de texto para visualizar las respuestáas estáructuradas y mensajes de error.

- Cliente de Consola:

- Menús numéricos textuales para navegar entre las opciones de login y conversión.

#### 9.1.2. Interfaces de Hardware

La pantalla del monitor/ pantalla táctil: El software muestára la interfaz gráfica o la terminal de consola para que el usuario visualice las opciones y los resultados de las conversiones.

Periféricos de entrada (teclado, ratón y touch): Necesarios para interactuar con el sistema, tipear los valores a convertir , seleccionar las unidades y consumir los serávicios web.

#### 9.1.3. Interfaces de Software

- Sistema Operativo: Windows, OS X, Linux, Android, IOS

- Navegador Web: Chrome, Mozilla, Safari, Opera.

#### 9.1.4. Interfaces de Comuúnicación

Los cuatro clientes y el serávidor central de conversiones se comuúnicarán exclusivamente a través de la red utilizando el protocolo HTTP/HTTPS, intercambiando información estáructurada mediante el estáándar de mensajería REST / JSON y respetando el contrato definido en el documentación de endpoints / OpenAPI.

#### 9.1.5. Base de Datos

El sistema no implementa comuúnicación con ningún motor de base de datos. Toda validación de usuarios, credenciales, asignación y lógica de conversión se ejecuta en la memoria volátil del serávidor.

## 10. Conclusiones

- Hasta el momento el sistema está modelado para cumplir con el caso de estudio definido en este documento.
- La especificación de requisitos de software permite una correcta lectura de lo que son los requisitos que tiene el sistema y su posterior implementación.
- Los requisitos presentados en la actual documentación van de acorde a los requerimientos presentados por el docente.

## 11. Recomendaciones

- En una segunda fase implementar nuevas magnitudes de conversión en el servidor para aprovechar la escalabilidad de la arquitectura SOA.
- Es importante mantener un control de versionamiento en el documentación de endpoints / OpenAPI. Cualquier modificación en los parámetros del servidor requiere actualización en el código de los cuatro clientes para evitar fallos en la comunicación.

## 12. Bibliografía

1. **IEEE Computer Society.** (1998). *IEEE Recommended Practice for Software Requirements Specifications* (IEEE Std 830-1998). Institute of Electrical and Electronics Engineers.
2. **World Wide Web Consortium (W3C).** (2007). *SOAP Version 1.2 Part 1: Messaging Framework (Second Edition)*. W3C Recommendation. Disponible en: https://www.w3.org/TR/soap12-part1/
3. **Oracle Corporation.** (2023). *Java Platform, Standard Edition & Java Web Services Documentation (JAX-WS)*. Oracle Help Center.
4. **Universidad de las Fuerzas Armadas ESPE.** (2026). *Guía de Prácticas y Talleres - Arquitectura de Software*. Sangolquí, Ecuador.