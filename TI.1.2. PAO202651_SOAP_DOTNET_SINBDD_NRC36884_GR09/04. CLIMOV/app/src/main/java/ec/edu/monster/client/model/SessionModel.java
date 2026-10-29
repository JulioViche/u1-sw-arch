package ec.edu.monster.client.model;

import java.io.Serializable;

/**
 * [MODELO] Representa la sesión del usuario autenticado en la aplicación móvil.
 * Almacenamiento en memoria volátil (SINBDD).
 */
public class SessionModel implements Serializable {

    private final String usuario;
    private final String token;
    private final long timestampInicio;

    public SessionModel(String usuario, String token) {
        this.usuario = usuario;
        this.token = token;
        this.timestampInicio = System.currentTimeMillis();
    }

    public String getUsuario() {
        return usuario;
    }

    public String getToken() {
        return token;
    }

    public String getTokenAbreviado() {
        if (token != null && token.length() > 8) {
            return token.substring(0, 8) + "...";
        }
        return token != null ? token : "N/A";
    }

    public boolean isActivo() {
        return token != null && !token.trim().isEmpty();
    }
}
