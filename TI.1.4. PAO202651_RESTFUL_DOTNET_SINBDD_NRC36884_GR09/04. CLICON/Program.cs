using ClienteConsola_DotNet_REST_GR09.Controlador;

Console.Title = "ESPE · Cliente Consola Conversor de Unidades RESTful .NET (GR09)";
var controlador = new ControladorConsola();
await controlador.IniciarAsync();
