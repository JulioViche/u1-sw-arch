using System.Text.Json.Serialization;

namespace WSConversionUnidades_DotNet_REST_GR09.Modelos;

public sealed class ConversionSolicitud
{
    [JsonPropertyName("token")]
    public string? Token { get; set; }

    [JsonPropertyName("valor")]
    public double Valor { get; set; }

    [JsonPropertyName("origen")]
    public string Origen { get; set; } = string.Empty;

    [JsonPropertyName("destino")]
    public string Destino { get; set; } = string.Empty;

    // Alias compatibles
    [JsonPropertyName("value")]
    public double? Value { set => Valor = value ?? Valor; }

    [JsonPropertyName("from")]
    public string? From { set => Origen = string.IsNullOrEmpty(Origen) ? (value ?? "") : Origen; }

    [JsonPropertyName("to")]
    public string? To { set => Destino = string.IsNullOrEmpty(Destino) ? (value ?? "") : Destino; }
}
