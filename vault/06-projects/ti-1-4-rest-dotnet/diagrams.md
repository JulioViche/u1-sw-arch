---
tipo: diagrams
taller: TI.1.4
protocolo: RESTful
plataforma: .NET
tags: [diagrams, uml, mermaid, rest, dotnet, webapi, ti-1-4]
---

# Diagramas UML · TI.1.4 RESTful .NET (SINBDD)

Este documento contiene la representación visual en sintaxis **Mermaid** de todos los modelos
UML correspondientes al taller **TI.1.4** (Servicios Web RESTful en plataforma **.NET 8 / C# / ASP.NET Core Web API**, `SINBDD`).

---

## 1. Casos de Uso (`01_CASOS_DE_USO`)

### 1.1. Diagrama General de Casos de Uso
El actor `Usuario` interactúa con la API RESTful expuesta por Kestrel en .NET. La autenticación y adquisición del Bearer Token es obligatoria para acceder a las operaciones de conversión.

```mermaid
flowchart LR
    user(["fa:fa-user Usuario"])

    subgraph Sistema ["API RESTful ASP.NET Core (.NET 8)"]
        direction TB
        CU1(["CU1: Iniciar Sesión (Adquirir Token)"])
        CU2(["CU2: Convertir Longitud"])
        CU3(["CU3: Convertir Masa"])
        CU4(["CU4: Convertir Temperatura"])
        
        CU_VT(["Validar Bearer Token"])
        CU_VU(["Validar Unidades en Catálogo"])
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
Endpoint `POST /api/auth/login` validado contra las constantes en código (`MONSTER` / `MONSTER9`).

```mermaid
flowchart LR
    user(["fa:fa-user Usuario / Cliente HTTP"])

    subgraph ModuloAuth ["AuthController ASP.NET Core"]
        direction TB
        CU_IS(["Ingresar al Sistema"])
        CU_AU(["Autenticar Credenciales JSON"])
        CU_CC(["Validar Constantes (MONSTER / MONSTER9)"])
    end

    user --> CU_IS
    CU_IS -.->|"<<include>>"| CU_AU
    CU_IS -.->|"<<include>>"| CU_CC
```

---

## 2. Diagramas de Actividad (`02_ACTIVIDAD`)

### 2.1. Actividad: Iniciar Sesión (Autenticación REST .NET)
Flujo HTTP POST JSON con 3 swimlanes (*Usuario*, *Cliente C# / Multi-canal*, *Backend ASP.NET Core Web API*).

```mermaid
flowchart TD
    subgraph LaneUsuario ["Usuario"]
        A1([Inicio]) --> A2["Abrir aplicación cliente"]
        A2 --> A3["Visualizar formulario de Login"]
        A3 --> A4["Ingresar usuario y clave"]
        A4 --> A5["Presionar botón 'Iniciar Sesión'"]
        A13["Visualizar panel de conversiones"] --> A14([Fin - Acceso Concedido])
        A15["Visualizar mensaje de error 401"] --> A16([Fin - Acceso Denegado])
    end

    subgraph LaneCliente ["Cliente .NET (Consola / WPF / MAUI / Blazor)"]
        A5 --> B1["Serializar credenciales a JSON con System.Text.Json"]
        B1 --> B2["Enviar HTTP POST a /api/auth/login"]
        B3["Recibir respuesta HTTP"] --> B4{"¿StatusCode == 200?"}
        B4 -- Sí --> B5["Guardar Bearer Token en memoria cliente"]
        B5 --> B6["Cargar vista principal de conversor"]
        B6 --> A13
        B4 -- No --> B7["Capturar error 401 Unauthorized"]
        B7 --> A15
    end

    subgraph LaneServidor ["Backend ASP.NET Core (Kestrel / C# 12)"]
        B2 --> S1["Enrutar petición a AuthController"]
        S1 --> S2{"¿Username == 'MONSTER' && Password == 'MONSTER9'?"}
        S2 -- Sí --> S3["Generar Guid de sesión y almacenar en ConcurrentDictionary"]
        S3 --> S4["Retornar HTTP 200 OK con JSON {success: true, token: '...'}"]
        S2 -- No --> S5["Retornar HTTP 401 Unauthorized con JSON de error"]
        S4 --> B3
        S5 --> B3
    end
```

### 2.2. Actividad: Convertir Unidades (Endpoints REST .NET)
Flujo de invocación a `POST /api/conversion/{type}` con validación de cabecera `Authorization: Bearer <token>`.

```mermaid
flowchart TD
    subgraph LaneUser ["Usuario"]
        C1([Inicio]) --> C2["Seleccionar magnitud física<br/>(Longitud, Masa, Temperatura)"]
        C2 --> C3["Seleccionar unidades e ingresar valor"]
        C3 --> C4["Presionar botón 'Convertir'"]
        C11["Visualizar valor resultante en pantalla"] --> C12([Fin])
        C13["Visualizar mensaje de error en pantalla"] --> C14([Fin con Error])
    end

    subgraph LaneClient ["Cliente .NET (Consola / WPF / MAUI / Blazor)"]
        C2 --> D1["GET /api/conversion/units/{tipo} y llenar selectores"]
        D1 --> C3
        C4 --> D2["Serializar payload ConversionRequest a JSON"]
        D2 --> D3["Agregar Header 'Authorization: Bearer &lt;token&gt;'"]
        D3 --> D4["Enviar HTTP POST a /api/conversion/{tipo}"]
        D5["Deserializar JSON de respuesta"] --> D6{"¿StatusCode == 200?"}
        D6 -- Sí --> D7["Desplegar valor convertido en el campo destino"]
        D7 --> C11
        D6 -- No --> D8["Desplegar mensaje de error retornado"]
        D8 --> C13
    end

    subgraph LaneServer ["Backend ASP.NET Core (.NET 8 / Kestrel)"]
        D4 --> E1["Extraer y validar Token de la cabecera Authorization"]
        E1 --> E2{"¿Token activo en ConcurrentDictionary?"}
        E2 -- Sí --> E3["Cálculo matemático: unidad origen -> base -> destino"]
        E3 --> E4["Retornar HTTP 200 OK con ConversionResponse JSON"]
        E2 -- No --> E5["Retornar HTTP 403 Forbidden {error: 'Token no autorizado'}"]
        E4 --> D5
        E5 --> D5
    end
```

---

## 3. Diagramas de Secuencia (`03_SECUENCIA`)

### 3.1. Secuencia: Iniciar Sesión (REST JSON .NET)

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI)
    participant C as AuthController (Cliente)
    participant H as HttpClient (System.Net.Http)
    participant S as AuthController ([ApiController] ASP.NET Core)

    U->>V: Abre aplicación cliente
    V-->>U: Formulario de Login
    U->>V: Ingresa credenciales y presiona "Iniciar Sesión"
    V->>C: LoginAsync(username, password)
    C->>H: PostAsJsonAsync("/api/auth/login", dto)
    H->>S: HTTP POST /api/auth/login [Content-Type: application/json]
    
    rect rgb(240, 248, 255)
        note over S: Validación de credenciales quemadas en C#
        S->>S: ValidarCredenciales(dto.Username, dto.Password)
        alt Credenciales válidas (MONSTER / MONSTER9)
            S->>S: ActiveTokens[Guid.NewGuid().ToString()] = DateTime.UtcNow
            S-->>H: HTTP 200 OK {"success": true, "token": "guid-..."}
            H-->>C: AuthResponse(success=true, token)
            C->>V: Guardar token y navegar al conversor
            V-->>U: Muestra pantalla de Conversión
        else Credenciales inválidas
            S-->>H: HTTP 401 Unauthorized {"success": false, "message": "Credenciales inválidas"}
            H-->>C: AuthResponse(success=false)
            C->>V: Mostrar error en vista
            V-->>U: Muestra mensaje "Acceso denegado"
        end
    end
```

### 3.2. Secuencia: Conversión de Unidades (REST JSON .NET)

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI)
    participant C as ConversionController (Cliente)
    participant H as HttpClient (System.Net.Http)
    participant S as ConversionController ([ApiController] ASP.NET Core)

    U->>V: Selecciona magnitud física (Longitud/Masa/Temperatura)
    V-->>U: Muestra catálogo de unidades
    U->>V: Selecciona Origen, Destino, ingresa Valor y presiona "Convertir"
    V->>C: ConvertAsync(type, fromUnit, toUnit, value)
    C->>H: PostAsJsonAsync("/api/conversion/" + type, requestDto, token)
    H->>S: HTTP POST /api/conversion/{type} [Authorization: Bearer <token>]
    
    rect rgb(240, 248, 255)
        note over S: Validación de Token y Transformación en Memoria
        S->>S: ActiveTokens.ContainsKey(token)
        alt Token válido y activo
            S->>S: CalcularConversion(type, fromUnit, toUnit, value)
            S-->>H: HTTP 200 OK {"success": true, "convertedValue": 1000.0}
            H-->>C: ConversionResponse(success=true, 1000.0)
            C->>V: Actualizar campo de resultado
            V-->>U: Despliega valor convertido
        else Token inválido o expirado
            S-->>H: HTTP 403 Forbidden {"success": false, "error": "Token no autorizado"}
            H-->>C: ConversionResponse(success=false)
            C->>V: Mostrar mensaje de error
            V-->>U: Mensaje "Sesión no autorizada o expirada"
        end
    end
