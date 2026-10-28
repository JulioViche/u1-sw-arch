package ec.edu.monster.servicio;

import ec.edu.monster.modelo.IConversor;
import ec.edu.monster.modelo.UnidadLongitud;

/**
 * Servicio y estrategia de conversión de longitud bajo catálogo estricto de 5 unidades.
 * Catálogo: Metro, Centímetro, Pie, Yarda, Milla.
 * 
 * Implementa el patrón GoF Strategy para cálculo desacoplado y tipado.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversorLongitud implements IConversor<UnidadLongitud> {

    @Override
    public double convertir(double valor, UnidadLongitud origen, UnidadLongitud destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");
        }
        double valorEnMetros = valor * origen.getFactorAMetro();
        return valorEnMetros / destino.getFactorAMetro();
    }

    @Override
    public UnidadLongitud resolverUnidad(String identificador) {
        return UnidadLongitud.desdeTexto(identificador);
    }

    /**
     * Método de compatibilidad para controladores REST que reciben cadenas de texto.
     */
    public double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) {
        UnidadLongitud origen = resolverUnidad(unidadOrigen);
        UnidadLongitud destino = resolverUnidad(unidadDestino);
        return convertir(valor, origen, destino);
    }
}
