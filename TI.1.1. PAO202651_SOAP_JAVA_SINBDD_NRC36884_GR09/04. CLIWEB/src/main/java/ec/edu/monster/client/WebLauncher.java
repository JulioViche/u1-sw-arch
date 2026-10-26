package ec.edu.monster.client;

import ec.edu.monster.client.controller.ConversionServlet;
import ec.edu.monster.client.controller.LoginServlet;
import ec.edu.monster.client.controller.LogoutServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import java.io.File;

/**
 * Lanzador de Servidor Web Embebido (Jetty 11 / Jakarta EE 9/10).
 * Utiliza ServletContextHandler con soporte de sesiones HTTP para garantizar
 * un arranque 100% estable y confiable de la arquitectura MVC.
 * 
 * Asignatura: Arquitectura de Software | Tarea: TI.1.1
 * Grupo: GR09 | Autor: Julio Enrique Viche Castillo
 */
public class WebLauncher {

    private static final int PUERTO = 8081;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PUERTO);

        // ServletContextHandler con sesiones activas
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/ClienteWeb");

        // Directorio base de archivos web (HTML, CSS, JS)
        File webappDir = new File("src/main/webapp");
        if (!webappDir.exists()) {
            webappDir = new File("04. CLIWEB/src/main/webapp");
        }
        context.setBaseResource(Resource.newResource(webappDir));

        // Registro directo de los Servlets Controladores MVC
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new ConversionServlet()), "/convertir");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");

        // Servlet por defecto para servir recursos estáticos (index.html, conversor.html, CSS)
        ServletHolder defaultHolder = new ServletHolder("default", DefaultServlet.class);
        defaultHolder.setInitParameter("dirAllowed", "false");
        defaultHolder.setInitParameter("welcomeServlet", "true");
        context.addServlet(defaultHolder, "/");
        context.setWelcomeFiles(new String[]{"index.html", "index.jsp"});

        server.setHandler(context);

        System.out.println("==========================================================");
        System.out.println("   INICIANDO CLIENTE WEB SOAP - CONVERSOR DE UNIDADES");
        System.out.println("   Asignatura: Arquitectura de Software | Tarea: TI.1.1");
        System.out.println("   Grupo: GR09 | Autor: Julio Enrique Viche Castillo");
        System.out.println("==========================================================");
        System.out.printf(" [*] Servidor Web activo en: http://localhost:%d/ClienteWeb/\n", PUERTO);
        System.out.println(" [*] Conectando con Backend SOAP en: http://localhost:8080/ServidorSOAP");
        System.out.println("----------------------------------------------------------");
        System.out.println(" [OK] Servidor Web listo y respondiendo en el puerto 8081.");
        System.out.println(" Presione Ctrl + C para detener el servicio.");
        System.out.println("==========================================================");

        server.start();

        try {
            if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:" + PUERTO + "/ClienteWeb/"));
            }
        } catch (Exception ignored) {
        }

        server.join();
    }
}
