package ec.edu.monster.client.view;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;
import java.util.Scanner;

/**
 * [VISTA] Interfaz de usuario para la consola de comandos.
 * Responsable exclusivo de la entrada/salida por terminal, formateo de texto
 * y presentación visual de datos. No contiene lógica de negocio ni llamadas directas a red.
 */
public class ConsoleView {

    public static final String[] UNIDADES_LONGITUD = {
        "metro", "centimetro", "pie", "yarda", "milla"
    };

    public static final String[] UNIDADES_MASA = {
        "gramo", "kilogramo", "libra", "onza", "quintal"
    };

    public static final String[] UNIDADES_TEMPERATURA = {
        "celsius", "fahrenheit", "kelvin", "newton", "reaumur"
    };

    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarBanner() {
        System.out.println("==================================================================");
        System.out.println("  UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE - EXTENSIÓN LATACUNGA");
        System.out.println("            DEPARTAMENTO DE ENERGÍA Y MECÁNICA");
        System.out.println("                 ARQUITECTURA DE SOFTWARE");
        System.out.println("                TAREA TI.1.1 (PAO 2026-51)");
        System.out.println("------------------------------------------------------------------");
        System.out.println("   CLIENTE DE CONSOLA: CONVERSOR DE UNIDADES (SOAP JAVA - SINBDD)  ");
        System.out.println("   NRC: 36884 | Grupo: GR09 | Autor: Julio Enrique Viche Castillo ");
        System.out.println("   Patrón Arquitectónico: Modelo - Vista - Controlador (MVC)");
        System.out.println("==================================================================\n");
    }

    public String[] pedirCredenciales() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("              INICIO DE SESIÓN OBLIGATORIO (AUTENTICACIÓN)        ");
        System.out.println("------------------------------------------------------------------");
        System.out.println(" [Nota]: Credenciales del sistema:");
        System.out.println("         Usuario: MONSTER  |  Contraseña: MONSTER9");
        System.out.println("------------------------------------------------------------------");

        System.out.print("Ingrese Usuario: ");
        String usuario = scanner.nextLine().trim();

        System.out.print("Ingrese Contraseña: ");
        String clave = scanner.nextLine().trim();

        return new String[]{usuario, clave};
    }

    public boolean preguntarReintento() {
        System.out.print("\n¿Desea reintentar el inicio de sesión? (s/n): ");
        String resp = scanner.nextLine().trim().toLowerCase();
        return resp.equals("s") || resp.equals("si") || resp.equals("y");
    }

    public String mostrarMenuPrincipal(SessionModel session) {
        System.out.println("\n==========================================================");
        System.out.println("              MENÚ PRINCIPAL DE CONVERSIONES (MVC)        ");
        System.out.println("==========================================================");
        System.out.printf(" Sesión activa: %s | Token: %s\n",
                session.getUsuario(), session.getTokenAbreviado());
        System.out.println("----------------------------------------------------------");
        System.out.println("  1. Conversión de Longitud (metro, centimetro, pie, yarda, milla)");
        System.out.println("  2. Conversión de Masa (gramo, kilogramo, libra, onza, quintal)");
        System.out.println("  3. Conversión de Temperatura (Celsius, Fahrenheit, Kelvin, Newton, Reaumur)");
        System.out.println("  4. Verificar estado del token de sesión");
        System.out.println("  5. Cerrar sesión y Salir");
        System.out.println("==========================================================");
        System.out.print("Seleccione una opción (1-5): ");
        return scanner.nextLine().trim();
    }

    public ConversionRequest solicitarDatosConversion(String magnitud, String[] unidades) {
        System.out.println("\n--- [CONVERSIÓN DE " + magnitud.toUpperCase() + "] ---");
        String origen = seleccionarUnidad("origen", unidades);
        if (origen == null) return null;

        String destino = seleccionarUnidad("destino", unidades);
        if (destino == null) return null;

        double valor = pedirValorNumerico();
        return new ConversionRequest(magnitud, origen, destino, valor);
    }

    private String seleccionarUnidad(String tipo, String[] unidades) {
        System.out.println("Seleccione unidad de " + tipo + ":");
        for (int i = 0; i < unidades.length; i++) {
            System.out.printf("  %d. %s\n", (i + 1), capitalizar(unidades[i]));
        }
        System.out.print("Elija una opción (1-" + unidades.length + "): ");
        String input = scanner.nextLine().trim();

        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < unidades.length) {
                return unidades[idx];
            } else {
                mostrarError("Opción de unidad fuera de rango.");
                return null;
            }
        } catch (NumberFormatException e) {
            mostrarError("Entrada no numérica.");
            return null;
        }
    }

    public double pedirValorNumerico() {
        while (true) {
            System.out.print("Ingrese el valor numérico a convertir: ");
            String input = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                mostrarError("Ingrese un número válido (ejemplo: 25.5 o 100).");
            }
        }
    }

    public void mostrarResultadoConversion(ConversionRequest req, ConversionResponse resp) {
        if (resp != null && resp.isExito()) {
            System.out.println("\n==========================================================");
            System.out.println("                   RESULTADO DE LA CONVERSIÓN             ");
            System.out.println("==========================================================");
            System.out.printf("   %.4f %s  --->  %.4f %s\n",
                    req.getValor(),
                    capitalizar(req.getUnidadOrigen()),
                    resp.getValorConvertido(),
                    capitalizar(req.getUnidadDestino()));
            System.out.println("==========================================================");
        } else {
            String err = (resp != null) ? resp.getMensajeError() : "Sin respuesta del servidor";
            mostrarError("En la conversión: " + err);
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarExito(String mensaje) {
        System.out.println("\n[OK] " + mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("\n[ERROR] " + mensaje);
    }

    public void mostrarDespedida() {
        System.out.println("\n==========================================================");
        System.out.println(" Gracias por utilizar el Conversor de Unidades SOAP (GR09)");
        System.out.println("==========================================================\n");
    }

    private String capitalizar(String str) {
        if (str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
