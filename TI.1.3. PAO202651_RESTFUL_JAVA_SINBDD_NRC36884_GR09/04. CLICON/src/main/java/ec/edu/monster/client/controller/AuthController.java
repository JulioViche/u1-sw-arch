package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;

/**
 * [CONTROLADOR] Gestiona el flujo de autenticación contra el servicio RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class AuthController {

    private final RestClient restClient;
    private final SessionModel session;

    public AuthController(RestClient restClient, SessionModel session) {
        this.restClient = restClient;
        this.session = session;
    }

    public boolean login(String usuario, String clave) {
        if (usuario == null || usuario.trim().isEmpty() ||
            clave == null || clave.trim().isEmpty()) {
            return false;
        }

        LoginResponse resp = restClient.login(usuario.trim(), clave.trim());
        if (resp != null && resp.isExito() && resp.getToken() != null) {
            session.setUsuario(usuario.trim());
            session.setToken(resp.getToken());
            session.setActivo(true);
            return true;
        }

        return false;
    }

    public void logout() {
        session.limpiar();
    }
}
