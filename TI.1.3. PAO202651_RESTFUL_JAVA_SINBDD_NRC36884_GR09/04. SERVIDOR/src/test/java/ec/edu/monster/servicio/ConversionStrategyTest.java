package ec.edu.monster.servicio;

import ec.edu.monster.modelo.IConversor;
import ec.edu.monster.modelo.UnidadLongitud;
import ec.edu.monster.modelo.UnidadMasa;
import ec.edu.monster.modelo.UnidadTemperatura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para verificar el cumplimiento del patrón GoF Strategy
 * y el catálogo estricto de exactamente 5 unidades por magnitud (SRS).
 */
public class ConversionStrategyTest {

    @Test
    @DisplayName("Catálogo de longitud debe tener exactamente 5 unidades")
    void testCatalogoExactoLongitud() {
        assertEquals(5, UnidadLongitud.values().length, "Debe haber exactamente 5 unidades de longitud");
    }

    @Test
    @DisplayName("Catálogo de masa debe tener exactamente 5 unidades")
    void testCatalogoExactoMasa() {
        assertEquals(5, UnidadMasa.values().length, "Debe haber exactamente 5 unidades de masa");
    }

    @Test
    @DisplayName("Catálogo de temperatura debe tener exactamente 5 escalas")
    void testCatalogoExactoTemperatura() {
        assertEquals(5, UnidadTemperatura.values().length, "Debe haber exactamente 5 escalas de temperatura");
    }

    @Test
    @DisplayName("Estrategia de Longitud calcula correctamente")
    void testEstrategiaLongitud() {
        IConversor<UnidadLongitud> conversor = new ConversorLongitud();
        double valor = conversor.convertir(10.0, UnidadLongitud.METRO, UnidadLongitud.PIE);
        assertEquals(32.808, valor, 0.01);
    }

    @Test
    @DisplayName("Estrategia de Masa calcula correctamente")
    void testEstrategiaMasa() {
        IConversor<UnidadMasa> conversor = new ConversorMasa();
        double valor = conversor.convertir(1.0, UnidadMasa.QUINTAL, UnidadMasa.LIBRA);
        assertEquals(100.0, valor, 0.01);
    }

    @Test
    @DisplayName("Estrategia de Temperatura calcula correctamente")
    void testEstrategiaTemperatura() {
        IConversor<UnidadTemperatura> conversor = new ConversorTemperatura();
        double valor = conversor.convertir(100.0, UnidadTemperatura.CELSIUS, UnidadTemperatura.FAHRENHEIT);
        assertEquals(212.0, valor, 0.01);
    }
}
