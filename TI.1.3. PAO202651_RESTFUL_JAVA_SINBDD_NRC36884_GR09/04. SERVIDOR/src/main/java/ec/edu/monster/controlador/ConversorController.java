package ec.edu.monster.controlador;

import ec.edu.monster.modelo.Conversion;
import ec.edu.monster.servicio.ConversorLongitud;
import ec.edu.monster.servicio.ConversorMasa;
import ec.edu.monster.servicio.ConversorTemperatura;
import ec.edu.monster.servicio.ServicioAuth;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Controlador REST para conversión de unidades de longitud, masa y temperatura.
 * Cada magnitud cuenta con un catálogo estricto de exactamente 5 unidades.
 * Salidas numéricas estandarizadas a 2 decimales con punto decimal.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@Path("/conversor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConversorController {

    private final ConversorLongitud longitudService = new ConversorLongitud();
    private final ConversorMasa masaService = new ConversorMasa();
    private final ConversorTemperatura temperaturaService = new ConversorTemperatura();
    private final ServicioAuth authService = new ServicioAuth();

    @POST
    @Path("/longitud")
    public Response convertirLongitud(Conversion modelo) {
        if (modelo == null) {
            return respuestaError(Response.Status.BAD_REQUEST, "Cuerpo de solicitud requerido.");
        }

        if (!authService.esTokenValido(modelo.getToken())) {
            modelo.setSuccess(false);
            modelo.setMessage("Sesión inválida o expirada. Debe iniciar sesión primero.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(modelo).build();
        }

        try {
            double resultado = longitudService.convertirLongitud(modelo.getValue(), modelo.getFrom(), modelo.getTo());
            double resultadoRedondeado = redondear2Decimales(resultado);

            modelo.setResult(resultadoRedondeado);
            modelo.setSuccess(true);
            modelo.setMessage("Conversión de longitud realizada exitosamente.");
            return Response.ok(modelo).build();
        } catch (IllegalArgumentException e) {
            modelo.setSuccess(false);
            modelo.setMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(modelo).build();
        }
    }

    @POST
    @Path("/masa")
    public Response convertirMasa(Conversion modelo) {
        if (modelo == null) {
            return respuestaError(Response.Status.BAD_REQUEST, "Cuerpo de solicitud requerido.");
        }

        if (!authService.esTokenValido(modelo.getToken())) {
            modelo.setSuccess(false);
            modelo.setMessage("Sesión inválida o expirada. Debe iniciar sesión primero.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(modelo).build();
        }

        try {
            double resultado = masaService.convertirMasa(modelo.getValue(), modelo.getFrom(), modelo.getTo());
            double resultadoRedondeado = redondear2Decimales(resultado);

            modelo.setResult(resultadoRedondeado);
            modelo.setSuccess(true);
            modelo.setMessage("Conversión de masa realizada exitosamente.");
            return Response.ok(modelo).build();
        } catch (IllegalArgumentException e) {
            modelo.setSuccess(false);
            modelo.setMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(modelo).build();
        }
    }

    @POST
    @Path("/temperatura")
    public Response convertirTemperatura(Conversion modelo) {
        if (modelo == null) {
            return respuestaError(Response.Status.BAD_REQUEST, "Cuerpo de solicitud requerido.");
        }

        if (!authService.esTokenValido(modelo.getToken())) {
            modelo.setSuccess(false);
            modelo.setMessage("Sesión inválida o expirada. Debe iniciar sesión primero.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(modelo).build();
        }

        try {
            double resultado = temperaturaService.convertirTemperatura(modelo.getValue(), modelo.getFrom(), modelo.getTo());
            double resultadoRedondeado = redondear2Decimales(resultado);

            modelo.setResult(resultadoRedondeado);
            modelo.setSuccess(true);
            modelo.setMessage("Conversión de temperatura realizada exitosamente.");
            return Response.ok(modelo).build();
        } catch (IllegalArgumentException e) {
            modelo.setSuccess(false);
            modelo.setMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(modelo).build();
        }
    }

    private double redondear2Decimales(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private Response respuestaError(Response.Status status, String mensaje) {
        Conversion error = new Conversion();
        error.setSuccess(false);
        error.setMessage(mensaje);
        return Response.status(status).entity(error).build();
    }
}
