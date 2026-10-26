package ec.edu.monster.client.controller;

import android.os.Handler;
import android.os.Looper;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SoapClient;

import java.util.concurrent.Future;

/**
 * [CONTROLADOR] Gestiona el despacho de conversiones de unidades SOAP.
 */
public class ConversionController {

    public interface ConversionCallback {
        void onSuccess(ConversionResponse response);
        void onError(String mensaje);
    }

    private final SoapClient soapClient;
    private final Handler mainHandler;

    public ConversionController() {
        this.soapClient = new SoapClient();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void procesarConversion(String baseUrl, String token, ConversionRequest req, ConversionCallback callback) {
        new Thread(() -> {
            try {
                Future<ConversionResponse> future;
                String magnitud = req.getMagnitud().toLowerCase();

                switch (magnitud) {
                    case "longitud":
                        future = soapClient.convertirLongitud(baseUrl, token, req.getValor(), req.getUnidadOrigen(), req.getUnidadDestino());
                        break;
                    case "masa":
                        future = soapClient.convertirMasa(baseUrl, token, req.getValor(), req.getUnidadOrigen(), req.getUnidadDestino());
                        break;
                    case "temperatura":
                        future = soapClient.convertirTemperatura(baseUrl, token, req.getValor(), req.getUnidadOrigen(), req.getUnidadDestino());
                        break;
                    default:
                        mainHandler.post(() -> callback.onError("Magnitud física no soportada: " + req.getMagnitud()));
                        return;
                }

                ConversionResponse resp = future.get();
                mainHandler.post(() -> {
                    if (resp != null && resp.isExito()) {
                        callback.onSuccess(resp);
                    } else {
                        String err = (resp != null && resp.getMensajeError() != null) 
                                ? resp.getMensajeError() 
                                : "Error al procesar la conversión.";
                        callback.onError(err);
                    }
                });

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("Error de red: " + e.getMessage()));
            }
        }).start();
    }
}
