package ec.edu.monster.model;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Objeto de transferencia (DTO) para la respuesta de autenticación.
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Grupo: GR09
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginResponse", propOrder = {
    "exito",
    "token",
    "mensaje"
})
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "exito")
    private boolean exito;

    @XmlElement(name = "token")
    private String token;

    @XmlElement(name = "mensaje")
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
