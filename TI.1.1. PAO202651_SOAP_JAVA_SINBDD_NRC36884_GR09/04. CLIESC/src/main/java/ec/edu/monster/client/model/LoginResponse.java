package ec.edu.monster.client.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * DTO para la respuesta de autenticación del servicio SOAP.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loginResponse", namespace = "http://ws.monster.edu.ec/")
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

    @Override
    public String toString() {
        return "LoginResponse{" +
                "exito=" + exito +
                ", token='" + token + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
