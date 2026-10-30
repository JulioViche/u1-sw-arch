---
tipo: manual-tecnico
protocolo: RESTful
plataforma: .NET
tarea: TI.1.4
tags: [manual-tecnico, sinbdd, rest, dotnet, webapi, kestrel, capturas]
---

# Manual Técnico - Conversor de Unidades RESTful en .NET (SINBDD)

**Universidad de las Fuerzas Armadas ESPE — Sede Latacunga**  
**Departamento de Ciencias de la Computación**  
**Asignatura:** Arquitectura de Software | **NRC:** 36884 | **Grupo:** GR09  
**Autores:** Julio Enrique Viche Castillo, Gabriel Omar Murillo Medina  
**Docente:** Ing. Eduardo Mauricio Campaña Ortega, MIS.MDU.CCNA.CCIA. PhD. (c)  
**Fecha:** 28 de Octubre de 2026

---

## Pila tecnológica implementada (fuente vigente)

| Componente | Tecnología comprobada |
| --- | --- |
| Servidor REST | C# 14 y .NET 10 (`net10.0`), ASP.NET Core Web API, controladores y Kestrel. JSON se procesa con `System.Text.Json`; `Microsoft.AspNetCore.OpenApi` 10.0.12 publica OpenAPI en Development. Escucha por TCP 5000 en todas las interfaces. |
| Consola | C# y .NET 10 (`net10.0`), cliente HTTP/JSON. |
| Escritorio | C# y .NET 10 (`net10.0-windows`) con WPF. |
| Web | C# y .NET 10 (`net10.0`), cliente ASP.NET Core. |
| Móvil Android | .NET MAUI 10, C# 14, XAML, `HttpClient`, `System.Net.Http.Json` y `System.Text.Json`; destino `net10.0-android`, API mínima 21. La compilación revisada usó .NET SDK 10.0.402, workload MAUI 10.0.110, Android SDK 36 y JDK 17 como herramienta del build Android. El dispositivo usa `192.168.137.1:5000`; el emulador Android, `10.0.2.2:5000`. El endpoint queda definido en el cliente y no se solicita una URL en la interfaz. |
| Persistencia | SINBDD: credenciales, token y catálogos en memoria. |

Esta tabla describe la implementación vigente del repositorio. Las capturas o ejemplos históricos que mencionen Java, Maven, Swing, Jersey, Jetty o puertos distintos no describen estos proyectos .NET y no son instrucciones de compilación.

## PARTE PRÁCTICA

### INTRODUCCIÓN

REST es un estilo de arquitectura que organiza la funcionalidad en recursos identificados por rutas HTTP. Este proyecto intercambia solicitudes y respuestas JSON para autenticar usuarios y convertir longitud, masa y temperatura.

El servidor REST implementa recursos con ASP.NET Core Web API (Kestrel). Los clientes envían solicitudes HTTP a rutas definidas y reciben respuestas JSON; Kestrel proporciona el servidor HTTP embebido.

ServidorRestMain inicia ASP.NET Core sobre Kestrel (.NET 10) en el puerto 8080 y usa 8085 si el puerto preferido está ocupado. Jackson gestiona JSON; el servicio se ejecuta desde 04. SERVIDOR con dotnet run.

### OBJETIVOS

#### OBJETIVO GENERAL

Documentar la arquitectura, configuración y uso de un conversor de unidades con autenticación previa, clientes Java de consola, escritorio, web y móvil, y una API REST implementada con Jersey y Kestrel. El sistema convierte longitud, masa y temperatura y opera sin base de datos (SINBDD).

#### OBJETIVOS ESPECÍFICOS

Describir las rutas /health, /auth/login y /conversor/longitud, /masa y /temperatura, los métodos HTTP utilizados y los modelos JSON de solicitud y respuesta.

Explicar cómo los clientes Java de consola, escritorio, web y móvil envían solicitudes HTTP/JSON, realizan el inicio de sesión y usan el token antes de convertir.

Explicar la organización de recursos REST, los métodos HTTP y el intercambio de modelos JSON con los clientes.

### INSTALACIÓN Y CONFIGURACIÓN DE HERRAMIENTAS

Para dar inicio al desarrollo de la práctica, primero se deben instalar y configurar las herramientas esenciales. Para este proyectó se utilizó Windows 11.

#### INSTALACIÓN Y CONFIGURACIÓN DE APACHE NETBEANS 24

El proyecto REST requiere .NET 10 SDK. Verifica Java con dotnet --version y Maven con dotnet --info desde PowerShell o la terminal integrada de Visual Studio.

| Java --version |
| --- |

![Captura](imagenes/image2.png)

Figura 3. Versión de .NET 10 SDK en el símbolo del sistema

Para la instalación de la herramienta se accede a la página oficial de descarga de Apache https://netbeans.apache.org/front/main/download/ se selecciona la opción ‘Older Releases’, ‘Visual Studio 2022 / VS Code’ y la opción para el sistema operativo.

![Captura](imagenes/image3.png)

Figura 4. Página oficial de descarga de Visual Studio 2022

Una vez seleccionada la opción para el sistema operativo, se inicia la descarga y se obtendrá el siguiente ejecutable.

![Captura](imagenes/image4.png)

Figura 5. Instalador Visual Studio 2022 / VS Code

Al ejecutar el instalador se muestra la siguiente pantalla, en el proceso de instalación se aceptan los términos y condiciones, se selecciona la ubicación de la descarga y se finaliza la instalación.

![Captura](imagenes/image5.png)

Figura 6. Pantalla inicial de instalación Visual Studio 24

Una vez finalizada la descarga se ejecuta la aplicación y se iniciará Visual Studio 2022 / VS Code.

![Captura](imagenes/image6.png)

Figura 7. Aplicación Visual Studio 2022 / VS Code

#### INSTALACIÓN Y CONFIGURACIÓN DE VISUAL STUDIO 2022 Y .NET MAUI

