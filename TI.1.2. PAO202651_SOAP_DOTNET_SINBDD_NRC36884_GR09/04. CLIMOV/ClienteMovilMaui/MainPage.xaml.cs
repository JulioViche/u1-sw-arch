using System.Globalization;
using System.Text.RegularExpressions;
using ClienteMovilSoapDotNetGR09.Services;

namespace ClienteMovilSoapDotNetGR09;

public partial class MainPage : ContentPage
{
    private static readonly Regex ValidNumber = new(
        @"^-?\d+(\.\d+)?$",
        RegexOptions.CultureInvariant | RegexOptions.Compiled);

    private static readonly Dictionary<string, string[]> UnitCatalog = new()
    {
        ["Longitud"] = ["Metro", "Centímetro", "Pie", "Yarda", "Milla"],
        ["Masa"] = ["Gramo", "Kilogramo", "Libra", "Onza", "Quintal"],
        ["Temperatura"] = ["Celsius", "Fahrenheit", "Kelvin", "Newton", "Réaumur"]
    };

    private readonly SoapClient _soapClient = new();
    private string? _sessionToken;
    private string _activeServerUrl = string.Empty;
    private string _selectedMagnitude = "Longitud";

    public MainPage()
    {
        InitializeComponent();
        SetMagnitude(_selectedMagnitude);
    }

    private async void OnLoginClicked(object? sender, EventArgs e)
    {
        LoginMessage.IsVisible = false;
        var serverUrl = SoapClient.GetDefaultBaseUrl();
        var username = (UsernameEntry.Text ?? string.Empty).Trim();
        var password = PasswordEntry.Text ?? string.Empty;

        if (username.Length == 0 || password.Length == 0)
        {
            ShowLoginError("Ingresa usuario y contraseña.");
            return;
        }

        _activeServerUrl = SoapClient.NormalizeBaseUrl(serverUrl);
        SetLoginBusy(true);

        try
        {
            var result = await _soapClient.LoginAsync(_activeServerUrl, username, password);
            if (!result.Success || string.IsNullOrWhiteSpace(result.Token))
            {
                ShowLoginError(string.IsNullOrWhiteSpace(result.Message)
                    ? "No se pudo iniciar sesión."
                    : result.Message);
                return;
            }

            _sessionToken = result.Token;
            SessionLabel.Text = $"Sesión iniciada como {username}.";
            LoginHeader.IsVisible = false;
            LoginCard.IsVisible = false;
            SessionCard.IsVisible = true;
            ConverterCard.IsVisible = true;
            ConversionMessage.IsVisible = false;
            ResultCard.IsVisible = false;
        }
        catch (Exception ex)
        {
            ShowLoginError($"No se pudo conectar con el servidor SOAP: {ex.Message}");
        }
        finally
        {
            SetLoginBusy(false);
        }
    }

    private async void OnConvertClicked(object? sender, EventArgs e)
    {
        ConversionMessage.IsVisible = false;
        ResultCard.IsVisible = false;

        var rawValue = (ValueEntry.Text ?? string.Empty).Trim();
        if (rawValue.Length > 16 || !ValidNumber.IsMatch(rawValue))
        {
            ShowConversionError("Usa hasta 16 caracteres: dígitos, punto decimal y '-' solo al inicio (ej.: -15.50).");
            return;
        }

        if (!double.TryParse(
                rawValue,
                NumberStyles.AllowLeadingSign | NumberStyles.AllowDecimalPoint,
                CultureInfo.InvariantCulture,
                out var value) ||
            !double.IsFinite(value))
        {
            ShowConversionError("El valor numérico está fuera de rango.");
            return;
        }

        if (_sessionToken is null ||
            string.IsNullOrWhiteSpace(_selectedMagnitude) ||
            SourceUnitPicker.SelectedItem is not string sourceUnit ||
            DestinationUnitPicker.SelectedItem is not string destinationUnit)
        {
            ShowConversionError("Selecciona la magnitud y las dos unidades.");
            return;
        }

        SetConversionBusy(true);
        try
        {
            var result = await _soapClient.ConvertAsync(
                _activeServerUrl,
                _selectedMagnitude,
                _sessionToken,
                value,
                sourceUnit,
                destinationUnit);

            if (!result.Success)
            {
                ShowConversionError(string.IsNullOrWhiteSpace(result.Message)
                    ? "El servidor no pudo completar la conversión."
                    : result.Message);
                return;
            }

            ResultValueLabel.Text = result.Value.ToString("F2", CultureInfo.InvariantCulture);
            ResultDetailLabel.Text = $"{rawValue} {sourceUnit} = {ResultValueLabel.Text} {destinationUnit}";
            ResultCard.IsVisible = true;
        }
        catch (Exception ex)
        {
            ShowConversionError($"Error al consultar SOAP: {ex.Message}");
        }
        finally
        {
            SetConversionBusy(false);
        }
    }

