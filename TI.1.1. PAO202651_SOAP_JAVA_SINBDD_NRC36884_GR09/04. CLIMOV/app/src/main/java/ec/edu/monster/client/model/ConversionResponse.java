package ec.edu.monster.client.model;

import java.io.Serializable;

/**
 * [MODELO] Respuesta emitida por el servicio SOAP de conversión.
 */
public class ConversionResponse implements Serializable {

    private final boolean exito;
    private final double valorConvertido;
    private final String mensajeError;

    public ConversionResponse(boolean exito, double valorConvertido, String mensajeError) {
        this.exito = exito;
        this.valorConvertido = valorConvertido;
        this.mensajeError = mensajeError;
    }

    public boolean isExito() {
        return exito;
    }

    public double getValorConvertido() {
        return valorConvertido;
    }

    public String getMensajeError() {
        return mensajeError;
    }
}
