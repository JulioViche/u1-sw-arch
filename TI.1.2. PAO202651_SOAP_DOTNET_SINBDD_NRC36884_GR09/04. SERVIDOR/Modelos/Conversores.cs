namespace WSServidorSOAP_DotNet_GR09.Modelos;

public interface IConversor<TUnidad> where TUnidad : struct, Enum
{
    double Convertir(double valor, TUnidad unidadOrigen, TUnidad unidadDestino);
}

public sealed class ConversorLongitud : IConversor<UnidadLongitud>
{
    public double Convertir(double valor, UnidadLongitud unidadOrigen, UnidadLongitud unidadDestino)
    {
        double valorEnMetros = valor * FactorHaciaMetro(unidadOrigen);
        return valorEnMetros / FactorHaciaMetro(unidadDestino);
    }

    private static double FactorHaciaMetro(UnidadLongitud unidad) => unidad switch
    {
        UnidadLongitud.Metro => 1.0,
        UnidadLongitud.Centimetro => 0.01,
        UnidadLongitud.Pie => 0.3048,
        UnidadLongitud.Yarda => 0.9144,
        UnidadLongitud.Milla => 1609.344,
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Unidad no soportada: {unidad}")
    };
}

public sealed class ConversorMasa : IConversor<UnidadMasa>
{
    public double Convertir(double valor, UnidadMasa unidadOrigen, UnidadMasa unidadDestino)
    {
        double valorEnKilogramos = valor * FactorHaciaKilogramo(unidadOrigen);
        return valorEnKilogramos / FactorHaciaKilogramo(unidadDestino);
    }

    private static double FactorHaciaKilogramo(UnidadMasa unidad) => unidad switch
    {
        UnidadMasa.Kilogramo => 1.0,
        UnidadMasa.Gramo => 0.001,
        UnidadMasa.Libra => 0.45359237,
        UnidadMasa.Onza => 0.028349523125,
        UnidadMasa.Quintal => 45.359237,
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Unidad no soportada: {unidad}")
    };
}

public sealed class ConversorTemperatura : IConversor<UnidadTemperatura>
{
    public double Convertir(double valor, UnidadTemperatura unidadOrigen, UnidadTemperatura unidadDestino)
    {
        if (unidadOrigen == unidadDestino) return valor;
        double celsius = A_Celsius(valor, unidadOrigen);
        return De_Celsius(celsius, unidadDestino);
    }

    private static double A_Celsius(double valor, UnidadTemperatura unidad) => unidad switch
    {
        UnidadTemperatura.Celsius => valor,
        UnidadTemperatura.Fahrenheit => (valor - 32.0) * 5.0 / 9.0,
        UnidadTemperatura.Kelvin => valor - 273.15,
        UnidadTemperatura.Newton => valor * 100.0 / 33.0,
        UnidadTemperatura.Reaumur => valor * 5.0 / 4.0,
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Escala no soportada: {unidad}")
    };

    private static double De_Celsius(double celsius, UnidadTemperatura unidad) => unidad switch
    {
        UnidadTemperatura.Celsius => celsius,
        UnidadTemperatura.Fahrenheit => (celsius * 9.0 / 5.0) + 32.0,
        UnidadTemperatura.Kelvin => celsius + 273.15,
        UnidadTemperatura.Newton => celsius * 33.0 / 100.0,
        UnidadTemperatura.Reaumur => celsius * 4.0 / 5.0,
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Escala no soportada: {unidad}")
    };
}
