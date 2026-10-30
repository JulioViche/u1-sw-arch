using System.Globalization;
using System.Text.RegularExpressions;
using ClienteMovilGR09.Controladores;
using ClienteMovilGR09.Modelos;
using ClienteMovilGR09.Servicios;

namespace ClienteMovilGR09;

public partial class PaginaPrincipal : ContentPage
{
    private ControladorConversor _controlador = new(new ClienteRest());
    private CatalogoRespuesta? _catalogo;
    private bool _ocupado;
    private string _magnitudSeleccionada = "";
    private static readonly Regex FormatoNumero = new(@"^-?\d+(\.\d+)?$", RegexOptions.CultureInvariant | RegexOptions.Compiled);

    public PaginaPrincipal()
    {
        InitializeComponent();
    }

    private async void IniciarSesion_Clicked(object? sender, EventArgs e)
    {
        if (_ocupado) return;
        lblMensajeLogin.Text = "";
        lblMensajeLogin.IsVisible = false;
        if (string.IsNullOrWhiteSpace(txtUsuario.Text) || string.IsNullOrWhiteSpace(txtClave.Text))
        {
            lblMensajeLogin.Text = "Ingresa usuario y contraseña.";
            return;
        }

        try
        {
            EstablecerOcupado(true, BotonIngresar, "Conectando…");
            _controlador = new ControladorConversor(new ClienteRest());
            txtClave.Unfocus();
            var sesion = await _controlador.IniciarSesionAsync(txtUsuario.Text ?? "", txtClave.Text ?? "");
            _catalogo = await _controlador.ObtenerCatalogoAsync();
            _magnitudSeleccionada = _catalogo.Magnitudes.Keys.FirstOrDefault() ?? "";
            EstablecerMagnitud(_magnitudSeleccionada);
            lblSesion.Text = $"Usuario: {sesion.Usuario}";
            VerContrasenia.IsChecked = false;
            panelLogin.IsVisible = false;
            LoginHeader.IsVisible = false;
            SessionCard.IsVisible = true;
            panelConversion.IsVisible = true;
            await Desplazamiento.ScrollToAsync(0, 0, false);
        }
        catch (InvalidOperationException ex)
        {
            lblMensajeLogin.Text = ex.Message;
            lblMensajeLogin.IsVisible = true;
        }
        finally
        {
            EstablecerOcupado(false, BotonIngresar, "Entrar al conversor");
        }
    }

    private void Magnitud_Clicked(object? sender, EventArgs e)
    {
        if (sender is Button button && button.CommandParameter is string magnitud)
        {
            EstablecerMagnitud(magnitud);
        }
    }

    private void EstablecerMagnitud(string magnitud)
    {
        if (_catalogo is null) return;
        var clave = _catalogo.Magnitudes.Keys.FirstOrDefault(
            key => key.Equals(magnitud, StringComparison.OrdinalIgnoreCase));
        if (clave is null || !_catalogo.Magnitudes.TryGetValue(clave, out var unidades)) return;
        _magnitudSeleccionada = clave;
        pickerOrigen.ItemsSource = unidades;
        pickerDestino.ItemsSource = unidades;
        pickerOrigen.SelectedIndex = 0;
        pickerDestino.SelectedIndex = unidades.Count > 1 ? 1 : 0;
        foreach (var (button, selected) in new[]
        {
            (BotonLongitud, clave.Equals("Longitud", StringComparison.OrdinalIgnoreCase)),
            (BotonMasa, clave.Equals("Masa", StringComparison.OrdinalIgnoreCase)),
            (BotonTemperatura, clave.Equals("Temperatura", StringComparison.OrdinalIgnoreCase))
        })
        {
            button.BackgroundColor = selected ? Color.FromArgb("#0F3C6E") : Colors.White;
            button.TextColor = selected ? Colors.White : Color.FromArgb("#0F3C6E");
            button.BorderColor = Color.FromArgb("#0F3C6E");
            button.BorderWidth = selected ? 0 : 1;
        }
    }

