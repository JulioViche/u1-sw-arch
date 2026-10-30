# Guía de Ubicación e Instalación de APKs Móviles (Android)

Este documento detalla la **ubicación exacta**, rutas del sistema, comandos de compilación y pasos de instalación de los paquetes ejecutables Android (**APK**) para las **4 variantes** de la tarea académica **TI.1 (Arquitectura de Software - GR09 - NRC 36884)**.

---

## 1. Tabla de Archivos APK Compilados (Las 4 Variantes)

| Variante | Protocolo | Plataforma | Archivo APK | Tamaño | Ruta Relativa |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TI.1.1** | **SOAP** | Java | `app-soap-debug.apk` | ~6.00 MB | `TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/04. CLIMOV/APK/app-soap-debug.apk` |
| **TI.1.2** | **SOAP** | **.NET MAUI** | `app-soap-maui-signed.apk` | ~263 MB | `TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/04. CLIMOV/APK/app-soap-maui-signed.apk` |
| **TI.1.3** | **RESTful** | Java | `app-restful-debug.apk` | ~6.67 MB | `TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09/04. CLIMOV/APK/app-restful-debug.apk` |
| **TI.1.4** | **RESTful** | **.NET MAUI** | `app-restful-maui-signed.apk` | ~263 MB | `TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09/04. CLIMOV/APK/app-restful-maui-signed.apk` |

---

## 2. Rutas Absolutas en el Sistema (Windows)

Puedes copiar y pegar estas rutas directamente en el Explorador de Archivos de Windows:

### A. Cliente Móvil SOAP Java (TI.1.1)
- **Carpeta de entrega:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV\APK`
- **Archivo ejecutable APK:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV\APK\app-soap-debug.apk`

### B. Cliente Móvil SOAP .NET MAUI (TI.1.2)
- **Carpeta de entrega:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\APK`
- **Archivo ejecutable APK firmado:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\APK\app-soap-maui-signed.apk`
- **Ruta de compilación binaria interna:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\ClienteMovilGR09\bin\Debug\net10.0-android\ec.edu.monster.conversor.gr09-Signed.apk`

### C. Cliente Móvil RESTful Java (TI.1.3)
- **Carpeta de entrega:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV\APK`
- **Archivo ejecutable APK:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV\APK\app-restful-debug.apk`

### D. Cliente Móvil RESTful .NET MAUI (TI.1.4)
- **Carpeta de entrega:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\APK`
- **Archivo ejecutable APK firmado:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\APK\app-restful-maui-signed.apk`
- **Ruta de compilación binaria interna:**  
  `C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\ClienteMovilGR09\bin\Debug\net10.0-android\ec.edu.monster.conversor.rest.gr09-Signed.apk`

---

## 3. Comandos Rápidos para Abrir las Carpetas en Windows

Desde PowerShell, puedes abrir cualquiera de las carpetas directamente:

```powershell
# TI.1.1 (SOAP Java)
explorer "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV\APK"

# TI.1.2 (SOAP .NET MAUI)
explorer "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\APK"

# TI.1.3 (REST Java)
explorer "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV\APK"

# TI.1.4 (REST .NET MAUI)
explorer "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\APK"
```

---

## 4. Cómo Compilar los APKs por Comando

### A. Para .NET MAUI (`TI.1.2` y `TI.1.4`):
Una vez instaladas las herramientas de MAUI en Visual Studio, compilas el APK directamente desde PowerShell con:

```powershell
# Compilar TI.1.2 (SOAP .NET MAUI):
cd "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.2. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\ClienteMovilGR09"
dotnet build -f net10.0-android

# Compilar TI.1.4 (RESTful .NET MAUI):
cd "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09\04. CLIMOV\ClienteMovilGR09"
dotnet build -f net10.0-android
```
> El APK firmado generado queda en `bin\Debug\net10.0-android\*Signed.apk`.

### B. Para Java con Gradle (`TI.1.1` y `TI.1.3`):
```powershell
# Compilar TI.1.1 (SOAP Java):
cd "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV"
.\gradlew.bat assembleDebug

# Compilar TI.1.3 (RESTful Java):
cd "C:\Dev\GitHub\REPOS ARCH\u1-sw-arch\TI.1.3. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09\04. CLIMOV"
.\gradlew.bat assembleDebug
```

---

## 5. Instalación en el Celular Android

1. **Enviar el archivo `.apk` a tu teléfono:**
   - Vía WhatsApp Web, Telegram, Google Drive, o cable USB (carpeta *Downloads* / *Descargas*).
2. **Abrir e Instalar en Android:**
   - Toca el archivo `.apk` en el celular.
   - Si solicita permisos, activa **Confiar en esta fuente** (o *Instalar fuentes desconocidas*).
   - Presiona **Instalar**.

---

## 6. Conexión Móvil $\leftrightarrow$ Servidor PC

- **Credenciales quemadas (SINBDD):**
  - **Usuario:** `MONSTER`
  - **Contraseña:** `MONSTER9`
- **Red:**
  - Si usas la **Zona con cobertura móvil** (Mobile Hotspot de Windows), el celular se conecta a la PC en `192.168.137.1`.
  - Si conectas por **cable USB**, puedes redirigir el puerto con ADB:
    ```powershell
    # Para puertos 8080 (Java), 8733 (SOAP .NET) o 5000 (REST .NET):
    & "C:\Users\TheJVC\AppData\Local\Android\Sdk\platform-tools\adb.exe" reverse tcp:8080 tcp:8080
    & "C:\Users\TheJVC\AppData\Local\Android\Sdk\platform-tools\adb.exe" reverse tcp:8733 tcp:8733
    & "C:\Users\TheJVC\AppData\Local\Android\Sdk\platform-tools\adb.exe" reverse tcp:5000 tcp:5000
    ```
