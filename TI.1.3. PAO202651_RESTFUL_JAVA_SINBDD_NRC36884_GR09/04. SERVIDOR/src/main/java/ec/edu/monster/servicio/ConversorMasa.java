package ec.edu.monster.servicio;

/**
 * Servicio de conversión de masa (5 unidades exactas).
 * Catálogo: Gramos, Kilogramos, Onzas, Libras, Quintales.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversorMasa {

    public double convertirMasa(double valor, String unidadOrigen, String unidadDestino) {
        if (unidadOrigen == null || unidadDestino == null) {
            throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");
        }

        String origen = normalizar(unidadOrigen);
        String destino = normalizar(unidadDestino);

        double valorEnKilos;

        // 1. Paso a Kilogramos (unidad base)
        switch (origen) {
            case "gramos":
            case "gramo":
            case "g":
                valorEnKilos = valor / 1000.0;
                break;
            case "kilogramos":
            case "kilogramo":
            case "kilos":
            case "kilo":
            case "kg":
                valorEnKilos = valor;
                break;
            case "onzas":
            case "onza":
            case "oz":
                valorEnKilos = valor * 0.0283495;
                break;
            case "libras":
            case "libra":
            case "lb":
            case "lbs":
                valorEnKilos = valor * 0.453592;
                break;
            case "quintales":
            case "quintal":
            case "q":
                // 1 quintal = 100 libras
                valorEnKilos = (valor * 100.0) * 0.453592;
                break;
            default:
                throw new IllegalArgumentException("Unidad de origen no reconocida en catálogo de masa: " + unidadOrigen);
        }

        // 2. Paso de Kilogramos a Destino
        switch (destino) {
            case "gramos":
            case "gramo":
            case "g":
                return valorEnKilos * 1000.0;
            case "kilogramos":
            case "kilogramo":
            case "kilos":
            case "kilo":
            case "kg":
                return valorEnKilos;
            case "onzas":
            case "onza":
            case "oz":
                return valorEnKilos / 0.0283495;
            case "libras":
            case "libra":
            case "lb":
            case "lbs":
                return valorEnKilos / 0.453592;
            case "quintales":
            case "quintal":
            case "q":
                return (valorEnKilos / 0.453592) / 100.0;
            default:
                throw new IllegalArgumentException("Unidad de destino no reconocida en catálogo de masa: " + unidadDestino);
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