    private void OnMagnitudeButtonClicked(object? sender, EventArgs e)
    {
        if (sender is not Button button || button.CommandParameter is not string magnitude)
        {
            return;
        }

        SetMagnitude(magnitude);
        ResultCard.IsVisible = false;
    }

    private void SetMagnitude(string magnitude)
    {
        if (!UnitCatalog.TryGetValue(magnitude, out var units)) return;
        _selectedMagnitude = magnitude;
        SourceUnitPicker.ItemsSource = units;
        DestinationUnitPicker.ItemsSource = units;
        SourceUnitPicker.SelectedIndex = 0;
        DestinationUnitPicker.SelectedIndex = units.Length > 1 ? 1 : 0;
        foreach (var (button, selected) in new[]
        {
            (MagnitudeLengthButton, magnitude == "Longitud"),
            (MagnitudeMassButton, magnitude == "Masa"),
            (MagnitudeTemperatureButton, magnitude == "Temperatura")
        })
        {
            button.BackgroundColor = selected ? Color.FromArgb("#0F3C6E") : Colors.White;
            button.TextColor = selected ? Colors.White : Color.FromArgb("#0F3C6E");
            button.BorderColor = Color.FromArgb("#0F3C6E");
            button.BorderWidth = selected ? 0 : 1;
        }
    }

    private void OnSwapClicked(object? sender, EventArgs e)
    {
        var sourceIndex = SourceUnitPicker.SelectedIndex;
        var destinationIndex = DestinationUnitPicker.SelectedIndex;
        if (sourceIndex < 0 || destinationIndex < 0)
        {
            return;
        }

        SourceUnitPicker.SelectedIndex = destinationIndex;
        DestinationUnitPicker.SelectedIndex = sourceIndex;
    }

    private void OnLogoutClicked(object? sender, EventArgs e)
    {
        _sessionToken = null;
        PasswordEntry.Text = string.Empty;
        LoginMessage.IsVisible = false;
        ConversionMessage.IsVisible = false;
        ResultCard.IsVisible = false;
        ConverterCard.IsVisible = false;
        SessionCard.IsVisible = false;
        LoginCard.IsVisible = true;
        LoginHeader.IsVisible = true;
    }

    private void SetLoginBusy(bool busy)
    {
        LoginActivity.IsVisible = busy;
        LoginActivity.IsRunning = busy;
        LoginButton.IsEnabled = !busy;
    }

    private void SetConversionBusy(bool busy)
    {
        ConversionActivity.IsVisible = busy;
        ConversionActivity.IsRunning = busy;
        ConvertButton.IsEnabled = !busy;
    }

    private void ShowLoginError(string message)
    {
        LoginMessage.Text = message;
        LoginMessage.IsVisible = true;
    }

    private void ShowConversionError(string message)
    {
        ConversionMessage.Text = message;
        ConversionMessage.IsVisible = true;
    }
}
