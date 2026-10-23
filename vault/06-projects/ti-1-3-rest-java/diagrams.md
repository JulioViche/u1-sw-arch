---
tipo: diagrams
taller: TI.1.3
protocolo: RESTful
plataforma: Java
tags: [diagrams, uml, mermaid, rest, java, spring, ti-1-3]
---

# Diagramas UML · TI.1.3 RESTful Java (SINBDD)

Este documento contiene la representación visual en sintaxis **Mermaid** de todos los modelos
UML correspondientes al taller **TI.1.3** (Servicios Web RESTful con payloads JSON en plataforma **Java 17+ / Spring Boot / Jakarta REST**, `SINBDD`).

---

## 1. Casos de Uso (`01_CASOS_DE_USO`)

### 1.1. Diagrama General de Casos de Uso
El usuario interactúa con la API RESTful. La obtención del Bearer Token es precondición obligatoria antes de consumir los endpoints de conversión.

```mermaid
flowchart LR
    user(["fa:fa-user Usuario"])

    subgraph Sistema ["API RESTful Conversor de Unidades (Java)"]
        direction TB
        CU1(["CU1: Iniciar Sesión (Obtener Token)"])
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

### 1.2. Detalle del Módulo de Autenticación REST
Endpoint `POST /api/v1/auth/login` contra credenciales quemadas `MONSTER` / `MONSTER9`.

```mermaid
flowchart LR
    user(["fa:fa-user Usuario / Cliente HTTP"])

    subgraph ModuloAuth ["Controlador de Autenticación REST"]
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

### 2.1. Actividad: Iniciar Sesión (Autenticación REST)
Flujo HTTP POST JSON con 3 swimlanes (*Usuario*, *Cliente Java*, *Backend REST Spring Boot*).

```mermaid
flowchart TD
    subgraph LaneUsuario ["Usuario"]
        A1([Inicio]) --> A2["Abrir aplicación cliente"]
        A2 --> A3["Visualizar formulario de Login"]
        A3 --> A4["Ingresar usuario y clave"]
        A4 --> A5["Presionar 'Iniciar Sesión'"]
        A13["Visualizar módulo de conversión"] --> A14([Fin - Acceso Concedido])
        A15["Visualizar mensaje de error HTTP 401"] --> A16([Fin - Acceso Denegado])
    end

    subgraph LaneCliente ["Cliente (Consola / Swing / Android / Web)"]
        A5 --> B1["Serializar credenciales a JSON"]
        B1 --> B2["Enviar HTTP POST a /api/v1/auth/login"]
        B3["Recibir respuesta HTTP y deserializar JSON"] --> B4{"¿Código HTTP == 200?"}
        B4 -- Sí --> B5["Almacenar Bearer Token en memoria cliente"]
        B5 --> B6["Cargar interfaz de conversiones"]
        B6 --> A13
        B4 -- No --> B7["Capturar error 401 Unauthorized"]
        B7 --> A15
    end

    subgraph LaneServidor ["Backend REST (Java / Spring Boot / Tomcat)"]
        B2 --> S1["Recibir solicitud POST y parsear JSON"]
        S1 --> S2{"¿usuario == 'MONSTER' && clave == 'MONSTER9'?"}
        S2 -- Sí --> S3["Generar Token UUID y registrar en TokensActivos"]
        S3 --> S4["Retornar HTTP 200 OK con JSON: exito=true y token"]
        S2 -- No --> S5["Retornar HTTP 401 Unauthorized con mensaje de error"]
        S4 --> B3
        S5 --> B3
    end
```

### 2.2. Actividad: Convertir Unidades (Endpoints REST)
Flujo de petición `POST /api/v1/convert/{type}` con cabecera `Authorization: Bearer <token>`.

```mermaid
flowchart TD
    subgraph LaneUser ["Usuario"]
        C1([Inicio]) --> C2["Seleccionar magnitud física<br/>(Longitud, Masa, Temperatura)"]
        C2 --> C3["Seleccionar unidades e ingresar valor"]
        C3 --> C4["Presionar botón 'Convertir'"]
        C11["Visualizar resultado calculado en pantalla"] --> C12([Fin])
        C13["Visualizar mensaje de error en pantalla"] --> C14([Fin con Error])
    end

    subgraph LaneClient ["Cliente Java (Consola / Swing / Android / Web)"]
        C2 --> D1["GET /api/v1/units/{tipo} y poblar combos"]
        D1 --> C3
        C4 --> D2["Construir payload JSON con valor, origen y destino"]
        D2 --> D3["Adjuntar cabecera HTTP 'Authorization: Bearer token'"]
        D3 --> D4["Enviar HTTP POST a /api/v1/convert/{tipo}"]
        D5["Parsear respuesta JSON"] --> D6{"¿HTTP == 200 y exito == true?"}
        D6 -- Sí --> D7["Mostrar el valorConvertido en interfaz"]
        D7 --> C11
        D6 -- No --> D8["Mostrar mensaje de fallo o sesión inválida"]
        D8 --> C13
    end

    subgraph LaneServer ["Backend REST (Spring Boot / Java 17)"]
        D4 --> E1["Interceptar petición y extraer Bearer Token"]
        E1 --> E2{"¿Token registrado en Set en memoria?"}
        E2 -- Sí --> E3["Ejecutar conversión matemática: origen -> base -> destino"]
        E3 --> E4["Retornar HTTP 200 OK con JSON {exito:true, valorConvertido:...}"]
        E2 -- No --> E5["Retornar HTTP 403 Forbidden {exito:false, error:'Token no autorizado'}"]
        E4 --> D5
        E5 --> D5
    end
```

---

## 3. Diagramas de Secuencia (`03_SECUENCIA`)

