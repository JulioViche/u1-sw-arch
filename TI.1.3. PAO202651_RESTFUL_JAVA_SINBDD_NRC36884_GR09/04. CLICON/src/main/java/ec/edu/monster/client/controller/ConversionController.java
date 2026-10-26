package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;

/**
 * [CONTROLADOR] Orquesta la ejecución de conversiones consumiendo el servicio RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ConversionController {

    private final RestClient restClient;
    private final SessionModel session;

    public ConversionController(RestClient restClient, SessionModel session) {
        this.restClient = restClient;
        this.session = session;
    }

    public ConversionResponse ejecutarConversion(ConversionRequest req) {
        if (!session.isActivo() || session.getToken() == null) {
            return new ConversionResponse(false, 0.0, "Sesión inactiva. Debe iniciar sesión primero.");
        }

        if (req == null) {
            return new ConversionResponse(false, 0.0, "Parámetros de conversión inválidos.");
        }

        return restClient.convertir(
                req.getMagnitud(),
                req.getUnidadOrigen(),
                req.getUnidadDestino(),
                req.getValor(),
                session.getToken()
        );
    }
}
