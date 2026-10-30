using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;

namespace ClienteWeb_DotNet_REST_GR09.Servicios;

public sealed class ClienteServicioRest
{
    private static readonly string[] UrlsBase =
    [
        "http://192.168.137.1:5000/api",
        "http://10.40.64.228:5000/api",
        "http://localhost:5000/api",
        "http://127.0.0.1:5000/api"
    ];

    private readonly HttpClient _http;
    private string _urlActiva;

    public ClienteServicioRest()
    {
        _http = new HttpClient { Timeout = TimeSpan.FromSeconds(5) };
        _urlActiva = UrlsBase[0];
    }

    public async Task<(bool Exito, string? Token, string? Mensaje)> IniciarSesionAsync(string usuario, string clave)
    {
        var payload = new { usuario, clave, from = usuario, to = clave };

        foreach (var url in UrlsBase)
        {
            try
            {
                var response = await _http.PostAsJsonAsync($"{url}/auth/login", payload);
                var content = await response.Content.ReadAsStringAsync();
                using var doc = JsonDocument.Parse(content);
                var root = doc.RootElement;

                bool exito = (root.TryGetProperty("exito", out var pEx) && pEx.GetBoolean()) ||
                             (root.TryGetProperty("success", out var pSu) && pSu.GetBoolean());

                string? token = root.TryGetProperty("token", out var pTok) ? pTok.GetString() : null;
                string? mensaje = root.TryGetProperty("mensaje", out var pMsg) ? pMsg.GetString() :
                                 (root.TryGetProperty("message", out var pM2) ? pM2.GetString() : null);

                if (exito && !string.IsNullOrWhiteSpace(token))
                {
                    _urlActiva = url;
                    return (true, token, mensaje ?? "Inicio de sesión correcto.");
                }

                if (!exito)
                {
                    return (false, null, mensaje ?? "Credenciales incorrectas.");
                }
            }
            catch
            {
                // Fallback al siguiente URL
            }
        }

        return (false, null, "No se pudo conectar con el servidor RESTful (192.168.137.1:5000 o localhost:5000).");
    }

    public async Task<(bool Exito, double Resultado, string? MensajeError)> ConvertirAsync(string magnitud, string token, string origen, string destino, double valor)
    {
        string endpoint = magnitud.Trim().ToLowerInvariant() switch
        {
            "longitud" => "longitud",
            "masa" => "masa",
            "temperatura" => "temperatura",
            _ => magnitud.Trim().ToLowerInvariant()
        };

        var payload = new
        {
            token,
            origen,
            destino,
            valor,
            from = origen,
            to = destino,
            value = valor
        };

        try
        {
            var response = await _http.PostAsJsonAsync($"{_urlActiva}/conversor/{endpoint}", payload);
            var content = await response.Content.ReadAsStringAsync();
            using var doc = JsonDocument.Parse(content);
            var root = doc.RootElement;

            bool exito = (root.TryGetProperty("exito", out var pEx) && pEx.GetBoolean()) ||
                         (root.TryGetProperty("success", out var pSu) && pSu.GetBoolean());

            double resultado = 0;
            if (root.TryGetProperty("valorConvertido", out var pVal))
            {
                resultado = pVal.GetDouble();
            }
            else if (root.TryGetProperty("result", out var pRes))
            {
                resultado = pRes.GetDouble();
            }

            string? mensaje = root.TryGetProperty("mensajeError", out var pErr) ? pErr.GetString() :
                             (root.TryGetProperty("message", out var pMsg) ? pMsg.GetString() : null);

            return (exito, resultado, exito ? null : (mensaje ?? "Error en el cálculo."));
        }
        catch (Exception ex)
        {
            return (false, 0, $"Error HTTP al contactar con el servidor RESTful: {ex.Message}");
        }
    }

    public async Task CerrarSesionAsync(string token)
    {
        try
        {
            await _http.PostAsJsonAsync($"{_urlActiva}/auth/logout", new { token });
        }
        catch
        {
            // Silencioso
        }
    }
}
