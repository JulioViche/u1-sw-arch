package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.ws.IServicioAutenticacion;
import ec.edu.monster.client.ws.SoapClientManager;

/**
 * [CONTROLADOR] Gestiona la autenticación en el cliente de escritorio.
 */
public class AuthController {

    private final SoapClientManager clientManager;
    private final SessionModel sessionModel;

    public AuthController(SoapClientManager clientManager, SessionModel sessionModel) {
        this.clientManager = clientManager;
        this.sessionModel = sessionModel;
    }

    public LoginResponse login(String usuario, String clave) throws Exception {
        IServicioAutenticacion authService = clientManager.getAuthService();
        LoginResponse response = authService.iniciarSesion(usuario, clave);

        if (response != null && response.isExito()) {
            sessionModel.setUsuario(usuario);
            sessionModel.setToken(response.getToken());
            sessionModel.setActivo(true);
        } else {
            sessionModel.limpiar();
        }
        return response;
    }

    public boolean validarToken() throws Exception {
        if (!sessionModel.isActivo() || sessionModel.getToken() == null) {
            return false;
        }
        IServicioAutenticacion authService = clientManager.getAuthService();
        boolean valido = authService.validarToken(sessionModel.getToken());
        sessionModel.setActivo(valido);
        return valido;
    }

    public boolean logout() {
        if (!sessionModel.isActivo() || sessionModel.getToken() == null) {
            sessionModel.limpiar();
            return true;
        }
        try {
            IServicioAutenticacion authService = clientManager.getAuthService();
            boolean res = authService.cerrarSesion(sessionModel.getToken());
            sessionModel.limpiar();
            return res;
        } catch (Exception e) {
            sessionModel.limpiar();
            return false;
        }
    }

    public SessionModel getSessionModel() {
        return sessionModel;
    }
}
