package ec.edu.monster.client.ws;

import ec.edu.monster.client.model.ConversionResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

/**
 * Interfaz de cliente JAX-WS para el Servicio Web de Conversión de Unidades.
 */
@WebService(name = "ServicioConversion", targetNamespace = "http://ws.monster.edu.ec/")
public interface IServicioConversion {

    @WebMethod(operationName = "convertirLongitud")
    @WebResult(name = "conversionResult")
    ConversionResponse convertirLongitud(
            @WebParam(name = "token") String token,
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadOrigen") String unidadOrigen,
            @WebParam(name = "unidadDestino") String unidadDestino);

    @WebMethod(operationName = "convertirMasa")
    @WebResult(name = "conversionResult")
    ConversionResponse convertirMasa(
            @WebParam(name = "token") String token,
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadOrigen") String unidadOrigen,
            @WebParam(name = "unidadDestino") String unidadDestino);

    @WebMethod(operationName = "convertirTemperatura")
    @WebResult(name = "conversionResult")
    ConversionResponse convertirTemperatura(
            @WebParam(name = "token") String token,
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadOrigen") String unidadOrigen,
            @WebParam(name = "unidadDestino") String unidadDestino);
}
