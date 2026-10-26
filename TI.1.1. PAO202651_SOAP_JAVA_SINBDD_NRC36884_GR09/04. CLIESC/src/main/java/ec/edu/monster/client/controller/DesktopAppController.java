package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.view.LoginForm;
import ec.edu.monster.client.view.MainConversionForm;
import javax.swing.SwingUtilities;

/**
 * [CONTROLADOR ORQUESTADOR] Coordina la navegación de vistas y eventos en la aplicación de escritorio.
 * Mantiene desacopladas las ventanas Swing de los servicios de dominio y red.
 */
public class DesktopAppController {

    private final AuthController authController;
    private final ConversionController conversionController;

    private LoginForm loginForm;
    private MainConversionForm mainForm;

    public DesktopAppController(
            AuthController authController,
            ConversionController conversionController) {
        this.authController = authController;
        this.conversionController = conversionController;
    }

    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            loginForm = new LoginForm(this);
            loginForm.setVisible(true);
        });
    }

    /**
     * Procesa la solicitud de inicio de sesión desde el LoginForm.
     */
    public void procesarLogin(String usuario, String clave) {
        try {
            LoginResponse response = authController.login(usuario, clave);

            SwingUtilities.invokeLater(() -> {
                if (response != null && response.isExito()) {
                    loginForm.setVisible(false);
                    mainForm = new MainConversionForm(this, authController.getSessionModel());
                    mainForm.setVisible(true);
                } else {
                    String msg = (response != null) ? response.getMensaje() : "Error desconocido en el servidor";
                    loginForm.mostrarMensaje(msg, false);
                }
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                loginForm.mostrarMensaje("Error de conexión con el Servidor SOAP: " + e.getMessage(), false);
            });
        }
    }

    /**
     * Procesa la solicitud de conversión de unidades desde el MainConversionForm.
     */
    public void procesarConversion(ConversionRequest request) {
        try {
            ConversionResponse response = conversionController.convertir(request);
            SwingUtilities.invokeLater(() -> {
                if (mainForm != null) {
                    mainForm.mostrarResultado(request, response);
                }
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                if (mainForm != null) {
                    mainForm.mostrarError("Error al procesar conversión: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Procesa el cierre de sesión y regresa a la pantalla de login.
     */
    public void procesarLogout() {
        authController.logout();
        SwingUtilities.invokeLater(() -> {
            if (mainForm != null) {
                mainForm.dispose();
                mainForm = null;
            }
            if (loginForm != null) {
                loginForm.mostrarMensaje("Sesión finalizada correctamente.", true);
                loginForm.setVisible(true);
            }
        });
    }
}
