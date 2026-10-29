using ClienteConsola_DotNet_SOAP_GR09.Modelos;
using ClienteConsola_DotNet_SOAP_GR09.Servicios;
using ClienteConsola_DotNet_SOAP_GR09.Vista;

namespace ClienteConsola_DotNet_SOAP_GR09.Controlador;

public sealed class ControladorConsola
{
    private readonly VistaConsola _vista;
    private readonly ClienteServicioSoap _servicio;
    private readonly ModeloSesion _sesion;

    public ControladorConsola()
    {
        _vista = new VistaConsola();
        _servicio = new ClienteServicioSoap();
        _sesion = new ModeloSesion();
    }

    public async Task IniciarAsync()
    {
        _vista.MostrarBanner();

        // Autenticación obligatoria
        bool autenticado = false;
        while (!autenticado)
        {
            var (usuario, clave) = _vista.PedirCredenciales();
            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(clave))
            {
                _vista.MostrarError("El usuario y la contraseña no pueden estar vacíos.");
                if (!_vista.PreguntarReintento()) return;
                continue;
            }

            Console.WriteLine("\n[Conectando con el servidor SOAP .NET (8733)...]");
            var (exito, token, mensaje) = await _servicio.IniciarSesionAsync(usuario, clave);
            if (exito && !string.IsNullOrWhiteSpace(token))
            {
                _sesion.Usuario = usuario.ToUpperInvariant();
                _sesion.Token = token;
                _vista.MostrarExito($"Autenticación SOAP satisfactoria. Token: {_sesion.TokenAbreviado}");
                autenticado = true;
            }
            else
            {
                _vista.MostrarError(mensaje ?? "Credenciales inválidas.");
                if (!_vista.PreguntarReintento()) return;
            }
        }

        // Menú
        bool salir = false;
        while (!salir)
        {
            string opcion = _vista.MostrarMenuPrincipal(_sesion);
            switch (opcion)
            {
                case "1":
                    await ProcesarConversionAsync("Longitud", VistaConsola.UnidadesLongitud);
                    break;
                case "2":
                    await ProcesarConversionAsync("Masa", VistaConsola.UnidadesMasa);
                    break;
                case "3":
                    await ProcesarConversionAsync("Temperatura", VistaConsola.UnidadesTemperatura);
                    break;
                case "4":
                    _vista.MostrarExito($"Sesión SOAP activa para {_sesion.Usuario} con Token GUID: {_sesion.Token}");
                    break;
                case "5":
                    _vista.MostrarDespedida();
                    salir = true;
                    break;
                default:
                    _vista.MostrarError("Opción no válida. Ingrese un número entre 1 y 5.");
                    break;
            }
        }
    }

    private async Task ProcesarConversionAsync(string magnitud, string[] unidades)
    {
        var datos = _vista.SolicitarDatosConversion(magnitud, unidades);
        if (datos is null) return;

        var (origen, destino, valor) = datos.Value;
        Console.WriteLine($"\n[Enviando petición SOAP para {magnitud} ({origen} -> {destino})...]");
        var (exito, resultado, err) = await _servicio.ConvertirAsync(magnitud, _sesion.Token, origen, destino, valor);
        _vista.MostrarResultadoConversion(origen, destino, valor, exito, resultado, err);
    }
}
