package ec.edu.monster;

import ec.edu.monster.model.UnidadLongitud;
import ec.edu.monster.model.UnidadMasa;
import ec.edu.monster.model.UnidadTemperatura;
import ec.edu.monster.strategy.ConversorLongitudStrategy;
import ec.edu.monster.strategy.ConversorMasaStrategy;
import ec.edu.monster.strategy.ConversorTemperaturaStrategy;
import ec.edu.monster.util.ConversionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para validar las estrategias de conversión
 * y el cumplimiento estricto del catálogo de 5 unidades por magnitud del SRS.
 */
public class ConversionStrategyTest {

    private final ConversorLongitudStrategy convLongitud = new ConversorLongitudStrategy();
    private final ConversorMasaStrategy convMasa = new ConversorMasaStrategy();
    private final ConversorTemperaturaStrategy convTemp = new ConversorTemperaturaStrategy();

    @Test
    @DisplayName("Catálogo Longitud: Debe contener exactamente 5 unidades")
    void testCatalogoLongitud() {
        assertEquals(5, UnidadLongitud.values().length, "El catálogo de longitud debe tener exactamente 5 unidades");
    }

    @Test
    @DisplayName("Conversión Longitud: 1 metro a centímetros = 100.0")
    void testConversionLongitudMetroACentimetro() {
        double res = convLongitud.convertir(1.0, UnidadLongitud.METRO, UnidadLongitud.CENTIMETRO);
        assertEquals(100.0, res, 0.0001);
    }

    @Test
    @DisplayName("Conversión Longitud: 1 pie a metros = 0.3048")
    void testConversionLongitudPieAMetro() {
        double res = convLongitud.convertir(1.0, UnidadLongitud.PIE, UnidadLongitud.METRO);
        assertEquals(0.3048, res, 0.0001);
    }

    @Test
    @DisplayName("Catálogo Masa: Debe contener exactamente 5 unidades")
    void testCatalogoMasa() {
        assertEquals(5, UnidadMasa.values().length, "El catálogo de masa debe tener exactamente 5 unidades");
    }

    @Test
    @DisplayName("Conversión Masa: 1 kilogramo a gramos = 1000.0")
    void testConversionMasaKgAGramo() {
        double res = convMasa.convertir(1.0, UnidadMasa.KILOGRAMO, UnidadMasa.GRAMO);
        assertEquals(1000.0, res, 0.0001);
    }

    @Test
    @DisplayName("Conversión Masa: 1 quintal a libras = 100.0")
    void testConversionMasaQuintalALibra() {
        double res = convMasa.convertir(1.0, UnidadMasa.QUINTAL, UnidadMasa.LIBRA);
        assertEquals(100.0, res, 0.01);
    }

    @Test
    @DisplayName("Catálogo Temperatura: Debe contener exactamente 5 escalas")
    void testCatalogoTemperatura() {
        assertEquals(5, UnidadTemperatura.values().length, "El catálogo de temperatura debe tener exactamente 5 escalas");
    }

    @Test
    @DisplayName("Conversión Temperatura: 100 °C a °F = 212.0")
    void testConversionTemperaturaCelsiusAFahrenheit() {
        double res = convTemp.convertir(100.0, UnidadTemperatura.CELSIUS, UnidadTemperatura.FAHRENHEIT);
        assertEquals(212.0, res, 0.0001);
    }

    @Test
    @DisplayName("Conversión Temperatura: 0 °C a Kelvin = 273.15")
    void testConversionTemperaturaCelsiusAKelvin() {
        double res = convTemp.convertir(0.0, UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
        assertEquals(273.15, res, 0.0001);
    }

    @Test
    @DisplayName("Fachada ConversionUtil: Conversión de cadenas y tolerancia a mayúsculas/tildes")
    void testConversionUtilFachada() {
        double longitud = ConversionUtil.convertirLongitud(2.5, "Metros", "cm");
        assertEquals(250.0, longitud, 0.001);

        double masa = ConversionUtil.convertirMasa(500.0, "Gramos", "Kilogramos");
        assertEquals(0.5, masa, 0.0001);

        double temp = ConversionUtil.convertirTemperatura(0.0, "Celsius", "Fahrenheit");
        assertEquals(32.0, temp, 0.0001);
    }

    @Test
    @DisplayName("Rechazo de unidades no soportadas")
    void testUnidadInvalidaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            ConversionUtil.convertirLongitud(10.0, "pulgada", "metro");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ConversionUtil.convertirMasa(10.0, "tonelada", "kilogramo");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ConversionUtil.convertirTemperatura(10.0, "rankine", "celsius");
        });
    }
}
