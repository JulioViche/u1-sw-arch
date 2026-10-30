using Microsoft.AspNetCore.Mvc;

namespace WSConversionUnidades_DotNet_REST_GR09.Controladores;

[ApiController]
[Route("api/health")]
[Route("health")]
[Produces("application/json")]
public sealed class ControladorSalud : ControllerBase
{
    [HttpGet]
    public ActionResult ObtenerEstado()
    {
        return Ok(new
        {
            estado = "SALUDABLE",
            servicio = "Servicio Web RESTful - Conversor de Unidades (SINBDD)",
            plataforma = ".NET 10.0 / ASP.NET Core",
            universidad = "Universidad de las Fuerzas Armadas ESPE",
            tarea = "TI.1.4 (PAO 2026-51)",
            nrc = "36884",
            grupo = "GR09",
            autor = "Julio Enrique Viche Castillo",
            timestamp = DateTime.UtcNow.ToString("o"),
            catalogo = new
            {
                longitud = new[] { "metro", "centimetro", "pie", "yarda", "milla" },
                masa = new[] { "gramo", "kilogramo", "libra", "onza", "quintal" },
                temperatura = new[] { "celsius", "fahrenheit", "kelvin", "newton", "reaumur" }
            }
        });
    }

    [HttpGet("/api/catalogo")]
    [HttpGet("/catalogo")]
    public ActionResult ObtenerCatalogo()
    {
        return Ok(new
        {
            exito = true,
            magnitudes = new Dictionary<string, List<string>>
            {
                ["longitud"] = ["metro", "centimetro", "pie", "yarda", "milla"],
                ["masa"] = ["gramo", "kilogramo", "libra", "onza", "quintal"],
                ["temperatura"] = ["celsius", "fahrenheit", "kelvin", "newton", "reaumur"]
            },
            mensaje = "Catálogo de unidades de medida (5 por magnitud)"
        });
    }
}
