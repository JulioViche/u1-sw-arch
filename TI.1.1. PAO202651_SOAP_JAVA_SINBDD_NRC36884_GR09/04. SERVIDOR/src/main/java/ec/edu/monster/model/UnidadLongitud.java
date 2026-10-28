package ec.edu.monster.model;

import java.text.Normalizer;

/**
 * Catálogo estricto de exactamente 5 unidades para la magnitud de Longitud.
 * Cumple con la especificación IEEE-830 del SRS (RQF-002) y la regla académica:
 * "Exactamente 5 unidades por magnitud (ni una más, ni una menos)".
 * Unidad base del Sistema Internacional: METRO (m).
 */
public enum UnidadLongitud {
    METRO("Metro", "m", 1.0),
    CENTIMETRO("Centímetro", "cm", 0.01),
    PIE("Pie", "ft", 0.3048),
    YARDA("Yarda", "yd", 0.9144),
    MILLA("Milla", "mi", 1609.34);

    private final String nombre;
    private final String simbolo;
    private final double factorAMetro;

    UnidadLongitud(String nombre, String simbolo, double factorAMetro) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.factorAMetro = factorAMetro;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getFactorAMetro() {
        return factorAMetro;
    }

    private static String normalizar(String str) {
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                         .trim()
                         .toLowerCase();
    }

    public static UnidadLongitud desdeTexto(String texto) {
        String normalizado = normalizar(texto);
        switch (normalizado) {
            case "metro":
            case "metros":
            case "m":
                return METRO;
            case "centimetro":
            case "centimetros":
            case "cm":
                return CENTIMETRO;
            case "pie":
            case "pies":
            case "ft":
                return PIE;
            case "yarda":
            case "yardas":
            case "yd":
                return YARDA;
            case "milla":
            case "millas":
            case "mi":
                return MILLA;
            default:
                throw new IllegalArgumentException("Unidad de longitud no soportada: '" + texto + 
                    "'. Catálogo permitido de 5 unidades: Metro (m), Centímetro (cm), Pie (ft), Yarda (yd), Milla (mi).");
        }
    }
}
