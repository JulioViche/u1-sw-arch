using WSConversionUnidades_DotNet_REST_GR09.Modelos.Unidades;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos.Utilidades;

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
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Escala de temperatura no soportada: {unidad}")
    };

    private static double De_Celsius(double celsius, UnidadTemperatura unidad) => unidad switch
    {
        UnidadTemperatura.Celsius => celsius,
        UnidadTemperatura.Fahrenheit => (celsius * 9.0 / 5.0) + 32.0,
        UnidadTemperatura.Kelvin => celsius + 273.15,
        UnidadTemperatura.Newton => celsius * 33.0 / 100.0,
        UnidadTemperatura.Reaumur => celsius * 4.0 / 5.0,
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Escala de temperatura no soportada: {unidad}")
    };
}
