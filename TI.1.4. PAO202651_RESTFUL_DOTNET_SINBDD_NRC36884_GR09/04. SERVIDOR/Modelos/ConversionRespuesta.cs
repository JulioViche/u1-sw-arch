using System.Text.Json.Serialization;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos;

public sealed record ConversionRespuesta(
    [property: JsonPropertyName("exito")] bool Exito,
    [property: JsonPropertyName("valorConvertido")] double ValorConvertido,
    [property: JsonPropertyName("mensajeError")] string? MensajeError
)
{
    // Alias para clientes en inglés o formato unificado
    [JsonPropertyName("success")]
    public bool Success => Exito;

    [JsonPropertyName("result")]
    public double Result => ValorConvertido;

    [JsonPropertyName("message")]
    public string? Message => MensajeError ?? (Exito ? "Conversión realizada exitosamente." : "Error en conversión.");
}
