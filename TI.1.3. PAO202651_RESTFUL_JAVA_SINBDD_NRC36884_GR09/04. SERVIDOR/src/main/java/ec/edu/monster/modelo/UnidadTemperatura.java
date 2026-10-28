package ec.edu.monster.modelo;

/**
 * Catálogo oficial estricto de exactamente 5 escalas termométricas para Temperatura.
 * 
 * Escalas oficiales:
 * 1. Celsius (°C)
 * 2. Fahrenheit (°F)
 * 3. Kelvin (K)
 * 4. Newton (°N)
 * 5. Réaumur (°Ré)
 * 
 * Requerimiento: "Exactamente 5 unidades por magnitud, ni una más, ni una menos".
 */
public enum UnidadTemperatura {
    CELSIUS("celsius", "c"),
    FAHRENHEIT("fahrenheit", "f"),
    KELVIN("kelvin", "k"),
    NEWTON("newton", "n"),
    REAUMUR("reaumur", "re");

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

    public static UnidadTemperatura desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La escala de temperatura no puede ser nula o vacía.");
        }
        String normalizado = texto.trim().toLowerCase()
                .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace("°", "").replace(" ", "");

        for (UnidadTemperatura u : values()) {
            if (u.nombre.equals(normalizado) || u.simbolo.equals(normalizado)) {
                return u;
            }
        }

        // Variaciones comunes
        if (normalizado.equals("centigrados") || normalizado.equals("centigrado")) return CELSIUS;

        throw new IllegalArgumentException("Escala de temperatura no reconocida: '" + texto +
                "'. Catálogo permitido (5 escalas): Celsius, Fahrenheit, Kelvin, Newton, Reaumur.");
    }

    public static double aCelsius(double valor, UnidadTemperatura origen) {
        switch (origen) {
            case CELSIUS:
                return valor;
            case FAHRENHEIT:
                return (valor - 32.0) * (5.0 / 9.0);
            case KELVIN:
                return valor - 273.15;
            case NEWTON:
                return valor * (100.0 / 33.0);
            case REAUMUR:
                return valor * (5.0 / 4.0);
            default:
                throw new IllegalArgumentException("Escala origen no soportada: " + origen);
        }
    }

    public static double desdeCelsius(double celsius, UnidadTemperatura destino) {
        switch (destino) {
            case CELSIUS:
                return celsius;
            case FAHRENHEIT:
                return (celsius * (9.0 / 5.0)) + 32.0;
            case KELVIN:
                return celsius + 273.15;
            case NEWTON:
                return celsius * (33.0 / 100.0);
            case REAUMUR:
                return celsius * (4.0 / 5.0);
            default:
                throw new IllegalArgumentException("Escala destino no soportada: " + destino);
        }
    }
}
