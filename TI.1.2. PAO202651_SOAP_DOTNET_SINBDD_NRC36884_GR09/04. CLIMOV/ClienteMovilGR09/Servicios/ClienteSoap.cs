using System.Globalization;
using System.Net.Http;
using System.Text;
using System.Xml.Linq;
using ClienteMovilGR09.Modelos;

namespace ClienteMovilGR09.Servicios;

public sealed class ClienteSOAP
{
    private const string EspacioNombres = "http://ws.controladores.monster.edu.ec/";
    private const string NombreContrato = "WSConversionUnidades_Java_SOAP_GR09";
    private const string EspacioSoap = "http://schemas.xmlsoap.org/soap/envelope/";
    private const string RutaServicio = "/04.SERVIDOR/WSConversionUnidades_DotNet_SOAP_GR09";
    private static readonly HttpClient ClienteHttp = new() { Timeout = TimeSpan.FromSeconds(8) };

    private static readonly string[] EndpointsCandidatos =
    [
        $"http://192.168.137.1:8733{RutaServicio}",
        $"http://10.40.64.228:8733{RutaServicio}",
        $"http://10.0.2.2:8733{RutaServicio}",
        $"http://127.0.0.1:8733{RutaServicio}"
    ];

    private Uri _endpointActivo;

    public ClienteSOAP(string? endpoint = null)
    {
        var direccion = endpoint
            ?? Environment.GetEnvironmentVariable("CONVERSOR_SOAP_URL")
            ?? EndpointsCandidatos[0];
        _endpointActivo = new Uri(direccion, UriKind.Absolute);
    }

    public Task<string> IniciarSesionAsync(string usuario, string contrasenia, CancellationToken cancellationToken = default) =>
        EnviarAsync("login", null, cancellationToken,
            ("usuario", usuario),
            ("contrasenia", contrasenia));

    public Task<string> CambiarContraseniaAsync(string token, string contraseniaActual, string contraseniaNueva, CancellationToken cancellationToken = default) =>
        EnviarAsync("cambiarContrasenia", token, cancellationToken,
            ("contraseniaActual", contraseniaActual),
            ("contraseniaNueva", contraseniaNueva));

    public async Task<double> ConvertirAsync(
        string token,
        TipoConversion tipo,
        double valor,
        string unidadInicial,
        string unidadFinal,
        CancellationToken cancellationToken = default)
    {
        var operacion = CatalogoUnidades.Operacion(tipo);
        var resultado = await EnviarAsync(operacion, token, cancellationToken,
            ("valor", valor.ToString("R", CultureInfo.InvariantCulture)),
            ("unidadInicial", unidadInicial),
            ("unidadFinal", unidadFinal));
        return double.Parse(resultado, NumberStyles.Float, CultureInfo.InvariantCulture);
    }

    private async Task<string> EnviarAsync(
        string operacion,
        string? token,
        CancellationToken cancellationToken,
        params (string Nombre, string Valor)[] parametros)
    {
        XNamespace soap = EspacioSoap;
        XNamespace servicio = EspacioNombres;
        var encabezado = new XElement(soap + "Header");
        if (!string.IsNullOrWhiteSpace(token))
        {
            encabezado.Add(new XElement(servicio + "token", token));
        }

        var solicitud = new XElement(servicio + operacion,
            parametros.Select(parametro => new XElement(servicio + parametro.Nombre, parametro.Valor)));
        var sobre = new XDocument(new XElement(soap + "Envelope",
            new XAttribute(XNamespace.Xmlns + "soap", soap),
            encabezado,
            new XElement(soap + "Body", solicitud)));

        var xmlCuerpo = sobre.ToString(SaveOptions.DisableFormatting);
        var accion = $"{EspacioNombres}{NombreContrato}/{operacion}";

        // Probar endpoint activo primero y luego fallbacks
        List<Uri> urlsAIntentar = [_endpointActivo];
        foreach (var c in EndpointsCandidatos)
        {
            var u = new Uri(c);
            if (!urlsAIntentar.Contains(u)) urlsAIntentar.Add(u);
        }

        Exception? ultimaExcepcion = null;

        foreach (var url in urlsAIntentar)
        {
            try
            {
                using var peticion = new HttpRequestMessage(HttpMethod.Post, url);
                peticion.Headers.TryAddWithoutValidation("SOAPAction", $"\"{accion}\"");
                peticion.Content = new StringContent(xmlCuerpo, Encoding.UTF8, "text/xml");

                using var respuesta = await ClienteHttp.SendAsync(peticion, cancellationToken);
                var xml = await respuesta.Content.ReadAsStringAsync(cancellationToken);
                var documento = XDocument.Parse(xml);

                var fallo = documento.Descendants().FirstOrDefault(elemento => elemento.Name.LocalName == "Fault");
                if (fallo is not null)
                {
                    var detalle = fallo.Descendants()
                        .FirstOrDefault(elemento => elemento.Name.LocalName is "faultstring" or "Text")
                        ?.Value;
                    throw new InvalidOperationException(detalle ?? "El servidor rechazó la operación SOAP.");
                }

                if (!respuesta.IsSuccessStatusCode)
                {
                    throw new HttpRequestException($"El servidor respondió HTTP {(int)respuesta.StatusCode}.");
                }

                var valorRetorno = documento.Descendants()
                    .FirstOrDefault(elemento => elemento.Name.LocalName == "return")
                    ?.Value;

                if (valorRetorno != null)
                {
                    _endpointActivo = url;
                    return valorRetorno;
                }
            }
            catch (Exception ex) when (ex is HttpRequestException or TaskCanceledException or System.Net.Sockets.SocketException)
            {
                ultimaExcepcion = ex;
                // Probar siguiente endpoint candidato
            }
        }

        throw new InvalidOperationException(
            $"No se pudo conectar con el servidor SOAP en {urlsAIntentar[0]}. Revise que el servidor esté iniciado en el puerto 8733.",
            ultimaExcepcion);
    }
}
