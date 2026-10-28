package ec.edu.monster.servicio;

import ec.edu.monster.modelo.IConversor;
import ec.edu.monster.modelo.UnidadMasa;

/**
 * Servicio y estrategia de conversión de masa bajo catálogo estricto de 5 unidades.
 * Catálogo: Kilogramo, Gramo, Libra, Onza, Quintal.
 * 
 * Implementa el patrón GoF Strategy para cálculo desacoplado y tipado.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversorMasa implements IConversor<UnidadMasa> {

    @Override
    public double convertir(double valor, UnidadMasa origen, UnidadMasa destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");
        }
        double valorEnKilos = valor * origen.getFactorAKilogramo();
        return valorEnKilos / destino.getFactorAKilogramo();
    }

    @Override
    public UnidadMasa resolverUnidad(String identificador) {
        return UnidadMasa.desdeTexto(identificador);
    }

    /**
     * Método de compatibilidad para controladores REST que reciben cadenas de texto.
     */
    public double convertirMasa(double valor, String unidadOrigen, String unidadDestino) {
        UnidadMasa origen = resolverUnidad(unidadOrigen);
        UnidadMasa destino = resolverUnidad(unidadDestino);
        return convertir(valor, origen, destino);
    }
}
