using System.Windows;
using ClienteEscritorio_DotNet_SOAP_GR09.Modelos;
using ClienteEscritorio_DotNet_SOAP_GR09.Servicios;

namespace ClienteEscritorio_DotNet_SOAP_GR09.Vistas;

public partial class VentanaLogin : Window
{
    private readonly ClienteServicioSoap _servicio = new();

    public VentanaLogin()
    {
        InitializeComponent();
    }

    private async void BtnIngresar_Click(object sender, RoutedEventArgs e)
    {
        string usuario = TxtUsuario.Text.Trim();
        string clave = TxtClave.Password.Trim();

        if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(clave))
        {
            MostrarError("Por favor ingrese el usuario y la contraseña.");
            return;
        }

        BtnIngresar.IsEnabled = false;
        BtnIngresar.Content = "Autenticando...";
        LblEstado.Visibility = Visibility.Collapsed;

        var (exito, token, mensaje) = await _servicio.IniciarSesionAsync(usuario, clave);
        BtnIngresar.IsEnabled = true;
        BtnIngresar.Content = "Ingresar al Conversor";

        if (exito && !string.IsNullOrWhiteSpace(token))
        {
            var sesion = new ModeloSesion
            {
                Usuario = usuario.ToUpperInvariant(),
                Token = token
            };

            var ventanaPrincipal = new VentanaPrincipal(sesion, _servicio);
            ventanaPrincipal.Show();
            Close();
        }
        else
        {
            MostrarError(mensaje ?? "Credenciales inválidas. (Usuario: MONSTER / MONSTER9)");
        }
    }

    private void MostrarError(string mensaje)
    {
        LblEstado.Text = mensaje;
        LblEstado.Visibility = Visibility.Visible;
    }

    private void BtnSalir_Click(object sender, RoutedEventArgs e)
    {
        Application.Current.Shutdown();
    }
}
