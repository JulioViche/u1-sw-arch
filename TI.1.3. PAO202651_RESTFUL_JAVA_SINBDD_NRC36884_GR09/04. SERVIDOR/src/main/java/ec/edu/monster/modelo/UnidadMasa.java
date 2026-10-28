package ec.edu.monster.modelo;

/**
 * Catálogo oficial estricto de exactamente 5 unidades para la magnitud Masa.
 * Factores de conversión referenciados al Kilogramo como unidad base del SI.
 * 
 * Requerimiento: "Exactamente 5 unidades por magnitud, ni una más, ni una menos".
 */
public enum UnidadMasa {
    KILOGRAMO("kilogramo", "kg", 1.0),
    GRAMO("gramo", "g", 0.001),
    LIBRA("libra", "lb", 0.453592),
    ONZA("onza", "oz", 0.0283495),
    QUINTAL("quintal", "q", 45.3592); // 1 quintal comercial = 100 libras = 45.3592 kg

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

    public static UnidadMasa desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La unidad de masa no puede ser nula o vacía.");
        }
        String normalizado = texto.trim().toLowerCase()
                .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace(" ", "");

        for (UnidadMasa u : values()) {
            if (u.nombre.equals(normalizado) || u.simbolo.equals(normalizado)) {
                return u;
            }
        }

        // Variaciones comunes en plural y sinónimos
        if (normalizado.equals("kilogramos") || normalizado.equals("kilo") || normalizado.equals("kilos")) return KILOGRAMO;
        if (normalizado.equals("gramos")) return GRAMO;
        if (normalizado.equals("libras") || normalizado.equals("lbs")) return LIBRA;
        if (normalizado.equals("onzas")) return ONZA;
        if (normalizado.equals("quintales")) return QUINTAL;

        throw new IllegalArgumentException("Unidad de masa no reconocida: '" + texto +
                "'. Catálogo permitido (5 unidades): Kilogramo, Gramo, Libra, Onza, Quintal.");
    }
}
