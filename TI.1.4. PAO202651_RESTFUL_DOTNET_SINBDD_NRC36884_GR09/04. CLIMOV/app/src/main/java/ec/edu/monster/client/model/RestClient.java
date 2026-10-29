package ec.edu.monster.client.model;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Cliente HTTP RESTful nativo para Android (utiliza HttpURLConnection y org.json).
 * Diseñado para conectarse al host mediante 10.0.2.2 (emulador) o IP local (celular físico).
 * Soporta auto-detección entre puerto 8080 y 8085.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class RestClient {

    private static final String DEFAULT_HOTSPOT_5000 = "http://192.168.137.1:5000/api/";
    private static final String DEFAULT_EMULATOR_5000 = "http://10.0.2.2:5000/api/";
    private static final String FALLBACK_EMULATOR_5000 = "http://10.0.2.2:5000/api/";

    private static final int TIMEOUT_MS = 6000;
    private String baseUrl;

    public RestClient() {
        this(null);
    }

    public RestClient(String customUrl) {
        if (customUrl != null && !customUrl.trim().isEmpty()) {
            this.baseUrl = normalizarUrl(customUrl);
        } else {
            this.baseUrl = DEFAULT_HOTSPOT_5000;
        }
    }

    private String normalizarUrl(String url) {
        String u = url.trim();
        return u.endsWith("/") ? u : u + "/";
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = normalizarUrl(baseUrl);
    }

    public LoginResponse login(String usuario, String clave) {
        // Intentar primero con la URL activa, y si falla, probar fallback entre hotspot y emulador
        LoginResponse res = intentarLogin(this.baseUrl, usuario, clave);
        if (!res.isExito()) {
            String alt = this.baseUrl.contains("192.168.137.1") ? DEFAULT_EMULATOR_5000 : DEFAULT_HOTSPOT_5000;
            if (!alt.equalsIgnoreCase(this.baseUrl)) {
                LoginResponse resAlt = intentarLogin(alt, usuario, clave);
                if (resAlt.isExito()) {
                    this.baseUrl = alt;
                    return resAlt;
                }
            }
        }
        return res;
    }

    private LoginResponse intentarLogin(String urlServidor, String usuario, String clave) {
        try {
            JSONObject body = new JSONObject();
            body.put("from", usuario);
            body.put("to", clave);

            JSONObject response = ejecutarPost(urlServidor + "auth/login", body);

            boolean success = response.optBoolean("success", false);
            String token = response.optString("token", null);
            String message = response.optString("message", success ? "OK" : "Error en autenticación");

            return new LoginResponse(success, token, message);
        } catch (Exception e) {
            return new LoginResponse(false, null, "Error conectando con " + urlServidor + ": " + e.getMessage());
        }
    }

    public ConversionResponse convertir(ConversionRequest req, String token) {
        try {
            String endpoint = switch (req.getMagnitud().toLowerCase().trim()) {
                case "longitud" -> "conversor/longitud";
                case "masa" -> "conversor/masa";
                case "temperatura" -> "conversor/temperatura";
                default -> throw new IllegalArgumentException("Magnitud no reconocida: " + req.getMagnitud());
            };

            JSONObject body = new JSONObject();
            body.put("from", req.getUnidadOrigen());
            body.put("to", req.getUnidadDestino());
            body.put("value", req.getValor());
            body.put("token", token);

            JSONObject response = ejecutarPost(this.baseUrl + endpoint, body);

            boolean success = response.optBoolean("success", false);
            double result = response.optDouble("result", 0.0);
            String message = response.optString("message", "");

            if (success) {
                return new ConversionResponse(true, result, null);
            } else {
                return new ConversionResponse(false, 0.0, message);
            }
        } catch (Exception e) {
            return new ConversionResponse(false, 0.0, "Error en conversión REST: " + e.getMessage());
        }
    }

    private JSONObject ejecutarPost(String urlString, JSONObject payload) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setDoOutput(true);

            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            BufferedReader br;
            if (code >= 200 && code < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                if (conn.getErrorStream() != null) {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                } else {
                    throw new IOException("Error HTTP " + code);
                }
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            br.close();

            return new JSONObject(sb.toString());
        } finally {
            conn.disconnect();
        }
    }
}