Para el desarrollo del cliente móvil multiplataforma se utiliza .NET MAUI (Multi-platform App UI). Se accede al instalador de Visual Studio 2022 y se selecciona la carga de trabajo "Desarrollo de .NET Multi-platform App UI" (.NET MAUI), la cual incluye el SDK de Android, herramientas de emulación y plantillas base.

![Captura](imagenes/image7.png)

Figura 8. Instalación de la carga de trabajo .NET MAUI en Visual Studio

Al finalizar la instalación se obtienen las herramientas del SDK de Android integradas y el compilador cruzado para generar el paquete APK/AAB correspondiente.

![Captura](imagenes/image8.png)

Figura 9. Componentes y emulador Android configurados

Al abrir Visual Studio se puede verificar la disponibilidad de plantillas de proyecto .NET MAUI:

![Captura](imagenes/image9.png)

Figura 10. Plantilla de aplicación .NET MAUI en Visual Studio

La configuración permite compilar nativamente la lógica compartida en C# y renderizar controles nativos en Android mediante XAML.

![Captura](imagenes/image10.png)

Figura 11. Entorno de desarrollo para .NET MAUI

### ESTRUCTURA DE PROYECTO

Para la elaboración del proyecto, en primer lugar, es necesario crear una carpeta en la cual se alojarán los archivos necesarios para su funcionamiento.

![Captura](imagenes/image11.png)

Figura 1. Creación de la carpeta raíz

Dentro de la carpeta principal del proyecto, se debe ubicar la siguiente subestructura.

![Captura](imagenes/image12.png)

Figura 2. Estructura inicial del proyecto

Todo esto con la finalidad de llevar buenas prácticas y mantener organizado el proyecto.

## APLICACIONES SERVIDOR

### CREACIÓN DEL PROYECTO SERVIDOR

El servidor es un proyecto Java con Maven. En Visual Studio abre la carpeta 04. SERVIDOR, revisa 04. SERVIDOR.csproj y ejecuta Monster.Server.ServidorRestMain mediante dotnet run; Maven descarga Jersey, Kestrel y Jackson según el 04. SERVIDOR.csproj.

![Captura](imagenes/image13.png)

Figura 12. Creación del proyecto – SERVIDOR RESTFUL

![Captura](imagenes/image14.png)

Figura 13. Configuración del proyecto 1 - SERVIDOR RESTFUL

![Captura](imagenes/image15.png)

Figura 14. Configuración del proyecto 2 - SERVIDOR RESTFUL

### CODIFICACIÓN DEL PROYECTO SERVIDOR

#### CREACIÓN DE PAQUETES

El servidor separa la configuración Jersey, los controladores REST, los modelos de intercambio y los servicios de conversión. Los paquetes reales son Monster.config, Monster.controlador, Monster.modelo, Monster.servicio y Monster.Server; no hay base de datos.

![Captura](imagenes/image16.png)

Figura 15. Estructura de paquetes - SERVIDOR RESTFUL

#### CREACIÓN DE RECURSOS REST

AuthController expone el inicio de sesión. ConversorController publica las operaciones HTTP de longitud, masa y temperatura; las clases de servicio realizan los cálculos y el servidor serializa las respuestas como JSON.

Los recursos usan @Path, @POST, @Consumes y @Produces. AuthController valida MONSTER / MONSTER9 y crea un token en memoria; ConversorController verifica el token antes de ejecutar una conversión.

![Captura](imagenes/image17.png)

Figura 16. Estructura de paquetes - controladores REST

Tabla 1. Método login - AuthController.cs

public Response login(Conversion modelo) {

if (modelo == null) {

Conversion error = new Conversion();

error.setSuccess(false);

error.setMessage("Cuerpo de solicitud requerido.");

return Response.status(Response.Status.BAD_REQUEST).entity(error).build();

}

String usuario = modelo.getFrom();

String clave = modelo.getTo();

String token = authService.validarLogin(usuario, clave);

if (token != null) {

modelo.setToken(token);

modelo.setSuccess(true);

modelo.setMessage("Autenticación satisfactoria. Bienvenido al sistema.");

return Response.ok(modelo).build();

} else {

modelo.setSuccess(false);

modelo.setMessage("Usuario o contraseña incorrectos. Credenciales válidas: MONSTER / MONSTER9");

return Response.status(Response.Status.UNAUTHORIZED).entity(modelo).build();

}

}

Tabla 2. Método convertirLongitud - ConversorController.cs

public Response convertirLongitud(Conversion modelo) {

if (modelo == null) {

return respuestaError(Response.Status.BAD_REQUEST, "Cuerpo de solicitud requerido.");

}

if (!authService.esTokenValido(modelo.getToken())) {

modelo.setSuccess(false);

modelo.setMessage("Sesión inválida o expirada. Debe iniciar sesión primero.");

return Response.status(Response.Status.UNAUTHORIZED).entity(modelo).build();

}

try {

double resultado = longitudService.convertirLongitud(modelo.getValue(), modelo.getFrom(), modelo.getTo());

double resultadoRedondeado = redondear2Decimales(resultado);

modelo.setResult(resultadoRedondeado);

modelo.setSuccess(true);

modelo.setMessage("Conversión de longitud realizada exitosamente.");

return Response.ok(modelo).build();

} catch (IllegalArgumentException e) {

modelo.setSuccess(false);

modelo.setMessage(e.getMessage());

return Response.status(Response.Status.BAD_REQUEST).entity(modelo).build();

}

}

Tabla 3. Método convertirLongitud - ConversorLongitud.cs

