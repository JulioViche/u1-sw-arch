package ec.edu.monster.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * Filtro JAX-RS para habilitar CORS (Cross-Origin Resource Sharing)
 * permitiendo consumo desde clientes Web (CLIWEB) y móviles en cualquier origen.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@Provider
public class CORSResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Accept");
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", "3600");
    }
}
