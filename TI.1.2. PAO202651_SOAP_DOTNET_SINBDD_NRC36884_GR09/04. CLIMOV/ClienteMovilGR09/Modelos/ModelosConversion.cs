namespace ClienteMovilGR09.Modelos;

public enum TipoConversion
{
    Longitud,
    Masa,
    Temperatura
}

public sealed record OpcionUnidad(string Codigo, string Etiqueta);

public sealed class ModeloSesion
{
    public string? Token { get; private set; }
    public string Usuario { get; private set; } = "MONSTER";
    public bool Autenticado => !string.IsNullOrWhiteSpace(Token);

    public void Iniciar(string token, string usuario = "MONSTER")
    {
        Token = token;
        Usuario = usuario;
    }

    public void Cerrar()
    {
        Token = null;
        Usuario = "MONSTER";
    }
}

public static class CatalogoUnidades
{
    // Exactamente 5 unidades por magnitud (REGLA 10 AGENTS.md)
    private static readonly OpcionUnidad[] Longitudes =
    [
        new("METROS", "Metros"),
        new("CENTIMETROS", "Centímetros"),
        new("PIES", "Pies"),
        new("YARDAS", "Yardas"),
        new("MILLAS", "Millas")
    ];

    private static readonly OpcionUnidad[] Masas =
    [
        new("KILOGRAMOS", "Kilogramos"),
        new("GRAMOS", "Gramos"),
        new("LIBRAS", "Libras"),
        new("ONZAS", "Onzas"),
        new("QUINTALES", "Quintales")
    ];

    private static readonly OpcionUnidad[] Temperaturas =
    [
        new("CELSIUS", "Celsius"),
        new("FAHRENHEIT", "Fahrenheit"),
        new("KELVIN", "Kelvin"),
        new("NEWTON", "Newton"),
        new("REAUMUR", "Réaumur")
    ];

    public static IReadOnlyList<OpcionUnidad> Unidades(TipoConversion tipo) => tipo switch
    {
        TipoConversion.Longitud => Longitudes,
        TipoConversion.Masa => Masas,
        TipoConversion.Temperatura => Temperaturas,
        _ => throw new ArgumentOutOfRangeException(nameof(tipo), tipo, null)
    };

    public static string Operacion(TipoConversion tipo) => tipo switch
    {
        TipoConversion.Longitud => "convertirLongitud",
        TipoConversion.Masa => "convertirMasa",
        TipoConversion.Temperatura => "convertirTemperatura",
        _ => throw new ArgumentOutOfRangeException(nameof(tipo), tipo, null)
    };
}
