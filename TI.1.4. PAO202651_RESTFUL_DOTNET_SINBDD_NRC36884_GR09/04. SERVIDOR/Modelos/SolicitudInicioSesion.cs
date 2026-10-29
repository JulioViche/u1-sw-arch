using System.Text.Json.Serialization;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos;

public sealed class SolicitudInicioSesion
{
    [JsonPropertyName("usuario")]
    public string Usuario { get; set; } = string.Empty;

    [JsonPropertyName("clave")]
    public string Clave { get; set; } = string.Empty;

    // Alias compatibles para máxima interoperabilidad
    [JsonPropertyName("username")]
    public string? Username { set => Usuario = string.IsNullOrEmpty(Usuario) ? (value ?? "") : Usuario; }

    [JsonPropertyName("password")]
    public string? Password { set => Clave = string.IsNullOrEmpty(Clave) ? (value ?? "") : Clave; }

    [JsonPropertyName("from")]
    public string? From { set => Usuario = string.IsNullOrEmpty(Usuario) ? (value ?? "") : Usuario; }

    [JsonPropertyName("to")]
    public string? To { set => Clave = string.IsNullOrEmpty(Clave) ? (value ?? "") : Clave; }
}