    private async void Convertir_Clicked(object? sender, EventArgs e)
    {
        if (_ocupado || !panelConversion.IsVisible) return;
        AlCambiarEntrada(sender, e);
        if (string.IsNullOrWhiteSpace(_magnitudSeleccionada)
            || pickerOrigen.SelectedItem is not string origen
            || pickerDestino.SelectedItem is not string destino
            || (txtValor.Text?.Length ?? 0) > 16
            || !FormatoNumero.IsMatch(txtValor.Text ?? string.Empty)
            || !double.TryParse(txtValor.Text, NumberStyles.AllowLeadingSign | NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture, out var valor)
            || !double.IsFinite(valor))
        {
            lblMensaje.Text = "Ingrese un número finito y seleccione las unidades.";
            lblMensaje.IsVisible = true;
            return;
        }
        if (string.Equals(_magnitudSeleccionada, "masa", StringComparison.OrdinalIgnoreCase) && valor < 0)
        {
            lblMensaje.Text = "La masa no puede ser negativa.";
            lblMensaje.IsVisible = true;
            return;
        }

        try
        {
            EstablecerOcupado(true, BotonConvertir, "Convirtiendo…");
            txtValor.Unfocus();
            EstablecerOcupado(true, BotonConvertir, "Calculando…");
            var resultado = await _controlador.ConvertirAsync(_magnitudSeleccionada, valor, origen, destino);
            if (!double.IsFinite(resultado.Resultado))
            {
                throw new InvalidOperationException("El servidor devolvió un resultado inválido.");
            }
            lblResultado.Text = $"{resultado.Resultado.ToString("F2", CultureInfo.InvariantCulture)} {destino}";
            await Desplazamiento.ScrollToAsync(TarjetaResultado, ScrollToPosition.MakeVisible, true);
        }
        catch (ExcepcionServicio ex)
        {
            lblMensaje.Text = ex.Message;
            lblMensaje.IsVisible = true;
            if (ex.Codigo == 401)
            {
                panelConversion.IsVisible = false;
                SessionCard.IsVisible = false;
                panelLogin.IsVisible = true;
                LoginHeader.IsVisible = true;
                lblMensajeLogin.Text = "La sesión venció. Inicie sesión nuevamente.";
                lblMensajeLogin.IsVisible = true;
            }
        }
        catch (InvalidOperationException ex)
        {
            lblMensaje.Text = ex.Message;
            lblMensaje.IsVisible = true;
        }
        finally
        {
            EstablecerOcupado(false, BotonConvertir, "Convertir ahora");
        }
    }

    private async void CerrarSesion_Clicked(object? sender, EventArgs e)
    {
        try
        {
            await _controlador.CerrarSesionAsync();
        }
        catch (InvalidOperationException ex)
        {
            lblMensaje.Text = ex.Message;
            lblMensaje.IsVisible = true;
        }

        panelConversion.IsVisible = false;
        SessionCard.IsVisible = false;
        panelLogin.IsVisible = true;
        LoginHeader.IsVisible = true;
        txtClave.Text = "";
        AlCambiarEntrada(sender, e);
    }

    private void AlMostrarContrasenia(object? sender, CheckedChangedEventArgs e) =>
        txtClave.IsPassword = !e.Value;

    private void AlIntercambiarUnidades(object? sender, EventArgs e)
    {
        var origen = pickerOrigen.SelectedIndex;
        pickerOrigen.SelectedIndex = pickerDestino.SelectedIndex;
        pickerDestino.SelectedIndex = origen;
    }

    private void AlCambiarEntrada(object? sender, EventArgs e)
    {
        lblResultado.Text = "Tu resultado aparecerá aquí";
        lblMensaje.Text = "";
        lblMensaje.IsVisible = false;
    }

    private void EstablecerOcupado(bool ocupado, Button boton, string texto)
    {
        _ocupado = ocupado;
        panelLogin.IsEnabled = !ocupado;
        panelConversion.IsEnabled = !ocupado;
        if (boton == BotonIngresar)
        {
            ActividadLogin.IsRunning = ocupado;
            ActividadLogin.IsVisible = ocupado;
        }
        if (boton == BotonConvertir)
        {
            ActividadConversion.IsRunning = ocupado;
            ActividadConversion.IsVisible = ocupado;
        }
        boton.Text = texto;
    }
}
