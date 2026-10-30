---
tipo: guia
taller: TI.1.2
protocolo: SOAP
plataforma: .NET
tags: [android, hotspot, red-local, soap, ti-1-2]
---

# Conectar el cliente móvil al hotspot de Windows

Esta guía conecta el cliente móvil .NET MAUI de TI.1.2 con el servidor SOAP .NET que se ejecuta en el mismo equipo Windows que comparte su hotspot.

## Requisitos

- El SDK de .NET 10 requerido por el servidor está instalado.
- El workload maui-android está disponible para compilar el cliente MAUI.
- El teléfono Android está conectado al hotspot móvil creado por el equipo Windows.
- El servidor está escuchando en el puerto TCP `8733`. `Program.cs` lo enlaza a todas las interfaces con `ListenAnyIP(8733)`.

## Iniciar el servidor

Desde la raíz del repositorio, ejecuta:

```powershell
dotnet run --project "TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/04. SERVIDOR/WSServidorSOAP_DotNet_GR09.csproj"
```

Deja esa terminal abierta mientras uses la app. El endpoint SOAP base es:

```text
http://<IPv4-del-equipo>:8733/ServidorSOAP
```

La app agrega las rutas de autenticación y conversión a esa dirección.

## Conectar el teléfono

1. En Windows, activa **Configuración → Red e Internet → Zona con cobertura inalámbrica móvil** y confirma que el teléfono se conecte a esa red.
2. El cliente usa `http://192.168.137.1:8733/ServidorSOAP` automáticamente en el teléfono. Es la dirección habitual del adaptador del hotspot de Windows; si el equipo fue configurado con otra IPv4, hay que actualizar la constante del cliente y recompilar.
3. Ingresa con `MONSTER` / `MONSTER9`.

En el emulador Android, el cliente usa `http://10.0.2.2:8733/ServidorSOAP` para llegar al equipo anfitrión.

## Compilar el APK MAUI

Desde 04. CLIMOV, ejecuta:

    dotnet build ".\ClienteMovilMaui\ClienteMovilSoapDotNetGR09.csproj" -f net10.0-android -c Debug -p:AndroidPackageFormats=apk

El cliente usa el icono propio del Conversor Monster. No uses el proyecto Android Java heredado de 04. CLIMOV/app para generar el APK de esta variante.

## Firewall de Windows

Si el teléfono está conectado al hotspot pero no puede iniciar sesión, permite conexiones TCP entrantes al puerto `8733` en Windows Defender Firewall para el perfil de red activo. La regla debe permitir el tráfico en el perfil que Windows asignó al adaptador del hotspot.

Comprueba también que el servidor siga ejecutándose y que la IPv4 del hotspot sea `192.168.137.1`; el cliente ya no muestra un campo para cambiar la URL.
