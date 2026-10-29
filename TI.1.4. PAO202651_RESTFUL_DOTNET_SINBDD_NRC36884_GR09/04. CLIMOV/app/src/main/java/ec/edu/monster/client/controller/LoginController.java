package ec.edu.monster.client.controller;

import android.os.Handler;
import android.os.Looper;

import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.model.RestClient;
import ec.edu.monster.client.model.SessionModel;

/**
 * [CONTROLADOR] Gestiona el flujo de autenticación para la vista móvil RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class LoginController {

    public interface LoginCallback {
        void onSuccess(SessionModel session, String resolvedBaseUrl);
        void onError(String mensaje);
    }

    private final RestClient restClient;
    private final Handler mainHandler;

    public LoginController() {
        this.restClient = new RestClient();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void procesarLogin(String baseUrl, String usuario, String clave, LoginCallback callback) {
        new Thread(() -> {
            try {
                if (baseUrl != null && !baseUrl.trim().isEmpty()) {
                    restClient.setBaseUrl(baseUrl);
                }

                LoginResponse resp = restClient.login(usuario, clave);

                mainHandler.post(() -> {
                    if (resp != null && resp.isExito() && resp.getToken() != null) {
                        SessionModel session = new SessionModel(usuario, resp.getToken());
                        callback.onSuccess(session, restClient.getBaseUrl());
                    } else {
                        String err = (resp != null && resp.getMensaje() != null)
                                ? resp.getMensaje()
                                : "Error al iniciar sesión.";
                        callback.onError(err);
                    }
                });

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("Error de conexión: " + e.getMessage()));
            }
        }).start();
    }
}
