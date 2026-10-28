package ec.edu.monster.seguridad;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro de seguridad CORS (Cross-Origin Resource Sharing) para permitir el consumo
 * seguro de los servicios SOAP desde clientes Web modernos alojados en distintos dominios/puertos.
 * 
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Asignatura: Arquitectura de Software | NRC: 36884 | Grupo: GR09
 * Autor: Julio Enrique Viche Castillo
 */
@WebFilter(filterName = "FiltroCors", urlPatterns = {"/*"})
public class FiltroCors implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Configuración de encabezados CORS estándar
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, HEAD");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, SOAPAction");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        // Responder directamente a las solicitudes preflight (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Limpieza de recursos
    }
}
