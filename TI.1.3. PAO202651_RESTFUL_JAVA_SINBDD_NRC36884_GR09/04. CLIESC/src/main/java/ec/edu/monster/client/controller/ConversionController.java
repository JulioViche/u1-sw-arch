package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;

/**
 * [CONTROLADOR] Ejecución de conversiones desde la interfaz de escritorio Swing.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversionController {

    private final RestClient restClient;
    private final SessionModel sessionModel;

    public ConversionController(RestClient restClient, SessionModel sessionModel) {
        this.restClient = restClient;
        this.sessionModel = sessionModel;
    }

    public ConversionResponse convertir(ConversionRequest req) {
        if (!sessionModel.isActivo() || sessionModel.getToken() == null) {
            return new ConversionResponse(false, 0.0, "Sesión no iniciada o token vencido.");
        }

        return restClient.convertir(
                req.getMagnitud(),
                req.getUnidadOrigen(),
                req.getUnidadDestino(),
                req.getValor(),
                sessionModel.getToken()
        );
    }
}
