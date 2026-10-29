using WSConversionUnidades_DotNet_REST_GR09.Modelos;
using WSConversionUnidades_DotNet_REST_GR09.Seguridad;

namespace WSConversionUnidades_DotNet_REST_GR09.Servicios;

public sealed class ServicioAutenticacion(AdministradorTokens administradorTokens)
{
    private const string UsuarioValido = "MONSTER";
    private const string ClaveValida = "MONSTER9";

    public TokenRespuesta IniciarSesion(SolicitudInicioSesion solicitud) =>
        IniciarSesion(solicitud.Usuario, solicitud.Clave);

    public TokenRespuesta IniciarSesion(string? usuario, string? clave)
    {
        if (string.Equals(usuario?.Trim(), UsuarioValido, StringComparison.OrdinalIgnoreCase) &&
            string.Equals(clave?.Trim(), ClaveValida, StringComparison.Ordinal))
        {
            string token = administradorTokens.GenerarToken(usuario!.Trim().ToUpperInvariant());
            return new TokenRespuesta(true, token, "Inicio de sesión exitoso en .NET RESTful (SINBDD)");
        }

        return new TokenRespuesta(false, null, "Credenciales inválidas. Usuario o contraseña incorrectos. Credenciales válidas: MONSTER / MONSTER9");
    }

    public void CerrarSesion(string? token)
    {
        administradorTokens.RevocarToken(token);
    }
}
