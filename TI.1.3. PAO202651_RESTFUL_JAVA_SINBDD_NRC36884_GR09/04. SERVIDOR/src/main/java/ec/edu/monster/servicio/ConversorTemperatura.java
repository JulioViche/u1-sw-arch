package ec.edu.monster.servicio;

import ec.edu.monster.modelo.IConversor;
import ec.edu.monster.modelo.UnidadTemperatura;

/**
 * Servicio y estrategia de conversión de temperatura bajo catálogo estricto de 5 escalas.
 * Catálogo: Celsius, Fahrenheit, Kelvin, Newton, Réaumur.
 * 
 * Implementa el patrón GoF Strategy para cálculo desacoplado y tipado.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversorTemperatura implements IConversor<UnidadTemperatura> {

    @Override
    public double convertir(double valor, UnidadTemperatura origen, UnidadTemperatura destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las escalas de origen y destino no pueden ser nulas.");
        }
        double celsius = UnidadTemperatura.aCelsius(valor, origen);
        return UnidadTemperatura.desdeCelsius(celsius, destino);
    }

    @Override
    public UnidadTemperatura resolverUnidad(String identificador) {
        return UnidadTemperatura.desdeTexto(identificador);
    }

    /**
     * Método de compatibilidad para controladores REST que reciben cadenas de texto.
     */
    public double convertirTemperatura(double valor, String opcionOrigen, String opcionDestino) {
        UnidadTemperatura origen = resolverUnidad(opcionOrigen);
        UnidadTemperatura destino = resolverUnidad(opcionDestino);
        return convertir(valor, origen, destino);
    }
}
