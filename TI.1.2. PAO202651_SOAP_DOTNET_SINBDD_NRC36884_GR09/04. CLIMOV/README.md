# Cliente Móvil .NET MAUI (SOAP) - GR09

Aplicación móvil desarrollada en **.NET MAUI (Multi-platform App UI) sobre C# (.NET 10)** para la variante **SOAP en .NET SIN BDD** (NRC 36884, Grupo GR09).

## Arquitectura y Estructura

El proyecto sigue una arquitectura en capas adaptada a .NET MAUI:

```
04. CLIMOV/
├── APK/                                # Ejecutables APK compilados y optimizados
│   └── app-soap-maui-signed.apk
├── ClienteMovilGR09/                   # Proyecto principal .NET MAUI (C# / XAML)
│   ├── ClienteMovilGR09.csproj         # Definición del proyecto MAUI
│   ├── InicializadorMaui.cs            # Configuración de MauiAppBuilder y servicios
│   ├── Aplicacion.xaml / .cs           # Ciclo de vida de la aplicación MAUI
│   ├── NavegacionAplicacion.xaml / .cs # Shell de navegación móvil
│   ├── Modelos/                        # Modelos de sesión, catálogos y magnitudes (SINBDD)
│   ├── Servicios/                      # ClienteSOAP (detección automática de endpoints)
│   ├── Controladores/                  # ControladorConversor (estado, token, validación)
│   ├── Vistas/                         # Vistas XAML (PaginaPrincipal con diseño institucional)
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

### Opción 1: Compilación de APK Android (.NET MAUI)
Desde esta carpeta, ejecuta:

```powershell
dotnet publish ".\ClienteMovilGR09\ClienteMovilGR09.csproj" -f net10.0-android -c Release -p:AndroidPackageFormat=apk
```

El APK optimizado y firmado para entrega se genera en `04. CLIMOV/APK/app-soap-maui-signed.apk`.

### Opción 2: Verificación Rápida por Línea de Comandos (CLI)
Para verificar que toda la lógica de red, modelos, controladores y validaciones compilan y funcionan correctamente sin necesidad de emulador:

```powershell
# 1. Asegúrate de tener el servidor SOAP iniciado (en otra terminal)
cd "..\04. SERVIDOR"
dotnet run

# 2. Ejecutar la suite de pruebas del cliente móvil
cd "..\04. CLIMOV\ClienteMovilGR09.Pruebas"
dotnet run
```

### Opción 3: Ejecución en Emulador o Dispositivo Físico
1. Abrir la solución o el proyecto `ClienteMovilGR09\ClienteMovilGR09.csproj` en **Visual Studio 2022**.
2. Asegurarse de tener instalada la carga de trabajo **.NET MAUI** y el workload `maui-android`.
3. Seleccionar el emulador Android (ej. `Pixel 5 - API 34`) o conectar dispositivo físico por USB.
4. Presionar **F5** (Iniciar depuración).

> **Nota sobre Direcciones IP y Red:**
> - En emulador Android: se redirige automáticamente al host en `10.0.2.2:8733`.
> - En dispositivo físico (Hotspot o Wi-Fi institucional): se conecta automáticamente a `192.168.137.1:8733` o `10.40.64.228:8733`. Consulta `../../vault/06-projects/ti-1-2-soap-dotnet/mobile-hotspot.md` para habilitar el puerto TCP `8733` en el Firewall de Windows.
