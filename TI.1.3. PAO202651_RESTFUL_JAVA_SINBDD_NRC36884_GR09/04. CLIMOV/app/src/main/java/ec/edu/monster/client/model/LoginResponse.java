package ec.edu.monster.client.model;

import java.io.Serializable;

/**
 * [MODELO] Respuesta de autenticación del servicio RESTful para Android.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean exito;
    private String token;
    private String mensaje;

    public LoginResponse() {
    }

    public LoginResponse(boolean exito, String token, String mensaje) {
        this.exito = exito;
        this.token = token;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
