package ec.edu.monster.server;

import ec.edu.monster.config.ConvUnitApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.URI;

/**
 * Punto de entrada standalone para ejecutar el servidor REST con Grizzly HTTP embebido.
 * Permite ejecutar el servidor localmente mediante: mvn exec:java
 * Si el puerto 8080 está ocupado (ej. por Docker), hace fallback inteligente al 8085.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ServidorRestMain {

    public static final String CONTEXT_PATH = "/WS_CONV_UNI_RESTFULL_JAVA-1.0-SNAPSHOT/";
    private static int activePort = 8080;

    public static HttpServer startServer(int port) {
        String baseUri = "http://0.0.0.0:" + port + CONTEXT_PATH;
        final ConvUnitApplication app = new ConvUnitApplication();
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), app);
    }

    public static void main(String[] args) {
        int preferredPort = Integer.getInteger("server.port", 8080);
        HttpServer server = null;

        try {
            server = startServer(preferredPort);
            activePort = preferredPort;
        } catch (Exception e) {
            if (e.getCause() instanceof BindException || e.getMessage().contains("Address already in use")) {
                System.out.println("⚠️  El puerto " + preferredPort + " está ocupado. Intentando en puerto alternativo 8085...");
                try {
                    server = startServer(8085);
                    activePort = 8085;
                } catch (Exception ex) {
                    System.err.println("❌ No se pudo iniciar el servidor en puerto 8085: " + ex.getMessage());
                    return;
                }
            } else {
                System.err.println("❌ Error al iniciar el servidor: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        try {
            System.out.println("====================================================================");
            System.out.println("🚀 SERVIDOR RESTFUL INICIADO (TI.1.3 - SINBDD)");
            System.out.println("   URL Base:    http://localhost:" + activePort + CONTEXT_PATH);
            System.out.println("   Health:      http://localhost:" + activePort + CONTEXT_PATH + "health");
            System.out.println("   Login:       http://localhost:" + activePort + CONTEXT_PATH + "auth/login");
            System.out.println("   Longitud:    http://localhost:" + activePort + CONTEXT_PATH + "conversor/longitud");
            System.out.println("   Masa:        http://localhost:" + activePort + CONTEXT_PATH + "conversor/masa");
            System.out.println("   Temperatura: http://localhost:" + activePort + CONTEXT_PATH + "conversor/temperatura");
            System.out.println("====================================================================");
            System.out.println("Credenciales quemadas: usuario 'MONSTER', contraseña 'MONSTER9'");
            System.out.println("Presione Ctrl+C en la consola para detener el servidor...");

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("Servidor detenido.");
            Thread.currentThread().interrupt();
        }
    }
}
