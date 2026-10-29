using Microsoft.AspNetCore.Mvc;
using WSConversionUnidades_DotNet_REST_GR09.Modelos;
using WSConversionUnidades_DotNet_REST_GR09.Seguridad;
using WSConversionUnidades_DotNet_REST_GR09.Servicios;

namespace WSConversionUnidades_DotNet_REST_GR09.Controladores;

[ApiController]
[Route("api/auth")]
[Route("auth")]
[Produces("application/json")]
public sealed class ControladorAutenticacion : ControllerBase
{
    private readonly ServicioAutenticacion _servicioAuth;
    private readonly AdministradorTokens _administradorTokens;

    public ControladorAutenticacion(ServicioAutenticacion servicioAuth, AdministradorTokens administradorTokens)
    {
        _servicioAuth = servicioAuth;
        _administradorTokens = administradorTokens;
    }

    [HttpPost("login")]
    public ActionResult<TokenRespuesta> IniciarSesion([FromBody] SolicitudInicioSesion? solicitud)
    {
        if (solicitud == null || string.IsNullOrWhiteSpace(solicitud.Usuario))
        {
            return BadRequest(new TokenRespuesta(false, null, "Cuerpo de solicitud requerido con usuario y contraseña."));
        }

        var respuesta = _servicioAuth.IniciarSesion(solicitud.Usuario, solicitud.Clave);
        if (!respuesta.Exito)
        {
            return Unauthorized(respuesta);
        }

        return Ok(respuesta);
    }

    [HttpPost("logout")]
    public ActionResult CerrarSesion([FromQuery] string? token, [FromBody] ConversionSolicitud? cuerpo)
    {
        string? tokenEfectivo = token ?? cuerpo?.Token ?? ExtraerBearerToken();
        if (!string.IsNullOrWhiteSpace(tokenEfectivo))
        {
            _administradorTokens.RevocarToken(tokenEfectivo);
        }

        return Ok(new { exito = true, mensaje = "Sesión cerrada satisfactoriamente en el servidor RESTful .NET." });
    }

    [HttpGet("validar")]
    public ActionResult ValidarToken([FromQuery] string? token)
    {
        string? tokenEfectivo = token ?? ExtraerBearerToken();
        if (string.IsNullOrWhiteSpace(tokenEfectivo) || !_administradorTokens.EsTokenValido(tokenEfectivo))
        {
            return Unauthorized(new { exito = false, mensaje = "Token inválido o expirado." });
        }

        return Ok(new { exito = true, mensaje = "Token activo y válido." });
    }

    private string? ExtraerBearerToken()
    {
        var authHeader = Request.Headers.Authorization.ToString();
        if (authHeader.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase))
        {
            return authHeader["Bearer ".Length..].Trim();
        }
        return null;
    }
}
