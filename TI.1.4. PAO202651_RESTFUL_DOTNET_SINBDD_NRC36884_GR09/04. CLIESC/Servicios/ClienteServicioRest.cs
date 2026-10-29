using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using ClienteEscritorio_DotNet_REST_GR09.Modelos;

namespace ClienteEscritorio_DotNet_REST_GR09.Servicios;

public sealed class ClienteServicioRest
{
    private static readonly string[] UrlsBase =
    [
        "http://192.168.137.1:5000/api",
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

    public async Task<TokenRespuesta> IniciarSesionAsync(string usuario, string clave)
    {
        var payload = new { usuario, clave, from = usuario, to = clave };

        foreach (var url in UrlsBase)
        {
            try
            {
                var response = await _http.PostAsJsonAsync($"{url}/auth/login", payload);
                var content = await response.Content.ReadAsStringAsync();
                var res = JsonSerializer.Deserialize<TokenRespuesta>(content, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
                if (res != null)
                {
                    _urlActiva = url;
                    return res;
                }
            }
            catch
            {
                // Fallback a siguiente URL
            }
        }

        return new TokenRespuesta
        {
            Exito = false,
            Mensaje = "No se pudo conectar con el servidor RESTful (192.168.137.1:5000 o localhost:5000)."
        };
    }

    public async Task<ConversionRespuesta> ConvertirAsync(string magnitud, string token, string origen, string destino, double valor)
    {
        string endpoint = magnitud.Trim().ToLowerInvariant() switch
        {
            "longitud" => "longitud",
            "masa" => "masa",
            "temperatura" => "temperatura",
            _ => magnitud.Trim().ToLowerInvariant()
        };

        var solicitud = new ConversionSolicitud
        {
            Token = token,
            Origen = origen,
            Destino = destino,
            Valor = valor
        };

        try
        {
            var response = await _http.PostAsJsonAsync($"{_urlActiva}/conversor/{endpoint}", solicitud);
            var content = await response.Content.ReadAsStringAsync();
            var res = JsonSerializer.Deserialize<ConversionRespuesta>(content, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            return res ?? new ConversionRespuesta { Exito = false, MensajeError = "Respuesta vacía del servidor." };
        }
        catch (Exception ex)
        {
            return new ConversionRespuesta
            {
                Exito = false,
                MensajeError = $"Error al comunicarse con el servidor: {ex.Message}"
            };
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
