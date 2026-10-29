using WSConversionUnidades_DotNet_REST_GR09.Modelos;
using WSConversionUnidades_DotNet_REST_GR09.Modelos.Unidades;
using WSConversionUnidades_DotNet_REST_GR09.Modelos.Utilidades;

namespace WSConversionUnidades_DotNet_REST_GR09.Servicios;

public sealed class ServicioConversion
{
    private readonly ConversorLongitud _conversorLongitud = new();
    private readonly ConversorMasa _conversorMasa = new();
    private readonly ConversorTemperatura _conversorTemperatura = new();

    public ConversionRespuesta ConvertirLongitud(string origen, string destino, double valor)
    {
        if (!TryParseUnidadLongitud(origen, out var uOrigen))
        {
            return new ConversionRespuesta(false, 0, $"Unidad de longitud de origen no válida: '{origen}'. Unidades permitidas: metro, centimetro, pie, yarda, milla.");
        }

        if (!TryParseUnidadLongitud(destino, out var uDestino))
        {
            return new ConversionRespuesta(false, 0, $"Unidad de longitud de destino no válida: '{destino}'. Unidades permitidas: metro, centimetro, pie, yarda, milla.");
        }

        try
        {
            double res = _conversorLongitud.Convertir(valor, uOrigen, uDestino);
            double redondeado = Math.Round(res, 2, MidpointRounding.AwayFromZero);
            return new ConversionRespuesta(true, redondeado, null);
        }
        catch (Exception ex)
        {
            return new ConversionRespuesta(false, 0, $"Error en conversión de longitud: {ex.Message}");
        }
    }

    public ConversionRespuesta ConvertirMasa(string origen, string destino, double valor)
    {
        if (!TryParseUnidadMasa(origen, out var uOrigen))
        {
            return new ConversionRespuesta(false, 0, $"Unidad de masa de origen no válida: '{origen}'. Unidades permitidas: gramo, kilogramo, libra, onza, quintal.");
        }

        if (!TryParseUnidadMasa(destino, out var uDestino))
        {
            return new ConversionRespuesta(false, 0, $"Unidad de masa de destino no válida: '{destino}'. Unidades permitidas: gramo, kilogramo, libra, onza, quintal.");
        }

        try
        {
            double res = _conversorMasa.Convertir(valor, uOrigen, uDestino);
            double redondeado = Math.Round(res, 2, MidpointRounding.AwayFromZero);
            return new ConversionRespuesta(true, redondeado, null);
        }
        catch (Exception ex)
        {
            return new ConversionRespuesta(false, 0, $"Error en conversión de masa: {ex.Message}");
        }
    }

    public ConversionRespuesta ConvertirTemperatura(string origen, string destino, double valor)
    {
        if (!TryParseUnidadTemperatura(origen, out var uOrigen))
        {
            return new ConversionRespuesta(false, 0, $"Escala de temperatura de origen no válida: '{origen}'. Escalas permitidas: celsius, fahrenheit, kelvin, newton, reaumur.");
        }

        if (!TryParseUnidadTemperatura(destino, out var uDestino))
        {
            return new ConversionRespuesta(false, 0, $"Escala de temperatura de destino no válida: '{destino}'. Escalas permitidas: celsius, fahrenheit, kelvin, newton, reaumur.");
        }

        try
        {
            double res = _conversorTemperatura.Convertir(valor, uOrigen, uDestino);
            double redondeado = Math.Round(res, 2, MidpointRounding.AwayFromZero);
            return new ConversionRespuesta(true, redondeado, null);
        }
        catch (Exception ex)
        {
            return new ConversionRespuesta(false, 0, $"Error en conversión de temperatura: {ex.Message}");
        }
    }

    private static string Normalizar(string texto) =>
        (texto ?? string.Empty).Trim().ToLowerInvariant()
            .Replace("á", "a").Replace("é", "e").Replace("í", "i").Replace("ó", "o").Replace("ú", "u");

    private static bool TryParseUnidadLongitud(string texto, out UnidadLongitud unidad)
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

    private static bool TryParseUnidadMasa(string texto, out UnidadMasa unidad)
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

    private static bool TryParseUnidadTemperatura(string texto, out UnidadTemperatura unidad)
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
