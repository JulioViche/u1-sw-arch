package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;
import ec.edu.monster.client.view.ConsoleView;

/**
 * [CONTROLADOR PRINCIPAL] Coordina la vista de consola y los controladores de negocio.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConsoleAppController {

    private final ConsoleView view;
    private final SessionModel session;
    private final AuthController authController;
    private final ConversionController conversionController;

    public ConsoleAppController(RestClient restClient, SessionModel session, ConsoleView view) {
        this.view = view;
        this.session = session;
        this.authController = new AuthController(restClient, session);
        this.conversionController = new ConversionController(restClient, session);
    }

    public void iniciar() {
        view.mostrarBanner();

        // 1. Paso Obligatorio: Autenticación inicial
        if (!gestionarAutenticacion()) {
            view.mostrarMensaje("\nNo se completó la autenticación. Saliendo del sistema...");
            return;
        }

        view.mostrarExito("¡Autenticación exitosa! Bienvenido " + session.getUsuario());

        // 2. Menú interactivo
        boolean continuar = true;
        while (continuar) {
            String opcion = view.mostrarMenuPrincipal(session);
            switch (opcion) {
                case "1" -> procesarConversion("longitud", ConsoleView.UNIDADES_LONGITUD);
                case "2" -> procesarConversion("masa", ConsoleView.UNIDADES_MASA);
                case "3" -> procesarConversion("temperatura", ConsoleView.UNIDADES_TEMPERATURA);
                case "4" -> mostrarEstadoSesion();
                case "5" -> {
                    authController.logout();
                    view.mostrarDespedida();
                    continuar = false;
                }
                default -> view.mostrarError("Opción no válida. Ingrese un valor entre 1 y 5.");
            }
        }
    }

    private boolean gestionarAutenticacion() {
        while (!session.isActivo()) {
            String[] creds = view.pedirCredenciales();
            boolean ok = authController.login(creds[0], creds[1]);

            if (ok) {
                return true;
            }

            view.mostrarError("Credenciales incorrectas o servidor no disponible.");
            if (!view.preguntarReintento()) {
                return false;
            }
        }
        return false;
    }

    private void procesarConversion(String magnitud, String[] unidades) {
        ConversionRequest req = view.solicitarDatosConversion(magnitud, unidades);
        if (req != null) {
            ConversionResponse resp = conversionController.ejecutarConversion(req);
            view.mostrarResultadoConversion(req, resp);
        }
    }

    private void mostrarEstadoSesion() {
        System.out.println("\n----------------------------------------------------------");
        System.out.println("                 ESTADO DE LA SESIÓN ACTIVA               ");
        System.out.println("----------------------------------------------------------");
        System.out.println(" Usuario:    " + session.getUsuario());
        System.out.println(" Token:      " + session.getToken());
        System.out.println(" Estado:     " + (session.isActivo() ? "VÁLIDO / ACTIVO" : "INACTIVO"));
        System.out.println("----------------------------------------------------------");
    }
}
