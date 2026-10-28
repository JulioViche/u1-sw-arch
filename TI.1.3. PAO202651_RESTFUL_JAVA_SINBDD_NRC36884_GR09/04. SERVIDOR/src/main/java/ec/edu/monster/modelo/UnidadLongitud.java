package ec.edu.monster.modelo;

/**
 * Catálogo oficial estricto de exactamente 5 unidades para la magnitud Longitud.
 * Factores de conversión referenciados al Metro como unidad base del SI.
 * 
 * Requerimiento: "Exactamente 5 unidades por magnitud, ni una más, ni una menos".
 */
public enum UnidadLongitud {
    METRO("metro", "m", 1.0),
    CENTIMETRO("centimetro", "cm", 0.01),
    PIE("pie", "ft", 0.3048),
    YARDA("yarda", "yd", 0.9144),
    MILLA("milla", "mi", 1609.34);

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

    public static UnidadLongitud desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La unidad de longitud no puede ser nula o vacía.");
        }
        String normalizado = texto.trim().toLowerCase()
                .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace(" ", "");

        for (UnidadLongitud u : values()) {
            if (u.nombre.equals(normalizado) || u.simbolo.equals(normalizado)) {
                return u;
            }
        }

        // Variaciones comunes en plural
        if (normalizado.equals("metros")) return METRO;
        if (normalizado.equals("centimetros")) return CENTIMETRO;
        if (normalizado.equals("pies")) return PIE;
        if (normalizado.equals("yardas")) return YARDA;
        if (normalizado.equals("millas")) return MILLA;

        throw new IllegalArgumentException("Unidad de longitud no reconocida: '" + texto +
                "'. Catálogo permitido (5 unidades): Metro, Centímetro, Pie, Yarda, Milla.");
    }
}
