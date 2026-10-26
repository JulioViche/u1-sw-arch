package ec.edu.monster.client.model;

import java.io.Serializable;

/**
 * [MODELO] Respuesta emitida por el servicio SOAP de autenticación.
 */
public class LoginResponse implements Serializable {

    private final boolean exito;
    private final String token;
    private final String mensaje;

    public LoginResponse(boolean exito, String token, String mensaje) {
        this.exito = exito;
        this.token = token;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public String getToken() {
        return token;
    }

    public String getMensaje() {
        return mensaje;
    }
}
