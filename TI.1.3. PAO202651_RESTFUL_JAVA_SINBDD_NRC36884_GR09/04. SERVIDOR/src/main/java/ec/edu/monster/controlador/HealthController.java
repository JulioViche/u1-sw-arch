package ec.edu.monster.controlador;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Endpoint de comprobación de salud para monitoreo y verificación de la API REST.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthController {

    @GET
    public Response checkHealth() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "ServidorREST");
        status.put("protocol", "RESTful JSON");
        status.put("platform", "Java 17+ / Jakarta REST");
        status.put("author", "Julio Enrique Viche Castillo");
        status.put("nrc", "36884");
        status.put("grupo", "GR09");
        return Response.ok(status).build();
    }
}
