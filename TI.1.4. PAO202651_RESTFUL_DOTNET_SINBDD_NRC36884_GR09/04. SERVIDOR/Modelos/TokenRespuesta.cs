using System.Text.Json.Serialization;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos;

public sealed record TokenRespuesta(
    [property: JsonPropertyName("exito")] bool Exito,
    [property: JsonPropertyName("token")] string? Token,
    [property: JsonPropertyName("mensaje")] string Mensaje
)
{
    // Alias para clientes en inglés o formato estándar
    [JsonPropertyName("success")]
    public bool Success => Exito;

    [JsonPropertyName("message")]
    public string Message => Mensaje;
}
