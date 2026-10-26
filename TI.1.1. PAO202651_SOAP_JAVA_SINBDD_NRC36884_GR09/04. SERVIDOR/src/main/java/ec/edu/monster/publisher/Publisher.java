package ec.edu.monster.publisher;

import ec.edu.monster.ws.AuthWebService;
import ec.edu.monster.ws.ConversionWebService;
import jakarta.xml.ws.Endpoint;

/**
 * Publicador independiente (Standalone) de los servicios web SOAP.
 * Permite ejecutar y probar el servidor directamente en Java sin necesidad
 * de configurar previamente un servidor de aplicaciones externo (Payara/GlassFish).
 * 
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Grupo: GR09
 */
public class Publisher {

    private static final String URL_AUTH = "http://localhost:8080/ServidorSOAP/ServicioAutenticacion";
    private static final String URL_CONV = "http://localhost:8080/ServidorSOAP/ServicioConversion";

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  INICIANDO SERVIDOR SOAP - CONVERSOR DE UNIDADES (SINBDD)");
        System.out.println("  Asignatura: Arquitectura de Software | Tarea: TI.1.1");
        System.out.println("  Grupo: GR09 | Autor: Julio Enrique Viche Castillo");
        System.out.println("==========================================================");

        try {
            // Publicar Servicio de Autenticación
            System.out.println("\n[1/2] Publicando Servicio de Autenticacion...");
            Endpoint.publish(URL_AUTH, new AuthWebService());
            System.out.println("-> Endpoint activo en: " + URL_AUTH);
            System.out.println("-> WSDL disponible en: " + URL_AUTH + "?wsdl");

            // Publicar Servicio de Conversión
            System.out.println("\n[2/2] Publicando Servicio de Conversion...");
            Endpoint.publish(URL_CONV, new ConversionWebService());
            System.out.println("-> Endpoint activo en: " + URL_CONV);
            System.out.println("-> WSDL disponible en: " + URL_CONV + "?wsdl");

            System.out.println("\n[OK] Servidor SOAP listo y a la escucha de peticiones en el puerto 8080.");
            System.out.println("Presione Ctrl + C para detener el servicio.\n");

        } catch (Exception e) {
            System.err.println("\n[ERROR] No se pudo publicar el servicio SOAP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
