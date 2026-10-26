package ec.edu.monster.servicio;

/**
 * Servicio de conversión de longitud (5 unidades exactas).
 * Catálogo: Centímetros, Metros, Pies, Yardas, Millas.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversorLongitud {

    public double convertirLongitud(double valor, String unidadOrigen, String unidadDestino) {
        if (unidadOrigen == null || unidadDestino == null) {
            throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");
        }

        String origen = normalizar(unidadOrigen);
        String destino = normalizar(unidadDestino);

        double valorEnMetros;

        // 1. Paso a Metros (unidad base)
        switch (origen) {
            case "centimetros":
            case "centimetro":
            case "cm":
                valorEnMetros = valor / 100.0;
                break;
            case "metros":
            case "metro":
            case "m":
                valorEnMetros = valor;
                break;
            case "pies":
            case "pie":
            case "ft":
                valorEnMetros = valor * 0.3048;
                break;
            case "yardas":
            case "yarda":
            case "yd":
                valorEnMetros = valor * 0.9144;
                break;
            case "millas":
            case "milla":
            case "mi":
                valorEnMetros = valor * 1609.34;
                break;
            default:
                throw new IllegalArgumentException("Unidad de origen no reconocida en catálogo de longitud: " + unidadOrigen);
        }

        // 2. Paso de Metros a Destino
        switch (destino) {
            case "centimetros":
            case "centimetro":
            case "cm":
                return valorEnMetros * 100.0;
            case "metros":
            case "metro":
            case "m":
                return valorEnMetros;
            case "pies":
            case "pie":
            case "ft":
                return valorEnMetros / 0.3048;
            case "yardas":
            case "yarda":
            case "yd":
                return valorEnMetros / 0.9144;
            case "millas":
            case "milla":
            case "mi":
                return valorEnMetros / 1609.34;
            default:
                throw new IllegalArgumentException("Unidad de destino no reconocida en catálogo de longitud: " + unidadDestino);
        }
    }

    private String normalizar(String str) {
        return str.toLowerCase().trim()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
    }
}
