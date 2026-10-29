using WSServidorSOAP_DotNet_GR09.Seguridad;
using WSServidorSOAP_DotNet_GR09.Servicios;
using WSServidorSOAP_DotNet_GR09.Soap;

var builder = WebApplication.CreateBuilder(args);

// Configuración de Kestrel para escuchar en 0.0.0.0:8733
builder.WebHost.ConfigureKestrel(options =>
{
    options.ListenAnyIP(8733);
});

builder.Services.AddSingleton<AdministradorTokens>();
builder.Services.AddSingleton<ServicioSOAP>();
builder.Services.AddSingleton<ManejadorSOAP>();

var app = builder.Build();

app.UseRouting();

// Rutas compatibles con todos los clientes SOAP
app.MapMethods("/ServidorSOAP", ["GET", "POST"], async (HttpContext ctx, ManejadorSOAP handler) =>
{
    await handler.ProcesarSolicitudAsync(ctx);
});

app.MapMethods("/ServicioAutenticacion", ["GET", "POST"], async (HttpContext ctx, ManejadorSOAP handler) =>
{
    await handler.ProcesarSolicitudAsync(ctx);
});

app.MapMethods("/ServicioConversion", ["GET", "POST"], async (HttpContext ctx, ManejadorSOAP handler) =>
{
    await handler.ProcesarSolicitudAsync(ctx);
});

app.MapMethods("/", ["GET", "POST"], async (HttpContext ctx, ManejadorSOAP handler) =>
{
    await handler.ProcesarSolicitudAsync(ctx);
});

app.Run();
