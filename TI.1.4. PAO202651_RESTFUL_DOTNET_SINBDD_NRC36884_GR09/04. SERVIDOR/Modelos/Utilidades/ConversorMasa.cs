using WSConversionUnidades_DotNet_REST_GR09.Modelos.Unidades;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos.Utilidades;

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
        UnidadMasa.Quintal => 45.359237, // Quintal estadounidense estándar (100 lb = 45.3592 kg)
        _ => throw new ArgumentOutOfRangeException(nameof(unidad), $"Unidad de masa no soportada: {unidad}")
    };
}
