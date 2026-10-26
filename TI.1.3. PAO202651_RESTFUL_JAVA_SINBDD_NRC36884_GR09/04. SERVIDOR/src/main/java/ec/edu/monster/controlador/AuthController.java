package ec.edu.monster.controlador;

import ec.edu.monster.modelo.Conversion;
import ec.edu.monster.servicio.ServicioAuth;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controlador REST para autenticación de usuarios (SINBDD).
 * Credenciales quemadas: MONSTER / MONSTER9
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private final ServicioAuth authService = new ServicioAuth();

    @POST
    @Path("/login")
    public Response login(Conversion modelo) {
        if (modelo == null) {
            Conversion error = new Conversion();
            error.setSuccess(false);
            error.setMessage("Cuerpo de solicitud requerido.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        String usuario = modelo.getFrom();
        String clave = modelo.getTo();

        String token = authService.validarLogin(usuario, clave);

        if (token != null) {
            modelo.setToken(token);
            modelo.setSuccess(true);
            modelo.setMessage("Autenticación satisfactoria. Bienvenido al sistema.");
            return Response.ok(modelo).build();
        } else {
            modelo.setSuccess(false);
            modelo.setMessage("Usuario o contraseña incorrectos. Credenciales válidas: MONSTER / MONSTER9");
            return Response.status(Response.Status.UNAUTHORIZED).entity(modelo).build();
        }
    }
}
