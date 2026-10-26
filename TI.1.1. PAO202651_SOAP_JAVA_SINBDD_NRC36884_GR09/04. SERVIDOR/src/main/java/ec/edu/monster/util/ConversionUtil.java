package ec.edu.monster.util;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad matemática para conversiones de unidades normalizadas a una unidad base.
 * Cumple estrictamente con las 5 unidades por magnitud especificadas en el SRS:
 * - Longitud: Metros, Centímetros, Pies, Yardas, Millas (base: metro)
 * - Masa: Gramos, Kilogramos, Onzas, Libras, Quintales (base: kilogramo)
 * - Temperatura: Celsius, Fahrenheit, Kelvin, Newton, Réaumur (base: Celsius)
 * 
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Grupo: GR09
 */
public class ConversionUtil {

    // Factores a metro (m) - Exactamente 5 unidades
    private static final Map<String, Double> FACTORES_LONGITUD = new HashMap<>();
    // Factores a kilogramo (kg) - Exactamente 5 unidades
    private static final Map<String, Double> FACTORES_MASA = new HashMap<>();

    static {
        // 1. Longitud (metro, centimetro, pie, yarda, milla)
        FACTORES_LONGITUD.put("metro", 1.0);
        FACTORES_LONGITUD.put("metros", 1.0);
        FACTORES_LONGITUD.put("m", 1.0);

        FACTORES_LONGITUD.put("centimetro", 0.01);
        FACTORES_LONGITUD.put("centimetros", 0.01);
        FACTORES_LONGITUD.put("cm", 0.01);

        FACTORES_LONGITUD.put("pie", 0.3048);
        FACTORES_LONGITUD.put("pies", 0.3048);
        FACTORES_LONGITUD.put("ft", 0.3048);

        FACTORES_LONGITUD.put("yarda", 0.9144);
        FACTORES_LONGITUD.put("yardas", 0.9144);
        FACTORES_LONGITUD.put("yd", 0.9144);

        FACTORES_LONGITUD.put("milla", 1609.34);
        FACTORES_LONGITUD.put("millas", 1609.34);
        FACTORES_LONGITUD.put("mi", 1609.34);

        // 2. Masa (gramo, kilogramo, onza, libra, quintal)
        FACTORES_MASA.put("kilogramo", 1.0);
        FACTORES_MASA.put("kilogramos", 1.0);
        FACTORES_MASA.put("kg", 1.0);

        FACTORES_MASA.put("gramo", 0.001);
        FACTORES_MASA.put("gramos", 0.001);
        FACTORES_MASA.put("g", 0.001);

        FACTORES_MASA.put("onza", 0.0283495);
        FACTORES_MASA.put("onzas", 0.0283495);
        FACTORES_MASA.put("oz", 0.0283495);

        FACTORES_MASA.put("libra", 0.453592);
        FACTORES_MASA.put("libras", 0.453592);
        FACTORES_MASA.put("lb", 0.453592);

        // Quintal (100 libras = 45.3592 kg según patrón de referencia de la cátedra)
        FACTORES_MASA.put("quintal", 45.3592);
        FACTORES_MASA.put("quintales", 45.3592);
        FACTORES_MASA.put("q", 45.3592);
    }

    private static String normalizar(String str) {
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                         .trim()
                         .toLowerCase();
    }

    /**
     * Convierte valores de longitud (metros, centímetros, pies, yardas, millas).
     */
    public static double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) {
        String uOrigen = normalizar(unidadOrigen);
        String uDestino = normalizar(unidadDestino);

        if (!FACTORES_LONGITUD.containsKey(uOrigen)) {
            throw new IllegalArgumentException("Unidad de origen no reconocida para longitud: " + unidadOrigen + 
                    ". Las unidades permitidas son: metros, centimetros, pies, yardas, millas.");
        }
        if (!FACTORES_LONGITUD.containsKey(uDestino)) {
            throw new IllegalArgumentException("Unidad de destino no reconocida para longitud: " + unidadDestino +
                    ". Las unidades permitidas son: metros, centimetros, pies, yardas, millas.");
        }

        double factorOrigen = FACTORES_LONGITUD.get(uOrigen);
        double factorDestino = FACTORES_LONGITUD.get(uDestino);

        // Convertir origen a metros y luego metros a destino
        double valorEnMetros = valor * factorOrigen;
        return valorEnMetros / factorDestino;
    }

    /**
     * Convierte valores de masa (gramos, kilogramos, onzas, libras, quintales).
     */
    public static double convertirMasa(double valor, String unidadOrigen, String unidadDestino) {
        String uOrigen = normalizar(unidadOrigen);
        String uDestino = normalizar(unidadDestino);

        if (!FACTORES_MASA.containsKey(uOrigen)) {
            throw new IllegalArgumentException("Unidad de origen no reconocida para masa: " + unidadOrigen +
                    ". Las unidades permitidas son: gramos, kilogramos, onzas, libras, quintales.");
        }
        if (!FACTORES_MASA.containsKey(uDestino)) {
            throw new IllegalArgumentException("Unidad de destino no reconocida para masa: " + unidadDestino +
                    ". Las unidades permitidas son: gramos, kilogramos, onzas, libras, quintales.");
        }

        double factorOrigen = FACTORES_MASA.get(uOrigen);
        double factorDestino = FACTORES_MASA.get(uDestino);

        // Convertir origen a kilogramos y luego a destino
        double valorEnKilos = valor * factorOrigen;
        return valorEnKilos / factorDestino;
    }

    /**
     * Convierte valores de temperatura (Celsius, Fahrenheit, Kelvin, Newton, Réaumur).
     */
    public static double convertirTemperatura(double valor, String unidadOrigen, String unidadDestino) {
        String uOrigen = normalizar(unidadOrigen);
        String uDestino = normalizar(unidadDestino);

        // Paso 1: Convertir de origen a Celsius (base)
        double celsius;
        switch (uOrigen) {
            case "celsius":
            case "c":
            case "centigrados":
                celsius = valor;
                break;
            case "fahrenheit":
            case "f":
                celsius = (valor - 32.0) * 5.0 / 9.0;
                break;
            case "kelvin":
            case "k":
                celsius = valor - 273.15;
                break;
            case "newton":
            case "n":
                celsius = valor * 100.0 / 33.0;
                break;
            case "reaumur":
            case "re":
                celsius = valor * 5.0 / 4.0;
                break;
            default:
                throw new IllegalArgumentException("Escala de temperatura de origen no válida: " + unidadOrigen +
                        ". Las escalas permitidas son: Celsius, Fahrenheit, Kelvin, Newton, Reaumur.");
        }

        // Paso 2: Convertir de Celsius a destino
        switch (uDestino) {
            case "celsius":
            case "c":
            case "centigrados":
                return celsius;
            case "fahrenheit":
            case "f":
                return (celsius * 9.0 / 5.0) + 32.0;
            case "kelvin":
            case "k":
                return celsius + 273.15;
            case "newton":
            case "n":
                return celsius * 33.0 / 100.0;
            case "reaumur":
            case "re":
                return celsius * 4.0 / 5.0;
            default:
                throw new IllegalArgumentException("Escala de temperatura de destino no válida: " + unidadDestino +
                        ". Las escalas permitidas son: Celsius, Fahrenheit, Kelvin, Newton, Reaumur.");
        }
    }
}
