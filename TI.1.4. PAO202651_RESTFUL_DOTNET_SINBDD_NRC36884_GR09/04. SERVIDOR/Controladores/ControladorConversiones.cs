using Microsoft.AspNetCore.Mvc;
using WSConversionUnidades_DotNet_REST_GR09.Modelos;
using WSConversionUnidades_DotNet_REST_GR09.Seguridad;
using WSConversionUnidades_DotNet_REST_GR09.Servicios;

namespace WSConversionUnidades_DotNet_REST_GR09.Controladores;

[ApiController]
[Route("api/conversor")]
[Route("conversor")]
[Produces("application/json")]
public sealed class ControladorConversiones : ControllerBase
{
    private readonly ServicioConversion _servicioConversion;
    private readonly AdministradorTokens _administradorTokens;

    public ControladorConversiones(ServicioConversion servicioConversion, AdministradorTokens administradorTokens)
    {
        _servicioConversion = servicioConversion;
        _administradorTokens = administradorTokens;
    }

    [HttpPost("longitud")]
    public ActionResult<ConversionRespuesta> ConvertirLongitud([FromBody] ConversionSolicitud? solicitud)
    {
        if (!ValidarAutenticacion(solicitud, out var errorAuth))
        {
            return Unauthorized(errorAuth);
        }

        if (solicitud == null)
        {
            return BadRequest(new ConversionRespuesta(false, 0, "Cuerpo de solicitud requerido."));
        }

        var resultado = _servicioConversion.ConvertirLongitud(solicitud.Origen, solicitud.Destino, solicitud.Valor);
        if (!resultado.Exito)
        {
            return BadRequest(resultado);
        }

        return Ok(resultado);
    }

    [HttpPost("masa")]
    public ActionResult<ConversionRespuesta> ConvertirMasa([FromBody] ConversionSolicitud? solicitud)
    {
        if (!ValidarAutenticacion(solicitud, out var errorAuth))
        {
            return Unauthorized(errorAuth);
        }

        if (solicitud == null)
        {
            return BadRequest(new ConversionRespuesta(false, 0, "Cuerpo de solicitud requerido."));
        }

        var resultado = _servicioConversion.ConvertirMasa(solicitud.Origen, solicitud.Destino, solicitud.Valor);
        if (!resultado.Exito)
        {
            return BadRequest(resultado);
        }

        return Ok(resultado);
    }

    [HttpPost("temperatura")]
    public ActionResult<ConversionRespuesta> ConvertirTemperatura([FromBody] ConversionSolicitud? solicitud)
    {
        if (!ValidarAutenticacion(solicitud, out var errorAuth))
        {
            return Unauthorized(errorAuth);
        }

        if (solicitud == null)
        {
            return BadRequest(new ConversionRespuesta(false, 0, "Cuerpo de solicitud requerido."));
        }

        var resultado = _servicioConversion.ConvertirTemperatura(solicitud.Origen, solicitud.Destino, solicitud.Valor);
        if (!resultado.Exito)
        {
            return BadRequest(resultado);
        }

        return Ok(resultado);
    }

    [HttpPost("{magnitud}")]
    public ActionResult<ConversionRespuesta> ConvertirGenerico(string magnitud, [FromBody] ConversionSolicitud? solicitud)
    {
        return (magnitud?.Trim().ToLowerInvariant()) switch
        {
            "longitud" or "length" => ConvertirLongitud(solicitud),
            "masa" or "mass" => ConvertirMasa(solicitud),
            "temperatura" or "temperature" => ConvertirTemperatura(solicitud),
            _ => BadRequest(new ConversionRespuesta(false, 0, $"Magnitud física no soportada: '{magnitud}'. Magnitudes válidas: longitud, masa, temperatura."))
        };
    }

    private bool ValidarAutenticacion(ConversionSolicitud? solicitud, out ConversionRespuesta? error)
    {
        string? token = solicitud?.Token;
        if (string.IsNullOrWhiteSpace(token))
        {
            var authHeader = Request.Headers.Authorization.ToString();
            if (authHeader.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase))
            {
                token = authHeader["Bearer ".Length..].Trim();
            }
        }

        if (string.IsNullOrWhiteSpace(token) || !_administradorTokens.EsTokenValido(token))
        {
            error = new ConversionRespuesta(false, 0, "Sesión inválida o expirada. Debe autenticarse previamente en /api/auth/login con credenciales MONSTER / MONSTER9.");
            return false;
        }

        error = null;
        return true;
    }
}
