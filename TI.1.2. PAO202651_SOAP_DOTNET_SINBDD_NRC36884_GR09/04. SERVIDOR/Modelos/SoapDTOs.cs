using System.Text.Json.Serialization;

namespace WSServidorSOAP_DotNet_GR09.Modelos;

public sealed class LoginResponse
{
    [JsonPropertyName("exito")]
    public bool Exito { get; set; }

    [JsonPropertyName("token")]
    public string? Token { get; set; }

    [JsonPropertyName("mensaje")]
    public string Mensaje { get; set; } = string.Empty;

    public LoginResponse() { }

    public LoginResponse(bool exito, string? token, string mensaje)
    {
        Exito = exito;
        Token = token;
        Mensaje = mensaje;
    }
}

public sealed class ConversionResponse
{
    [JsonPropertyName("exito")]
    public bool Exito { get; set; }

    [JsonPropertyName("valorConvertido")]
    public double ValorConvertido { get; set; }

    [JsonPropertyName("mensajeError")]
    public string? MensajeError { get; set; }

    public ConversionResponse() { }

    public ConversionResponse(bool exito, double valorConvertido, string? mensajeError)
    {
        Exito = exito;
        ValorConvertido = valorConvertido;
        MensajeError = mensajeError;
    }
}
