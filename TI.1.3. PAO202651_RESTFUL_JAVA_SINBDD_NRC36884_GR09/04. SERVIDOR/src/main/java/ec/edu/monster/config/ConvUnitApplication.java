package ec.edu.monster.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Configuración de la aplicación JAX-RS / Jersey.
 * Mapeada en la raíz del contexto web.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@ApplicationPath("/")
public class ConvUnitApplication extends ResourceConfig {

    public ConvUnitApplication() {
        // Escaneo de controladores y proveedores
        packages("ec.edu.monster.controlador", "ec.edu.monster.config");
        register(CORSResponseFilter.class);
    }
}
