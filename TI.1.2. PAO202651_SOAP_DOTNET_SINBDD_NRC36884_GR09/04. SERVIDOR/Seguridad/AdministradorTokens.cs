using System.Collections.Concurrent;

namespace WSServidorSOAP_DotNet_GR09.Seguridad;

public sealed class AdministradorTokens
{
    private readonly ConcurrentDictionary<string, (string Usuario, DateTime Expiracion)> _tokens = new();
    private static readonly TimeSpan TiempoVidaToken = TimeSpan.FromMinutes(30);

    public string GenerarToken(string usuario)
    {
        string token = Guid.NewGuid().ToString("N");
        _tokens[token] = (usuario, DateTime.UtcNow.Add(TiempoVidaToken));
        return token;
    }

    public bool ValidarToken(string? token)
    {
        if (string.IsNullOrWhiteSpace(token)) return false;

        if (_tokens.TryGetValue(token, out var sesion))
        {
            if (DateTime.UtcNow <= sesion.Expiracion)
            {
                _tokens[token] = (sesion.Usuario, DateTime.UtcNow.Add(TiempoVidaToken));
                return true;
            }
            _tokens.TryRemove(token, out _);
        }
        return false;
    }

    public bool EsTokenValido(string? token) => ValidarToken(token);

    public void RevocarToken(string? token)
    {
        if (!string.IsNullOrWhiteSpace(token))
        {
            _tokens.TryRemove(token, out _);
        }
    }
}
