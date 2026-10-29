using System.Globalization;
using System.Text.RegularExpressions;
using Microsoft.AspNetCore.Mvc;
using ClienteWeb_DotNet_REST_GR09.Modelos;
using ClienteWeb_DotNet_REST_GR09.Servicios;

namespace ClienteWeb_DotNet_REST_GR09.Controladores;

public sealed class ConversorController : Controller
{
    private readonly ClienteServicioRest _servicio;

    public ConversorController(ClienteServicioRest servicio)
    {
        _servicio = servicio;
    }

    [HttpGet("/conversor")]
    public IActionResult Index()
    {
        string? token = HttpContext.Session.GetString("TOKEN");
        string? usuario = HttpContext.Session.GetString("USUARIO");

        if (string.IsNullOrEmpty(token) || string.IsNullOrEmpty(usuario))
        {
            return RedirectToAction("Login", "Auth", new { error = "Su sesión no es válida o ha expirado. Autentíquese nuevamente." });
        }

        ViewBag.Usuario = usuario;
        ViewBag.Token = token;
        ViewBag.TokenAbreviado = token.Length > 8 ? token[..8] + "..." : token;

        var modelo = new ConversionViewModel
        {
            Magnitud = "Longitud",
            UnidadOrigen = "Metro",
            UnidadDestino = "Centimetro",
            Valor = 100.0,
            Exito = false
        };

        return View(modelo);
    }

    [HttpPost("/convertir")]
    public async Task<IActionResult> Convertir([FromForm] string magnitud, [FromForm] string unidadOrigen, [FromForm] string unidadDestino, [FromForm] string valor)
    {
        string? token = HttpContext.Session.GetString("TOKEN");
        string? usuario = HttpContext.Session.GetString("USUARIO");

        if (string.IsNullOrEmpty(token) || string.IsNullOrEmpty(usuario))
        {
            return RedirectToAction("Login", "Auth", new { error = "Su sesión no es válida o ha expirado. Autentíquese nuevamente." });
        }

        ViewBag.Usuario = usuario;
        ViewBag.Token = token;
        ViewBag.TokenAbreviado = token.Length > 8 ? token[..8] + "..." : token;

        var modelo = new ConversionViewModel
        {
            Magnitud = string.IsNullOrWhiteSpace(magnitud) ? "Longitud" : magnitud,
            UnidadOrigen = unidadOrigen,
            UnidadDestino = unidadDestino
        };

        // Validación estricta: 16 caracteres y regex ^-?\d+(\.\d+)?$
        string input = (valor ?? string.Empty).Trim();
        if (input.Length > 16 || !Regex.IsMatch(input, @"^-?\d+(\.\d+)?$"))
        {
            modelo.Exito = false;
            modelo.MensajeError = "Entrada inválida. Debe ser numérico, hasta 16 caracteres y punto decimal '.' (ej: 100, -15.5 o 25.5).";
            return View("Index", modelo);
        }

        if (!double.TryParse(input, NumberStyles.Float, CultureInfo.InvariantCulture, out double valNumerico))
        {
            modelo.Exito = false;
            modelo.MensajeError = "Valor numérico no válido.";
            return View("Index", modelo);
        }

        modelo.Valor = valNumerico;

        var (exito, resultado, err) = await _servicio.ConvertirAsync(modelo.Magnitud, token, unidadOrigen, unidadDestino, valNumerico);
        modelo.Exito = exito;
        modelo.Resultado = resultado;
        modelo.MensajeError = err;

        return View("Index", modelo);
    }
}
