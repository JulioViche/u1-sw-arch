# Cliente móvil SOAP .NET

El cliente oficial de TI.1.2 está implementado en .NET MAUI dentro de la carpeta ClienteMovilMaui. Los fuentes Android Java/Gradle anteriores fueron retirados de este cliente .NET.

## Requisitos

- SDK de .NET 10.
- Workload maui-android.
- Android SDK y un dispositivo o emulador Android.

## Compilar

Desde esta carpeta, ejecuta:

    dotnet build ".\ClienteMovilMaui\ClienteMovilSoapDotNetGR09.csproj" -f net10.0-android -c Debug -p:AndroidPackageFormats=apk

El APK de depuración se genera dentro de ClienteMovilMaui\bin\Debug\net10.0-android. El artefacto de entrega se copia como CONVUNI_SOAP_DOTNET_CLIMOV_GR09.apk a esta carpeta.

El cliente no pide la dirección del servidor en pantalla: usa `10.0.2.2` en el emulador y `192.168.137.1` en un teléfono conectado al hotspot de Windows. Consulta `../../vault/06-projects/ti-1-2-soap-dotnet/mobile-hotspot.md` para iniciar el servidor y permitir el puerto TCP `8733` en el Firewall.
