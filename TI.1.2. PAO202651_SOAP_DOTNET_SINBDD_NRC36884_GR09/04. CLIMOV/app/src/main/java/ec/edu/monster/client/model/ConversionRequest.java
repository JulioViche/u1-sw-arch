package ec.edu.monster.client.model;

import java.io.Serializable;

/**
 * [MODELO] Objeto de transferencia de datos con los parámetros de la solicitud de conversión.
 */
public class ConversionRequest implements Serializable {

    private final String magnitud;
    private final String unidadOrigen;
    private final String unidadDestino;
    private final double valor;

    public ConversionRequest(String magnitud, String unidadOrigen, String unidadDestino, double valor) {
        this.magnitud = magnitud;
        this.unidadOrigen = unidadOrigen;
        this.unidadDestino = unidadDestino;
        this.valor = valor;
    }

    public String getMagnitud() {
        return magnitud;
    }

    public String getUnidadOrigen() {
        return unidadOrigen;
    }

    public String getUnidadDestino() {
        return unidadDestino;
    }

    public double getValor() {
        return valor;
    }
}
