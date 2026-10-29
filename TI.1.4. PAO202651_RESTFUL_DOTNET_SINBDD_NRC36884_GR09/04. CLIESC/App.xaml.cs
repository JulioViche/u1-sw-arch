using System.Windows;
using ClienteEscritorio_DotNet_REST_GR09.Vistas;

namespace ClienteEscritorio_DotNet_REST_GR09;

public partial class App : Application
{
    private void Application_Startup(object sender, StartupEventArgs e)
    {
        var loginWin = new VentanaLogin();
        loginWin.Show();
    }
}
