package ec.edu.monster.strategy;

import ec.edu.monster.model.IConversor;
import ec.edu.monster.model.UnidadTemperatura;

/**
 * Estrategia concreta de conversión para la magnitud de Temperatura.
 * Implementa el patrón Strategy (GoF) desacoplando la lógica matemática del servicio web.
 */
public class ConversorTemperaturaStrategy implements IConversor<UnidadTemperatura> {

    @Override
    public double convertir(double valor, UnidadTemperatura origen, UnidadTemperatura destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las escalas termométricas de origen y destino no pueden ser nulas.");
        }
        // Conversión a escala base (Celsius) y luego a destino
        double celsius = origen.aCelsius(valor);
        return destino.desdeCelsius(celsius);
    }

    @Override
    public UnidadTemperatura resolverUnidad(String identificador) {
        return UnidadTemperatura.desdeTexto(identificador);
    }
}
