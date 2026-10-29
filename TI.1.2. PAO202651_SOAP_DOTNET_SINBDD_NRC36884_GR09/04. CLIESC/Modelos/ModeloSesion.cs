namespace ClienteEscritorio_DotNet_SOAP_GR09.Modelos;

public sealed class ModeloSesion
{
    public string Usuario { get; set; } = string.Empty;
    public string Token { get; set; } = string.Empty;
    public bool Activo => !string.IsNullOrWhiteSpace(Token);

    public string TokenAbreviado =>
        string.IsNullOrEmpty(Token) ? "N/A" : (Token.Length > 8 ? Token[..8] + "..." : Token);
}
