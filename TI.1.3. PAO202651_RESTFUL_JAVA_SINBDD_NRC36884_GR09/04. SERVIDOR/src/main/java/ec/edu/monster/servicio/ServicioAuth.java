package ec.edu.monster.servicio;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio de autenticación en memoria (SINBDD).
 * Credenciales fijas: MONSTER / MONSTER9
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class ServicioAuth {

    private static final String USUARIO_VALIDO = "MONSTER";
    private static final String CLAVE_VALIDA = "MONSTER9";

    // Almacén de tokens activos en memoria (Token -> Timestamp creación)
    public static final Map<String, Long> TOKENS_VALIDOS = new ConcurrentHashMap<>();

    /**
     * Valida las credenciales y genera un token si son correctas.
     *
     * @param usuario Nombre de usuario enviado en request.
     * @param clave   Contraseña enviada en request.
     * @return Token de sesión generado o null si credenciales inválidas.
     */
    public String validarLogin(String usuario, String clave) {
        if (usuario == null || clave == null) {
            return null;
        }

        if (USUARIO_VALIDO.equalsIgnoreCase(usuario.trim()) && CLAVE_VALIDA.equals(clave.trim())) {
            String token = UUID.randomUUID().toString();
            TOKENS_VALIDOS.put(token, System.currentTimeMillis());
            return token;
        }

        return null;
    }

    /**
     * Comprueba si un token existe en memoria.
     *
     * @param token Token a verificar.
     * @return true si el token es válido.
     */
    public boolean esTokenValido(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        return TOKENS_VALIDOS.containsKey(token.trim());
    }

    /**
     * Invalida un token (logout).
     *
     * @param token Token a revocar.
     */
    public void revocarToken(String token) {
        if (token != null) {
            TOKENS_VALIDOS.remove(token.trim());
        }
    }
}
