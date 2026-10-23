---
tipo: diagrams
taller: TI.1.2
protocolo: SOAP
plataforma: .NET
tags: [diagrams, uml, mermaid, soap, dotnet, ti-1-2]
---

# Diagramas UML · TI.1.2 SOAP .NET (SINBDD)

Este documento contiene la representación visual en sintaxis **Mermaid** de todos los modelos
UML correspondientes al taller **TI.1.2** (Servicios Web SOAP en plataforma **.NET 8 / C#**, `SINBDD`).

---

## 1. Casos de Uso (`01_CASOS_DE_USO`)

### 1.1. Diagrama General de Casos de Uso
El actor `Usuario` interactúa con el sistema requiriendo autenticación previa obligatoria antes de realizar transformaciones físicas en tres magnitudes (Longitud, Masa, Temperatura).

```mermaid
flowchart LR
    user(["fa:fa-user Usuario"])

    subgraph Sistema ["Sistema de Conversión de Unidades (.NET)"]
        direction TB
        CU1(["CU1: Iniciar Sesión"])
        CU2(["CU2: Convertir Longitud"])
        CU3(["CU3: Convertir Masa"])
        CU4(["CU4: Convertir Temperatura"])
        
        CU_VT(["Validar Token de Sesión"])
        CU_VU(["Validar Unidades de Medida"])
    end

    user --> CU1
    user --> CU2
    user --> CU3
    user --> CU4

    CU2 -.->|"<<include>>"| CU_VT
    CU2 -.->|"<<include>>"| CU_VU

    CU3 -.->|"<<include>>"| CU_VT
    CU3 -.->|"<<include>>"| CU_VU

    CU4 -.->|"<<include>>"| CU_VT
    CU4 -.->|"<<include>>"| CU_VU
```

### 1.2. Detalle del Módulo de Autenticación
Descompone el inicio de sesión contra credenciales fijas (`MONSTER` / `MONSTER9`).

```mermaid
flowchart LR
    user(["fa:fa-user Usuario / Cliente .NET"])

    subgraph ModuloAuth ["Módulo de Autenticación SOAP (.NET)"]
        direction TB
        CU_IS(["Ingresar al Sistema"])
        CU_AU(["Autenticar Usuario"])
        CU_CC(["Comprobar Contraseña (MONSTER / MONSTER9)"])
    end

    user --> CU_IS
    CU_IS -.->|"<<include>>"| CU_AU
    CU_IS -.->|"<<include>>"| CU_CC
```

---

## 2. Diagramas de Actividad (`02_ACTIVIDAD`)

### 2.1. Actividad: Iniciar Sesión
Flujo con 3 swimlanes (*Usuario*, *Cliente .NET*, *Servidor CoreWCF .NET*).

```mermaid
flowchart TD
    subgraph LaneUsuario ["Usuario"]
        A1([Inicio]) --> A2["Abrir aplicación cliente"]
        A2 --> A3["Visualizar formulario de Login"]
        A3 --> A4["Ingresar credenciales"]
        A4 --> A5["Presionar 'Iniciar Sesión'"]
        A13["Visualizar pantalla de Conversión"] --> A14([Fin - Acceso Concedido])
        A15["Visualizar mensaje de error"] --> A16([Fin - Acceso Denegado])
    end

    subgraph LaneCliente ["Cliente (Consola / WPF / Móvil / Web)"]
        A5 --> B1["Capturar usuario y clave"]
        B1 --> B2["Construir mensaje SOAP Envelope"]
        B2 --> B3["Enviar HTTP POST a endpoint CoreWCF"]
        B4["Deserializar respuesta XML SOAP"] --> B5{"¿Exito == true?"}
        B5 -- Sí --> B6["Almacenar Token en contexto de sesión"]
        B6 --> B7["Navegar a vista de conversiones"]
        B7 --> A13
        B5 -- No --> B8["Mostrar mensaje de credenciales inválidas"]
        B8 --> A15
    end

    subgraph LaneServidor ["Servidor CoreWCF (Kestrel / .NET 8)"]
        B3 --> S1["Recibir solicitud SOAP y parsear credenciales"]
        S1 --> S2{"¿Usuario == 'MONSTER' y Password == 'MONSTER9'?"}
        S2 -- Sí --> S3["Generar Guid de sesión y almacenar en ConcurrentDictionary"]
        S3 --> S4["Retornar SOAP Envelope con Token y Exito = true"]
        S2 -- No --> S5["Retornar SOAP Envelope con Exito = false"]
        S4 --> B4
        S5 --> B4
    end
```

### 2.2. Actividad: Convertir Unidades
Flujo de validación de token y cómputo matemático en el servicio CoreWCF.

```mermaid
flowchart TD
    subgraph LaneUser ["Usuario"]
        C1([Inicio]) --> C2["Seleccionar Magnitud<br/>(Longitud, Masa, Temperatura)"]
        C2 --> C3["Seleccionar Unidad Origen y Destino"]
        C3 --> C4["Ingresar valor y presionar 'Convertir'"]
        C11["Ver resultado en pantalla"] --> C12([Fin])
        C13["Visualizar error en pantalla"] --> C14([Fin con Error])
    end

    subgraph LaneClient ["Cliente .NET (Consola / WPF / Android / Web)"]
        C2 --> D1["Desplegar unidades de medida"]
        D1 --> C3
        C4 --> D2["Capturar parámetros"]
        D2 --> D3["Construir Solicitud SOAP con Token de Sesión"]
        D3 --> D4["Invocar operación CoreWCF via ChannelFactory"]
        D5["Procesar respuesta SOAP"] --> D6{"¿Exito == true?"}
        D6 -- Sí --> D7["Mostrar valor calculado"]
        D7 --> C11
        D6 -- No --> D8["Mostrar mensaje de error"]
        D8 --> C13
    end

    subgraph LaneServer ["Servidor CoreWCF (.NET 8 / Kestrel)"]
        D4 --> E1["Extraer parámetros y Token de sesión"]
        E1 --> E2{"¿Token registrado en memoria?"}
        E2 -- Sí --> E3["Convertir unidad origen a unidad base"]
        E3 --> E4["Convertir unidad base a unidad destino"]
        E4 --> E5["Retornar SOAP Response con Valor y Exito = true"]
        E2 -- No --> E6["Retornar SOAP Fault / Error 'Token no autorizado'"]
        E5 --> D5
        E6 --> D5
    end
```

---

## 3. Diagramas de Secuencia (`03_SECUENCIA`)

### 3.1. Secuencia: Iniciar Sesión

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI WPF/Blazor/CLI)
    participant C as AuthController
    participant P as ClientProxy (ChannelFactory CoreWCF)
    participant S as AuthService CoreWCF (Kestrel .NET)

    U->>V: Abre aplicación
    V-->>U: Formulario de Login
    U->>V: Ingresa Usuario y Clave, clic "Iniciar Sesión"
    V->>C: IniciarSesion(usuario, clave)
    C->>P: SolicitarAutenticacion(credenciales)
    P->>S: SOAP POST /AuthService.svc (XML Envelope)
    
    rect rgb(240, 248, 255)
        note over S: Validación de credenciales quemadas en C#
        S->>S: CompararUsuarioClave(user, pass)
        alt Credenciales correctas (MONSTER / MONSTER9)
            S->>S: TokensActivos[Guid.NewGuid().ToString()] = DateTime.UtcNow
            S-->>P: RespuestaSOAP(Exito=true, Token)
            P-->>C: AuthResult(true, Token)
            C->>V: Redirigir a módulo de Conversión
            V-->>U: Presenta formulario de Conversión
        else Credenciales incorrectas
            S-->>P: RespuestaSOAP(Exito=false, "Credenciales incorrectas")
            P-->>C: AuthResult(false, MensajeError)
            C->>V: Mostrar error
            V-->>U: Mensaje "Acceso denegado"
        end
    end
