package ec.edu.monster.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * [MODELO] DTO para la respuesta de autenticación del servicio RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
