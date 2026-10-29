package ec.edu.monster.client.model;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * [MODELO DE RED] Cliente SOAP para Android utilizando la biblioteca ksoap2-android.
 * Ejecuta llamadas asíncronas a los endpoints JAX-WS del Servidor SOAP.
 */
public class SoapClient {

    private static final String NAMESPACE = "http://ws.monster.edu.ec/";
    private static final int TIMEOUT_MS = 10000;

    private final ExecutorService executor;

    public SoapClient() {
        this.executor = Executors.newFixedThreadPool(2);
    }

    public Future<LoginResponse> iniciarSesion(String baseUrl, String usuario, String clave) {
        return executor.submit(() -> {
            try {
                String endpoint = normalizarUrl(baseUrl) + "/ServicioAutenticacion";
                String soapAction = NAMESPACE + "iniciarSesion";

                SoapObject request = new SoapObject(NAMESPACE, "iniciarSesion");
                request.addProperty("usuario", usuario);
                request.addProperty("clave", clave);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE transport = new HttpTransportSE(endpoint, TIMEOUT_MS);
                transport.call(soapAction, envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                boolean exito = Boolean.parseBoolean(obtenerPropiedad(response, "exito"));
                String token = obtenerPropiedad(response, "token");
                String mensaje = obtenerPropiedad(response, "mensaje");

                return new LoginResponse(exito, token, mensaje);

            } catch (Exception e) {
                return new LoginResponse(false, null, "Error de comunicación SOAP: " + e.getMessage());
            }
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
            try {
                String endpoint = normalizarUrl(baseUrl) + "/ServicioConversion";
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

                return new ConversionResponse(exito, valorConvertido, mensajeError);

            } catch (Exception e) {
                return new ConversionResponse(false, 0.0, "Error de comunicación SOAP (" + operacion + "): " + e.getMessage());
            }
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

    private String normalizarUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "http://192.168.137.1:8733/ServidorSOAP";
        }
        String u = url.trim();
        while (u.endsWith("/")) {
            u = u.substring(0, u.length() - 1);
        }
        return u;
    }
}

