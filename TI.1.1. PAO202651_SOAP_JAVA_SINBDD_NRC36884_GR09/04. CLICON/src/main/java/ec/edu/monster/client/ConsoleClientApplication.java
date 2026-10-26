package ec.edu.monster.client;

import ec.edu.monster.client.controller.AuthController;
import ec.edu.monster.client.controller.ConsoleAppController;
import ec.edu.monster.client.controller.ConversionController;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.view.ConsoleView;
import ec.edu.monster.client.ws.SoapClientManager;

/**
 * Punto de entrada principal (Bootstrapper) del Cliente de Consola SOAP.
 * Instancia y enlaza los componentes según el patrón arquitectónico
 * Modelo - Vista - Controlador (MVC).
 * 
 * Asignatura: Arquitectura de Software | Tarea: TI.1.1
 * Grupo: GR09 | Autor: Julio Enrique Viche Castillo
 */
public class ConsoleClientApplication {

    public static void main(String[] args) {
        // 1. Instanciación del Modelo y Gestor de Servicios Web SOAP
        SessionModel sessionModel = new SessionModel();
        SoapClientManager clientManager = new SoapClientManager();

        // 2. Instanciación de los Controladores de Dominio
        AuthController authController = new AuthController(clientManager, sessionModel);
        ConversionController conversionController = new ConversionController(clientManager, sessionModel);

        // 3. Instanciación de la Vista
        ConsoleView view = new ConsoleView();

        // 4. Inyección en el Controlador Orquestador y arranque
        ConsoleAppController appController = new ConsoleAppController(view, authController, conversionController);
        appController.iniciar();
    }
}
