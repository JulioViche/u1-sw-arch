package ec.edu.monster.client.model;

import android.os.Build;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * [MODELO DE RED] Cliente SOAP para Android utilizando la biblioteca ksoap2-android.
 * Ejecuta llamadas asíncronas a los endpoints JAX-WS del Servidor SOAP.
 * Soporta fallback inteligente entre Emulador (10.0.2.2) y Hotspot (192.168.137.1).
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class SoapClient {

    private static final String NAMESPACE = "http://ws.monster.edu.ec/";
    private static final int TIMEOUT_MS = 4000;

    public static final String EMULATOR_URL_04_SERVIDOR = "http://10.0.2.2:8080/04.%20SERVIDOR";
    public static final String EMULATOR_URL_SERVIDOR_SOAP = "http://10.0.2.2:8080/ServidorSOAP";
    public static final String EMULATOR_URL_04_SINDISPACE = "http://10.0.2.2:8080/04.SERVIDOR";
    public static final String EMULATOR_URL_8085 = "http://10.0.2.2:8085/ServidorSOAP";
    public static final String EMULATOR_URL_8080 = EMULATOR_URL_04_SERVIDOR;

    public static final String HOTSPOT_URL_04_SERVIDOR = "http://192.168.137.1:8080/04.%20SERVIDOR";
    public static final String HOTSPOT_URL_SERVIDOR_SOAP = "http://192.168.137.1:8080/ServidorSOAP";
    public static final String HOTSPOT_URL_8085 = "http://192.168.137.1:8085/ServidorSOAP";
    public static final String HOTSPOT_URL_8080 = HOTSPOT_URL_04_SERVIDOR;

    private final ExecutorService executor;
    private String activeBaseUrl;

    public SoapClient() {
        this.executor = Executors.newFixedThreadPool(2);
        this.activeBaseUrl = isEmulator() ? EMULATOR_URL_04_SERVIDOR : HOTSPOT_URL_04_SERVIDOR;
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
            list.add(EMULATOR_URL_04_SERVIDOR);
            list.add(EMULATOR_URL_04_SINDISPACE);
            list.add(EMULATOR_URL_SERVIDOR_SOAP);
            list.add(EMULATOR_URL_8085);
            list.add(HOTSPOT_URL_04_SERVIDOR);
            list.add(HOTSPOT_URL_SERVIDOR_SOAP);
            list.add(HOTSPOT_URL_8085);
        } else {
            list.add(HOTSPOT_URL_04_SERVIDOR);
            list.add(HOTSPOT_URL_SERVIDOR_SOAP);
            list.add(HOTSPOT_URL_8085);
            list.add(EMULATOR_URL_04_SERVIDOR);
            list.add(EMULATOR_URL_04_SINDISPACE);
            list.add(EMULATOR_URL_SERVIDOR_SOAP);
            list.add(EMULATOR_URL_8085);
        }

        List<String> unique = new ArrayList<>();
        for (String url : list) {
            if (!unique.contains(url)) {
                unique.add(url);
            }
        }
        return unique;
    }

    public String getActiveBaseUrl() {
        return activeBaseUrl != null ? activeBaseUrl : (isEmulator() ? EMULATOR_URL_04_SERVIDOR : HOTSPOT_URL_04_SERVIDOR);
    }

    public void setActiveBaseUrl(String url) {
        this.activeBaseUrl = normalizarUrl(url);
    }

    public Future<LoginResponse> iniciarSesion(String baseUrl, String usuario, String clave) {
        return executor.submit(() -> {
            List<String> candidates = getCandidateUrls(baseUrl);
            LoginResponse lastResponse = null;

            for (String candidate : candidates) {
                try {
                    String endpoint = normalizarUrl(candidate) + "/ServicioAutenticacion";
                    String soapAction = NAMESPACE + "iniciarSesion";

                    SoapObject request = new SoapObject(NAMESPACE, "iniciarSesion");
                    request.addProperty("usuario", usuario);
                    request.addProperty("clave", clave);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE transport = new HttpTransportSE(endpoint, 2500);
                    transport.call(soapAction, envelope);

                    SoapObject response = (SoapObject) envelope.getResponse();
                    boolean exito = Boolean.parseBoolean(obtenerPropiedad(response, "exito"));
                    String token = obtenerPropiedad(response, "token");
                    String mensaje = obtenerPropiedad(response, "mensaje");

                    if (exito && token != null) {
                        this.activeBaseUrl = candidate;
                        return new LoginResponse(true, token, mensaje);
                    }
                    lastResponse = new LoginResponse(exito, token, mensaje);

                } catch (Exception e) {
                    lastResponse = new LoginResponse(false, null, "Error conectando a " + candidate + ": " + e.getMessage());
                }
            }

            return (lastResponse != null) ? lastResponse : new LoginResponse(false, null, "No se pudo conectar a ningún servidor.");
        });
    }

    public Future<ConversionResponse> convertirLongitud(String baseUrl, String token, double valor, String origen, String destino) {
        return ejecutarConversion(baseUrl, "convertirLongitud", token, valor, origen, destino);
    }

    public Future<ConversionResponse> convertirMasa(String baseUrl, String token, double valor, String origen, String destino) {
        return ejecutarConversion(baseUrl, "convertirMasa", token, valor, origen, destino);
    }

    public Future<ConversionResponse> convertirTemperatura(String baseUrl, String token, double valor, String origen, String destino) {
        return ejecutarConversion(baseUrl, "convertirTemperatura", token, valor, origen, destino);
    }

    private Future<ConversionResponse> ejecutarConversion(String baseUrl, String operacion, String token, double valor, String origen, String destino) {
        return executor.submit(() -> {
            List<String> candidates = getCandidateUrls(baseUrl != null ? baseUrl : activeBaseUrl);

            for (String candidate : candidates) {
                try {
                    String endpoint = normalizarUrl(candidate) + "/ServicioConversion";
                    String soapAction = NAMESPACE + operacion;

                    SoapObject request = new SoapObject(NAMESPACE, operacion);
                    request.addProperty("token", token);
                    request.addProperty("valor", String.valueOf(valor));
                    request.addProperty("unidadOrigen", origen);
                    request.addProperty("unidadDestino", destino);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE transport = new HttpTransportSE(endpoint, TIMEOUT_MS);
                    transport.call(soapAction, envelope);

                    SoapObject response = (SoapObject) envelope.getResponse();
                    boolean exito = Boolean.parseBoolean(obtenerPropiedad(response, "exito"));
                    double valorConvertido = 0.0;
                    String valorStr = obtenerPropiedad(response, "valorConvertido");
                    if (valorStr != null && !valorStr.trim().isEmpty()) {
                        try {
                            valorConvertido = Double.parseDouble(valorStr);
                        } catch (NumberFormatException ignored) {}
                    }
                    String mensajeError = obtenerPropiedad(response, "mensajeError");

                    this.activeBaseUrl = candidate;
                    return new ConversionResponse(exito, valorConvertido, mensajeError);

                } catch (Exception ignored) {
                    // Si falla el candidato actual, continuar con el siguiente
                }
            }

            return new ConversionResponse(false, 0.0, "Error de comunicación SOAP (" + operacion + "): No hubo respuesta del servidor.");
        });
    }

    private String obtenerPropiedad(SoapObject obj, String nombre) {
        if (obj == null) return null;
        try {
            if (obj.hasProperty(nombre)) {
                Object val = obj.getProperty(nombre);
                return (val != null) ? val.toString() : null;
            }
        } catch (Exception ignored) {}
        return null;
    }

    public static String normalizarUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return isEmulator() ? EMULATOR_URL_04_SERVIDOR : HOTSPOT_URL_04_SERVIDOR;
        }
        String u = url.trim();
        while (u.endsWith("/")) {
            u = u.substring(0, u.length() - 1);
        }
        return u;
    }
}
