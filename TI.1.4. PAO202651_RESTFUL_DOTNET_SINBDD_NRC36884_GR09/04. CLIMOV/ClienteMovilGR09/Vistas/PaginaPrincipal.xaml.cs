using System.Globalization;
using System.Text.RegularExpressions;
using ClienteMovilGR09.Controladores;
using ClienteMovilGR09.Modelos;
using ClienteMovilGR09.Servicios;

namespace ClienteMovilGR09;

public partial class PaginaPrincipal : ContentPage
{
    private static readonly Regex FormatoNumerico = new(@"^-?\d+(\.\d+)?$", RegexOptions.Compiled);
    private static readonly Color ColorTabActivo = Color.FromArgb("#0F3C6E");
    private static readonly Color ColorTabInactivo = Color.FromArgb("#F1F5F9");
    private static readonly Color ColorTextoActivo = Colors.White;
    private static readonly Color ColorTextoInactivo = Color.FromArgb("#64748B");

    private static readonly Dictionary<string, List<string>> CatalogoUnidades = new(StringComparer.OrdinalIgnoreCase)
    {
        ["longitud"] = ["metro", "centimetro", "pie", "yarda", "milla"],
        ["masa"] = ["gramo", "kilogramo", "libra", "onza", "quintal"],
        ["temperatura"] = ["celsius", "fahrenheit", "kelvin", "newton", "reaumur"]
    };

    private readonly ControladorConversor _controlador = new(new ClienteRest());
    private string _magnitudActiva = "longitud";
    private bool _ocupado;

    public PaginaPrincipal()
    {
        InitializeComponent();
        AplicarEstiloTabs();
        CargarUnidadesPorMagnitud();
    }

    private void AlMostrarContrasenia(object? sender, CheckedChangedEventArgs e)
    {
        txtClave.IsPassword = !e.Value;
    }

    private async void IniciarSesion_Clicked(object? sender, EventArgs e)
    {
        if (_ocupado) return;

        lblMensajeLogin.IsVisible = false;
        lblMensajeLogin.Text = "";

        string usuario = txtUsuario.Text?.Trim() ?? "";
        string clave = txtClave.Text?.Trim() ?? "";

        if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(clave))
        {
            MostrarErrorLogin("Por favor, ingrese usuario y contraseña.");
            return;
        }

