namespace ClienteWeb_DotNet_SOAP_GR09.Modelos;

public sealed class ModeloSesion
{
    public string Usuario { get; set; } = string.Empty;
    public string Token { get; set; } = string.Empty;
    public bool Activo => !string.IsNullOrWhiteSpace(Token);

    public string TokenAbreviado =>
        string.IsNullOrEmpty(Token) ? "N/A" : (Token.Length > 8 ? Token[..8] + "..." : Token);
}

public sealed class ConversionViewModel
{
    public string Magnitud { get; set; } = "Longitud";
    public string UnidadOrigen { get; set; } = "Metro";
    public string UnidadDestino { get; set; } = "Centimetro";
    public double Valor { get; set; } = 100.0;
    public double? Resultado { get; set; }
    public bool Exito { get; set; }
    public string? MensajeError { get; set; }
}
