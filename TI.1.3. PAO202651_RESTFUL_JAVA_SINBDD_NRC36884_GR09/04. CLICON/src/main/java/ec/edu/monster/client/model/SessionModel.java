package ec.edu.monster.client.model;

import java.io.Serializable;

/**
 * [MODELO] Estado de la sesión del usuario en memoria del cliente (SINBDD).
 * Almacena el usuario autenticado y el token emitido por el servicio RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class SessionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario;
    private String token;
    private boolean activo;

    public SessionModel() {
        this.activo = false;
    }

    public SessionModel(String usuario, String token) {
        this.usuario = usuario;
        this.token = token;
        this.activo = (token != null && !token.trim().isEmpty());
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        this.activo = (token != null && !token.trim().isEmpty());
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void limpiar() {
        this.usuario = null;
        this.token = null;
        this.activo = false;
    }

    public String getTokenAbreviado() {
        if (token == null) return "N/A";
        return token.length() <= 8 ? token : token.substring(0, 8) + "...";
    }
}
