# Cliente móvil REST .NET MAUI

El cliente móvil de esta variante está implementado con .NET MAUI para Android. Los fuentes Android Java/Gradle anteriores se retiraron.

## Conexión al hotspot de Windows

1. Conecta el teléfono al hotspot que comparte el equipo Windows.
2. Inicia el servidor REST .NET. Escucha en todas las interfaces (`0.0.0.0`) por el puerto TCP `5000`.
3. El cliente usa automáticamente `http://192.168.137.1:5000/`, la dirección que Windows asigna normalmente al adaptador del hotspot. La URL no se solicita en la pantalla.
4. Permite conexiones entrantes al puerto TCP `5000` en el Firewall de Windows para el perfil asignado al hotspot.
5. Inicia sesión con `MONSTER` / `MONSTER9`.

En el emulador Android, el cliente usa `http://10.0.2.2:5000/`. El manifiesto permite HTTP en la red local. El teléfono y el servidor deben estar conectados a la misma red.

## Compilar APK

Desde la raíz del repositorio, con el workload `maui-android` y Android SDK instalados:

```powershell
dotnet build "TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09/04. CLIMOV/ClienteMovil/ClienteMovilGR09/ClienteMovilGR09.csproj" -f net10.0-android -c Debug -p:AndroidPackageFormats=apk
```

El APK de depuración se genera bajo `ClienteMovilGR09/bin/Debug/net10.0-android/`.
