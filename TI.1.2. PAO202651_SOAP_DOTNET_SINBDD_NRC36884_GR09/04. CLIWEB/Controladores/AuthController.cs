using Microsoft.AspNetCore.Mvc;
using ClienteWeb_DotNet_SOAP_GR09.Servicios;

namespace ClienteWeb_DotNet_SOAP_GR09.Controladores;

public sealed class AuthController : Controller
{
    private readonly ClienteServicioSoap _servicio;

    public AuthController(ClienteServicioSoap servicio)
    {
        _servicio = servicio;
    }

    [HttpGet("/")]
    [HttpGet("/login")]
    public IActionResult Login(string? error, string? mensaje)
    {
        string? token = HttpContext.Session.GetString("TOKEN");
        if (!string.IsNullOrEmpty(token))
        {
            return RedirectToAction("Index", "Conversor");
        }

        ViewBag.Error = error;
        ViewBag.Mensaje = mensaje;
        return View();
    }

    [HttpPost("/login")]
    public async Task<IActionResult> ProcesarLogin([FromForm] string usuario, [FromForm] string clave)
    {
        if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(clave))
        {
            ViewBag.Error = "Debe ingresar el usuario y la contraseña.";
            return View("Login");
        }

        var (exito, token, mensaje) = await _servicio.IniciarSesionAsync(usuario.Trim(), clave.Trim());
        if (!exito || string.IsNullOrWhiteSpace(token))
        {
            ViewBag.Error = mensaje ?? "Usuario o contraseña incorrectos. Credenciales válidas: MONSTER / MONSTER9";
            return View("Login");
        }

        HttpContext.Session.SetString("TOKEN", token);
        HttpContext.Session.SetString("USUARIO", usuario.Trim().ToUpperInvariant());

        return RedirectToAction("Index", "Conversor");
    }

    [HttpGet("/logout")]
    public IActionResult Logout()
    {
        HttpContext.Session.Clear();
        return RedirectToAction("Login", new { mensaje = "Ha cerrado su sesión exitosamente en el cliente Web SOAP .NET." });
    }
}
