using WSServidorSOAP_DotNet_GR09.Modelos;
using WSServidorSOAP_DotNet_GR09.Seguridad;

namespace WSServidorSOAP_DotNet_GR09.Servicios;

public sealed class ServicioSOAP
{
    private const string UsuarioValido = "MONSTER";
    private const string ClaveValida = "MONSTER9";
    private const string ErrorTokenInvalido = "Acceso no autorizado: Token de sesión inválido o expirado. Debe iniciar sesión primero.";

    private readonly AdministradorTokens _tokens;
    private readonly ConversorLongitud _conversorLongitud = new();
    private readonly ConversorMasa _conversorMasa = new();
    private readonly ConversorTemperatura _conversorTemperatura = new();

    public ServicioSOAP(AdministradorTokens tokens)
    {
        _tokens = tokens;
    }

    public LoginResponse IniciarSesion(string? usuario, string? clave)
    {
        if (string.Equals(usuario?.Trim(), UsuarioValido, StringComparison.OrdinalIgnoreCase) &&
            string.Equals(clave?.Trim(), ClaveValida, StringComparison.Ordinal))
        {
            string token = _tokens.GenerarToken(usuario!.Trim().ToUpperInvariant());
            return new LoginResponse(true, token, "Inicio de sesión exitoso. Bienvenido al Conversor de Unidades SOAP .NET.");
        }

        return new LoginResponse(false, null, "Error de autenticación: Credenciales inválidas. (MONSTER / MONSTER9)");
    }

    public bool ValidarToken(string? token) => _tokens.ValidarToken(token);

    public void CerrarSesion(string? token) => _tokens.RevocarToken(token);

    public ConversionResponse ConvertirLongitud(string? token, double valor, string? origen, string? destino)
    {
        if (!_tokens.ValidarToken(token))
        {
            return new ConversionResponse(false, 0.0, ErrorTokenInvalido);
        }

        if (!TryParseUnidadLongitud(origen, out var uOrigen))
        {
            return new ConversionResponse(false, 0.0, $"Unidad de longitud de origen no válida: '{origen}'. Unidades permitidas: metro, centimetro, pie, yarda, milla.");
        }

        if (!TryParseUnidadLongitud(destino, out var uDestino))
        {
            return new ConversionResponse(false, 0.0, $"Unidad de longitud de destino no válida: '{destino}'. Unidades permitidas: metro, centimetro, pie, yarda, milla.");
        }

        try
        {
            double res = _conversorLongitud.Convertir(valor, uOrigen, uDestino);
            double redondeado = Math.Round(res, 2, MidpointRounding.AwayFromZero);
            return new ConversionResponse(true, redondeado, null);
        }
        catch (Exception ex)
        {
            return new ConversionResponse(false, 0.0, $"Error en unidades de longitud: {ex.Message}");
        }
    }

    public ConversionResponse ConvertirMasa(string? token, double valor, string? origen, string? destino)
    {
        if (!_tokens.ValidarToken(token))
        {
            return new ConversionResponse(false, 0.0, ErrorTokenInvalido);
        }

        if (!TryParseUnidadMasa(origen, out var uOrigen))
        {
            return new ConversionResponse(false, 0.0, $"Unidad de masa de origen no válida: '{origen}'. Unidades permitidas: gramo, kilogramo, libra, onza, quintal.");
        }

        if (!TryParseUnidadMasa(destino, out var uDestino))
        {
            return new ConversionResponse(false, 0.0, $"Unidad de masa de destino no válida: '{destino}'. Unidades permitidas: gramo, kilogramo, libra, onza, quintal.");
        }

        try
        {
            double res = _conversorMasa.Convertir(valor, uOrigen, uDestino);
            double redondeado = Math.Round(res, 2, MidpointRounding.AwayFromZero);
            return new ConversionResponse(true, redondeado, null);
        }
        catch (Exception ex)
        {
            return new ConversionResponse(false, 0.0, $"Error en unidades de masa: {ex.Message}");
        }
    }

    public ConversionResponse ConvertirTemperatura(string? token, double valor, string? origen, string? destino)
    {
        if (!_tokens.ValidarToken(token))
        {
            return new ConversionResponse(false, 0.0, ErrorTokenInvalido);
        }

        if (!TryParseUnidadTemperatura(origen, out var uOrigen))
        {
            return new ConversionResponse(false, 0.0, $"Escala de temperatura de origen no válida: '{origen}'. Escalas permitidas: celsius, fahrenheit, kelvin, newton, reaumur.");
        }

        if (!TryParseUnidadTemperatura(destino, out var uDestino))
        {
            return new ConversionResponse(false, 0.0, $"Escala de temperatura de destino no válida: '{destino}'. Escalas permitidas: celsius, fahrenheit, kelvin, newton, reaumur.");
        }

        try
        {
            double res = _conversorTemperatura.Convertir(valor, uOrigen, uDestino);
            double redondeado = Math.Round(res, 2, MidpointRounding.AwayFromZero);
            return new ConversionResponse(true, redondeado, null);
        }
        catch (Exception ex)
        {
            return new ConversionResponse(false, 0.0, $"Error en escalas de temperatura: {ex.Message}");
        }
    }

    private static string Normalizar(string? s) =>
        (s ?? string.Empty).Trim().ToLowerInvariant()
            .Replace("á", "a").Replace("é", "e").Replace("í", "i").Replace("ó", "o").Replace("ú", "u");

    private static bool TryParseUnidadLongitud(string? texto, out UnidadLongitud unidad)
    {
        switch (Normalizar(texto))
        {
            case "metro" or "metros" or "m":
                unidad = UnidadLongitud.Metro;
                return true;
            case "centimetro" or "centimetros" or "cm":
                unidad = UnidadLongitud.Centimetro;
                return true;
            case "pie" or "pies" or "ft":
                unidad = UnidadLongitud.Pie;
                return true;
            case "yarda" or "yardas" or "yd":
                unidad = UnidadLongitud.Yarda;
                return true;
            case "milla" or "millas" or "mi":
                unidad = UnidadLongitud.Milla;
                return true;
            default:
                unidad = default;
                return false;
        }
    }

    private static bool TryParseUnidadMasa(string? texto, out UnidadMasa unidad)
    {
        switch (Normalizar(texto))
        {
            case "gramo" or "gramos" or "g":
                unidad = UnidadMasa.Gramo;
                return true;
            case "kilogramo" or "kilogramos" or "kg":
                unidad = UnidadMasa.Kilogramo;
                return true;
            case "libra" or "libras" or "lb":
                unidad = UnidadMasa.Libra;
                return true;
            case "onza" or "onzas" or "oz":
                unidad = UnidadMasa.Onza;
                return true;
            case "quintal" or "quintales" or "q":
                unidad = UnidadMasa.Quintal;
                return true;
            default:
                unidad = default;
                return false;
        }
    }

    private static bool TryParseUnidadTemperatura(string? texto, out UnidadTemperatura unidad)
    {
        switch (Normalizar(texto))
        {
            case "celsius" or "centigrados" or "c":
                unidad = UnidadTemperatura.Celsius;
                return true;
            case "fahrenheit" or "f":
                unidad = UnidadTemperatura.Fahrenheit;
                return true;
            case "kelvin" or "k":
                unidad = UnidadTemperatura.Kelvin;
                return true;
            case "newton" or "n":
                unidad = UnidadTemperatura.Newton;
                return true;
            case "reaumur" or "re":
                unidad = UnidadTemperatura.Reaumur;
                return true;
            default:
                unidad = default;
                return false;
        }
    }
}