        try
        {
            EstablecerOcupadoLogin(true);
            txtClave.Unfocus();

            var sesion = await _controlador.IniciarSesionAsync(usuario, clave);

            txtUsuarioActivo.Text = $"Usuario: {sesion.Usuario}";
            txtTokenActivo.Text = $"Token: {sesion.Token[..Math.Min(8, sesion.Token.Length)]}... (activo)";

            panelLogin.IsVisible = false;
            panelConversion.IsVisible = true;
            await Desplazamiento.ScrollToAsync(0, 0, false);
        }
        catch (Exception ex)
        {
            MostrarErrorLogin(ex.Message);
        }
        finally
        {
            EstablecerOcupadoLogin(false);
        }
    }

    private async void CerrarSesion_Clicked(object? sender, EventArgs e)
    {
        try
        {
            await _controlador.CerrarSesionAsync();
        }
        catch
        {
            // Fallback silencioso
        }

        txtClave.Text = "MONSTER9";
        chkMostrarClave.IsChecked = false;
        txtClave.IsPassword = true;
        lblMensajeConversion.IsVisible = false;
        cardResultado.BackgroundColor = Color.FromArgb("#F0FDF4");
        txtResultadoValor.Text = "Presione 'Calcular Conversión'";
        txtResultadoDetalle.Text = "Listo para despachar petición RESTful.";
        btnCopiarResultado.IsVisible = false;

        panelConversion.IsVisible = false;
        panelLogin.IsVisible = true;
        await Desplazamiento.ScrollToAsync(0, 0, false);
    }

    private void TabLongitud_Clicked(object? sender, EventArgs e) => CambiarMagnitud("longitud");
    private void TabMasa_Clicked(object? sender, EventArgs e) => CambiarMagnitud("masa");
    private void TabTemperatura_Clicked(object? sender, EventArgs e) => CambiarMagnitud("temperatura");

    private void CambiarMagnitud(string magnitud)
    {
        _magnitudActiva = magnitud;
        AplicarEstiloTabs();
        CargarUnidadesPorMagnitud();
        lblMensajeConversion.IsVisible = false;
    }

    private void AplicarEstiloTabs()
    {
        btnTabLongitud.BackgroundColor = _magnitudActiva == "longitud" ? ColorTabActivo : ColorTabInactivo;
        btnTabLongitud.TextColor = _magnitudActiva == "longitud" ? ColorTextoActivo : ColorTextoInactivo;

        btnTabMasa.BackgroundColor = _magnitudActiva == "masa" ? ColorTabActivo : ColorTabInactivo;
        btnTabMasa.TextColor = _magnitudActiva == "masa" ? ColorTextoActivo : ColorTextoInactivo;

        btnTabTemperatura.BackgroundColor = _magnitudActiva == "temperatura" ? ColorTabActivo : ColorTabInactivo;
        btnTabTemperatura.TextColor = _magnitudActiva == "temperatura" ? ColorTextoActivo : ColorTextoInactivo;
    }

    private void CargarUnidadesPorMagnitud()
    {
        if (!CatalogoUnidades.TryGetValue(_magnitudActiva, out var unidades))
            return;

        pickerOrigen.ItemsSource = unidades;
        pickerDestino.ItemsSource = unidades;

        pickerOrigen.SelectedIndex = 0;
        pickerDestino.SelectedIndex = unidades.Count > 1 ? 1 : 0;
    }

    private void Swap_Clicked(object? sender, EventArgs e)
    {
        int idxOrigen = pickerOrigen.SelectedIndex;
        int idxDestino = pickerDestino.SelectedIndex;

        pickerOrigen.SelectedIndex = idxDestino;
        pickerDestino.SelectedIndex = idxOrigen;
    }

    private async void Convertir_Clicked(object? sender, EventArgs e)
    {
        if (_ocupado || !panelConversion.IsVisible) return;

        lblMensajeConversion.IsVisible = false;
        string valorTexto = (txtValor.Text ?? "").Trim();

        if (string.IsNullOrWhiteSpace(valorTexto))
        {
            MostrarErrorConversion("Ingrese un valor numérico para convertir.");
            return;
        }

        if (valorTexto.Length > 16)
        {
            MostrarErrorConversion("El valor no puede superar 16 caracteres.");
            return;
        }

        if (!FormatoNumerico.IsMatch(valorTexto))
        {
            MostrarErrorConversion("Formato numérico inválido. Solo dígitos y punto (ej. 100.00).");
            return;
        }

        if (!double.TryParse(valorTexto, NumberStyles.Float, CultureInfo.InvariantCulture, out double valor) || !double.IsFinite(valor))
        {
            MostrarErrorConversion("El valor ingresado no es un número válido.");
            return;
        }

        if (string.Equals(_magnitudActiva, "masa", StringComparison.OrdinalIgnoreCase) && valor < 0)
        {
            MostrarErrorConversion("La magnitud 'Masa' no admite valores negativos.");
            return;
        }

        string? origen = pickerOrigen.SelectedItem as string;
        string? destino = pickerDestino.SelectedItem as string;

        if (string.IsNullOrWhiteSpace(origen) || string.IsNullOrWhiteSpace(destino))
        {
            MostrarErrorConversion("Debe seleccionar las unidades de origen y destino.");
            return;
        }

        try
        {
            EstablecerOcupadoConversion(true);
            txtValor.Unfocus();

            var resp = await _controlador.ConvertirAsync(_magnitudActiva, valor, origen, destino);

            if (!resp.Exito || !double.IsFinite(resp.Resultado))
            {
                throw new InvalidOperationException(resp.Mensaje ?? "El servidor no pudo procesar la conversión.");
            }

            // Formateo estricto a 2 decimales (REGLA 10 AGENTS.md)
            string valorFormateado = valor.ToString("F2", CultureInfo.InvariantCulture);
            string resultadoFormateado = resp.Resultado.ToString("F2", CultureInfo.InvariantCulture);

            txtResultadoValor.Text = $"{valorFormateado} {origen.ToUpperInvariant()} = {resultadoFormateado} {destino.ToUpperInvariant()}";
            txtResultadoDetalle.Text = $"Conversión procesada exitosamente en el servidor RESTful.";
            btnCopiarResultado.IsVisible = true;
        }
        catch (Exception ex)
        {
            MostrarErrorConversion(ex.Message);
        }
        finally
        {
            EstablecerOcupadoConversion(false);
        }
    }

    private async void CopiarResultado_Clicked(object? sender, EventArgs e)
    {
        if (!string.IsNullOrWhiteSpace(txtResultadoValor.Text))
        {
            await Clipboard.Default.SetTextAsync(txtResultadoValor.Text);
            txtResultadoDetalle.Text = "¡Resultado copiado al portapapeles!";
        }
    }

    private void MostrarErrorLogin(string mensaje)
    {
        lblMensajeLogin.Text = mensaje;
        lblMensajeLogin.IsVisible = true;
    }

    private void MostrarErrorConversion(string mensaje)
    {
        lblMensajeConversion.Text = mensaje;
        lblMensajeConversion.IsVisible = true;
    }

    private void EstablecerOcupadoLogin(bool ocupado)
    {
        _ocupado = ocupado;
        btnIngresar.IsEnabled = !ocupado;
        btnIngresar.Text = ocupado ? "Conectando..." : "Ingresar al Conversor";
        indicadorLogin.IsRunning = ocupado;
        indicadorLogin.IsVisible = ocupado;
    }

    private void EstablecerOcupadoConversion(bool ocupado)
    {
        _ocupado = ocupado;
        btnCalcular.IsEnabled = !ocupado;
        btnCalcular.Text = ocupado ? "Procesando..." : "Calcular Conversión RESTful (.NET MAUI)";
        indicadorConversion.IsRunning = ocupado;
        indicadorConversion.IsVisible = ocupado;
    }
}
