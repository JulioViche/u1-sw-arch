using System.Globalization;
using System.Net.Http;
using System.Text;
using System.Xml.Linq;
using Microsoft.Maui.Devices;

namespace ClienteMovilSoapDotNetGR09.Services;

internal sealed class SoapClient
{
    private const string SoapNamespace = "http://schemas.xmlsoap.org/soap/envelope/";
    private const string ServiceNamespace = "http://ws.monster.edu.ec/";
    private static readonly HttpClient Http = new() { Timeout = TimeSpan.FromSeconds(10) };

    public static string GetDefaultBaseUrl() =>
        DeviceInfo.Current.DeviceType == DeviceType.Virtual
            ? "http://10.0.2.2:8733/ServidorSOAP"
            : "http://192.168.137.1:8733/ServidorSOAP";

    public static bool IsValidBaseUrl(string? value)
    {
        if (!Uri.TryCreate(value?.Trim(), UriKind.Absolute, out var uri))
        {
            return false;
        }

        return (uri.Scheme.Equals(Uri.UriSchemeHttp, StringComparison.OrdinalIgnoreCase) ||
                uri.Scheme.Equals(Uri.UriSchemeHttps, StringComparison.OrdinalIgnoreCase)) &&
               !string.IsNullOrWhiteSpace(uri.Host) &&
               string.IsNullOrEmpty(uri.UserInfo) &&
               string.IsNullOrEmpty(uri.Query) &&
               string.IsNullOrEmpty(uri.Fragment) &&
               uri.Port is >= 1 and <= 65535;
    }

    public static string NormalizeBaseUrl(string value) => value.Trim().TrimEnd('/');

    public async Task<LoginResult> LoginAsync(
        string baseUrl,
        string username,
        string password,
        CancellationToken cancellationToken = default)
    {
        var document = await SendAsync(
            baseUrl,
            "ServicioAutenticacion",
            "iniciarSesion",
            [
                new("usuario", username),
                new("clave", password)
            ],
            cancellationToken);

        return new LoginResult(
            ReadBoolean(document, "exito"),
            ReadValue(document, "token"),
            ReadValue(document, "mensaje"));
    }

    public async Task<ConversionResult> ConvertAsync(
        string baseUrl,
        string magnitude,
        string token,
        double value,
        string sourceUnit,
        string destinationUnit,
        CancellationToken cancellationToken = default)
    {
        var operation = magnitude switch
        {
            "Longitud" => "convertirLongitud",
            "Masa" => "convertirMasa",
            "Temperatura" => "convertirTemperatura",
            _ => throw new ArgumentOutOfRangeException(nameof(magnitude), "Magnitud no válida.")
        };

        var document = await SendAsync(
            baseUrl,
            "ServicioConversion",
            operation,
            [
                new("token", token),
                new("valor", value.ToString("R", CultureInfo.InvariantCulture)),
                new("unidadOrigen", sourceUnit),
                new("unidadDestino", destinationUnit)
            ],
            cancellationToken);

        var rawValue = ReadValue(document, "valorConvertido");
        if (!double.TryParse(rawValue, NumberStyles.Float, CultureInfo.InvariantCulture, out var converted))
        {
            throw new InvalidDataException("El servidor SOAP devolvió un resultado numérico inválido.");
        }

        return new ConversionResult(
            ReadBoolean(document, "exito"),
            converted,
            ReadValue(document, "mensajeError"));
    }

    private static async Task<XDocument> SendAsync(
        string baseUrl,
        string path,
        string operation,
        IEnumerable<KeyValuePair<string, string>> parameters,
        CancellationToken cancellationToken)
    {
        var soap = XNamespace.Get(SoapNamespace);
        var service = XNamespace.Get(ServiceNamespace);
        var operationElement = new XElement(service + operation);
        foreach (var (name, value) in parameters)
        {
            operationElement.Add(new XElement(service + name, value));
        }

        var envelope = new XDocument(
            new XElement(
                soap + "Envelope",
                new XAttribute(XNamespace.Xmlns + "soapenv", soap),
                new XAttribute(XNamespace.Xmlns + "ws", service),
                new XElement(soap + "Header"),
                new XElement(soap + "Body", operationElement)));

        using var request = new HttpRequestMessage(
            HttpMethod.Post,
            $"{NormalizeBaseUrl(baseUrl)}/{path}");
        request.Content = new StringContent(
            envelope.ToString(SaveOptions.DisableFormatting),
            Encoding.UTF8,
            "text/xml");
        request.Headers.TryAddWithoutValidation(
            "SOAPAction",
            $"\"{ServiceNamespace}{operation}\"");

        using var response = await Http.SendAsync(request, cancellationToken);
        var responseBody = await response.Content.ReadAsStringAsync(cancellationToken);
        if (!response.IsSuccessStatusCode)
        {
            throw new HttpRequestException(
                $"El servidor devolvió HTTP {(int)response.StatusCode}: {responseBody}");
        }

        return XDocument.Parse(responseBody);
    }

    private static string ReadValue(XDocument document, string name) =>
        document.Descendants().FirstOrDefault(element => element.Name.LocalName == name)?.Value ?? string.Empty;

    private static bool ReadBoolean(XDocument document, string name) =>
        bool.TryParse(ReadValue(document, name), out var value) && value;
}

internal sealed record LoginResult(bool Success, string Token, string Message);

internal sealed record ConversionResult(bool Success, double Value, string Message);
