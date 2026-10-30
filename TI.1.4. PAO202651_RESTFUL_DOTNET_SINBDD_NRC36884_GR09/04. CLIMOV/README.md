# Cliente Móvil .NET MAUI (RESTful) - GR09

Aplicación móvil desarrollada en **.NET MAUI (Multi-platform App UI) sobre C# (.NET 10)** para la variante **RESTful en .NET SIN BDD** (NRC 36884, Grupo GR09).

## Arquitectura y Estructura

El proyecto implementa una arquitectura desacoplada en capas para .NET MAUI:

```
04. CLIMOV/
├── APK/                                # Ejecutables APK compilados y optimizados
│   └── app-restful-maui-signed.apk
├── ClienteMovilGR09/                   # Proyecto principal .NET MAUI (C# / XAML)
│   ├── ClienteMovilGR09.csproj         # Definición del proyecto MAUI
│   ├── InicializadorMaui.cs            # Configuración de MauiAppBuilder y dependencias
│   ├── Aplicacion.xaml / .cs           # Ciclo de vida de la aplicación MAUI
│   ├── NavegacionAplicacion.xaml / .cs # Shell de navegación móvil
│   ├── Modelos/                        # Modelos de solicitud, respuesta y token (JSON)
│   ├── Servicios/                      # ClienteRest (detección automática de endpoints)
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

El APK optimizado y firmado para entrega se genera en `04. CLIMOV/APK/app-restful-maui-signed.apk`.

### Opción 2: Verificación Rápida por Línea de Comandos (CLI)
Para verificar que toda la lógica de red, modelos, controladores y validaciones compilan y funcionan correctamente sin necesidad de emulador:

```powershell
# 1. Asegúrate de tener el servidor REST Web API iniciado (en otra terminal)
cd "..\04. SERVIDOR"
dotnet run

# 2. Ejecutar la suite de pruebas del cliente móvil
cd "..\04. CLIMOV\ClienteMovilGR09.Pruebas"
dotnet run
```

### Opción 3: Ejecución en Emulador o Dispositivo Físico (Hotspot)
1. Conecta el teléfono al hotspot que comparte el equipo Windows o a la misma red Wi-Fi institucional (`10.40.64.228`).
2. Inicia el servidor REST .NET (`04. SERVIDOR`). Escucha en todas las interfaces (`0.0.0.0`) por el puerto TCP `5000`.
3. El cliente móvil detecta automáticamente `http://192.168.137.1:5000/api`, `http://10.40.64.228:5000/api` o `http://10.0.2.2:5000/api` (en emulador).
4. Asegúrate de permitir conexiones entrantes al puerto TCP `5000` en el Firewall de Windows para el perfil asignado.
5. Inicia sesión con `MONSTER` / `MONSTER9`.
