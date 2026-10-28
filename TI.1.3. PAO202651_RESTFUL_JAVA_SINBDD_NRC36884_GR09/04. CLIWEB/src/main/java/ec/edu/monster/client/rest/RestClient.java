package ec.edu.monster.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.LoginResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Cliente HTTP RESTful para consumo desde los Servlets Web.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class RestClient {

    private static final String DEFAULT_URL_8080 = "http://192.168.137.1:8080/ServidorREST/";
    private static final String FALLBACK_URL_8085 = "http://localhost:8085/ServidorREST/";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String baseUrl;

    public RestClient() {
        this(null);
    }

    public RestClient(String customBaseUrl) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();

        if (customBaseUrl != null && !customBaseUrl.trim().isEmpty()) {
            this.baseUrl = normalizarUrl(customBaseUrl);
        } else {
            this.baseUrl = resolverUrlActiva();
        }
    }

    private String normalizarUrl(String url) {
        String u = url.trim();
        return u.endsWith("/") ? u : u + "/";
    }

    private String resolverUrlActiva() {
        String envUrl = System.getenv("API_BASE_URL");
        if (envUrl == null) {
            envUrl = System.getProperty("api.base.url");
        }
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            return normalizarUrl(envUrl);
        }

        String[] candidatos = {
            "http://192.168.137.1:8080/ServidorREST/",
            "http://localhost:8080/ServidorREST/",
            "http://127.0.0.1:8080/ServidorREST/",
            "http://localhost:8080/04.%20SERVIDOR/",
            "http://localhost:8080/04.SERVIDOR/",
            "http://localhost:8080/WS_CONV_UNI_RESTFULL_JAVA-1.0-SNAPSHOT/",
            "http://localhost:8085/ServidorREST/",
            "http://localhost:8085/04.%20SERVIDOR/",
            "http://localhost:8085/04.SERVIDOR/",
            "http://localhost:8085/WS_CONV_UNI_RESTFULL_JAVA-1.0-SNAPSHOT/"
        };

        for (String candidato : candidatos) {
            if (probarServidor(candidato)) {
                return candidato;
            }
        }

        return DEFAULT_URL_8080;
    }

    private boolean probarServidor(String testUrl) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(testUrl + "health"))
                    .timeout(Duration.ofMillis(800))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public LoginResponse login(String usuario, String clave) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("from", usuario);
            payload.put("to", clave);

            String requestJson = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "auth/login"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());

            boolean success = root.path("success").asBoolean(false);
            String token = root.path("token").asText(null);
            String message = root.path("message").asText(success ? "OK" : "Error en autenticación");

            return new LoginResponse(success, token, message);
        } catch (Exception e) {
            return new LoginResponse(false, null, "Error conectando con el servidor REST: " + e.getMessage());
        }
    }

    public ConversionResponse convertir(String magnitud, String unidadOrigen, String unidadDestino, double valor, String token) {
        try {
            String endpoint;
            switch (magnitud.toLowerCase().trim()) {
                case "longitud":
                    endpoint = "conversor/longitud";
                    break;
                case "masa":
                    endpoint = "conversor/masa";
                    break;
                case "temperatura":
                    endpoint = "conversor/temperatura";
                    break;
                default:
                    throw new IllegalArgumentException("Magnitud no reconocida: " + magnitud);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("from", unidadOrigen);
            payload.put("to", unidadDestino);
            payload.put("value", valor);
            payload.put("token", token);

            String requestJson = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());

            boolean success = root.path("success").asBoolean(false);
            double result = root.path("result").asDouble(0.0);
            String message = root.path("message").asText("");

            if (success) {
                return new ConversionResponse(true, result, null);
            } else {
                return new ConversionResponse(false, 0.0, message);
            }
        } catch (Exception e) {
            return new ConversionResponse(false, 0.0, "Error de comunicación REST: " + e.getMessage());
        }
    }
}
