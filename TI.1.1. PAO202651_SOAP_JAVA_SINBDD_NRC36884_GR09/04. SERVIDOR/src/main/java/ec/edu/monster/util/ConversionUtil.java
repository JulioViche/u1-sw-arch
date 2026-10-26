package ec.edu.monster.util;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad matemática para conversiones de unidades normalizadas a una unidad base.
 * Soporta magnitudes físicas: Longitud (base: metro), Masa (base: kilogramo) y Temperatura (base: Celsius).
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Grupo: GR09
 */
public class ConversionUtil {

    // Factores a metro (m)
    private static final Map<String, Double> FACTORES_LONGITUD = new HashMap<>();
    // Factores a kilogramo (kg)
    private static final Map<String, Double> FACTORES_MASA = new HashMap<>();

    static {
        // Inicialización de factores de Longitud
        FACTORES_LONGITUD.put("metro", 1.0);
        FACTORES_LONGITUD.put("m", 1.0);
        FACTORES_LONGITUD.put("kilometro", 1000.0);
        FACTORES_LONGITUD.put("km", 1000.0);
        FACTORES_LONGITUD.put("centimetro", 0.01);
        FACTORES_LONGITUD.put("cm", 0.01);
        FACTORES_LONGITUD.put("milimetro", 0.001);
        FACTORES_LONGITUD.put("mm", 0.001);
        FACTORES_LONGITUD.put("milla", 1609.344);
        FACTORES_LONGITUD.put("mi", 1609.344);
        FACTORES_LONGITUD.put("yarda", 0.9144);
        FACTORES_LONGITUD.put("yd", 0.9144);
        FACTORES_LONGITUD.put("pie", 0.3048);
        FACTORES_LONGITUD.put("ft", 0.3048);
        FACTORES_LONGITUD.put("pulgada", 0.0254);
        FACTORES_LONGITUD.put("in", 0.0254);

        // Inicialización de factores de Masa
        FACTORES_MASA.put("kilogramo", 1.0);
        FACTORES_MASA.put("kg", 1.0);
        FACTORES_MASA.put("gramo", 0.001);
        FACTORES_MASA.put("g", 0.001);
        FACTORES_MASA.put("miligramo", 0.000001);
        FACTORES_MASA.put("mg", 0.000001);
        FACTORES_MASA.put("libra", 0.45359237);
        FACTORES_MASA.put("lb", 0.45359237);
        FACTORES_MASA.put("onza", 0.028349523125);
        FACTORES_MASA.put("oz", 0.028349523125);
        FACTORES_MASA.put("tonelada", 1000.0);
        FACTORES_MASA.put("t", 1000.0);
    }

    private static String normalizar(String str) {
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                         .trim()
                         .toLowerCase();
    }

    /**
     * Convierte valores de longitud.
     */
    public static double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) {
        String uOrigen = normalizar(unidadOrigen);
        String uDestino = normalizar(unidadDestino);

        if (!FACTORES_LONGITUD.containsKey(uOrigen)) {
            throw new IllegalArgumentException("Unidad de origen no reconocida para longitud: " + unidadOrigen);
        }
        if (!FACTORES_LONGITUD.containsKey(uDestino)) {
            throw new IllegalArgumentException("Unidad de destino no reconocida para longitud: " + unidadDestino);
        }

        double factorOrigen = FACTORES_LONGITUD.get(uOrigen);
        double factorDestino = FACTORES_LONGITUD.get(uDestino);

        // Convertir origen a metros y luego metros a destino
        double valorEnBase = valor * factorOrigen;
        return valorEnBase / factorDestino;
    }

    /**
     * Convierte valores de masa.
     */
    public static double convertirMasa(double valor, String unidadOrigen, String unidadDestino) {
        String uOrigen = normalizar(unidadOrigen);
        String uDestino = normalizar(unidadDestino);

        if (!FACTORES_MASA.containsKey(uOrigen)) {
            throw new IllegalArgumentException("Unidad de origen no reconocida para masa: " + unidadOrigen);
        }
        if (!FACTORES_MASA.containsKey(uDestino)) {
            throw new IllegalArgumentException("Unidad de destino no reconocida para masa: " + unidadDestino);
        }

        double factorOrigen = FACTORES_MASA.get(uOrigen);
        double factorDestino = FACTORES_MASA.get(uDestino);

        // Convertir origen a kilogramos y luego a destino
        double valorEnBase = valor * factorOrigen;
        return valorEnBase / factorDestino;
    }

    /**
     * Convierte valores de temperatura (Celsius, Fahrenheit, Kelvin).
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
                celsius = (valor - 32.0) * (5.0 / 9.0);
                break;
            case "kelvin":
            case "k":
                celsius = valor - 273.15;
                break;
            default:
                throw new IllegalArgumentException("Escala de temperatura de origen no válida: " + unidadOrigen);
        }

        // Paso 2: Convertir de Celsius a destino
        switch (uDestino) {
            case "celsius":
            case "c":
            case "centigrados":
                return celsius;
            case "fahrenheit":
            case "f":
                return (celsius * (9.0 / 5.0)) + 32.0;
            case "kelvin":
            case "k":
                return celsius + 273.15;
            default:
                throw new IllegalArgumentException("Escala de temperatura de destino no válida: " + unidadDestino);
        }
    }
}
