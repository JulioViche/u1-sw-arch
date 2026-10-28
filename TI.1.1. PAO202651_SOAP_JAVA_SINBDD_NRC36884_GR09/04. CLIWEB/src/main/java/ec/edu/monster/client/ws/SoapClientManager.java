package ec.edu.monster.client.ws;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 * Gestor de conexión y consumo dinámico de los servicios web SOAP en el cliente web.
 * Soporta auto-detección de ruta de contexto tanto para '/04. SERVIDOR' como para '/ServidorSOAP'.
 */
public class SoapClientManager {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final String NAMESPACE_URI = "http://ws.monster.edu.ec/";

    private String baseEndpoint;
    private IServicioAutenticacion authService;
    private IServicioConversion conversionService;

    public SoapClientManager() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public SoapClientManager(String host, int port) {
        this.baseEndpoint = "http://" + host + ":" + port + "/04.%20SERVIDOR";
    }

    private synchronized String resolverBaseActiva() {
        String[] candidatos = {
            "http://localhost:8080/04.%20SERVIDOR",
            "http://localhost:8080/04.SERVIDOR",
            "http://localhost:8080/ServidorSOAP"
        };

        for (String base : candidatos) {
            try {
                URL url = new URL(base + "/ServicioAutenticacion?wsdl");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(600);
                conn.setReadTimeout(600);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    this.baseEndpoint = base;
                    return base;
                }
            } catch (Exception ignored) {
            }
        }
        return this.baseEndpoint;
    }

    public synchronized IServicioAutenticacion getAuthService() throws MalformedURLException {
        if (authService == null) {
            String activeBase = resolverBaseActiva();
            URL wsdlUrl = new URL(activeBase + "/ServicioAutenticacion?wsdl");
            QName serviceName = new QName(NAMESPACE_URI, "ServicioAutenticacion");

            Service service = Service.create(wsdlUrl, serviceName);
            authService = service.getPort(IServicioAutenticacion.class);

            BindingProvider bp = (BindingProvider) authService;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, activeBase + "/ServicioAutenticacion");
        }
        return authService;
    }

    public synchronized IServicioConversion getConversionService() throws MalformedURLException {
        if (conversionService == null) {
            String activeBase = resolverBaseActiva();
            URL wsdlUrl = new URL(activeBase + "/ServicioConversion?wsdl");
            QName serviceName = new QName(NAMESPACE_URI, "ServicioConversion");

            Service service = Service.create(wsdlUrl, serviceName);
            conversionService = service.getPort(IServicioConversion.class);

            BindingProvider bp = (BindingProvider) conversionService;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, activeBase + "/ServicioConversion");
        }
        return conversionService;
    }
}
