package ec.edu.monster.util;

import ec.edu.monster.model.UnidadLongitud;
import ec.edu.monster.model.UnidadMasa;
import ec.edu.monster.model.UnidadTemperatura;
import ec.edu.monster.strategy.ConversorLongitudStrategy;
import ec.edu.monster.strategy.ConversorMasaStrategy;
import ec.edu.monster.strategy.ConversorTemperaturaStrategy;

/**
 * Fachada utilitaria de conversión que delega la ejecución de los algoritmos
 * a las estrategias concretas del patrón GoF Strategy.
 * 
 * Cumple estrictamente con las 5 unidades por magnitud especificadas en el SRS:
 * - Longitud (5): Metro, Centímetro, Pie, Yarda, Milla (base: Metro)
 * - Masa (5): Gramo, Kilogramo, Onza, Libra, Quintal (base: Kilogramo)
 * - Temperatura (5): Celsius, Fahrenheit, Kelvin, Newton, Réaumur (base: Celsius)
 * 
 * Modalidad: SINBDD (Cálculos en memoria).
 * Proyecto: TI.1.1 SOAP Java SINBDD | Grupo: GR09
 * Autor: Julio Enrique Viche Castillo
 */
public final class ConversionUtil {

    private static final ConversorLongitudStrategy ESTRATEGIA_LONGITUD = new ConversorLongitudStrategy();
    private static final ConversorMasaStrategy ESTRATEGIA_MASA = new ConversorMasaStrategy();
    private static final ConversorTemperaturaStrategy ESTRATEGIA_TEMPERATURA = new ConversorTemperaturaStrategy();

    private ConversionUtil() {
        // Constructor privado para clase utilitaria estática
    }

    /**
     * Convierte valores de longitud entre las 5 unidades oficiales.
     */
    public static double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) {
        UnidadLongitud origen = ESTRATEGIA_LONGITUD.resolverUnidad(unidadOrigen);
        UnidadLongitud destino = ESTRATEGIA_LONGITUD.resolverUnidad(unidadDestino);
        return ESTRATEGIA_LONGITUD.convertir(valor, origen, destino);
    }

    /**
     * Convierte valores de masa entre las 5 unidades oficiales.
     */
    public static double convertirMasa(double valor, String unidadOrigen, String unidadDestino) {
        UnidadMasa origen = ESTRATEGIA_MASA.resolverUnidad(unidadOrigen);
        UnidadMasa destino = ESTRATEGIA_MASA.resolverUnidad(unidadDestino);
        return ESTRATEGIA_MASA.convertir(valor, origen, destino);
    }

    /**
     * Convierte valores de temperatura entre las 5 escalas oficiales.
     */
    public static double convertirTemperatura(double valor, String unidadOrigen, String unidadDestino) {
        UnidadTemperatura origen = ESTRATEGIA_TEMPERATURA.resolverUnidad(unidadOrigen);
        UnidadTemperatura destino = ESTRATEGIA_TEMPERATURA.resolverUnidad(unidadDestino);
        return ESTRATEGIA_TEMPERATURA.convertir(valor, origen, destino);
    }
}
