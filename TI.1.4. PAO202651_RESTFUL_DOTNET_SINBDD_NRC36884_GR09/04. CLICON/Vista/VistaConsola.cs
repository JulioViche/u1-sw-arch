using System.Globalization;
using ClienteConsola_DotNet_REST_GR09.Modelos;

namespace ClienteConsola_DotNet_REST_GR09.Vista;

public sealed class VistaConsola
{
    public static readonly string[] UnidadesLongitud = ["metro", "centimetro", "pie", "yarda", "milla"];
    public static readonly string[] UnidadesMasa = ["gramo", "kilogramo", "libra", "onza", "quintal"];
    public static readonly string[] UnidadesTemperatura = ["celsius", "fahrenheit", "kelvin", "newton", "reaumur"];

    public void MostrarBanner()
    {
        Console.WriteLine("==================================================================");
        Console.WriteLine("  UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE - EXTENSIÓN LATACUNGA");
        Console.WriteLine("            DEPARTAMENTO DE ENERGÍA Y MECÁNICA");
        Console.WriteLine("                 ARQUITECTURA DE SOFTWARE");
        Console.WriteLine("                TAREA TI.1.4 (PAO 2026-51)");
        Console.WriteLine("------------------------------------------------------------------");
        Console.WriteLine("  CLIENTE DE CONSOLA: CONVERSOR DE UNIDADES (RESTFUL .NET - SINBDD)");
        Console.WriteLine("   NRC: 36884 | Grupo: GR09 | Autor: Julio Enrique Viche Castillo ");
        Console.WriteLine("   Patrón Arquitectónico: Modelo - Vista - Controlador (MVC)");
        Console.WriteLine("==================================================================\n");
    }

    public (string usuario, string clave) PedirCredenciales()
    {
        Console.WriteLine("------------------------------------------------------------------");
        Console.WriteLine("              INICIO DE SESIÓN OBLIGATORIO (AUTENTICACIÓN)        ");
        Console.WriteLine("------------------------------------------------------------------");
        Console.WriteLine(" [Nota]: Credenciales del sistema:");
        Console.WriteLine("         Usuario: MONSTER  |  Contraseña: MONSTER9");
        Console.WriteLine("------------------------------------------------------------------");

        Console.Write("Ingrese Usuario: ");
        string usuario = (Console.ReadLine() ?? string.Empty).Trim();

        Console.Write("Ingrese Contraseña: ");
        string clave = (Console.ReadLine() ?? string.Empty).Trim();

        return (usuario, clave);
    }

    public bool PreguntarReintento()
    {
        Console.Write("\n¿Desea reintentar el inicio de sesión? (s/n): ");
        string resp = (Console.ReadLine() ?? string.Empty).Trim().ToLowerInvariant();
        return resp is "s" or "si" or "y";
    }

    public string MostrarMenuPrincipal(ModeloSesion sesion)
    {
        Console.WriteLine("\n==========================================================");
        Console.WriteLine("           MENÚ PRINCIPAL DE CONVERSIONES REST (.NET)     ");
        Console.WriteLine("==========================================================");
        Console.WriteLine($" Sesión activa: {sesion.Usuario} | Token: {sesion.TokenAbreviado}");
        Console.WriteLine("----------------------------------------------------------");
        Console.WriteLine("  1. Conversión de Longitud (metro, centimetro, pie, yarda, milla)");
        Console.WriteLine("  2. Conversión de Masa (gramo, kilogramo, libra, onza, quintal)");
        Console.WriteLine("  3. Conversión de Temperatura (celsius, fahrenheit, kelvin, newton, reaumur)");
        Console.WriteLine("  4. Verificar estado del token de sesión");
        Console.WriteLine("  5. Cerrar sesión y Salir");
        Console.WriteLine("==========================================================");
        Console.Write("Seleccione una opción (1-5): ");
        return (Console.ReadLine() ?? string.Empty).Trim();
    }

    public (string origen, string destino, double valor)? SolicitarDatosConversion(string magnitud, string[] unidades)
    {
        Console.WriteLine($"\n--- [CONVERSIÓN DE {magnitud.ToUpperInvariant()}] ---");
        string? origen = SeleccionarUnidad("origen", unidades);
        if (origen is null) return null;

        string? destino = SeleccionarUnidad("destino", unidades);
        if (destino is null) return null;

        double valor = PedirValorNumerico();
        return (origen, destino, valor);
    }

    private string? SeleccionarUnidad(string tipo, string[] unidades)
    {
        Console.WriteLine($"Seleccione unidad de {tipo}:");
        for (int i = 0; i < unidades.Length; i++)
        {
            Console.WriteLine($"  {i + 1}. {Capitalizar(unidades[i])}");
        }
        Console.Write($"Elija una opción (1-{unidades.Length}): ");
        string input = (Console.ReadLine() ?? string.Empty).Trim();

        if (int.TryParse(input, out int idx) && idx >= 1 && idx <= unidades.Length)
        {
            return unidades[idx - 1];
        }

        MostrarError("Opción de unidad fuera de rango o entrada inválida.");
        return null;
    }

    public double PedirValorNumerico()
    {
        while (true)
        {
            Console.Write("Ingrese el valor numérico a convertir (números, '.' y '-' al inicio): ");
            string input = (Console.ReadLine() ?? string.Empty).Trim();

            if (input.Length > 16)
            {
                MostrarError("Longitud excedida. El valor no puede superar los 16 caracteres.");
                continue;
            }

            if (!System.Text.RegularExpressions.Regex.IsMatch(input, @"^-?\d+(\.\d+)?$"))
            {
                MostrarError("Entrada no válida. Solo se permiten números, el punto decimal '.' y '-' al inicio (ejemplo: 25.5, -10 o 100).");
                continue;
            }

            if (double.TryParse(input, NumberStyles.Float, CultureInfo.InvariantCulture, out double valor))
            {
                return valor;
            }

            MostrarError("Ingrese un número válido con punto decimal (ejemplo: 25.5, -10 o 100).");
        }
    }

    public void MostrarResultadoConversion(string origen, string destino, double valorOriginal, ConversionRespuesta resp)
    {
        if (resp.Exito)
        {
            Console.WriteLine("\n==========================================================");
            Console.WriteLine("                   RESULTADO DE LA CONVERSIÓN             ");
            Console.WriteLine("==========================================================");
            Console.WriteLine(string.Create(CultureInfo.InvariantCulture, $"   {valorOriginal:F2} {Capitalizar(origen)}  --->  {resp.ValorConvertido:F2} {Capitalizar(destino)}"));
            Console.WriteLine("==========================================================");
        }
        else
        {
            MostrarError($"En la conversión: {resp.MensajeError ?? "Sin respuesta del servidor"}");
        }
    }

    public void MostrarExito(string mensaje) => Console.WriteLine($"\n[OK] {mensaje}");

    public void MostrarError(string mensaje) => Console.WriteLine($"\n[ERROR] {mensaje}");

    public void MostrarDespedida()
    {
        Console.WriteLine("\n==========================================================");
        Console.WriteLine(" Gracias por utilizar el Conversor de Unidades RESTful .NET (GR09)");
        Console.WriteLine("==========================================================\n");
    }

    private static string Capitalizar(string str) =>
        string.IsNullOrEmpty(str) ? string.Empty : char.ToUpperInvariant(str[0]) + str[1..];
}
