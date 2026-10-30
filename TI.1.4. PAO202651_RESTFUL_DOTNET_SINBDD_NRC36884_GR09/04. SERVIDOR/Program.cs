using WSConversionUnidades_DotNet_REST_GR09.Seguridad;
using WSConversionUnidades_DotNet_REST_GR09.Servicios;

var builder = WebApplication.CreateBuilder(args);

// Configuración de Kestrel para escuchar en 0.0.0.0:5000
builder.WebHost.ConfigureKestrel(options =>
{
    options.ListenAnyIP(5000);
});

// Inyección de dependencias
builder.Services.AddSingleton<AdministradorTokens>();
builder.Services.AddSingleton<ServicioAutenticacion>();
builder.Services.AddSingleton<ServicioConversion>();

builder.Services.AddControllers();
builder.Services.AddOpenApi();

// Habilitar CORS para clientes Web, Móvil y Escritorio
builder.Services.AddCors(options =>
{
    options.AddPolicy("PermitirTodo", policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyHeader()
              .AllowAnyMethod();
    });
});

var app = builder.Build();

app.UseRouting();
app.UseCors("PermitirTodo");

if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.MapControllers();

// Endpoint raíz informativo
app.MapGet("/", () => Results.Content(
    """
    <!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Servidor RESTful .NET - Conversor de Unidades GR09</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background: #0F172A; color: #F8FAFC; margin: 0; padding: 40px; display: flex; justify-content: center; }
            .card { background: #1E293B; border-radius: 12px; padding: 32px; max-width: 640px; width: 100%; box-shadow: 0 10px 25px rgba(0,0,0,0.5); border: 1px solid #334155; }
            h1 { color: #38BDF8; margin-top: 0; font-size: 1.5rem; }
            .badge { background: #0284C7; color: white; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: bold; }
            p { color: #94A3B8; line-height: 1.6; }
            code { background: #0F172A; color: #F43F5E; padding: 2px 6px; border-radius: 4px; }
            ul { line-height: 1.8; color: #E2E8F0; }
        </style>
    </head>
    <body>
        <div class="card">
            <span class="badge">ACTIVO · PUERTO 5000</span>
            <h1>ESPE · Servidor Web RESTful .NET 10 (SINBDD)</h1>
            <p>Tarea TI.1.4 (PAO 2026-51) · NRC: 36884 · Grupo: GR09</p>
            <p>Autor: Julio Enrique Viche Castillo</p>
            <hr style="border-color: #334155; margin: 20px 0;">
            <h3>Endpoints de la API:</h3>
            <ul>
                <li><code>POST /api/auth/login</code> - Autenticación (MONSTER / MONSTER9)</li>
                <li><code>POST /api/auth/logout</code> - Revocación de token</li>
                <li><code>POST /api/conversor/longitud</code> - Conversión de longitud</li>
                <li><code>POST /api/conversor/masa</code> - Conversión de masa</li>
                <li><code>POST /api/conversor/temperatura</code> - Conversión de temperatura</li>
                <li><code>GET /api/health</code> - Estado de salud y catálogo</li>
            </ul>
        </div>
    </body>
    </html>
    """, "text/html"));

app.Run();
