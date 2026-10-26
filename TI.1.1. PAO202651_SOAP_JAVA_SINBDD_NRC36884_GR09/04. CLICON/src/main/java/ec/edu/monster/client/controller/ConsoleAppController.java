package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.view.ConsoleView;

/**
 * [CONTROLADOR PRINCIPAL] Orquestador de la aplicación de consola.
 * Coordina la Vista (ConsoleView) y los Controladores de Dominio (AuthController, ConversionController)
 * según el patrón arquitectónico Modelo-Vista-Controlador (MVC).
 */
public class ConsoleAppController {

    private final ConsoleView view;
    private final AuthController authController;
    private final ConversionController conversionController;

    public ConsoleAppController(
            ConsoleView view,
            AuthController authController,
            ConversionController conversionController) {
        this.view = view;
        this.authController = authController;
        this.conversionController = conversionController;
    }

    /**
     * Inicia el ciclo de vida de la aplicación: banner, autenticación obligatoria y menú principal.
     */
    public void iniciar() {
        view.mostrarBanner();

        // 1. Caso de Uso obligatorio: Iniciar Sesión (Autenticación)
        boolean autenticado = false;
        while (!autenticado) {
            String[] credenciales = view.pedirCredenciales();
            try {
                view.mostrarMensaje("\n[+] Enviando solicitud de autenticación SOAP...");
                LoginResponse loginResp = authController.login(credenciales[0], credenciales[1]);

                if (loginResp != null && loginResp.isExito()) {
                    view.mostrarExito(loginResp.getMensaje());
                    view.mostrarMensaje("[*] Token de sesión obtenido: " + loginResp.getToken());
                    autenticado = true;
                } else {
                    String msg = (loginResp != null) ? loginResp.getMensaje() : "Respuesta vacía del servidor";
                    view.mostrarError(msg);
                    if (!view.preguntarReintento()) {
                        view.mostrarMensaje("\n[!] Saliendo de la aplicación. Hasta luego.");
                        return;
                    }
                }
            } catch (Exception e) {
                view.mostrarError("No se pudo conectar al servidor SOAP: " + e.getMessage());
                view.mostrarMensaje("Verifique que el servidor (Publisher.java) esté activo en el puerto 8080.");
                if (!view.preguntarReintento()) {
                    return;
                }
            }
        }

        // 2. Bucle principal de operaciones de conversión
        boolean ejecutando = true;
        while (ejecutando) {
            String opcion = view.mostrarMenuPrincipal(authController.getSessionModel());
            switch (opcion) {
                case "1":
                    ejecutarConversion("longitud", ConsoleView.UNIDADES_LONGITUD);
                    break;
                case "2":
                    ejecutarConversion("masa", ConsoleView.UNIDADES_MASA);
                    break;
                case "3":
                    ejecutarConversion("temperatura", ConsoleView.UNIDADES_TEMPERATURA);
                    break;
                case "4":
                    verificarToken();
                    break;
                case "5":
                    cerrarSesion();
                    ejecutando = false;
                    break;
                default:
                    view.mostrarError("Opción no válida. Seleccione un número del 1 al 5.");
            }
        }

        view.mostrarDespedida();
    }

    private void ejecutarConversion(String magnitud, String[] unidades) {
        ConversionRequest req = view.solicitarDatosConversion(magnitud, unidades);
        if (req == null) return;

        try {
            view.mostrarMensaje("\n[+] Enviando petición SOAP al servidor...");
            ConversionResponse resp = conversionController.convertir(req);
            view.mostrarResultadoConversion(req, resp);
        } catch (Exception e) {
            view.mostrarError("Falla en la comunicación SOAP: " + e.getMessage());
        }
    }

    private void verificarToken() {
        try {
            boolean valido = authController.validarToken();
            if (valido) {
                view.mostrarExito("El token de sesión sigue ACTIVO y válido en el servidor.");
            } else {
                view.mostrarError("El token de sesión ya NO es válido en el servidor.");
            }
        } catch (Exception e) {
            view.mostrarError("Error al verificar el token: " + e.getMessage());
        }
    }

    private void cerrarSesion() {
        view.mostrarMensaje("\n[+] Cerrando sesión en el servidor SOAP...");
        boolean res = authController.logout();
        if (res) {
            view.mostrarExito("Sesión cerrada satisfactoriamente en el servidor.");
        } else {
            view.mostrarMensaje("[*] Sesión finalizada localmente.");
        }
    }
}
