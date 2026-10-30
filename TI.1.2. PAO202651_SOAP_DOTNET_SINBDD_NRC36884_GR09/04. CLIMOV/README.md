# Cliente Móvil .NET MAUI (SOAP) - GR09

Aplicación móvil desarrollada en **.NET MAUI (Multi-platform App UI) sobre C# (.NET 10)** para la variante **SOAP en .NET SIN BDD** (NRC 36884, Grupo GR09).

## Arquitectura y Estructura

El proyecto sigue una arquitectura en capas adaptada a .NET MAUI:

```
04. CLIMOV/
├── ClienteMovilGR09/                   # Proyecto principal .NET MAUI (C# / XAML)
│   ├── ClienteMovilGR09.csproj         # Definición del proyecto MAUI
│   ├── InicializadorMaui.cs            # Configuración de MauiAppBuilder y servicios
│   ├── Aplicacion.xaml / .cs           # Ciclo de vida de la aplicación MAUI
│   ├── NavegacionAplicacion.xaml / .cs # Shell de navegación móvil
│   ├── Modelos/                        # Modelos de sesión, catálogos y magnitudes (SINBDD)
│   ├── Servicios/                      # ClienteSOAP (HttpClient + sobres SOAP XML)
│   ├── Controladores/                  # ControladorConversor (estado, token, validación)
│   ├── Vistas/                         # Vistas XAML (PaginaPrincipal con diseño Monster)
│   ├── Platforms/                      # Adaptadores específicos (Android, Windows, iOS)
│   └── Resources/                      # Recursos visuales (colores, estilos, fuentes, splash)
└── ClienteMovilGR09.Pruebas/           # Proyecto de pruebas y verificación de lógica (CLI)
    ├── ClienteMovilGR09.Pruebas.csproj
    └── Program.cs
```

## Requisitos de Negocio
- **Autenticación obligatoria:** Inicio de sesión con credenciales quemadas `MONSTER` / `MONSTER9` antes de operar.
- **Formato:** Salida formateada a exactamente **2 decimales** (`%.2f`).
- **Validación de entrada:** Máximo 16 caracteres numéricos (`^-?\d+(\.\d+)?$`).
- **Catálogo:** Exactamente 5 unidades por magnitud (Longitud, Masa, Temperatura).

---

## Cómo Probar y Ejecutar

### Opción 1: Verificación Rápida por Línea de Comandos (CLI)
Para verificar que toda la lógica de red, modelos, controladores y validaciones compilan y funcionan correctamente sin necesidad de emulador:

```powershell
# 1. Asegúrate de tener el servidor SOAP iniciado (en otra terminal)
cd "TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\04. SERVIDOR"
dotnet run

# 2. Ejecutar la suite de pruebas del cliente móvil
cd "TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\ClienteMovilGR09.Pruebas"
dotnet run
```

### Opción 2: Ejecución de la Interfaz Visual en Visual Studio 2022
1. Abrir la solución o el proyecto `ClienteMovilGR09\ClienteMovilGR09.csproj` en **Visual Studio 2022**.
2. Asegurarse de tener instalada la carga de trabajo:
   - **Desarrollo de interfaz de usuario de aplicaciones multiplataforma de .NET (.NET MAUI)**.
3. En la barra superior de dispositivos de depuración:
   - Seleccionar un emulador de Android (ej. `Pixel 5 - API 34`).
   - O bien conectar un dispositivo físico con *Depuración por USB*.
4. Presionar **F5** (Iniciar depuración).

> **Nota sobre Direcciones IP en Emuladores:**
> - En el emulador Android de Visual Studio / Android SDK, el equipo host (anfitrión) se accede mediante `http://10.0.2.2:19081`. La clase `ClienteSOAP.cs` detecta automáticamente el entorno Android y redirige las peticiones al puerto del servidor host.
> - Para dispositivos físicos en la misma red Wi-Fi, configura la variable de entorno `CONVERSOR_SOAP_URL` o define la IP LAN del host (ej. `http://192.168.1.50:19081/...`).