public double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) {

if (unidadOrigen == null || unidadDestino == null) {

throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");

}

String origen = normalizar(unidadOrigen);

String destino = normalizar(unidadDestino);

double valorEnMetros;

// 1. Paso a Metros (unidad base)

switch (origen) {

case "centimetros":

case "centimetro":

case "cm":

valorEnMetros = valor / 100.0;

break;

case "metros":

case "metro":

case "m":

valorEnMetros = valor;

break;

case "pies":

case "pie":

case "ft":

valorEnMetros = valor * 0.3048;

break;

case "yardas":

case "yarda":

case "yd":

valorEnMetros = valor * 0.9144;

break;

case "millas":

case "milla":

case "mi":

valorEnMetros = valor * 1609.34;

break;

default:

throw new IllegalArgumentException("Unidad de origen no reconocida en catálogo de longitud: " + unidadOrigen);

}

// 2. Paso de Metros a Destino

switch (destino) {

case "centimetros":

case "centimetro":

case "cm":

return valorEnMetros * 100.0;

case "metros":

case "metro":

case "m":

return valorEnMetros;

case "pies":

case "pie":

case "ft":

return valorEnMetros / 0.3048;

case "yardas":

case "yarda":

case "yd":

return valorEnMetros / 0.9144;

case "millas":

case "milla":

case "mi":

return valorEnMetros / 1609.34;

default:

throw new IllegalArgumentException("Unidad de destino no reconocida en catálogo de longitud: " + unidadDestino);

}

}

Tabla 4. Configuración del Servidor Kestrel - Program.cs

| var builder = WebApplication.CreateBuilder(args); builder.WebHost.ConfigureKestrel(options => { options.ListenAnyIP(5000); }); builder.Services.AddSingleton<AdministradorTokens>(); builder.Services.AddSingleton<ServicioAutenticacion>(); builder.Services.AddSingleton<ServicioConversion>(); builder.Services.AddControllers(); builder.Services.AddCors(options => { options.AddPolicy("PermitirTodo", policy => { policy.AllowAnyOrigin().AllowAnyHeader().AllowAnyMethod(); }); }); var app = builder.Build(); app.UseCors("PermitirTodo"); app.UseRouting(); app.MapControllers(); app.Run(); |
| --- |



#### CREACIÓN DE SERVICIO

En esta sección se implementa la "capa de servicio", que es el cerebro de la aplicación. Aquí se separa la lógica matemática y de seguridad del protocolo de comunicación. Al no contar con una base de datos, toda la gestión de datos se realiza en memoria, optimizando la velocidad de respuesta.

Servicio de Seguridad: Encargado de la validación de credenciales "hardcoded" y la generación/verificación de tokens de sesión para proteger los puntos de entrada del servidor.

Servicio de Conversiones: Contiene los algoritmos y factores de conversión para las magnitudes de longitud, masa y temperatura. Está diseñado para ser escalable, permitiendo añadir nuevas unidades sin afectar la estructura principal.

![Captura](imagenes/image18.png)

Figura 17. Estructura de paquetes - servicios

Tabla 5. Método ConvertirLongitud - ConversorLongitud.cs (.NET 10)

| public double Convertir(double valor, UnidadLongitud origen, UnidadLongitud destino) { double metros = origen switch { UnidadLongitud.Metro => valor, UnidadLongitud.Centimetro => valor / 100.0, UnidadLongitud.Pie => valor * 0.3048, UnidadLongitud.Yarda => valor * 0.9144, UnidadLongitud.Milla => valor * 1609.34, _ => throw new ArgumentOutOfRangeException(nameof(origen)) }; return destino switch { UnidadLongitud.Metro => metros, UnidadLongitud.Centimetro => metros * 100.0, UnidadLongitud.Pie => metros / 0.3048, UnidadLongitud.Yarda => metros / 0.9144, UnidadLongitud.Milla => metros / 1609.34, _ => throw new ArgumentOutOfRangeException(nameof(destino)) }; } |
| --- |

Tabla 6. Método ConvertirMasa - ConversorMasa.cs (.NET 10)

| public double Convertir(double valor, UnidadMasa origen, UnidadMasa destino) { double gramos = origen switch { UnidadMasa.Gramo => valor, UnidadMasa.Kilogramo => valor * 1000.0, UnidadMasa.Libra => valor * 453.59237, UnidadMasa.Onza => valor * 28.349523125, UnidadMasa.Quintal => valor * 100000.0, _ => throw new ArgumentOutOfRangeException(nameof(origen)) }; return destino switch { UnidadMasa.Gramo => gramos, UnidadMasa.Kilogramo => gramos / 1000.0, UnidadMasa.Libra => gramos / 453.59237, UnidadMasa.Onza => gramos / 28.349523125, UnidadMasa.Quintal => gramos / 100000.0, _ => throw new ArgumentOutOfRangeException(nameof(destino)) }; } |
| --- |

Tabla 7. Método ConvertirTemperatura - ConversorTemperatura.cs (.NET 10)

| public double Convertir(double valor, UnidadTemperatura origen, UnidadTemperatura destino) { double celsius = origen switch { UnidadTemperatura.Celsius => valor, UnidadTemperatura.Fahrenheit => (valor - 32.0) * (5.0 / 9.0), UnidadTemperatura.Kelvin => valor - 273.15, UnidadTemperatura.Newton => valor * (100.0 / 33.0), UnidadTemperatura.Reaumur => valor * 1.25, _ => throw new ArgumentOutOfRangeException(nameof(origen)) }; return destino switch { UnidadTemperatura.Celsius => celsius, UnidadTemperatura.Fahrenheit => (celsius * (9.0 / 5.0)) + 32.0, UnidadTemperatura.Kelvin => celsius + 273.15, UnidadTemperatura.Newton => celsius * (33.0 / 100.0), UnidadTemperatura.Reaumur => celsius * 0.8, _ => throw new ArgumentOutOfRangeException(nameof(destino)) }; } |
| --- |

### PRUEBAS DE LOS SERVICIOS

Comprueba el servidor en http://localhost:8080/api/health; usa el mismo contexto para POST auth/login y POST conversor/longitud, conversor/masa y conversor/temperatura. Si 8080 está ocupado, ServidorRestMain usa 8085.

La API publica health para comprobar disponibilidad, auth/login para autenticación y conversor/longitud, conversor/masa y conversor/temperatura para las conversiones. Todas las rutas usan el contexto de aplicación indicado en ServidorRestMain.

