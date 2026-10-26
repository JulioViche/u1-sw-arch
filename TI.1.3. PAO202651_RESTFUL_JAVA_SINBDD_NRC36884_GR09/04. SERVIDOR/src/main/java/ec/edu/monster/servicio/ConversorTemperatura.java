package ec.edu.monster.servicio;

/**
 * Servicio de conversión de temperatura (5 unidades exactas).
 * Catálogo: Celsius, Fahrenheit, Kelvin, Newton, Réaumur.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversorTemperatura {

    public double convertirTemperatura(double valor, String opcionOrigen, String opcionDestino) {
        if (opcionOrigen == null || opcionDestino == null) {
            throw new IllegalArgumentException("Las unidades de origen y destino no pueden ser nulas.");
        }

        String origen = normalizar(opcionOrigen);
        String destino = normalizar(opcionDestino);

        double valorEnCelsius;

        // 1. Paso a Celsius (unidad base de trabajo)
        switch (origen) {
            case "celsius":
            case "centigrados":
            case "c":
                valorEnCelsius = valor;
                break;
            case "fahrenheit":
            case "f":
                valorEnCelsius = (valor - 32.0) * 5.0 / 9.0;
                break;
            case "kelvin":
            case "k":
                valorEnCelsius = valor - 273.15;
                break;
            case "newton":
            case "n":
                valorEnCelsius = valor * 100.0 / 33.0;
                break;
            case "reaumur":
            case "re":
                valorEnCelsius = valor * 5.0 / 4.0;
                break;
            default:
                throw new IllegalArgumentException("Unidad de origen no reconocida en catálogo de temperatura: " + opcionOrigen);
        }

        // 2. Paso de Celsius a Destino
        switch (destino) {
            case "celsius":
            case "centigrados":
            case "c":
                return valorEnCelsius;
            case "fahrenheit":
            case "f":
                return (valorEnCelsius * 9.0 / 5.0) + 32.0;
            case "kelvin":
            case "k":
                return valorEnCelsius + 273.15;
            case "newton":
            case "n":
                return valorEnCelsius * 33.0 / 100.0;
            case "reaumur":
            case "re":
                return valorEnCelsius * 4.0 / 5.0;
            default:
                throw new IllegalArgumentException("Unidad de destino no reconocida en catálogo de temperatura: " + opcionDestino);
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
