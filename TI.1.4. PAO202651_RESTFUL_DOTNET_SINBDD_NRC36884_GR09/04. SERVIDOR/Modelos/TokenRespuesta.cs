using System.Text.Json.Serialization;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos;

public sealed record TokenRespuesta(
    [property: JsonPropertyName("exito")] bool Exito,
    [property: JsonPropertyName("token")] string? Token,
    [property: JsonPropertyName("mensaje")] string Mensaje
)
{
    [JsonPropertyName("tipo")]
    public string Tipo => "Bearer";

    [JsonPropertyName("usuario")]
    public string Usuario => "MONSTER";

    [JsonPropertyName("expiraEnSegundos")]
    public long ExpiraEnSegundos => 3600;

    // Alias para clientes en inglés o formato estándar
    [JsonPropertyName("success")]
    public bool Success => Exito;

    [JsonPropertyName("message")]
    public string Message => Mensaje;
}
