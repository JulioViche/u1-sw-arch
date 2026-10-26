package ec.edu.monster.client.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * DTO para la respuesta de conversión de unidades del servicio SOAP.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conversionResponse", namespace = "http://ws.monster.edu.ec/")
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

    @Override
    public String toString() {
        return "ConversionResponse{" +
                "exito=" + exito +
                ", valorConvertido=" + valorConvertido +
                ", mensajeError='" + mensajeError + '\'' +
                '}';
    }
}