WSLogin: Punto de entrada para el módulo de seguridad. Recibe la petición del cliente y procesa la validación de credenciales para permitir o denegar el acceso a las funciones del servidor.

WSLongitud: Expone las operaciones matemáticas para las conversiones de medidas de longitud soportadas por el sistema (centímetros, metros, pies, yardas y millas).

WSMasa: Se encarga de recibir los parámetros y retornar las transformaciones entre las distintas unidades de masa (gramos, kilogramos, onzas, libras y quintales).

WSTemperatura: Maneja las peticiones referentes a las variaciones térmicas, permitiendo convertir entre Fahrenheit, Kelvin, Celsius, Newton y Réaumur.

Consulta la ruta health y envía solicitudes JSON a auth/login y a las rutas conversor. Usa MONSTER / MONSTER9 para obtener un token y adjúntalo en las conversiones.

![Captura](imagenes/image19.png)

Figura 18. Rutas HTTP publicadas por ASP.NET Core sobre Kestrel (.NET 10) para autenticación y conversiones con respuestas JSON.

![Captura](imagenes/image20.png)

Figura 19. Prueba 1: Login.

![Captura](imagenes/image21.png)

Figura 20. Prueba 2: Conversor de Longitud.

![Captura](imagenes/image22.png)

Figura 21. Prueba 3: Conversor de masa.

![Captura](imagenes/image23.png)

Figura 22. Prueba 4: Conversor de temperatura.

## CODIFICACIÓN DE LOS CLIENTES

### CLIENTE CONSOLA

#### CREACIÓN DEL PROYECTO

El proyecto se crea usando Visual Studio seleccionando ‘Nuevo Proyecto’, ‘C# Console Application’ y ‘.NET Application’.

![Captura](imagenes/image24.png)

Figura 23. Creación del proyecto - Cliente consola

En este paso se define el nombre del proyecto y la ubicación.

![Captura](imagenes/image25.png)

Figura 24. Configuración del proyecto - Cliente consola

#### ESTRUCTURA DEL PROYECTO

Una vez creado el proyecto se crea la estructura de paquetes basada en el patrón MVC

![Captura](imagenes/image26.png)

Figura 25. Estructura del proyecto - Cliente consola

### CODIFICACIÓN DEL PROYECTO CLIENTE CONSOLA

#### CREACIÓN DE MODELOS Y VISTAS

Dado que no se requiere persistencia local ni mapeo de entidades complejas, el modelo se enfoca en la organización lógica de las opciones. Se crea la clase CatalogoUnidades dentro del paquete modelo para gestionar paramétricamente los tipos de unidades de medida disponibles.

En la capa de interacción, dentro del paquete vista, se implementan las clases InterfazConsola y el enumerador OpcionMenu. Estas clases se encargan de abstraer la clase Scanner, capturar los datos por teclado de forma segura y desplegar los menús interactivos al usuario sin mezclar la lógica de negocio.

![Captura](imagenes/image27.png)

Figura 26. Creación de modelos y vistas - Cliente consola

Tabla 8. DTO ConversionSolicitud - Cliente consola (.NET 10)

| namespace ClienteConsola_DotNet_REST_GR09.Modelos; public sealed class ConversionSolicitud { public string Magnitud { get; set; } = string.Empty; public string Origen { get; set; } = string.Empty; public string Destino { get; set; } = string.Empty; public double Valor { get; set; } public string? Token { get; set; } } |
| --- |

Tabla 9. Clase VistaConsola - Cliente consola (.NET 10)

| namespace ClienteConsola_DotNet_REST_GR09.Vista; public sealed class VistaConsola { public void MostrarBanner() { Console.WriteLine("=== CLIENTE CONSOLA RESTFUL .NET 10 (GR09) ==="); } public (string usuario, string clave) PedirCredenciales() { Console.Write("Usuario: "); var u = Console.ReadLine()?.Trim() ?? ""; Console.Write("Contraseña: "); var c = Console.ReadLine()?.Trim() ?? ""; return (u, c); } public double PedirValorNumerico() { while (true) { Console.Write("Valor numérico (máx 16 car., 2 decimales): "); var s = Console.ReadLine()?.Trim() ?? ""; if (s.Length <= 16 && System.Text.RegularExpressions.Regex.IsMatch(s, @"^-?\d+(\.\d+)?$")) return double.Parse(s, System.Globalization.CultureInfo.InvariantCulture); Console.WriteLine("[ERROR] Entrada inválida."); } } public void MostrarResultado(double v, string o, double r, string d) => Console.WriteLine($"\n[RESULTADO] {v:F2} {o} ---> {r:F2} {d}"); } |
| --- |

Tabla 10. Clase ClienteServicioRest - Cliente consola (.NET 10)

| namespace ClienteConsola_DotNet_REST_GR09.Servicios; public sealed class ClienteServicioRest { private readonly HttpClient _http = new() { BaseAddress = new Uri("http://127.0.0.1:5000/"), Timeout = TimeSpan.FromSeconds(10) }; public async Task<TokenRespuesta> IniciarSesionAsync(string u, string c) { var res = await _http.PostAsJsonAsync("api/auth/login", new { usuario = u, clave = c }); return (await res.Content.ReadFromJsonAsync<TokenRespuesta>())!; } public async Task<ConversionRespuesta> ConvertirAsync(string mag, string ori, string des, double val, string tok) { var req = new HttpRequestMessage(HttpMethod.Post, $"api/conversor/{mag}") { Content = JsonContent.Create(new { origen = ori, destino = des, valor = val, token = tok }) }; req.Headers.Authorization = new("Bearer", tok); var res = await _http.SendAsync(req); return (await res.Content.ReadFromJsonAsync<ConversionRespuesta>())!; } } |
| --- |

CONSUMO DEL Servicio RESTful (CONTROLADOR)

Los clientes .NET usan ClienteServicioRest para enviar solicitudes HTTP/JSON a la API. El cliente conserva la URL base y el token de sesión y procesa los modelos JSON de respuesta.