```

### 3.2. Secuencia: Conversión de Unidades

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI)
    participant C as ConversionController
    participant P as ClientProxy (ChannelFactory)
    participant S as ConversionService CoreWCF (Kestrel)

    U->>V: Selecciona Magnitud (Longitud/Masa/Temperatura)
    V-->>U: Muestra unidades disponibles
    U->>V: Selecciona Origen, Destino, Valor y clic "Convertir"
    V->>C: Convertir(tipo, origen, destino, valor)
    C->>P: CrearConversionRequest(token, tipo, origen, destino, valor)
    P->>S: SOAP POST /ConversionService.svc
    
    rect rgb(240, 248, 255)
        note over S: Validación de Token y Transformación en Memoria
        S->>S: ValidarToken(token) en ConcurrentDictionary
        alt Token activo y válido
            S->>S: OrigenToBase(valor, origen)
            S->>S: BaseToDestino(valorBase, destino)
            S-->>P: RespuestaConversion(Exito=true, ValorConvertido)
            P-->>C: Result(true, Valor)
            C->>V: Actualizar campo resultado
            V-->>U: Despliega valor convertido
        else Token inválido o ausente
            S-->>P: RespuestaConversion(Exito=false, "Token no autorizado")
            P-->>C: Result(false, Error)
            C->>V: Mostrar error
            V-->>U: Mensaje "Sesión inválida o expirada"
        end
    end
```

