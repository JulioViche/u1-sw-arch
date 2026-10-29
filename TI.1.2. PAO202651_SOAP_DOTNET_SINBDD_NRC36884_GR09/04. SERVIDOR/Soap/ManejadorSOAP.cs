using System.Globalization;
using System.Text;
using System.Xml.Linq;
using WSServidorSOAP_DotNet_GR09.Modelos;
using WSServidorSOAP_DotNet_GR09.Servicios;

namespace WSServidorSOAP_DotNet_GR09.Soap;

public sealed class ManejadorSOAP
{
    private const string NsSoap = "http://schemas.xmlsoap.org/soap/envelope/";
    private const string NsWs = "http://ws.monster.edu.ec/";

    private readonly ServicioSOAP _servicio;

    public ManejadorSOAP(ServicioSOAP servicio)
    {
        _servicio = servicio;
    }

    public async Task ProcesarSolicitudAsync(HttpContext context)
    {
        if (HttpMethods.IsGet(context.Request.Method))
        {
            if (context.Request.QueryString.Value?.Contains("wsdl", StringComparison.OrdinalIgnoreCase) == true)
            {
                context.Response.ContentType = "text/xml; charset=utf-8";
                await context.Response.WriteAsync(GenerarWsdl(context.Request.Host.Value ?? "localhost:8733"), Encoding.UTF8);
                return;
            }

            // Página informativa
            context.Response.ContentType = "text/html; charset=utf-8";
            await context.Response.WriteAsync(
                """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Servidor SOAP .NET - Conversor de Unidades GR09</title>
                    <style>
                        body { font-family: 'Segoe UI', sans-serif; background: #0F172A; color: #F8FAFC; margin: 0; padding: 40px; display: flex; justify-content: center; }
                        .card { background: #1E293B; border-radius: 12px; padding: 32px; max-width: 640px; width: 100%; box-shadow: 0 10px 25px rgba(0,0,0,0.5); border: 1px solid #334155; }
                        h1 { color: #38BDF8; font-size: 1.5rem; }
                        .badge { background: #0284C7; color: white; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: bold; }
                        p { color: #94A3B8; }
                        a { color: #38BDF8; text-decoration: none; font-weight: bold; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <span class="badge">ACTIVO · PUERTO 8733</span>
                        <h1>ESPE · Servidor Web SOAP .NET 10 (SINBDD)</h1>
                        <p>Tarea TI.1.2 (PAO 2026-51) · NRC: 36884 · Grupo: GR09 · Julio Enrique Viche Castillo</p>
                        <p>WSDL del servicio: <a href="/ServidorSOAP?wsdl">/ServidorSOAP?wsdl</a></p>
                    </div>
                </body>
                </html>
                """, Encoding.UTF8);
            return;
        }

        // Petición POST SOAP
        using var reader = new StreamReader(context.Request.Body, Encoding.UTF8);
        string cuerpo = await reader.ReadToEndAsync();

        if (string.IsNullOrWhiteSpace(cuerpo))
        {
            context.Response.StatusCode = 400;
            return;
        }

        try
        {
            var doc = XDocument.Parse(cuerpo);
            var xnSoap = XNamespace.Get(NsSoap);
            var body = doc.Descendants(xnSoap + "Body").FirstOrDefault() ?? doc.Root;

            if (body is null)
            {
                await EnviarFalloSoapAsync(context, "Client", "SOAP Envelope sin Body.");
                return;
            }

            var operacion = body.Elements().FirstOrDefault();
            if (operacion is null)
            {
                await EnviarFalloSoapAsync(context, "Client", "No se especificó operación SOAP.");
                return;
            }

            string nombreOp = operacion.Name.LocalName;
            string respuestaXml;

            switch (nombreOp)
            {
                case "iniciarSesion" or "login":
                {
                    string usuario = operacion.Elements().FirstOrDefault(e => e.Name.LocalName.Equals("usuario", StringComparison.OrdinalIgnoreCase))?.Value ?? "";
                    string clave = operacion.Elements().FirstOrDefault(e => e.Name.LocalName.Equals("clave", StringComparison.OrdinalIgnoreCase))?.Value ?? "";
                    var resp = _servicio.IniciarSesion(usuario, clave);
                    respuestaXml = ConstruirXmlLogin(resp);
                    break;
                }

                case "validarToken":
                {
                    string token = operacion.Elements().FirstOrDefault(e => e.Name.LocalName.Equals("token", StringComparison.OrdinalIgnoreCase))?.Value ?? "";
                    bool valido = _servicio.ValidarToken(token);
                    respuestaXml = $"""
                        <ws:validarTokenResponse xmlns:ws="{NsWs}">
                            <esValido>{(valido ? "true" : "false")}</esValido>
                        </ws:validarTokenResponse>
                        """;
                    break;
                }

                case "convertirLongitud":
                {
                    var (tok, val, ori, des) = ExtraerParametrosConversion(operacion);
                    var resp = _servicio.ConvertirLongitud(tok, val, ori, des);
                    respuestaXml = ConstruirXmlConversion("convertirLongitudResponse", resp);
                    break;
                }

                case "convertirMasa":
                {
                    var (tok, val, ori, des) = ExtraerParametrosConversion(operacion);
                    var resp = _servicio.ConvertirMasa(tok, val, ori, des);
                    respuestaXml = ConstruirXmlConversion("convertirMasaResponse", resp);
                    break;
                }

                case "convertirTemperatura":
                {
                    var (tok, val, ori, des) = ExtraerParametrosConversion(operacion);
                    var resp = _servicio.ConvertirTemperatura(tok, val, ori, des);
                    respuestaXml = ConstruirXmlConversion("convertirTemperaturaResponse", resp);
                    break;
                }

                default:
                    await EnviarFalloSoapAsync(context, "Client", $"Operación SOAP no reconocida: {nombreOp}");
                    return;
            }

            string envelope = $"""
                <?xml version="1.0" encoding="utf-8"?>
                <soapenv:Envelope xmlns:soapenv="{NsSoap}" xmlns:ws="{NsWs}">
                    <soapenv:Header/>
                    <soapenv:Body>
                        {respuestaXml}
                    </soapenv:Body>
                </soapenv:Envelope>
                """;

            context.Response.ContentType = "text/xml; charset=utf-8";
            await context.Response.WriteAsync(envelope, Encoding.UTF8);
        }
        catch (Exception ex)
        {
            await EnviarFalloSoapAsync(context, "Server", ex.Message);
        }
    }

