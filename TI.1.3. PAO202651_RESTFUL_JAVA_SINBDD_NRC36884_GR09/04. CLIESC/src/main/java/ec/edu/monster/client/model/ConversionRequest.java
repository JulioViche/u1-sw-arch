package ec.edu.monster.client.model;

import java.io.Serializable;

/**
 * [MODELO] Solicitud de conversión para el cliente de escritorio.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversionRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String magnitud;
    private String unidadOrigen;
    private String unidadDestino;
    private double valor;

    public ConversionRequest() {
    }

    public ConversionRequest(String magnitud, String unidadOrigen, String unidadDestino, double valor) {
        this.magnitud = magnitud;
        this.unidadOrigen = unidadOrigen;
        this.unidadDestino = unidadDestino;
        this.valor = valor;
    }

    public String getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }

    public String getUnidadOrigen() {
        return unidadOrigen;
    }

    public void setUnidadOrigen(String unidadOrigen) {
        this.unidadOrigen = unidadOrigen;
    }

    public String getUnidadDestino() {
        return unidadDestino;
    }

    public void setUnidadDestino(String unidadDestino) {
        this.unidadDestino = unidadDestino;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
