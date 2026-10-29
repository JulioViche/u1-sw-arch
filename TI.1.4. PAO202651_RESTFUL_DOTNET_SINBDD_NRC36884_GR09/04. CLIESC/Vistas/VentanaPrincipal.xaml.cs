using System.Globalization;
using System.Text.RegularExpressions;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using ClienteEscritorio_DotNet_REST_GR09.Modelos;
using ClienteEscritorio_DotNet_REST_GR09.Servicios;

namespace ClienteEscritorio_DotNet_REST_GR09.Vistas;

public partial class VentanaPrincipal : Window
{
    private readonly ModeloSesion _sesion;
    private readonly ClienteServicioRest _servicio;

    private static readonly Dictionary<string, string[]> CatalogoUnidades = new()
    {
        ["Longitud"] = ["Metro", "Centimetro", "Pie", "Yarda", "Milla"],
        ["Masa"] = ["Gramo", "Kilogramo", "Libra", "Onza", "Quintal"],
        ["Temperatura"] = ["Celsius", "Fahrenheit", "Kelvin", "Newton", "Reaumur"]
    };

    private string _magnitudActual = "Longitud";

    public VentanaPrincipal(ModeloSesion sesion, ClienteServicioRest servicio)
    {
        InitializeComponent();
        _sesion = sesion;
        _servicio = servicio;

        LblUsuarioSesion.Text = _sesion.Usuario;
        LblTokenAbreviado.Text = $" (Token: {_sesion.TokenAbreviado})";

        SeleccionarMagnitud("Longitud");
    }

    private void TabMagnitud_Click(object sender, RoutedEventArgs e)
    {
        if (sender is Button btn && btn.Content is string nombreTab)
        {
            SeleccionarMagnitud(nombreTab);
        }
    }

    private void SeleccionarMagnitud(string magnitud)
    {
        _magnitudActual = magnitud;

        // Estilos visuales de las pestañas
        ActualizarEstiloTab(BtnTabLongitud, magnitud == "Longitud");
        ActualizarEstiloTab(BtnTabMasa, magnitud == "Masa");
        ActualizarEstiloTab(BtnTabTemperatura, magnitud == "Temperatura");

        // Poblar ComboBoxes con las 5 unidades exactas
        if (CatalogoUnidades.TryGetValue(magnitud, out var unidades))
        {
            CmbOrigen.ItemsSource = unidades;
            CmbDestino.ItemsSource = unidades;

            CmbOrigen.SelectedIndex = 0;
            CmbDestino.SelectedIndex = unidades.Length > 1 ? 1 : 0;
        }

        BrdResultado.Visibility = Visibility.Collapsed;
        BrdError.Visibility = Visibility.Collapsed;
    }

    private static void ActualizarEstiloTab(Button btn, bool activo)
    {
        if (activo)
        {
            btn.Background = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#0284C7"));
            btn.Foreground = Brushes.White;
            btn.FontWeight = FontWeights.Bold;
        }
        else
        {
            btn.Background = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#E2E8F0"));
            btn.Foreground = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#334155"));
            btn.FontWeight = FontWeights.SemiBold;
        }
    }

    private void BtnIntercambiar_Click(object sender, RoutedEventArgs e)
    {
        var temp = CmbOrigen.SelectedItem;
        CmbOrigen.SelectedItem = CmbDestino.SelectedItem;
        CmbDestino.SelectedItem = temp;
    }

    private void TxtValor_PreviewTextInput(object sender, TextCompositionEventArgs e)
    {
        string textoNuevo = TxtValor.Text.Insert(TxtValor.CaretIndex, e.Text);
        e.Handled = textoNuevo.Length > 16 || !Regex.IsMatch(textoNuevo, @"^-?\d*(\.\d*)?$");
    }

    private void TxtValor_TextChanged(object sender, TextChangedEventArgs e)
    {
        if (TxtValor.Text.Length > 16)
        {
            TxtValor.Text = TxtValor.Text[..16];
            TxtValor.CaretIndex = 16;
        }
    }

    private async void BtnConvertir_Click(object sender, RoutedEventArgs e)
    {
        BrdError.Visibility = Visibility.Collapsed;
        BrdResultado.Visibility = Visibility.Collapsed;

        string input = TxtValor.Text.Trim();
        if (string.IsNullOrWhiteSpace(input) || !Regex.IsMatch(input, @"^-?\d+(\.\d+)?$"))
        {
            MostrarError("Por favor ingrese un valor numérico válido (ejemplo: 25.5, -10 o 100).");
            return;
        }

        if (!double.TryParse(input, NumberStyles.Float, CultureInfo.InvariantCulture, out double valor))
        {
            MostrarError("Valor numérico inválido.");
            return;
        }

        string? origen = CmbOrigen.SelectedItem?.ToString();
        string? destino = CmbDestino.SelectedItem?.ToString();

        if (string.IsNullOrEmpty(origen) || string.IsNullOrEmpty(destino))
        {
            MostrarError("Seleccione las unidades de origen y destino.");
            return;
        }

        BtnConvertir.IsEnabled = false;
        BtnConvertir.Content = "Calculando...";

        var respuesta = await _servicio.ConvertirAsync(_magnitudActual, _sesion.Token, origen, destino, valor);

        BtnConvertir.IsEnabled = true;
        BtnConvertir.Content = "Calcular Conversión REST";

        if (respuesta.Exito)
        {
            LblResultadoValor.Text = string.Create(CultureInfo.InvariantCulture, $"{valor:F2} {origen} = {respuesta.ValorConvertido:F2} {destino}");
            BrdResultado.Visibility = Visibility.Visible;
        }
        else
        {
            MostrarError(respuesta.MensajeError ?? "Error al procesar la conversión.");
        }
    }

    private void MostrarError(string mensaje)
    {
        LblError.Text = mensaje;
        BrdError.Visibility = Visibility.Visible;
    }

    private async void BtnCopiar_Click(object sender, RoutedEventArgs e)
    {
        try
        {
            Clipboard.SetText(LblResultadoValor.Text);
            BtnCopiar.Content = "¡Copiado!";
            await Task.Delay(1500);
            BtnCopiar.Content = "Copiar Resultado";
        }
        catch
        {
            // Silencioso
        }
    }

    private async void BtnCerrarSesion_Click(object sender, RoutedEventArgs e)
    {
        await _servicio.CerrarSesionAsync(_sesion.Token);
        var login = new VentanaLogin();
        login.Show();
        Close();
    }
}
