package ec.edu.monster.client.ws;

import ec.edu.monster.client.model.LoginResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

/**
 * Interfaz de cliente JAX-WS para el Servicio Web de Autenticación.
 */
@WebService(name = "ServicioAutenticacion", targetNamespace = "http://ws.monster.edu.ec/")
public interface IServicioAutenticacion {

    @WebMethod(operationName = "iniciarSesion")
    @WebResult(name = "loginResult")
    LoginResponse iniciarSesion(
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "clave") String clave);

    @WebMethod(operationName = "validarToken")
    @WebResult(name = "esValido")
    boolean validarToken(
            @WebParam(name = "token") String token);

    @WebMethod(operationName = "cerrarSesion")
    @WebResult(name = "cerrado")
    boolean cerrarSesion(
            @WebParam(name = "token") String token);
}