![Captura](imagenes/image28.png)

Figura 27. Creación de controlador - Cliente consola

ConversionController coordina la selección de magnitud y unidades con RestClient. Valida la sesión, invoca la ruta REST correspondiente y entrega el resultado a la vista.

Tabla 11. Clase ControladorConsola - Cliente consola (.NET 10)

| namespace ClienteConsola_DotNet_REST_GR09.Controlador; public sealed class ControladorConsola { private readonly VistaConsola _vista = new(); private readonly ClienteServicioRest _servicio = new(); private readonly ModeloSesion _sesion = new(); public async Task IniciarAsync() { _vista.MostrarBanner(); var (u, c) = _vista.PedirCredenciales(); var resp = await _servicio.IniciarSesionAsync(u, c); if (resp.Exito) { _sesion.Token = resp.Token; _vista.MostrarExito($"Autenticado: {_sesion.TokenAbreviado}"); } } } |
| --- |

CLASE PRINCIPAL (EJECUCIÓN)

Finalmente, se codifica la clase Program.cs (Consola) que orquesta el flujo completo de la aplicación. El sistema inicia solicitando credenciales al usuario para invocar el servicio de seguridad; únicamente si la autenticación es exitosa, se instancia la interfaz y el controlador para permitir el uso de las operaciones de conversión.

![Captura](imagenes/image29.png)

Figura 28. Creación de clase principal - Cliente consola

Tabla 12. Punto de Entrada - Program.cs (.NET 10)

| using ClienteConsola_DotNet_REST_GR09.Controlador; var controlador = new ControladorConsola(); await controlador.IniciarAsync(); |
| --- |

#### EJECUCIÓN DEL CLIENTE DE CONSOLA

![Captura](imagenes/image30.png)

Figura 29. Login - Cliente consola

![Captura](imagenes/image31.png)

Figura 30. Conversión de Longitud - Cliente consola

![Captura](imagenes/image32.png)

Figura 31. Conversión de Masa - Cliente consola

![Captura](imagenes/image33.png)

Figura 32. Conversión de Temperatura - Cliente consola

### CLIENTE ESCRITORIO

#### CREACIÓN DEL PROYECTO

El proyecto se crea de igual manera que el cliente de consola .NET Application

#### ESTRUCTURA DEL PROYECTO

El cliente de escritorio con interfaz gráfica se estructuró bajo el patrón MVC. Se crearon los paquetes modelo, vista y controlador para separar el diseño visual de la lógica de consumo del servicio web.

![Captura](imagenes/image34.png)

Figura 33. Estructura del proyecto - Cliente escritorio

### CODIFICACIÓN DEL PROYECTO CLIENTE ESCRITORIO

#### CREACIÓN DE MODELOS

Al no requerir base de datos, en el paquete modelo solo se manejan clases auxiliares y enumeradores. Estos sirven exclusivamente para llenar las opciones de listas desplegables en las ventanas.

![Captura](imagenes/image35.png)

Figura 34. Creación de modelo - Cliente escritorio

CREACIÓN DE CONTROLADOR

El cliente de escritorio usa RestClient para invocar la API REST. Sus controladores reciben los datos de las ventanas, verifican la sesión y presentan la respuesta de conversión.

![Captura](imagenes/image36.png)

Figura 35. Creación de servicio - Cliente consola

Tabla 13. Clase VentanaPrincipal.xaml.cs - Cliente escritorio (WPF .NET 10)

| namespace ClienteEscritorio_DotNet_REST_GR09.Vistas; public partial class VentanaPrincipal : Window { private readonly ClienteServicioRest _servicio; private readonly ModeloSesion _sesion; public VentanaPrincipal(ClienteServicioRest servicio, ModeloSesion sesion) { InitializeComponent(); _servicio = servicio; _sesion = sesion; } private async void BtnConvertir_Click(object sender, RoutedEventArgs e) { double val = double.Parse(TxtValor.Text, CultureInfo.InvariantCulture); var res = await _servicio.ConvertirAsync(CmbMagnitud.Text, CmbOrigen.Text, CmbDestino.Text, val, _sesion.Token!); LblResultado.Text = $"{res.ValorConvertido:F2} {CmbDestino.Text}"; } } |
| --- |

CLASE PRINCIPAL (EJECUCIÓN)

La clase principal del proyecto funciona como el punto de entrada de la aplicación. Su única responsabilidad es instanciar la ventana inicial del sistema (la interfaz de inicio de sesión) y hacerla visible en la pantalla para iniciar el flujo gráfico del programa.

![Captura](imagenes/image37.png)

Figura 37. Creación de vistas - Cliente escritorio

#### EJECUCIÓN DEL CLIENTE DE ESCRITORIO

![Captura](imagenes/image38.png)

Figura 38. Login - Cliente escritorio

![Captura](imagenes/image39.png)

Figura 39. Pantalla de Conversiones - Cliente escritorio

### CLIENTE WEB

#### CREACIÓN DEL PROYECTO

El proyecto se crea de igual manera que el webservice con ‘Web Application’.

#### ESTRUCTURA DEL PROYECTO

El cliente web se desarrolló siguiendo el patrón MVC utilizando ASP.NET Core MVC con Controllers, Razor Views y JavaScript. La estructura separa la lógica de control (en el paquete controlador) de las interfaces de usuario (páginas web).

![Captura](imagenes/image40.png)

Figura 40. Estructura del proyecto - Cliente web

### CODIFICACIÓN DEL PROYECTO CLIENTE WEB

CONSUMO DEL Servicio RESTful (SERVLETS)

El cliente web usa LoginServlet y ConversionServlet para gestionar solicitudes del navegador y llamar a RestClient. Jetty 11 embebido escucha en el puerto 8081; las vistas son index.html y conversor.html.

![Captura](imagenes/image41.png)

Figura 41. Creación de servlets - Cliente web

