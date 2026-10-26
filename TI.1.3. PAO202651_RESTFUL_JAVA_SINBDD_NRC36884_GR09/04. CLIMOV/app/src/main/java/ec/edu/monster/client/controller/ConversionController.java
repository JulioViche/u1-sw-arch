package ec.edu.monster.client.controller;

import android.os.Handler;
import android.os.Looper;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.RestClient;

/**
 * [CONTROLADOR] Gestiona el despacho de conversiones de unidades RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversionController {

    public interface ConversionCallback {
        void onSuccess(ConversionResponse response);
        void onError(String mensaje);
    }

    private final RestClient restClient;
    private final Handler mainHandler;

    public ConversionController() {
        this.restClient = new RestClient();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void procesarConversion(String baseUrl, String token, ConversionRequest req, ConversionCallback callback) {
        new Thread(() -> {
            try {
                if (baseUrl != null && !baseUrl.trim().isEmpty()) {
                    restClient.setBaseUrl(baseUrl);
                }

                ConversionResponse resp = restClient.convertir(req, token);

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