```

---

## 4. Clases del Dominio (`04_CLASES`)

Al tratarse de una arquitectura **SINBDD**, la solución en .NET RESTful no utiliza Entity Framework Core ni migraciones SQL. Todo el estado reside en colecciones en memoria documentadas en:
`TI.1.4. PAO202651_RESTFUL_DOTNET_SINBDD_NRC36884_GR09/01. UML/03. DIAGRAMAS UML/04_CLASES/README.txt`

---

## 5. Arquitectura de Componentes (`05_ARQUITECTURA`)

```mermaid
flowchart TB
    subgraph ClientesDotNet ["Capa de Consumidores (Clientes)"]
        direction LR
        CC["Cliente Consola<br/>(C# CLI HttpClient)"]
        CE["Cliente Escritorio<br/>(C# WPF / WinForms)"]
        CM["Cliente Móvil<br/>(Android Java / .NET MAUI)"]
        CW["Cliente Web<br/>(Blazor / Fetch API)"]
    end

    subgraph CapaHttpNet ["Capa de Red HTTP / Enrutamiento"]
        direction TB
        KESTREL_ROUTER["Middleware de Enrutamiento Kestrel<br/>Endpoints semánticos JSON"]
        SWAGGER["Swagger UI / OpenAPI 3.0 (/swagger)"]
    end

    subgraph BackendNetCore ["Backend ASP.NET Core (.NET 8 SDK / C# 12)"]
        direction TB
        subgraph ControladoresApi ["Controladores API ([ApiController])"]
            CTRL_AUTH["AuthController<br/>(/api/auth)"]
            CTRL_CONV["ConversionController<br/>(/api/conversion)"]
        end
        
        subgraph LogicaCalculo ["Lógica de Cómputo"]
            SVC_MATH["ConversionCalculator<br/>(factores y transformaciones)"]
        end
        
        subgraph MemoriaVolatil ["Memoria RAM (SINBDD)"]
            RAM_TOKENS[("Tokens Activos<br/>ConcurrentDictionary&lt;string, DateTime&gt;")]
            RAM_CREDS[("Credenciales Quemadas<br/>MONSTER / MONSTER9")]
        end
    end

    CC --> KESTREL_ROUTER
    CE --> KESTREL_ROUTER
    CM --> KESTREL_ROUTER
    CW --> KESTREL_ROUTER

    KESTREL_ROUTER --> CTRL_AUTH
    KESTREL_ROUTER --> CTRL_CONV
    KESTREL_ROUTER -.-> SWAGGER

    CTRL_AUTH --> RAM_CREDS
    CTRL_AUTH --> RAM_TOKENS
    CTRL_CONV --> RAM_TOKENS
    CTRL_CONV --> SVC_MATH
```

---

> [!info] Enlaces relacionados
> - [index.md](index.md): Panel principal del taller TI.1.4.
> - [srs.md](srs.md): Especificación de Requisitos de Software de TI.1.4.
> - [01-uml](../../03-deliverables/01-uml.md): Catálogo general de UML en el vault.
