using System.Globalization;
using System.Text.RegularExpressions;
using ClienteMovilGR09.Controladores;
using ClienteMovilGR09.Modelos;

namespace ClienteMovilGR09.Vistas;

public partial class PaginaPrincipal : ContentPage
{
    private static readonly Regex FormatoNumerico = new(@"^-?\d+(\.\d+)?$", RegexOptions.Compiled);
    private static readonly Color ColorTabActivo = Color.FromArgb("#0F3C6E");
    private static readonly Color ColorTabInactivo = Color.FromArgb("#F1F5F9");
    private static readonly Color ColorTextoActivo = Colors.White;
    private static readonly Color ColorTextoInactivo = Color.FromArgb("#64748B");

    private readonly ControladorConversor _controlador = new();
    private TipoConversion _tipoActivo = TipoConversion.Longitud;
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

            await _controlador.IniciarSesionAsync(usuario, clave);

            txtUsuarioActivo.Text = $"Usuario: {usuario.ToUpperInvariant()}";
            txtTokenActivo.Text = "Token: activo (SINBDD)";

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
            _controlador.CerrarSesion();
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
        txtResultadoDetalle.Text = "Listo para despachar petición SOAP.";
        btnCopiarResultado.IsVisible = false;

        panelConversion.IsVisible = false;
        panelLogin.IsVisible = true;
        await Desplazamiento.ScrollToAsync(0, 0, false);
    }

    private void TabLongitud_Clicked(object? sender, EventArgs e) => CambiarMagnitud(TipoConversion.Longitud);
    private void TabMasa_Clicked(object? sender, EventArgs e) => CambiarMagnitud(TipoConversion.Masa);
    private void TabTemperatura_Clicked(object? sender, EventArgs e) => CambiarMagnitud(TipoConversion.Temperatura);

    private void CambiarMagnitud(TipoConversion tipo)
    {
        _tipoActivo = tipo;
        AplicarEstiloTabs();
        CargarUnidadesPorMagnitud();
        lblMensajeConversion.IsVisible = false;
    }

    private void AplicarEstiloTabs()
    {
        btnTabLongitud.BackgroundColor = _tipoActivo == TipoConversion.Longitud ? ColorTabActivo : ColorTabInactivo;
        btnTabLongitud.TextColor = _tipoActivo == TipoConversion.Longitud ? ColorTextoActivo : ColorTextoInactivo;

        btnTabMasa.BackgroundColor = _tipoActivo == TipoConversion.Masa ? ColorTabActivo : ColorTabInactivo;
        btnTabMasa.TextColor = _tipoActivo == TipoConversion.Masa ? ColorTextoActivo : ColorTextoInactivo;

        btnTabTemperatura.BackgroundColor = _tipoActivo == TipoConversion.Temperatura ? ColorTabActivo : ColorTabInactivo;
        btnTabTemperatura.TextColor = _tipoActivo == TipoConversion.Temperatura ? ColorTextoActivo : ColorTextoInactivo;
    }

    private void CargarUnidadesPorMagnitud()
    {
        var unidades = CatalogoUnidades.Unidades(_tipoActivo);
        var etiquetas = unidades.Select(u => u.Etiqueta).ToList();

        pickerOrigen.ItemsSource = etiquetas;
        pickerDestino.ItemsSource = etiquetas;

        pickerOrigen.SelectedIndex = 0;
        pickerDestino.SelectedIndex = etiquetas.Count > 1 ? 1 : 0;
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

        if (_tipoActivo == TipoConversion.Masa && valor < 0)
        {
            MostrarErrorConversion("La magnitud 'Masa' no admite valores negativos.");
            return;
        }

        int idxOrigen = pickerOrigen.SelectedIndex;
        int idxDestino = pickerDestino.SelectedIndex;

        var unidades = CatalogoUnidades.Unidades(_tipoActivo);
        if (idxOrigen < 0 || idxOrigen >= unidades.Count || idxDestino < 0 || idxDestino >= unidades.Count)
        {
            MostrarErrorConversion("Debe seleccionar las unidades de origen y destino.");
            return;
        }

        var unidadOrigen = unidades[idxOrigen];
        var unidadDestino = unidades[idxDestino];

        try
        {
            EstablecerOcupadoConversion(true);
            txtValor.Unfocus();

            double resultado = await _controlador.ConvertirAsync(_tipoActivo, valor, unidadOrigen.Codigo, unidadDestino.Codigo);

            if (!double.IsFinite(resultado))
            {
                throw new InvalidOperationException("El servidor devolvió un resultado numérico inválido.");
            }

            // Formateo estricto a 2 decimales (REGLA 10 AGENTS.md)
            string valorFormateado = valor.ToString("F2", CultureInfo.InvariantCulture);
            string resultadoFormateado = resultado.ToString("F2", CultureInfo.InvariantCulture);

            txtResultadoValor.Text = $"{valorFormateado} {unidadOrigen.Codigo} = {resultadoFormateado} {unidadDestino.Codigo}";
            txtResultadoDetalle.Text = "Conversión procesada exitosamente en el servidor SOAP.";
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
        btnCalcular.Text = ocupado ? "Procesando..." : "Calcular Conversión SOAP (.NET MAUI)";
        indicadorConversion.IsRunning = ocupado;
        indicadorConversion.IsVisible = ocupado;
    }
}
