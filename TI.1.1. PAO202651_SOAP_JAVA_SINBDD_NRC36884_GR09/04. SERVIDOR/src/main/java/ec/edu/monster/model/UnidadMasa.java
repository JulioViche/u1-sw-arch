package ec.edu.monster.model;

import java.text.Normalizer;

/**
 * Catálogo estricto de exactamente 5 unidades para la magnitud de Masa.
 * Cumple con la especificación IEEE-830 del SRS (RQF-003) y la regla académica:
 * "Exactamente 5 unidades por magnitud (ni una más, ni una menos)".
 * Unidad base del Sistema Internacional: KILOGRAMO (kg).
 */
public enum UnidadMasa {
    GRAMO("Gramo", "g", 0.001),
    KILOGRAMO("Kilogramo", "kg", 1.0),
    ONZA("Onza", "oz", 0.0283495),
    LIBRA("Libra", "lb", 0.453592),
    QUINTAL("Quintal", "q", 45.3592); // 100 libras métricas según patrón de cátedra

    private final String nombre;
    private final String simbolo;
    private final double factorAKilogramo;

    UnidadMasa(String nombre, String simbolo, double factorAKilogramo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.factorAKilogramo = factorAKilogramo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getFactorAKilogramo() {
        return factorAKilogramo;
    }

    private static String normalizar(String str) {
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                         .trim()
                         .toLowerCase();
    }

    public static UnidadMasa desdeTexto(String texto) {
        String normalizado = normalizar(texto);
        switch (normalizado) {
            case "gramo":
            case "gramos":
            case "g":
                return GRAMO;
            case "kilogramo":
            case "kilogramos":
            case "kilo":
            case "kilos":
            case "kg":
                return KILOGRAMO;
            case "onza":
            case "onzas":
            case "oz":
                return ONZA;
            case "libra":
            case "libras":
            case "lb":
                return LIBRA;
            case "quintal":
            case "quintales":
            case "q":
                return QUINTAL;
            default:
                throw new IllegalArgumentException("Unidad de masa no soportada: '" + texto + 
                    "'. Catálogo permitido de 5 unidades: Gramo (g), Kilogramo (kg), Onza (oz), Libra (lb), Quintal (q).");
        }
    }
}
