using ClienteConsola_DotNet_SOAP_GR09.Controlador;

Console.Title = "ESPE · Cliente Consola Conversor de Unidades SOAP .NET (GR09)";
var controlador = new ControladorConsola();
await controlador.IniciarAsync();