---

## 4. Clases del Dominio (`04_CLASES`)

Al tratarse de una arquitectura **SINBDD**, la solución en .NET no utiliza Entity Framework Core ni bases de datos SQL. Las estructuras son DTOs y contratos en memoria documentados en:
`TI.1.1. PAO202651_SOAP_DOTNET_SINBDD_NRC36884_GR09/01. UML/03. DIAGRAMAS UML/04_CLASES/README.txt`

---

## 5. Arquitectura de Componentes (`05_ARQUITECTURA`)

```mermaid
flowchart TB
    subgraph ClientesDotNet ["Capa de Consumidores (Clientes)"]
        direction LR
        CC["Cliente Consola<br/>(C# .NET CLI)"]
        CE["Cliente Escritorio<br/>(C# WPF / WinForms)"]
        CM["Cliente Móvil<br/>(Android Java / .NET MAUI)"]
        CW["Cliente Web<br/>(ASP.NET Core / Blazor)"]
    end

    subgraph Integracion ["Capa de Red y WSDL"]
        direction TB
        WSDL_DEF["Contratos WSDL / Schemas XSD<br/>SOAP Envelopes sobre HTTP"]
        PROXY_CF["ChannelFactory&lt;T&gt; / ServiceReference"]
    end

    subgraph ServidorKestrel ["Servidor CoreWCF (Kestrel / .NET 8)"]
        direction TB
        subgraph ServiciosWCF ["Servicios CoreWCF"]
            SVC_AUTH["AuthService<br/>([ServiceContract])"]
            SVC_CONV["ConversionService<br/>([ServiceContract])"]
        end
        
        subgraph LogicaCalculo ["Lógica de Negocio"]
            MOD_CALC["ConversionCalculator<br/>(fórmulas matemáticas)"]
        end
        
        subgraph MemoriaRAM ["Memoria RAM (SINBDD)"]
            RAM_TOKENS[("Tokens Activos<br/>ConcurrentDictionary&lt;string, DateTime&gt;")]
            RAM_CREDS[("Credenciales Quemadas<br/>MONSTER / MONSTER9")]
        end
    end

    CC --> PROXY_CF
    CE --> PROXY_CF
    CM --> PROXY_CF
    CW --> PROXY_CF

    PROXY_CF --> WSDL_DEF
    WSDL_DEF --> SVC_AUTH
    WSDL_DEF --> SVC_CONV

    SVC_AUTH --> RAM_CREDS
    SVC_AUTH --> RAM_TOKENS
    SVC_CONV --> RAM_TOKENS
    SVC_CONV --> MOD_CALC
```

---

> [!info] Enlaces relacionados
> - [index.md](index.md): Panel principal del taller TI.1.2.
> - [srs.md](srs.md): Especificación de Requisitos de Software de TI.1.2.
> - [01-uml](../../03-deliverables/01-uml.md): Catálogo general de UML en el vault.
