package ec.edu.monster.client.ws;

import jakarta.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 * Gestor de conexión y consumo dinámico de los servicios web SOAP en el cliente web.
 */
public class SoapClientManager {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final String NAMESPACE_URI = "http://ws.monster.edu.ec/";

    private final String baseUrl;
    private IServicioAutenticacion authService;
    private IServicioConversion conversionService;

    public SoapClientManager() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public SoapClientManager(String host, int port) {
        this.baseUrl = "http://" + host + ":" + port + "/ServidorSOAP";
    }

    public IServicioAutenticacion getAuthService() throws MalformedURLException {
        if (authService == null) {
            URL wsdlUrl = new URL(baseUrl + "/ServicioAutenticacion?wsdl");
            QName qname = new QName(NAMESPACE_URI, "ServicioAutenticacion");
            Service service = Service.create(wsdlUrl, qname);
            authService = service.getPort(IServicioAutenticacion.class);
        }
        return authService;
    }

    public IServicioConversion getConversionService() throws MalformedURLException {
        if (conversionService == null) {
            URL wsdlUrl = new URL(baseUrl + "/ServicioConversion?wsdl");
            QName qname = new QName(NAMESPACE_URI, "ServicioConversion");
            Service service = Service.create(wsdlUrl, qname);
            conversionService = service.getPort(IServicioConversion.class);
        }
        return conversionService;
    }
}
