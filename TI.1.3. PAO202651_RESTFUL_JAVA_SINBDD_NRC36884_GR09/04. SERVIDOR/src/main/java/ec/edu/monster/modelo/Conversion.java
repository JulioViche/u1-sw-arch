package ec.edu.monster.modelo;

import java.io.Serializable;

/**
 * Modelo de datos para transferencias de conversión y autenticación REST.
 * Compatible con la convención de la tarea TI.1.3 RESTful Java.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class Conversion implements Serializable {

    private static final long serialVersionUID = 1L;

    // Datos que el cliente envía
    private String from;
    private String to;
    private double value;
    private String token;

    // Datos que el servidor responde
    private double result;
    private String message;
    private boolean success;

    public Conversion() {
    }

    public Conversion(String from, String to, double value, String token) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.token = token;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Conversion{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", token='" + token + '\'' +
                ", result=" + result +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
