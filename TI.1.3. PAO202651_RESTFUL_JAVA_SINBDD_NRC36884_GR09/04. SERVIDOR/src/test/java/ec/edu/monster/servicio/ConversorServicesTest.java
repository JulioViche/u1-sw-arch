package ec.edu.monster.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de los servicios de conversión y autenticación REST.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
class ConversorServicesTest {

    private ServicioAuth authService;
    private ConversorLongitud longitudService;
    private ConversorMasa masaService;
    private ConversorTemperatura temperaturaService;

    @BeforeEach
    void setUp() {
        authService = new ServicioAuth();
        longitudService = new ConversorLongitud();
        masaService = new ConversorMasa();
        temperaturaService = new ConversorTemperatura();
    }

    @Test
    @DisplayName("Autenticación con credenciales correctas MONSTER / MONSTER9")
    void testLoginExitoso() {
        String token = authService.validarLogin("MONSTER", "MONSTER9");
        assertNotNull(token);
        assertTrue(authService.esTokenValido(token));

        // Insensible a mayúsculas en usuario
        String tokenMinus = authService.validarLogin("monster", "MONSTER9");
        assertNotNull(tokenMinus);
        assertTrue(authService.esTokenValido(tokenMinus));
    }

    @Test
    @DisplayName("Autenticación con credenciales erróneas")
    void testLoginFallido() {
        assertNull(authService.validarLogin("ADMIN", "1234"));
        assertNull(authService.validarLogin("MONSTER", "WRONG_PASS"));
        assertFalse(authService.esTokenValido("token-inexistente"));
    }

    @Test
    @DisplayName("Conversión de longitud entre las 5 unidades del catálogo")
    void testConversionLongitud() {
        // Metros a Centímetros: 1 m = 100 cm
        double cm = longitudService.convertirLongitud(1.0, "metros", "centimetros");
        assertEquals(100.0, cm, 0.001);

        // Pies a Metros: 1 ft = 0.3048 m
        double m = longitudService.convertirLongitud(1.0, "pies", "metros");
        assertEquals(0.3048, m, 0.001);

        // Yardas a Metros: 1 yd = 0.9144 m
        double mYard = longitudService.convertirLongitud(1.0, "yardas", "metros");
        assertEquals(0.9144, mYard, 0.001);

        // Millas a Metros: 1 mi = 1609.34 m
        double mMilla = longitudService.convertirLongitud(1.0, "millas", "metros");
        assertEquals(1609.34, mMilla, 0.01);
    }

    @Test
    @DisplayName("Conversión de masa entre las 5 unidades del catálogo")
    void testConversionMasa() {
        // Kilogramos a Gramos: 1 kg = 1000 g
        double g = masaService.convertirMasa(1.0, "kilogramos", "gramos");
        assertEquals(1000.0, g, 0.001);

        // Libras a Kilogramos: 1 lb = 0.453592 kg
        double kg = masaService.convertirMasa(1.0, "libras", "kilogramos");
        assertEquals(0.453592, kg, 0.0001);

        // Onzas a Kilogramos: 1 oz = 0.0283495 kg
        double kgOz = masaService.convertirMasa(1.0, "onzas", "kilogramos");
        assertEquals(0.0283495, kgOz, 0.0001);

        // Quintales a Libras: 1 quintal = 100 libras
        double lbQuintal = masaService.convertirMasa(1.0, "quintales", "libras");
        assertEquals(100.0, lbQuintal, 0.01);
    }

    @Test
    @DisplayName("Conversión de temperatura entre las 5 unidades del catálogo")
    void testConversionTemperatura() {
        // 0 Celsius a Fahrenheit = 32 F
        double f = temperaturaService.convertirTemperatura(0.0, "celsius", "fahrenheit");
        assertEquals(32.0, f, 0.01);

        // 100 Celsius a Fahrenheit = 212 F
        double f100 = temperaturaService.convertirTemperatura(100.0, "celsius", "fahrenheit");
        assertEquals(212.0, f100, 0.01);

        // 0 Celsius a Kelvin = 273.15 K
        double k = temperaturaService.convertirTemperatura(0.0, "celsius", "kelvin");
        assertEquals(273.15, k, 0.01);

        // 100 Celsius a Newton = 33 N
        double n = temperaturaService.convertirTemperatura(100.0, "celsius", "newton");
        assertEquals(33.0, n, 0.01);

        // 100 Celsius a Réaumur = 80 Re
        double re = temperaturaService.convertirTemperatura(100.0, "celsius", "reaumur");
        assertEquals(80.0, re, 0.01);
    }

    @Test
    @DisplayName("Manejo de error ante unidades no existentes en el catálogo")
    void testUnidadInvalida() {
        assertThrows(IllegalArgumentException.class, () ->
                longitudService.convertirLongitud(10.0, "parsec", "metros"));

        assertThrows(IllegalArgumentException.class, () ->
                masaService.convertirMasa(10.0, "toneladas", "kilogramos"));

        assertThrows(IllegalArgumentException.class, () ->
                temperaturaService.convertirTemperatura(10.0, "rankine", "celsius"));
    }
}