    private static (string? token, double valor, string? origen, string? destino) ExtraerParametrosConversion(XElement op)
    {
        string? token = op.Elements().FirstOrDefault(e => e.Name.LocalName.Equals("token", StringComparison.OrdinalIgnoreCase))?.Value;
        string? valStr = op.Elements().FirstOrDefault(e => e.Name.LocalName.Equals("valor", StringComparison.OrdinalIgnoreCase) || e.Name.LocalName.Equals("value", StringComparison.OrdinalIgnoreCase))?.Value;
        string? origen = op.Elements().FirstOrDefault(e => e.Name.LocalName.Equals("unidadOrigen", StringComparison.OrdinalIgnoreCase) || e.Name.LocalName.Equals("from", StringComparison.OrdinalIgnoreCase))?.Value;
        string? destino = op.Elements().FirstOrDefault(e => e.Name.LocalName.Equals("unidadDestino", StringComparison.OrdinalIgnoreCase) || e.Name.LocalName.Equals("to", StringComparison.OrdinalIgnoreCase))?.Value;

        double.TryParse(valStr, NumberStyles.Float, CultureInfo.InvariantCulture, out double valor);
        return (token, valor, origen, destino);
    }

    private static string ConstruirXmlLogin(LoginResponse resp) =>
        $"""
        <ws:iniciarSesionResponse xmlns:ws="{NsWs}">
            <loginResult>
                <exito>{(resp.Exito ? "true" : "false")}</exito>
                <token>{resp.Token ?? ""}</token>
                <mensaje>{resp.Mensaje}</mensaje>
            </loginResult>
        </ws:iniciarSesionResponse>
        """;

