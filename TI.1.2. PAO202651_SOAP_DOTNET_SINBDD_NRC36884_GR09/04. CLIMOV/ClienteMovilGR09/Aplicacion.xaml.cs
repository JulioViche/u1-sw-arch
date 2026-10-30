using Microsoft.Extensions.DependencyInjection;

namespace ClienteMovilGR09;

public partial class Aplicacion : Application
{
	public Aplicacion()
	{
		InitializeComponent();
        UserAppTheme = AppTheme.Light;
	}

	protected override Window CreateWindow(IActivationState? activationState)
	{
		return new Window(new NavegacionAplicacion());
	}
}