### 3.1. Secuencia: Iniciar Sesión (REST JSON)

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI)
    participant C as AuthControlador (Cliente)
    participant H as HttpClient / Retrofit
    participant S as AuthRestController (Spring Boot)

    U->>V: Abre aplicación
    V-->>U: Formulario de Login
    U->>V: Ingresa credenciales y presiona "Iniciar Sesión"
    V->>C: login(usuario, clave)
    C->>H: postJson("/api/v1/auth/login", {usuario, clave})
    H->>S: HTTP POST /api/v1/auth/login [Content-Type: application/json]
    
    rect rgb(240, 248, 255)
        note over S: Validación de credenciales quemadas en memoria
        S->>S: validarCredenciales(body.usuario, body.clave)
        alt Credenciales válidas (MONSTER / MONSTER9)
            S->>S: TokensActivos.add(UUID.randomUUID().toString())
            S-->>H: HTTP 200 OK {"exito": true, "token": "uuid-..."}
            H-->>C: AuthResponse(exito=true, token)
            C->>V: setSessionToken(token) y abrir conversor
            V-->>U: Muestra pantalla de Conversión
        else Credenciales inválidas
            S-->>H: HTTP 401 Unauthorized {"exito": false, "mensaje": "Credenciales inválidas"}
            H-->>C: AuthResponse(exito=false)
            C->>V: mostrarError("Acceso no autorizado")
            V-->>U: Muestra mensaje de error
        end
    end
```

### 3.2. Secuencia: Conversión de Unidades (REST JSON)

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI)
    participant C as ConversionControlador (Cliente)
    participant H as HttpClient (Cliente HTTP)
    participant S as ConversionRestController (Spring Boot)

    U->>V: Selecciona magnitud (Longitud/Masa/Temperatura)
    V-->>U: Muestra unidades disponibles
    U->>V: Selecciona Origen, Destino, ingresa Valor y presiona "Convertir"
    V->>C: convertir(tipo, origen, destino, valor)
    C->>H: postWithBearerToken("/api/v1/convert/" + tipo, body, token)
    H->>S: HTTP POST /api/v1/convert/{tipo} [Authorization: Bearer <token>]
    
    rect rgb(240, 248, 255)
        note over S: Validación de Token y Cómputo de Conversión
        S->>S: TokensActivos.contains(token)
        alt Token presente en memoria
            S->>S: calcularConversion(origen, destino, valor)
            S-->>H: HTTP 200 OK {"exito": true, "valorConvertido": 1000.0}
            H-->>C: ConversionResult(true, 1000.0)
            C->>V: actualizarResultado(1000.0)
            V-->>U: Visualiza resultado en pantalla
        else Token ausente o inválido
            S-->>H: HTTP 403 Forbidden {"exito": false, "error": "Token no autorizado"}
            H-->>C: ConversionResult(false, error)
            C->>V: mostrarError("Sesión expirada o inválida")
            V-->>U: Muestra mensaje de error
        end
    end
```

---

## 4. Clases del Dominio (`04_CLASES`)

Al tratarse de una arquitectura **SINBDD**, la solución en Java RESTful no utiliza JPA ni Hibernate. Toda la lógica de transformación y catálogo reside en memoria RAM documentada en:
`TI.1.1. PAO202651_RESTFUL_JAVA_SINBDD_NRC36884_GR09/01. UML/03. DIAGRAMAS UML/04_CLASES/README.txt`

---

## 5. Arquitectura de Componentes (`05_ARQUITECTURA`)

```mermaid
flowchart TB
    subgraph ConsumidoresREST ["Capa de Consumidores (Clientes)"]
        direction LR
        CC["Cliente Consola<br/>(Java HttpClient)"]
        CE["Cliente Escritorio<br/>(Java Swing / JavaFX)"]
        CM["Cliente Móvil<br/>(Android Retrofit 2)"]
        CW["Cliente Web<br/>(HTML5 / Fetch API)"]
    end

    subgraph CapaRedREST ["Capa de Red HTTP / REST"]
        direction TB
        ROUTER["Enrutador HTTP / DispatcherServlet<br/>Endpoints semánticos JSON"]
        OPENAPI["OpenAPI 3.0 / Swagger UI"]
    end

    subgraph BackendJava ["Backend RESTful (Spring Boot / Tomcat / Java 17)"]
        direction TB
        subgraph Controladores ["Controladores REST (@RestController)"]
            CTRL_AUTH["AuthController<br/>(/api/v1/auth)"]
            CTRL_CONV["ConversionController<br/>(/api/v1/convert)"]
        end
        
        subgraph ServiciosNegocio ["Capa de Servicios"]
            SRV_CALC["ConversionService<br/>(algoritmos de conversión)"]
        end
        
        subgraph AlmacenamientoVolatil ["Memoria RAM (SINBDD)"]
            RAM_TOKENS[("Tokens Activos en RAM<br/>Set&lt;String&gt; / ConcurrentHashMap")]
            RAM_CREDS[("Credenciales Quemadas<br/>MONSTER / MONSTER9")]
        end
    end

    CC --> ROUTER
    CE --> ROUTER
    CM --> ROUTER
    CW --> ROUTER

    ROUTER --> CTRL_AUTH
    ROUTER --> CTRL_CONV
    ROUTER -.-> OPENAPI

    CTRL_AUTH --> RAM_CREDS
    CTRL_AUTH --> RAM_TOKENS
    CTRL_CONV --> RAM_TOKENS
    CTRL_CONV --> SRV_CALC
```

---

> [!info] Enlaces relacionados
> - [index.md](index.md): Panel principal del taller TI.1.3.
> - [srs.md](srs.md): Especificación de Requisitos de Software de TI.1.3.
> - [01-uml](../../03-deliverables/01-uml.md): Catálogo general de UML en el vault.
