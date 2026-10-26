package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;

/**
 * [CONTROLADOR] Autenticación gráfica contra el servicio RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class AuthController {

    private final RestClient restClient;
    private final SessionModel sessionModel;

    public AuthController(RestClient restClient, SessionModel sessionModel) {
        this.restClient = restClient;
        this.sessionModel = sessionModel;
    }

    public LoginResponse login(String usuario, String clave) {
        LoginResponse response = restClient.login(usuario, clave);
        if (response != null && response.isExito() && response.getToken() != null) {
            sessionModel.setUsuario(usuario);
            sessionModel.setToken(response.getToken());
            sessionModel.setActivo(true);
        }
        return response;
    }

    public void logout() {
        sessionModel.limpiar();
    }

    public SessionModel getSessionModel() {
        return sessionModel;
    }
}
