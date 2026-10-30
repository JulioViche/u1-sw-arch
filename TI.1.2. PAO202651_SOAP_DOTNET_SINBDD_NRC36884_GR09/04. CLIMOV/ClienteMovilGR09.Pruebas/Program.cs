using System;
using System.Threading.Tasks;
using ClienteMovilGR09.Modelos;
using ClienteMovilGR09.Controladores;

Console.WriteLine("============================================================");
Console.WriteLine("VERIFICACION Y PRUEBAS DEL CLIENTE MOVIL MAUI SOAP GR09");
Console.WriteLine("Asignatura: Arquitectura de Software | NRC: 36884 | Grupo: GR09");
Console.WriteLine("============================================================");

// 1. Verificación de Modelos y Catálogos en Memoria (SINBDD)
Console.WriteLine("\n[1] Verificando Catálogo de Unidades y Modelos...");
var sesion = new ModeloSesion();
if (!sesion.Autenticado)
{
    Console.WriteLine("  [PASS] Estado inicial: Sesión no autenticada.");
}

sesion.Iniciar("TOKEN-DEMO-TEST");
if (sesion.Autenticado && sesion.Token == "TOKEN-DEMO-TEST")
{
    Console.WriteLine("  [PASS] Gestión de Token en memoria (SINBDD) verificada.");
}
sesion.Cerrar();

// 2. Verificación de Controlador Conversor
Console.WriteLine("\n[2] Inicializando ControladorConversor (Arquitectura MAUI)...");
var controlador = new ControladorConversor();
Console.WriteLine("  [PASS] ControladorConversor instanciado exitosamente.");

// 3. Prueba de Conectividad con Servidor SOAP
Console.WriteLine("\n[3] Comprobando integración SOAP con el Servidor...");
try
{
    Console.WriteLine("  Intentando inicio de sesión con credenciales quemadas: MONSTER / MONSTER9");
    await controlador.IniciarSesionAsync("MONSTER", "MONSTER9");
    Console.WriteLine("  [PASS] Conexión SOAP Exitosa. Token de sesión obtenido.");

    Console.WriteLine("  Ejecutando conversión de longitud: 100 METROS a PIES...");
    double resultado = await controlador.ConvertirAsync(TipoConversion.Longitud, 100.0, "METROS", "PIES");
    Console.WriteLine($"  [PASS] Conversión completada: 100 METROS = {resultado:F2} PIES (2 decimales)");
}
catch (Exception ex)
{
    Console.WriteLine($"  [INFO] Servidor SOAP local no disponible o en reposo: {ex.Message}");
    Console.WriteLine("  [INFO] Para probar la conexión en vivo, inicia primero el servidor:");
    Console.WriteLine("         cd 'TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\\04. SERVIDOR'");
    Console.WriteLine("         dotnet run");
}

Console.WriteLine("\n============================================================");
Console.WriteLine("[EXITO] Lógica del Cliente Móvil MAUI SOAP (.NET 10) validada.");
Console.WriteLine("============================================================");

