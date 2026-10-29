using System.Globalization;
using System.Text;
using System.Xml.Linq;

namespace ClienteConsola_DotNet_SOAP_GR09.Servicios;

public sealed class ClienteServicioSoap
{
    private const string NsWs = "http://ws.monster.edu.ec/";

    private static readonly string[] UrlsCandidatas =
    [
        "http://192.168.137.1:8733/ServidorSOAP",
        "http://localhost:8733/ServidorSOAP",
        "http://127.0.0.1:8733/ServidorSOAP"
    ];

    private readonly HttpClient _http;
    private string _urlActiva;

    public ClienteServicioSoap()
    {
        _http = new HttpClient { Timeout = TimeSpan.FromSeconds(5) };
        _urlActiva = UrlsCandidatas[0];
    }

    public async Task<(bool Exito, string? Token, string? Mensaje)> IniciarSesionAsync(string usuario, string clave)
    {
        string soapXml = $"""
            <?xml version="1.0" encoding="utf-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="{NsWs}">
                <soapenv:Header/>
                <soapenv:Body>
                    <ws:iniciarSesion>
                        <usuario>{usuario}</usuario>
                        <clave>{clave}</clave>
                    </ws:iniciarSesion>
                </soapenv:Body>
            </soapenv:Envelope>
            """;

        foreach (var url in UrlsCandidatas)
        {
            try
            {
                var content = new StringContent(soapXml, Encoding.UTF8, "text/xml");
                var response = await _http.PostAsync(url, content);
                string responseXml = await response.Content.ReadAsStringAsync();

                var doc = XDocument.Parse(responseXml);
                var exitoElem = doc.Descendants().FirstOrDefault(e => e.Name.LocalName.Equals("exito", StringComparison.OrdinalIgnoreCase));
                var tokenElem = doc.Descendants().FirstOrDefault(e => e.Name.LocalName.Equals("token", StringComparison.OrdinalIgnoreCase));
                var mensajeElem = doc.Descendants().FirstOrDefault(e => e.Name.LocalName.Equals("mensaje", StringComparison.OrdinalIgnoreCase));

                bool exito = string.Equals(exitoElem?.Value, "true", StringComparison.OrdinalIgnoreCase);
                string? token = tokenElem?.Value;
                string? mensaje = mensajeElem?.Value;

                if (exito && !string.IsNullOrWhiteSpace(token))
                {
                    _urlActiva = url;
                    return (true, token, mensaje ?? "Inicio de sesión exitoso.");
                }

                if (!exito)
                {
                    return (false, null, mensaje ?? "Credenciales incorrectas.");
                }
            }
            catch
            {
                // Probar siguiente URL
            }
        }

        return (false, null, "No se pudo conectar con el servidor SOAP .NET (192.168.137.1:8733 o localhost:8733).");
    }

    public async Task<(bool Exito, double Resultado, string? MensajeError)> ConvertirAsync(string magnitud, string token, string origen, string destino, double valor)
    {
        string operacion = magnitud.Trim().ToLowerInvariant() switch
        {
            "longitud" => "convertirLongitud",
            "masa" => "convertirMasa",
            "temperatura" => "convertirTemperatura",
            _ => "convertir" + magnitud.Trim()
        };

        string valorStr = valor.ToString("F4", CultureInfo.InvariantCulture);

        string soapXml = $"""
            <?xml version="1.0" encoding="utf-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="{NsWs}">
                <soapenv:Header/>
                <soapenv:Body>
                    <ws:{operacion}>
                        <token>{token}</token>
                        <valor>{valorStr}</valor>
                        <unidadOrigen>{origen}</unidadOrigen>
                        <unidadDestino>{destino}</unidadDestino>
                    </ws:{operacion}>
                </soapenv:Body>
            </soapenv:Envelope>
            """;

        try
        {
            var content = new StringContent(soapXml, Encoding.UTF8, "text/xml");
            var response = await _http.PostAsync(_urlActiva, content);
            string responseXml = await response.Content.ReadAsStringAsync();

            var doc = XDocument.Parse(responseXml);
            var exitoElem = doc.Descendants().FirstOrDefault(e => e.Name.LocalName.Equals("exito", StringComparison.OrdinalIgnoreCase));
            var valorElem = doc.Descendants().FirstOrDefault(e => e.Name.LocalName.Equals("valorConvertido", StringComparison.OrdinalIgnoreCase));
            var errElem = doc.Descendants().FirstOrDefault(e => e.Name.LocalName.Equals("mensajeError", StringComparison.OrdinalIgnoreCase));

            bool exito = string.Equals(exitoElem?.Value, "true", StringComparison.OrdinalIgnoreCase);
            double resultado = 0;
            if (valorElem != null && double.TryParse(valorElem.Value, NumberStyles.Float, CultureInfo.InvariantCulture, out double v))
            {
                resultado = v;
            }

            return (exito, resultado, exito ? null : (errElem?.Value ?? "Error en conversión."));
        }
        catch (Exception ex)
        {
            return (false, 0, $"Error en comunicación SOAP: {ex.Message}");
        }
    }
}