Tabla 14. Clase ConversorController - Cliente web (ASP.NET Core C#)

| namespace ClienteWeb_DotNet_REST_GR09.Controladores; [Route("conversor")] public sealed class ConversorController : Controller { private readonly ClienteServicioRest _servicio; public ConversorController(ClienteServicioRest servicio) => _servicio = servicio; [HttpPost] public async Task<IActionResult> Convertir([FromForm] string magnitud, [FromForm] string origen, [FromForm] string destino, [FromForm] double valor, [FromForm] string token) { var resp = await _servicio.ConvertirAsync(magnitud, origen, destino, valor, token); return Json(resp); } } |
| --- |

Tabla 15. Clase AuthController - Cliente web (ASP.NET Core C#)

| namespace ClienteWeb_DotNet_REST_GR09.Controladores; [Route("auth")] public sealed class AuthController : Controller { private readonly ClienteServicioRest _servicio; public AuthController(ClienteServicioRest servicio) => _servicio = servicio; [HttpPost("login")] public async Task<IActionResult> Login([FromForm] string usuario, [FromForm] string clave) { var resp = await _servicio.IniciarSesionAsync(usuario, clave); return Json(resp); } } |
| --- |

CREACIÓN DE VISTAS

Las interfaces de usuario se construyeron utilizando HTML y la tecnología HTML (páginas HTML) dentro del directorio Web Pages. Se diseñaron dos vistas principales: index.html, que actúa como la pantalla de inicio de sesión interactuando con el LoginServlet, y conversor.html, que contiene los formularios dinámicos para seleccionar las unidades de medida, capturar los valores y mostrar los resultados procesados por el ConversionServlet.

![Captura](imagenes/image42.png)

Figura 42. Creación de vistas - Cliente web

Tabla 16. Archivo conversor.html - Cliente web

| <!DOCTYPE html><html lang="es"><head> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Conversor de Unidades RESTful - Panel Web (GR09)</title> <link rel="stylesheet" href="css/style.css"></head><body> <!-- Encabezado con sesión activa --> <header class="header-bar"> <div class="header-brand"> <h1>CONVERSOR DE UNIDADES (RESTFUL JAVA · SINBDD)</h1> <p>ESPE · Arquitectura de Software (TI.1.3) / Autor: Julio Enrique Viche Castillo</p> </div> <div class="header-user"> <span class="badge-session"> <span class="status-dot">●</span> <strong id="lblUsuario">MONSTER</strong> <span style="opacity: 0.8; font-size: 0.75rem;" id="lblToken">(Sesión Activa)</span> </span> <a href="logout" class="btn-logout">Cerrar Sesión</a> </div> </header> <!-- Panel Central del Conversor --> <main class="main-wrapper"> <div class="card-box"> <h2 class="card-title">Conversor de Unidades Multimagnitud</h2> <p class="card-subtitle">Cálculos procesados mediante servicios web RESTful JSON protegidos por token</p> <div id="alertBox" class="alert alert-error" style="display: none;"></div> <!-- 1. Pestañas de Magnitud --> <label class="form-label">1. Seleccione la Magnitud Física:</label> <div class="segmented-group"> <button type="button" class="segment-btn active" id="btnLongitud" onclick="cambiarMagnitud('Longitud')"> Longitud </button> <button type="button" class="segment-btn" id="btnMasa" onclick="cambiarMagnitud('Masa')"> Masa </button> <button type="button" class="segment-btn" id="btnTemperatura" onclick="cambiarMagnitud('Temperatura')"> Temperatura </button> </div> <!-- Formulario de Conversión --> <form action="convertir" method="POST" id="formConversion"> <input type="hidden" id="inputMagnitud" name="magnitud" value="Longitud"> <!-- 2. Valor Numérico --> <div class="form-group"> <label class="form-label" for="txtValor">2. Ingrese el Valor a Convertir:</label> <input type="text" id="txtValor" name="valor" class="form-input" autocomplete="off" inputmode="decimal" maxlength="16" pattern="^-?\d+(\.\d+)?$" title="Solo se permiten números y el punto decimal '.' (ej. 100 o 25.5)" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) // (event.charCode === 46 && this.value.indexOf('.') === -1)" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*?)\..*/g, '$1').slice(0, 16)" value="100.00" required> </div> <!-- 3. Unidades Origen y Destino --> <label class="form-label">3. Configuración de Unidades:</label> <div class="units-row"> <div class="units-col"> <select id="cmbOrigen" name="unidadOrigen" class="form-select"></select> </div> <button type="button" class="btn-swap" title="Intercambiar unidades" onclick="intercambiarUnidades()"> ⇄ </button> <div class="units-col"> <select id="cmbDestino" name="unidadDestino" class="form-select"></select> </div> </div> <button type="submit" class="btn-primary" style="margin-top: 20px;"> Calcular Conversión REST </button> </form> <!-- 4. Tarjeta de Resultado --> <div id="pnlResultado" class="result-card" style="display: none;"> <div class="result-value" id="lblResultadoValor">0.00</div> <div class="result-detail" id="lblResultadoDetalle">Cálculo completado exitosamente</div> </div> </div> </main> <!-- Barra de Estado Inferior --> <footer class="footer-bar"> <span>Servidor Backend: <code>WSConversionUnidades_DotNet_REST_GR09</code> (Modalidad SINBDD)</span> <span>Patrón Arquitectónico: Modelo - Vista - Controlador (JSP / Servlet)</span> </footer> <script> const CATALOGO = { 'Longitud': ['metro', 'centimetro', 'pie', 'yarda', 'milla'], 'Masa': ['gramo', 'kilogramo', 'libra', 'onza', 'quintal'], 'Temperatura': ['celsius', 'fahrenheit', 'kelvin', 'newton', 'reaumur'] }; let magnitudActual = 'Longitud'; function cambiarMagnitud(magnitud) { magnitudActual = magnitud; document.getElementById('inputMagnitud').value = magnitud; ['Longitud', 'Masa', 'Temperatura'].forEach(m => { const btn = document.getElementById('btn' + m); if (m === magnitud) { btn.classList.add('active'); } else { btn.classList.remove('active'); } }); const unidades = CATALOGO[magnitud]; const cmbO = document.getElementById('cmbOrigen'); const cmbD = document.getElementById('cmbDestino'); cmbO.innerHTML = ''; cmbD.innerHTML = ''; unidades.forEach((u, i) => { const nombre = u.charAt(0).toUpperCase() + u.slice(1); cmbO.add(new Option(nombre, u)); cmbD.add(new Option(nombre, u)); }); if (unidades.length > 1) { cmbD.selectedIndex = 1; } document.getElementById('pnlResultado').style.display = 'none'; } function intercambiarUnidades() { const cmbO = document.getElementById('cmbOrigen'); const cmbD = document.getElementById('cmbDestino'); const tmp = cmbO.selectedIndex; cmbO.selectedIndex = cmbD.selectedIndex; cmbD.selectedIndex = tmp; } // Leer parámetros URL (para mensajes de servlet o login) const params = new URLSearchParams(window.location.search); if (params.has('usuario')) { document.getElementById('lblUsuario').textContent = params.get('usuario'); } if (params.has('token')) { document.getElementById('lblToken').textContent = '(Token: ' + params.get('token') + ')'; } if (params.has('resultado')) { const pnl = document.getElementById('pnlResultado'); const val = document.getElementById('lblResultadoValor'); const det = document.getElementById('lblResultadoDetalle'); val.textContent = params.get('resultado'); det.textContent = params.get('detalle') // 'Cálculo completado exitosamente'; pnl.style.display = 'block'; } if (params.has('error')) { const alertBox = document.getElementById('alertBox'); alertBox.textContent = params.get('error'); alertBox.style.display = 'block'; } // Inicializar con Longitud cambiarMagnitud('Longitud'); </script></body></html> |
| --- |

Tabla 17. Archivo index.html - Cliente web

| <!DOCTYPE html><html lang="es"><head> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Acceso al Sistema - Conversor de Unidades RESTful (GR09)</title> <link rel="stylesheet" href="css/style.css"></head><body> <!-- Encabezado Institucional --> <header class="header-bar"> <div class="header-brand"> <h1>UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE</h1> <p>Arquitectura de Software (TI.1.3) · Grupo GR09 / Julio Enrique Viche Castillo</p> </div> <div class="header-user"> <span class="badge-session"> <span class="status-dot">●</span> Servidor RESTful JSON </span> </div> </header> <!-- Contenedor del Formulario de Login --> <main class="main-wrapper"> <div class="card-box card-login"> <h2 class="card-title">Iniciar Sesión</h2> <p class="card-subtitle">Autenticación requerida para acceder al Conversor REST (SINBDD)</p> <div id="alertBox" class="alert alert-error" style="display: none;"></div> <div id="infoBox" class="alert alert-info" style="display: none;"></div> <form action="login" method="POST"> <div class="form-group"> <label class="form-label" for="txtUsuario">Usuario:</label> <input type="text" id="txtUsuario" name="usuario" class="form-input" value="MONSTER" required autofocus> </div> <div class="form-group"> <label class="form-label" for="txtClave">Contraseña:</label> <input type="password" id="txtClave" name="clave" class="form-input" value="MONSTER9" required> </div> <div class="alert alert-info" style="font-size: 0.8rem; padding: 8px 12px; margin-bottom: 20px;"> <strong>Credenciales del curso:</strong> Usuario: <code>MONSTER</code> / Clave: <code>MONSTER9</code> </div> <button type="submit" class="btn-primary">Ingresar al Conversor</button> </form> </div> </main> <!-- Barra de Estado Inferior --> <footer class="footer-bar"> <span>Servidor Backend: <code>WSConversionUnidades_DotNet_REST_GR09</code> (Modalidad SINBDD)</span> <span>Patrón Arquitectónico: Modelo - Vista - Controlador (JSP / Servlet)</span> </footer> <script> const params = new URLSearchParams(window.location.search); if (params.has('error')) { const alertBox = document.getElementById('alertBox'); let err = params.get('error'); if (err === 'SesionInvalida') err = 'Su sesión ha expirado o es inválida. Inicie sesión nuevamente.'; alertBox.textContent = err; alertBox.style.display = 'block'; } if (params.has('msg')) { const infoBox = document.getElementById('infoBox'); infoBox.textContent = params.get('msg'); infoBox.style.display = 'block'; } </script></body></html> |
| --- |

EJECUCIÓN DEL CLIENTE DE WEB

![Captura](imagenes/image43.png)

Figura 43. Login - Cliente web

![Captura](imagenes/image44.png)

Figura 44. Pantalla de Conversiones - Cliente web

### CLIENTE MÓVIL (.NET MAUI)

#### CREACIÓN DEL PROYECTO

El proyecto móvil se crea en Visual Studio 2022 seleccionando la plantilla ".NET MAUI App" con destino .NET 10 y plataforma Android. Se establece el espacio de nombres `ClienteMovilGR09` y el identificador de aplicación `ec.edu.monster.conversor.gr09`.

![Captura](imagenes/image45.png)

Figura 45. Configuración del proyecto - Cliente móvil .NET MAUI

#### ESTRUCTURA DEL PROYECTO

El cliente móvil .NET MAUI se estructura bajo el patrón Modelo-Vista-Controlador (MVC), separando claramente las responsabilidades:
- **Modelos**: Registros inmutables en C# (`SolicitudInicioSesion`, `TokenRespuesta`, `ConversionSolicitud`, `ConversionRespuesta`).
- **Servicios**: `ClienteRest` implementado con `HttpClient` y `System.Text.Json` para consumir la API RESTful.
- **Controladores**: `ControladorConversor` que coordina el estado de la sesión y las llamadas asíncronas.
- **Vistas**: Páginas XAML declarativas (`PaginaInicioSesion.xaml`, `PaginaConversor.xaml`).

![Captura](imagenes/image46.png)

Figura 46. Estructura del proyecto - Cliente móvil .NET MAUI

### CODIFICACIÓN DEL PROYECTO CLIENTE MÓVIL

#### CREACIÓN DE MODELOS Y SERVICIOS REST

En la carpeta `Modelos` se definen los DTOs de intercambio de datos, y en `Servicios` se implementa `ClienteRest.cs`, encargado de la serialización JSON, configuración de cabeceras `Authorization: Bearer` y manejo de excepciones de red con fallback de endpoint local (`http://10.0.2.2:5000/` en emulador Android o `http://127.0.0.1:5000/` en escritorio).

![Captura](imagenes/image47.png)

Figura 47. Creación de modelos y cliente REST - Cliente móvil

#### CREACIÓN DE CONTROLADORES

`ControladorConversor.cs` administra la sesión del usuario en memoria (SINBDD) y provee métodos asíncronos para autenticación con credenciales quemadas (`MONSTER` / `MONSTER9`) y conversión paramétrica por magnitud (longitud, masa, temperatura).

![Captura](imagenes/image48.png)

Figura 48. Creación de controladores - Cliente móvil

Tabla 18. ControladorConversor - Cliente móvil .NET MAUI

| using ClienteMovilGR09.Modelos;using ClienteMovilGR09.Servicios;namespace ClienteMovilGR09.Controladores;public sealed class ControladorConversor{ private readonly ClienteRest _servicio; private readonly EstadoCliente _estado = new(); public ControladorConversor(ClienteRest? servicio = null){ _servicio = servicio ?? new ClienteRest(); } public async Task<TokenRespuesta> IniciarSesionAsync(string usuario, string clave){ var resp = await _servicio.IniciarSesionAsync(new SolicitudInicioSesion(usuario, clave)); if (resp.Exito){ _estado.Token = resp.Token; _estado.Usuario = usuario; } return resp; } public Task<ConversionRespuesta> ConvertirAsync(string magnitud, double valor, string origen, string destino){ if (string.IsNullOrWhiteSpace(_estado.Token)) throw new InvalidOperationException("Debe iniciar sesión primero."); return _servicio.ConvertirAsync(magnitud, new ConversionSolicitud(valor, origen, destino), _estado.Token); }} |
| --- |

#### CREACIÓN DE VISTAS (XAML)

Las interfaces gráficas se diseñan en XAML con estilos visuales modernos, contenedores responsivos y validaciones de entrada en tiempo de digitación (máximo 16 caracteres, dígitos y punto decimal).

![Captura](imagenes/image49.png)

Figura 49. Creación de vistas XAML - Cliente móvil

#### EJECUCIÓN DEL CLIENTE MÓVIL (.NET MAUI)

Se despliega la aplicación en el emulador Android de Visual Studio o dispositivo físico, verificando el inicio de sesión y la ejecución de conversiones con formateo estricto a 2 decimales.

![Captura](imagenes/image50.png)

Figura 50. Inicio de sesión - Cliente móvil .NET MAUI

![Captura](imagenes/image51.png)

Figura 51. Pantalla de Conversiones - Cliente móvil .NET MAUI

#### CONCLUSIONES

El proyecto integra la plataforma .NET 10, ASP.NET Core con servidor Kestrel y clientes multiplataforma (Consola, WPF, Web y .NET MAUI). La configuración de Visual Studio 2022 y el SDK de .NET permite ejecutar el servidor y los cuatro clientes bajo una arquitectura RESTful robusta y sin base de datos.

La implementación de los módulos de seguridad y conversiones evidenció la importancia de separar correctamente la lógica de negocio y la capa de exposición del servicio web. A pesar de no contar con conexión a una base de datos, el uso de DTOs, validaciones en memoria y manejo de sesiones simuladas dio como resultado soluciones mantenibles, escalables y con un nivel adecuado de desacoplamiento.

Los clientes de consola, escritorio, web con HTML/Controllers ASP.NET Core y móvil Android consumen la API REST. Envían solicitudes HTTP con JSON, obtienen un token en auth/login y lo incluyen en las conversiones de longitud, masa y temperatura.

La solución organiza la API en recursos Jakarta REST implementados con Jersey y ejecutados sobre Kestrel (.NET 10). Los clientes .NET consumen rutas HTTP con JSON y completan el flujo desde la autenticación hasta las conversiones, sin una base de datos.

#### RECOMENDACIONES

Mantener la estructura modular de los clientes y el patrón MVC en las interfaces para facilitar su evolución. Los recursos REST del servidor deben conservar responsabilidades claras para autenticación y conversiones.

Implementar respuestas HTTP de error con mensajes JSON claros cuando el cliente envíe unidades no soportadas o datos inválidos; esto facilitará el diagnóstico de solicitudes REST.

Añadir validaciones más estrictas en los servicios, especialmente en el control del tiempo de vida de las sesiones del usuario y en el bloqueo de peticiones con valores numéricos inválidos (como medidas de masa negativas).

Como mejora futura, se puede integrar un sistema de logs en Kestrel para registrar solicitudes HTTP, códigos de respuesta y errores de conversión, facilitando el diagnóstico de la API REST.

Implementar mecanismos de caché o estructuras de datos optimizadas en memoria para los catálogos de unidades, en caso de que el sistema escale a un volumen masivo de peticiones concurrentes.

Desarrollar pruebas unitarias en la capa de servicios para asegurar la exactitud de las fórmulas matemáticas utilizadas en las transformaciones de temperatura, longitud y masa ante futuras modificaciones.

Mejorar la interfaz de usuario en los clientes web y móvil incorporando retroalimentación visual más precisa, controles de validación en los formularios y manejo asíncrono avanzado para no bloquear las pantallas durante la espera de respuesta del servidor.

Documentar las rutas HTTP, sus métodos, los campos JSON de solicitud y respuesta y los códigos de estado para que otros clientes puedan consumir la API REST.
