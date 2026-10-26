package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.ws.IServicioConversion;
import ec.edu.monster.client.ws.SoapClientManager;

/**
 * [CONTROLADOR] Gestiona el cómputo de conversiones invocando el servicio web SOAP.
 */
public class ConversionController {

    private final SoapClientManager clientManager;
    private final SessionModel sessionModel;

    public ConversionController(SoapClientManager clientManager, SessionModel sessionModel) {
        this.clientManager = clientManager;
        this.sessionModel = sessionModel;
    }

    public ConversionResponse convertir(ConversionRequest request) throws Exception {
        if (!sessionModel.isActivo() || sessionModel.getToken() == null) {
            return new ConversionResponse(false, 0.0, "Acceso no autorizado: No existe una sesión activa.");
        }

        IServicioConversion convService = clientManager.getConversionService();
        String token = sessionModel.getToken();
        String magnitud = request.getMagnitud().toLowerCase();

        switch (magnitud) {
            case "longitud":
                return convService.convertirLongitud(
                        token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino());
            case "masa":
                return convService.convertirMasa(
                        token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino());
            case "temperatura":
                return convService.convertirTemperatura(
                        token, request.getValor(), request.getUnidadOrigen(), request.getUnidadDestino());
            default:
                return new ConversionResponse(false, 0.0, "Magnitud desconocida: " + request.getMagnitud());
        }
    }
}
