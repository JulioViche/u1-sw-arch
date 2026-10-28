package ec.edu.monster.client.ws;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 * Gestor de conexión y consumo dinámico de los servicios web SOAP en el cliente de escritorio.
 * Soporta auto-detección de ruta de contexto tanto para '/04. SERVIDOR' como para '/ServidorSOAP'.
 */
public class SoapClientManager {

    private static final String DEFAULT_HOST = "192.168.137.1";
    private static final int DEFAULT_PORT = 8080;
    private static final String NAMESPACE_URI = "http://ws.monster.edu.ec/";

    private String host;
    private int port;
    private String baseEndpoint;
    private IServicioAutenticacion authService;
    private IServicioConversion conversionService;

    public SoapClientManager() {
        this(
            System.getProperty("server.host", System.getenv("SERVER_HOST") != null ? System.getenv("SERVER_HOST") : DEFAULT_HOST),
            Integer.parseInt(System.getProperty("server.port", System.getenv("SERVER_PORT") != null ? System.getenv("SERVER_PORT") : String.valueOf(DEFAULT_PORT)))
        );
    }

    public SoapClientManager(String host, int port) {
        this.host = (host != null && !host.trim().isEmpty()) ? host.trim() : DEFAULT_HOST;
        this.port = port > 0 ? port : DEFAULT_PORT;
        this.baseEndpoint = "http://" + this.host + ":" + this.port + "/ServidorSOAP";
    }

    private synchronized String resolverBaseActiva() {
        String customUrl = System.getProperty("server.url", System.getenv("SERVER_URL"));
        if (customUrl != null && !customUrl.trim().isEmpty()) {
            this.baseEndpoint = customUrl.trim().replaceAll("/+$", "");
            return this.baseEndpoint;
        }

        String[] candidatos = {
            "http://" + host + ":" + port + "/ServidorSOAP",
            "http://192.168.137.1:" + port + "/ServidorSOAP",
            "http://localhost:" + port + "/ServidorSOAP",
            "http://127.0.0.1:" + port + "/ServidorSOAP",
            "http://" + host + ":" + port + "/04.%20SERVIDOR",
            "http://localhost:" + port + "/04.%20SERVIDOR",
            "http://" + host + ":" + port + "/04.SERVIDOR"
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
