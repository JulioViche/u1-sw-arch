package ec.edu.monster.client;

import ec.edu.monster.client.controller.ConsoleAppController;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;
import ec.edu.monster.client.view.ConsoleView;

/**
 * Punto de entrada principal para el Cliente de Consola (TI.1.3 RESTful Java).
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConsoleClientApplication {

    public static void main(String[] args) {
        // Inicializar componentes MVC
        RestClient restClient = new RestClient();
        SessionModel session = new SessionModel();
        ConsoleView view = new ConsoleView();

        ConsoleAppController controller = new ConsoleAppController(restClient, session, view);
        controller.iniciar();
    }
}
