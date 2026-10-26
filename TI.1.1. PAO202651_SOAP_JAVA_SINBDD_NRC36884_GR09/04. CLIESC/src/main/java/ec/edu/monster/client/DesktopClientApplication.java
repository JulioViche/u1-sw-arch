package ec.edu.monster.client;

import ec.edu.monster.client.controller.AuthController;
import ec.edu.monster.client.controller.ConversionController;
import ec.edu.monster.client.controller.DesktopAppController;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.ws.SoapClientManager;
import javax.swing.UIManager;

/**
 * Punto de entrada principal (Bootstrapper) del Cliente de Escritorio (Java Swing).
 * Arquitectura: Modelo - Vista - Controlador (MVC).
 * 
 * Asignatura: Arquitectura de Software | Tarea: TI.1.1
 * Grupo: GR09 | Autor: Julio Enrique Viche Castillo
 */
public class DesktopClientApplication {

    public static void main(String[] args) {
        // 1. Configuración de Look and Feel moderno (FlatLaf con fallback al sistema)
        configurarApariencia();

        // 2. Instanciación del Modelo y Gestor SOAP
        SessionModel sessionModel = new SessionModel();
        SoapClientManager clientManager = new SoapClientManager();

        // 3. Instanciación de Controladores de Dominio
        AuthController authController = new AuthController(clientManager, sessionModel);
        ConversionController conversionController = new ConversionController(clientManager, sessionModel);

        // 4. Instanciación del Controlador Orquestador y despliegue de vistas
        DesktopAppController appController = new DesktopAppController(authController, conversionController);
        appController.iniciar();
    }

    private static void configurarApariencia() {
        try {
            // Intenta cargar FlatLaf Light para diseño moderno plano
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Throwable t) {
            try {
                // Fallback al Look & Feel nativo del sistema operativo (Windows 10/11)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
    }
}
