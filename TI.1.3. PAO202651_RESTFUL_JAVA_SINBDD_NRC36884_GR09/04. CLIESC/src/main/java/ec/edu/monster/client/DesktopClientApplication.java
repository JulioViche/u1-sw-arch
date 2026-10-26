package ec.edu.monster.client;

import com.formdev.flatlaf.FlatLightLaf;
import ec.edu.monster.client.controller.AuthController;
import ec.edu.monster.client.controller.ConversionController;
import ec.edu.monster.client.controller.DesktopAppController;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;

import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada principal para el Cliente de Escritorio Swing (TI.1.3 RESTful Java).
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class DesktopClientApplication {

    public static void main(String[] args) {
        // Inicializar FlatLaf para aspecto moderno y limpio
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        } catch (Exception e) {
            System.err.println("No se pudo inicializar FlatLaf: " + e.getMessage());
        }

        // Crear componentes bajo patrón MVC
        RestClient restClient = new RestClient();
        SessionModel sessionModel = new SessionModel();

        AuthController authController = new AuthController(restClient, sessionModel);
        ConversionController conversionController = new ConversionController(restClient, sessionModel);

        DesktopAppController appController = new DesktopAppController(authController, conversionController);
        appController.iniciar();
    }
}
