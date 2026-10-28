package ec.edu.monster;

import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.ws.AuthWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para validar el servicio de autenticación con credenciales fijas
 * MONSTER / MONSTER9 y tokens de sesión en memoria RAM (SINBDD).
 */
public class AuthWebServiceTest {

    private AuthWebService authWebService;

    @BeforeEach
    void setUp() {
        authWebService = new AuthWebService();
    }

    @Test
    @DisplayName("Login con credenciales correctas MONSTER / MONSTER9 debe ser exitoso")
    void testLoginExitoso() {
        LoginResponse response = authWebService.iniciarSesion("MONSTER", "MONSTER9");
        assertTrue(response.isExito(), "El login debe ser exitoso");
        assertNotNull(response.getToken(), "El token generado no debe ser nulo");
        assertFalse(response.getToken().trim().isEmpty(), "El token no debe estar vacío");
        assertTrue(authWebService.validarToken(response.getToken()), "El token generado debe ser válido");
    }

    @Test
    @DisplayName("Login con credenciales erróneas debe fallar")
    void testLoginFallido() {
        LoginResponse responseClaveErronea = authWebService.iniciarSesion("MONSTER", "CLAVE_INCORRECTA");
        assertFalse(responseClaveErronea.isExito());
        assertNull(responseClaveErronea.getToken());

        LoginResponse responseUsuarioErroneo = authWebService.iniciarSesion("ADMIN", "MONSTER9");
        assertFalse(responseUsuarioErroneo.isExito());
        assertNull(responseUsuarioErroneo.getToken());
    }

    @Test
    @DisplayName("Cerrar sesión debe revocar el token")
    void testCerrarSesion() {
        LoginResponse login = authWebService.iniciarSesion("MONSTER", "MONSTER9");
        String token = login.getToken();
        assertTrue(authWebService.validarToken(token));

        boolean cerrado = authWebService.cerrarSesion(token);
        assertTrue(cerrado, "El cierre de sesión debe confirmar la eliminación");
        assertFalse(authWebService.validarToken(token), "El token ya no debe ser válido después de cerrar sesión");
    }
}
