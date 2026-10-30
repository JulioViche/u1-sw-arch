package ec.edu.monster.client.model;

import android.os.Build;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Cliente HTTP RESTful nativo para Android (utiliza HttpURLConnection y org.json).
 * Soporta fallback inteligente entre Emulador (10.0.2.2) y Hotspot (192.168.137.1),
 * con detección automática de puertos 8080 y 8085.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class RestClient {

    public static final String EMULATOR_URL_04_SERVIDOR = "http://10.0.2.2:8080/04.%20SERVIDOR/";
    public static final String EMULATOR_URL_04_SINDISPACE = "http://10.0.2.2:8080/04.SERVIDOR/";
    public static final String DEFAULT_EMULATOR_8080 = "http://10.0.2.2:8080/ServidorREST/";
    public static final String FALLBACK_EMULATOR_8085 = "http://10.0.2.2:8085/ServidorREST/";
    public static final String HOTSPOT_URL_04_SERVIDOR = "http://192.168.137.1:8080/04.%20SERVIDOR/";
    public static final String HOTSPOT_URL_04_SINDISPACE = "http://192.168.137.1:8080/04.SERVIDOR/";
    public static final String DEFAULT_HOTSPOT_8080 = "http://192.168.137.1:8080/ServidorREST/";
    public static final String FALLBACK_HOTSPOT_8085 = "http://192.168.137.1:8085/ServidorREST/";

    private static final int TIMEOUT_MS = 3000;
    private String baseUrl;

    public RestClient() {
        this(null);
    }

    public RestClient(String customUrl) {
        if (customUrl != null && !customUrl.trim().isEmpty()) {
            this.baseUrl = normalizarUrl(customUrl);
        } else {
            this.baseUrl = isEmulator() ? DEFAULT_EMULATOR_8080 : DEFAULT_HOTSPOT_8080;
        }
    }

    public static boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    public static List<String> getCandidateUrls(String preferredUrl) {
        List<String> list = new ArrayList<>();
        boolean isEmu = isEmulator();

        if (preferredUrl != null && !preferredUrl.trim().isEmpty()) {
            list.add(normalizarUrl(preferredUrl));
        }

        if (isEmu) {
            list.add(DEFAULT_EMULATOR_8080);
            list.add(FALLBACK_EMULATOR_8085);
            list.add(DEFAULT_HOTSPOT_8080);
            list.add(FALLBACK_HOTSPOT_8085);
        } else {
            list.add(DEFAULT_HOTSPOT_8080);
            list.add(FALLBACK_HOTSPOT_8085);
            list.add(DEFAULT_EMULATOR_8080);
            list.add(FALLBACK_EMULATOR_8085);
        }

        List<String> unique = new ArrayList<>();
        for (String url : list) {
            if (!unique.contains(url)) {
                unique.add(url);
            }
        }
        return unique;
    }

    public static String normalizarUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return isEmulator() ? DEFAULT_EMULATOR_8080 : DEFAULT_HOTSPOT_8080;
        }
        String u = url.trim();
        return u.endsWith("/") ? u : u + "/";
    }

    public String getBaseUrl() {
        return baseUrl != null ? baseUrl : (isEmulator() ? DEFAULT_EMULATOR_8080 : DEFAULT_HOTSPOT_8080);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = normalizarUrl(baseUrl);
    }

    public LoginResponse login(String usuario, String clave) {
        List<String> candidates = getCandidateUrls(this.baseUrl);
        LoginResponse lastResponse = null;

        for (String candidate : candidates) {
            LoginResponse res = intentarLogin(candidate, usuario, clave);
            if (res != null && res.isExito()) {
                this.baseUrl = candidate;
                return res;
            }
            lastResponse = res;
        }

        return lastResponse != null ? lastResponse : new LoginResponse(false, null, "No se pudo conectar a ningún servidor REST.");
    }

    private LoginResponse intentarLogin(String urlServidor, String usuario, String clave) {
        try {
            JSONObject body = new JSONObject();
            body.put("from", usuario);
            body.put("to", clave);

            JSONObject response = ejecutarPost(urlServidor + "auth/login", body);
            boolean exito = response.optBoolean("exito", false);
            String token = response.optString("token", null);
            String mensaje = response.optString("mensaje", "");

            return new LoginResponse(exito, token, mensaje);

        } catch (Exception e) {
            return new LoginResponse(false, null, "Fallo conectando a " + urlServidor + ": " + e.getMessage());
        }
    }

    public ConversionResponse convertir(ConversionRequest req, String token) {
        String endpoint;
        switch (req.getMagnitud().toLowerCase()) {
            case "longitud": endpoint = "conversor/longitud"; break;
            case "masa": endpoint = "conversor/masa"; break;
            case "temperatura": endpoint = "conversor/temperatura"; break;
            default: return new ConversionResponse(false, 0.0, "Magnitud desconocida");
        }

        List<String> candidates = getCandidateUrls(this.baseUrl);

        for (String candidate : candidates) {
            try {
                JSONObject body = new JSONObject();
                body.put("from", req.getUnidadOrigen());
                body.put("to", req.getUnidadDestino());
                body.put("value", req.getValor());
                body.put("token", token);

                JSONObject response = ejecutarPost(candidate + endpoint, body);
                boolean success = response.optBoolean("success", false);
                double result = response.optDouble("result", 0.0);
                String message = response.optString("message", "");

                if (success) {
                    this.baseUrl = candidate;
                    return new ConversionResponse(true, result, null);
                } else {
                    return new ConversionResponse(false, 0.0, message);
                }

            } catch (Exception ignored) {
                // Probar el siguiente candidato
            }
        }

        return new ConversionResponse(false, 0.0, "Error en llamada REST: Servidor no disponible.");
    }

    private JSONObject ejecutarPost(String urlCompleta, JSONObject jsonPayload) throws Exception {
        URL url = new URL(urlCompleta);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setDoOutput(true);

            byte[] input = jsonPayload.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            BufferedReader br;
            if (code >= 200 && code < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea.trim());
            }

            if (code >= 200 && code < 300) {
                return new JSONObject(sb.toString());
            } else {
                try {
                    return new JSONObject(sb.toString());
                } catch (Exception ignored) {
                    throw new Exception("Error HTTP " + code);
                }
            }
        } finally {
            conn.disconnect();
        }
    }
}
