package ec.edu.monster.ws;

import ec.edu.monster.model.ConversionResponse;
import ec.edu.monster.util.ConversionUtil;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

/**
 * Servicio Web SOAP de Conversión de Unidades protegida por token.
 * Soporta magnitudes físicas: Longitud, Masa y Temperatura.
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Grupo: GR09
 */
@WebService(
    name = "ServicioConversion",
    serviceName = "ServicioConversion",
    targetNamespace = "http://ws.monster.edu.ec/"
)
public class ConversionWebService {

    private static final String ERROR_TOKEN_INVALIDO = "Acceso no autorizado: Token de sesión inválido o expirado. Debe iniciar sesión primero.";

    /**
     * Convierte magnitudes de longitud previa validación del token de sesión.
     */
    @WebMethod(operationName = "convertirLongitud")
    @WebResult(name = "conversionResult")
    public ConversionResponse convertirLongitud(
            @WebParam(name = "token") String token,
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadOrigen") String unidadOrigen,
            @WebParam(name = "unidadDestino") String unidadDestino) {

        if (!AuthWebService.isTokenValido(token)) {
            return new ConversionResponse(false, 0.0, ERROR_TOKEN_INVALIDO);
        }

        try {
            double resultado = ConversionUtil.convertirLongitud(valor, unidadOrigen, unidadDestino);
            return new ConversionResponse(true, resultado, null);
        } catch (IllegalArgumentException e) {
            return new ConversionResponse(false, 0.0, "Error en unidades de longitud: " + e.getMessage());
        } catch (Exception e) {
            return new ConversionResponse(false, 0.0, "Error interno al calcular longitud: " + e.getMessage());
        }
    }

    /**
     * Convierte magnitudes de masa previa validación del token de sesión.
     */
    @WebMethod(operationName = "convertirMasa")
    @WebResult(name = "conversionResult")
    public ConversionResponse convertirMasa(
            @WebParam(name = "token") String token,
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadOrigen") String unidadOrigen,
            @WebParam(name = "unidadDestino") String unidadDestino) {

        if (!AuthWebService.isTokenValido(token)) {
            return new ConversionResponse(false, 0.0, ERROR_TOKEN_INVALIDO);
        }

        try {
            double resultado = ConversionUtil.convertirMasa(valor, unidadOrigen, unidadDestino);
            return new ConversionResponse(true, resultado, null);
        } catch (IllegalArgumentException e) {
            return new ConversionResponse(false, 0.0, "Error en unidades de masa: " + e.getMessage());
        } catch (Exception e) {
            return new ConversionResponse(false, 0.0, "Error interno al calcular masa: " + e.getMessage());
        }
    }

    /**
     * Convierte escalas de temperatura previa validación del token de sesión.
     */
    @WebMethod(operationName = "convertirTemperatura")
    @WebResult(name = "conversionResult")
    public ConversionResponse convertirTemperatura(
            @WebParam(name = "token") String token,
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadOrigen") String unidadOrigen,
            @WebParam(name = "unidadDestino") String unidadDestino) {

        if (!AuthWebService.isTokenValido(token)) {
            return new ConversionResponse(false, 0.0, ERROR_TOKEN_INVALIDO);
        }

        try {
            double resultado = ConversionUtil.convertirTemperatura(valor, unidadOrigen, unidadDestino);
            return new ConversionResponse(true, resultado, null);
        } catch (IllegalArgumentException e) {
            return new ConversionResponse(false, 0.0, "Error en escalas de temperatura: " + e.getMessage());
        } catch (Exception e) {
            return new ConversionResponse(false, 0.0, "Error interno al calcular temperatura: " + e.getMessage());
        }
    }
}
