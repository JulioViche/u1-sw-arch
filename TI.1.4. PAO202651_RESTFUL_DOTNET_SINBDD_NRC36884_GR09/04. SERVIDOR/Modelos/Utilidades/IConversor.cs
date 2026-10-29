namespace WSConversionUnidades_DotNet_REST_GR09.Modelos.Utilidades;

public interface IConversor<T> where T : struct, Enum
{
    double Convertir(double valor, T unidadOrigen, T unidadDestino);
}
