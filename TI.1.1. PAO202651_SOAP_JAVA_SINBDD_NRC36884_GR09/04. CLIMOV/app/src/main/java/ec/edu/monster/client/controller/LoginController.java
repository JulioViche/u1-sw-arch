package ec.edu.monster.client.controller;

import android.os.Handler;
import android.os.Looper;

import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.model.SoapClient;

import java.util.concurrent.Future;

/**
 * [CONTROLADOR] Gestiona el flujo de autenticación para la vista móvil.
 * Coordina llamadas asíncronas y publica resultados en el hilo principal (MainLooper).
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class LoginController {

    public interface LoginCallback {
        void onSuccess(SessionModel session, String resolvedUrl);
        void onError(String mensaje);
    }

    private final SoapClient soapClient;
    private final Handler mainHandler;

    public LoginController() {
        this.soapClient = new SoapClient();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void procesarLogin(String baseUrl, String usuario, String clave, LoginCallback callback) {
        new Thread(() -> {
            try {
                Future<LoginResponse> future = soapClient.iniciarSesion(baseUrl, usuario, clave);
                LoginResponse resp = future.get();

                mainHandler.post(() -> {
                    if (resp != null && resp.isExito() && resp.getToken() != null) {
                        SessionModel session = new SessionModel(usuario, resp.getToken());
                        callback.onSuccess(session, soapClient.getActiveBaseUrl());
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
