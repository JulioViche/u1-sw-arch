package ec.edu.monster.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * [MODELO] DTO para la respuesta de conversión del servicio RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean exito;
    private double valorConvertido;
    private String mensajeError;

    public ConversionResponse() {
    }

    public ConversionResponse(boolean exito, double valorConvertido, String mensajeError) {
        this.exito = exito;
        this.valorConvertido = valorConvertido;
        this.mensajeError = mensajeError;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public double getValorConvertido() {
        return valorConvertido;
    }

    public void setValorConvertido(double valorConvertido) {
        this.valorConvertido = valorConvertido;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}