    private static string ConstruirXmlConversion(string tagRespuesta, ConversionResponse resp) =>
        $"""
        <ws:{tagRespuesta} xmlns:ws="{NsWs}">
            <conversionResult>
                <exito>{(resp.Exito ? "true" : "false")}</exito>
                <valorConvertido>{resp.ValorConvertido.ToString("F2", CultureInfo.InvariantCulture)}</valorConvertido>
                <mensajeError>{resp.MensajeError ?? ""}</mensajeError>
            </conversionResult>
        </ws:{tagRespuesta}>
        """;

    private static async Task EnviarFalloSoapAsync(HttpContext context, string code, string message)
    {
        context.Response.StatusCode = 500;
        context.Response.ContentType = "text/xml; charset=utf-8";
        string fault = $"""
            <?xml version="1.0" encoding="utf-8"?>
            <soapenv:Envelope xmlns:soapenv="{NsSoap}">
                <soapenv:Body>
                    <soapenv:Fault>
                        <faultcode>soapenv:{code}</faultcode>
                        <faultstring>{message}</faultstring>
                    </soapenv:Fault>
                </soapenv:Body>
            </soapenv:Envelope>
            """;
        await context.Response.WriteAsync(fault, Encoding.UTF8);
    }

    private static string GenerarWsdl(string host) =>
        $"""
        <?xml version="1.0" encoding="UTF-8"?>
        <definitions xmlns="http://schemas.xmlsoap.org/wsdl/"
                     xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
                     xmlns:tns="{NsWs}"
                     xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                     targetNamespace="{NsWs}"
                     name="ServicioConversion">
            <types>
                <xsd:schema targetNamespace="{NsWs}" elementFormDefault="qualified">
                    <xsd:element name="iniciarSesion">
                        <xsd:complexType>
                            <xsd:sequence>
                                <xsd:element name="usuario" type="xsd:string"/>
                                <xsd:element name="clave" type="xsd:string"/>
                            </xsd:sequence>
                        </xsd:complexType>
                    </xsd:element>
                    <xsd:element name="iniciarSesionResponse">
                        <xsd:complexType>
                            <xsd:sequence>
                                <xsd:element name="loginResult">
                                    <xsd:complexType>
                                        <xsd:sequence>
                                            <xsd:element name="exito" type="xsd:boolean"/>
                                            <xsd:element name="token" type="xsd:string" minOccurs="0"/>
                                            <xsd:element name="mensaje" type="xsd:string"/>
                                        </xsd:sequence>
                                    </xsd:complexType>
                                </xsd:element>
                            </xsd:sequence>
                        </xsd:complexType>
                    </xsd:element>
                    <xsd:element name="convertirLongitud">
                        <xsd:complexType>
                            <xsd:sequence>
                                <xsd:element name="token" type="xsd:string"/>
                                <xsd:element name="valor" type="xsd:double"/>
                                <xsd:element name="unidadOrigen" type="xsd:string"/>
                                <xsd:element name="unidadDestino" type="xsd:string"/>
                            </xsd:sequence>
                        </xsd:complexType>
                    </xsd:element>
                    <xsd:element name="convertirLongitudResponse">
                        <xsd:complexType>
                            <xsd:sequence>
                                <xsd:element name="conversionResult">
                                    <xsd:complexType>
                                        <xsd:sequence>
                                            <xsd:element name="exito" type="xsd:boolean"/>
                                            <xsd:element name="valorConvertido" type="xsd:double"/>
                                            <xsd:element name="mensajeError" type="xsd:string" minOccurs="0"/>
                                        </xsd:sequence>
                                    </xsd:complexType>
                                </xsd:element>
                            </xsd:sequence>
                        </xsd:complexType>
                    </xsd:element>
                </xsd:schema>
            </types>
            <service name="ServicioConversion">
                <port name="ServicioConversionPort" binding="tns:ServicioConversionBinding">
                    <soap:address location="http://{host}/ServidorSOAP"/>
                </port>
            </service>
        </definitions>
        """;
}
