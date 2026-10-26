package ec.edu.monster.model;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Objeto de transferencia (DTO) para la respuesta de conversiones de unidades.
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Grupo: GR09
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConversionResponse", propOrder = {
    "exito",
    "valorConvertido",
    "mensajeError"
})
public class ConversionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "exito")
    private boolean exito;

    @XmlElement(name = "valorConvertido")
    private double valorConvertido;

    @XmlElement(name = "mensajeError")
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
