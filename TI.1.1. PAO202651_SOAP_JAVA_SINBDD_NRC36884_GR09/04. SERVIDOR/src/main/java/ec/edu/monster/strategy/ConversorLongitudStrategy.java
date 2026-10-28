package ec.edu.monster.strategy;

import ec.edu.monster.model.IConversor;
import ec.edu.monster.model.UnidadLongitud;

/**
 * Estrategia concreta de conversión para la magnitud de Longitud.
 * Implementa el patrón Strategy (GoF) desacoplando la lógica matemática del servicio web.
 */
public class ConversorLongitudStrategy implements IConversor<UnidadLongitud> {

    @Override
    public double convertir(double valor, UnidadLongitud origen, UnidadLongitud destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");
        }
        // Conversión a unidad base (Metro) y luego a destino
        double valorEnMetros = valor * origen.getFactorAMetro();
        return valorEnMetros / destino.getFactorAMetro();
    }

    @Override
    public UnidadLongitud resolverUnidad(String identificador) {
        return UnidadLongitud.desdeTexto(identificador);
    }
}
