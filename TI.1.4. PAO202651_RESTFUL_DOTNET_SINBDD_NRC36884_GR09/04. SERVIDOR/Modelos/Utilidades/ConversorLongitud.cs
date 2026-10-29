using WSConversionUnidades_DotNet_REST_GR09.Modelos.Unidades;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos.Utilidades;

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
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Unidad de longitud no soportada: {unidad}")
    };
}
