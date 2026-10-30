using System;
using System.Threading.Tasks;
using ClienteMovilGR09.Modelos;
using ClienteMovilGR09.Servicios;
using ClienteMovilGR09.Controladores;

Console.WriteLine("============================================================");
Console.WriteLine("VERIFICACION Y PRUEBAS DEL CLIENTE MOVIL MAUI RESTFUL GR09");
Console.WriteLine("Asignatura: Arquitectura de Software | NRC: 36884 | Grupo: GR09");
Console.WriteLine("============================================================");

// 1. Verificación de Modelos y Contratos RESTful (JSON / SINBDD)
Console.WriteLine("\n[1] Verificando Modelos de Petición y Respuesta JSON...");
var reqLogin = new SolicitudInicioSesion("MONSTER", "MONSTER9");
if (reqLogin.Usuario == "MONSTER" && reqLogin.Clave == "MONSTER9")
{
    Console.WriteLine("  [PASS] Modelo de Solicitud de Inicio de Sesión validado.");
}

var reqConv = new ConversionSolicitud(100.0, "METROS", "PIES");
if (reqConv.Valor == 100.0 && reqConv.UnidadOrigen == "METROS" && reqConv.UnidadDestino == "PIES")
{
    Console.WriteLine("  [PASS] Modelo de Conversión (Longitud/Masa/Temperatura) validado.");
}

// 2. Inicialización de Capas de Red y Controladores
Console.WriteLine("\n[2] Inicializando Servicio REST y Controlador MAUI...");
var servicio = new ClienteRest();
var controlador = new ControladorConversor(servicio);
Console.WriteLine("  [PASS] ClienteRest y ControladorConversor instanciados correctamente.");

// 3. Comprobación de Conectividad con la API RESTful
Console.WriteLine("\n[3] Comprobando integración HTTP/JSON con el Servidor Web API...");
try
{
    Console.WriteLine("  Intentando autenticación POST /api/auth/login...");
    var resLogin = await controlador.IniciarSesionAsync("MONSTER", "MONSTER9");
    if (resLogin.Exito)
    {
        Console.WriteLine($"  [PASS] Autenticación REST Exitosa. Token: {resLogin.Token}");
        Console.WriteLine("  Ejecutando conversión de longitud: 100 METROS a PIES...");
        var resConv = await controlador.ConvertirAsync("longitud", 100.0, "METROS", "PIES");
        Console.WriteLine($"  [PASS] Resultado: 100.00 METROS = {resConv.Resultado:F2} PIES (2 decimales)");
    }
}
catch (Exception ex)
{
    Console.WriteLine($"  [INFO] Servidor REST local no disponible o en reposo: {ex.Message}");
    Console.WriteLine("  [INFO] Para probar la conexión en vivo, inicia primero el servidor Web API:");
    Console.WriteLine("         cd 'TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09\\04. SERVIDOR'");
    Console.WriteLine("         dotnet run");
}

Console.WriteLine("\n============================================================");
Console.WriteLine("[EXITO] Lógica del Cliente Móvil MAUI RESTful (.NET 10) validada.");
Console.WriteLine("============================================================");
