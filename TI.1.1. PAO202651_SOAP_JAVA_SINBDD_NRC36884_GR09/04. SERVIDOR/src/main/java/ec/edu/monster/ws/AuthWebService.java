package ec.edu.monster.ws;

import ec.edu.monster.model.LoginResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio Web SOAP de Autenticación con credenciales fijas y tokens en memoria.
 * Modalidad: SINBDD (Sin Base de Datos).
 * Credenciales: MONSTER / MONSTER9
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Grupo: GR09
 */
@WebService(
    name = "ServicioAutenticacion",
    serviceName = "ServicioAutenticacion",
    targetNamespace = "http://ws.monster.edu.ec/"
)
public class AuthWebService {

    // Credenciales quemadas obligatorias según especificación del curso
    private static final String USUARIO_REQUERIDO = "MONSTER";
    private static final String CLAVE_REQUERIDA = "MONSTER9";

    // Almacenamiento volátil en memoria RAM (SINBDD) de tokens de sesión activos
    private static final Set<String> TOKENS_ACTIVOS = ConcurrentHashMap.newKeySet();

    /**
     * Valida credenciales contra los valores fijos del sistema y genera un token UUID si es válido.
     */
    @WebMethod(operationName = "iniciarSesion")
    @WebResult(name = "loginResult")
    public LoginResponse iniciarSesion(
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "clave") String clave) {

        if (USUARIO_REQUERIDO.equals(usuario) && CLAVE_REQUERIDA.equals(clave)) {
            String nuevoToken = UUID.randomUUID().toString();
            TOKENS_ACTIVOS.add(nuevoToken);
            return new LoginResponse(true, nuevoToken, "Inicio de sesión exitoso. Bienvenido al Conversor de Unidades.");
        } else {
            return new LoginResponse(false, null, "Error de autenticación: Credenciales inválidas.");
        }
    }

    /**
     * Comprueba si un token de sesión se encuentra activo en la memoria del servidor.
     */
    @WebMethod(operationName = "validarToken")
    @WebResult(name = "esValido")
    public boolean validarToken(@WebParam(name = "token") String token) {
        return isTokenValido(token);
    }

    /**
     * Cierra la sesión activa revocando el token de la memoria RAM.
     */
    @WebMethod(operationName = "cerrarSesion")
    @WebResult(name = "cerrado")
    public boolean cerrarSesion(@WebParam(name = "token") String token) {
        if (token != null) {
            return TOKENS_ACTIVOS.remove(token);
        }
        return false;
    }

    /**
     * Método utilitario estático para validar tokens internamente desde otros servicios.
     */
    public static boolean isTokenValido(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        return TOKENS_ACTIVOS.contains(token.trim());
    }
}
