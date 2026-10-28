package ec.edu.monster.strategy;

import ec.edu.monster.model.IConversor;
import ec.edu.monster.model.UnidadMasa;

/**
 * Estrategia concreta de conversión para la magnitud de Masa.
 * Implementa el patrón Strategy (GoF) desacoplando la lógica matemática del servicio web.
 */
public class ConversorMasaStrategy implements IConversor<UnidadMasa> {

    @Override
    public double convertir(double valor, UnidadMasa origen, UnidadMasa destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");
        }
        // Conversión a unidad base (Kilogramo) y luego a destino
        double valorEnKilogramos = valor * origen.getFactorAKilogramo();
        return valorEnKilogramos / destino.getFactorAKilogramo();
    }

    @Override
    public UnidadMasa resolverUnidad(String identificador) {
        return UnidadMasa.desdeTexto(identificador);
    }
}
