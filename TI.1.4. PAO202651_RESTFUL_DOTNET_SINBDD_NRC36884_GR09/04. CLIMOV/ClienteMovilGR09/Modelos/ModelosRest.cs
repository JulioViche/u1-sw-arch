namespace ClienteMovilGR09.Modelos;

public sealed record SolicitudInicioSesion(string Usuario, string Clave);

public sealed record TokenRespuesta(
    bool Exito = false,
    string? Token = null,
    string Tipo = "Bearer",
    string Usuario = "MONSTER",
    long ExpiraEnSegundos = 3600,
    string Mensaje = "");

public sealed record ConversionSolicitud(double Valor, string UnidadOrigen, string UnidadDestino);

public sealed record ConversionRespuesta(
    bool Exito = false,
    string Magnitud = "",
    double Valor = 0,
    string UnidadOrigen = "",
    string UnidadDestino = "",
    double Resultado = 0,
    string Mensaje = "");

public sealed record CatalogoRespuesta(
    bool Exito,
    Dictionary<string, List<string>> Magnitudes,
    string Mensaje);

public sealed record ErrorRespuesta(
    bool Exito,
    int Codigo,
    string Error,
    string Mensaje,
    string Ruta,
    DateTimeOffset Timestamp);

public sealed record RespuestaOperacion(bool Exito, string Mensaje);

public sealed class ExcepcionServicio(int codigo, string mensaje) : InvalidOperationException(mensaje)
{
    public int Codigo { get; } = codigo;
}

public sealed class EstadoCliente
{
    public string? Token { get; set; }
    public string Usuario { get; set; } = "";
}
