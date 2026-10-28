package ec.edu.monster.model;

import java.text.Normalizer;

/**
 * Catálogo estricto de exactamente 5 escalas para la magnitud de Temperatura.
 * Cumple con la especificación IEEE-830 del SRS (RQF-004) y la regla académica:
 * "Exactamente 5 unidades por magnitud (ni una más, ni una menos)".
 * Escala base de cálculo: CELSIUS (°C).
 */
public enum UnidadTemperatura {
    CELSIUS("Grado Celsius", "°C"),
    FAHRENHEIT("Grado Fahrenheit", "°F"),
    KELVIN("Kelvin", "K"),
    NEWTON("Grado Newton", "°N"),
    REAUMUR("Grado Réaumur", "°Ré");

    private final String nombre;
    private final String simbolo;

    UnidadTemperatura(String nombre, String simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Transforma un valor en la escala actual hacia Grados Celsius.
     */
    public double aCelsius(double valor) {
        switch (this) {
            case CELSIUS:
                return valor;
            case FAHRENHEIT:
                return (valor - 32.0) * 5.0 / 9.0;
            case KELVIN:
                return valor - 273.15;
            case NEWTON:
                return valor * 100.0 / 33.0;
            case REAUMUR:
                return valor * 5.0 / 4.0;
            default:
                throw new IllegalStateException("Escala desconocida: " + this);
        }
    }

    /**
     * Transforma un valor desde Grados Celsius hacia la escala actual.
     */
    public double desdeCelsius(double celsius) {
        switch (this) {
            case CELSIUS:
                return celsius;
            case FAHRENHEIT:
                return (celsius * 9.0 / 5.0) + 32.0;
            case KELVIN:
                return celsius + 273.15;
            case NEWTON:
                return celsius * 33.0 / 100.0;
            case REAUMUR:
                return celsius * 4.0 / 5.0;
            default:
                throw new IllegalStateException("Escala desconocida: " + this);
        }
    }

    private static String normalizar(String str) {
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                         .trim()
                         .toLowerCase();
    }

    public static UnidadTemperatura desdeTexto(String texto) {
        String normalizado = normalizar(texto);
        switch (normalizado) {
            case "celsius":
            case "c":
            case "centigrado":
            case "centigrados":
            case "gradocelsius":
            case "°c":
                return CELSIUS;
            case "fahrenheit":
            case "f":
            case "farenheit":
            case "gradofahrenheit":
            case "°f":
                return FAHRENHEIT;
            case "kelvin":
            case "k":
                return KELVIN;
            case "newton":
            case "n":
            case "gradonewton":
            case "°n":
                return NEWTON;
            case "reaumur":
            case "re":
            case "areaumur":
            case "gradoreaumur":
            case "°re":
                return REAUMUR;
            default:
                throw new IllegalArgumentException("Escala de temperatura no soportada: '" + texto + 
                    "'. Catálogo permitido de 5 escalas: Celsius (°C), Fahrenheit (°F), Kelvin (K), Newton (°N), Réaumur (°Ré).");
        }
    }
}
