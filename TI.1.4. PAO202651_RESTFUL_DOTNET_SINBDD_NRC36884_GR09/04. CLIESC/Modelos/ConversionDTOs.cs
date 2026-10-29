using System.Text.Json.Serialization;

namespace ClienteEscritorio_DotNet_REST_GR09.Modelos;

public sealed class ConversionSolicitud
{
    [JsonPropertyName("token")]
    public string Token { get; set; } = string.Empty;

    [JsonPropertyName("valor")]
    public double Valor { get; set; }

    [JsonPropertyName("origen")]
    public string Origen { get; set; } = string.Empty;

    [JsonPropertyName("destino")]
    public string Destino { get; set; } = string.Empty;

    [JsonPropertyName("from")]
    public string From { get => Origen; set => Origen = string.IsNullOrEmpty(Origen) ? value : Origen; }

    [JsonPropertyName("to")]
    public string To { get => Destino; set => Destino = string.IsNullOrEmpty(Destino) ? value : Destino; }

    [JsonPropertyName("value")]
    public double Value { get => Valor; set => Valor = value; }
}

public sealed class ConversionRespuesta
{
    [JsonPropertyName("exito")]
    public bool Exito { get; set; }

    [JsonPropertyName("valorConvertido")]
    public double ValorConvertido { get; set; }

    [JsonPropertyName("mensajeError")]
    public string? MensajeError { get; set; }

    [JsonPropertyName("success")]
    public bool Success { set => Exito = Exito || value; }

    [JsonPropertyName("result")]
    public double Result { set => ValorConvertido = ValorConvertido != 0 ? ValorConvertido : value; }

    [JsonPropertyName("message")]
    public string? Message { set => MensajeError ??= value; }
}

public sealed class TokenRespuesta
{
    [JsonPropertyName("exito")]
    public bool Exito { get; set; }

    [JsonPropertyName("token")]
    public string? Token { get; set; }

    [JsonPropertyName("mensaje")]
    public string? Mensaje { get; set; }

    [JsonPropertyName("success")]
    public bool Success { set => Exito = Exito || value; }

    [JsonPropertyName("message")]
    public string? Message { set => Mensaje ??= value; }
}
