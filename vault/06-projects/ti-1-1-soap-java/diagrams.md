---
tipo: diagrams
taller: TI.1.1
protocolo: SOAP
plataforma: Java
tags: [diagrams, uml, mermaid, soap, java, ti-1-1]
---

# Diagramas UML · TI.1.1 SOAP Java (SINBDD)

Este documento contiene la representación visual en sintaxis **Mermaid** de todos los modelos
UML nativos de PowerDesigner (`.oom` y `.eam`) correspondientes al taller **TI.1.1** (SOAP en Java, `SINBDD`).

Los modelos nativos editables y el espacio de trabajo se ubican en la carpeta del proyecto:
`TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/` y se abren conjuntamente mediante el archivo `CON_UNI_GR09.sws`.

---

## 1. Casos de Uso (`01_CASOS_DE_USO`)

**Modelo nativo:** `01_CASOS_DE_USO/01_CASOS_DE_USO.prj`, `OOm_Casos_De_Uso.oom`, `CU_Ingresar_Sistema.oom`.

### 1.1. Diagrama General de Casos de Uso
Ilustra el acceso protegido del actor `Usuario` a los servicios de conversión de longitud, masa y temperatura, los cuales incluyen obligatoriamente la validación de token y unidades.

```mermaid
flowchart LR
    user(["fa:fa-user Usuario"])

    subgraph Sistema ["Sistema de Conversión de Unidades"]
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
Descompone el caso de uso de inicio de sesión con validación de credenciales fijas (`MONSTER` / `MONSTER9`).

```mermaid
flowchart LR
    user(["fa:fa-user Usuario / Cliente"])

    subgraph ModuloAuth ["Módulo de Autenticación"]
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

**Modelo nativo:** `02_ACTIVIDAD/02_ACTIVIDAD.prj`, `ActividadIniciarSesión.oom`, `ActividadConvertirUnidades.oom`.

### 2.1. Actividad: Iniciar Sesión
Flujo de control con 3 swimlanes (*Usuario*, *Cliente*, *Servidor SOAP*).

```mermaid
flowchart TD
    subgraph LaneUsuario ["Usuario"]
        A1([Inicio]) --> A2["Abrir aplicación"]
        A2 --> A3["Visualizar formulario de Login"]
        A3 --> A4["Ingresar Usuario y Clave"]
        A4 --> A5["Presionar 'Iniciar Sesión'"]
        A13["Visualizar pantalla de Conversión"] --> A14([Fin - Acceso Concedido])
        A15["Visualizar mensaje de error"] --> A16([Fin - Acceso Denegado])
    end

    subgraph LaneCliente ["Cliente (Consola / Escritorio / Móvil / Web)"]
        A5 --> B1["Capturar datos de entrada"]
        B1 --> B2["Construir y serializar solicitud SOAP"]
        B2 --> B3["Enviar HTTP POST a ServicioAutenticación"]
        B4["Deserializar respuesta SOAP"] --> B5{"¿Éxito == true?"}
        B5 -- Sí --> B6["Guardar Token en objeto de Sesión"]
        B6 --> B7["Navegar a pantalla de Conversiones"]
        B7 --> A13
        B5 -- No --> B8["Mostrar mensaje de credenciales incorrectas"]
        B8 --> A15
    end

    subgraph LaneServidor ["Servidor SOAP (JAX-WS / Payara)"]
        B3 --> S1["Recibir solicitud SOAP y extraer credenciales"]
        S1 --> S2{"¿Usuario == 'MONSTER' y Clave == 'MONSTER9'?"}
        S2 -- Sí --> S3["Generar GUID Token y almacenar en Tokens Activos"]
        S3 --> S4["Retornar Respuesta SOAP con Token y Éxito = true"]
        S2 -- No --> S5["Retornar Respuesta SOAP con mensaje de error y Éxito = false"]
        S4 --> B4
        S5 --> B4
    end
```

### 2.2. Actividad: Convertir Unidades
Flujo de selección de magnitud, unidades, valor y procesamiento matemático de conversión en el servidor.

```mermaid
flowchart TD
    subgraph LaneUser ["Usuario"]
        C1([Inicio]) --> C2["Seleccionar Tipo de Conversión<br/>(Longitud, Masa, Temperatura)"]
        C2 --> C3["Seleccionar Unidad de Origen y Unidad de Destino"]
        C3 --> C4["Ingresar valor numérico y presionar 'Convertir'"]
        C11["Ver resultado convertido en pantalla"] --> C12([Fin])
        C13["Visualizar mensaje de error en pantalla"] --> C14([Fin con Error])
    end

    subgraph LaneClient ["Cliente (Consola / Escritorio / Móvil / Web)"]
        C2 --> D1["Mostrar unidades de medida asociadas al tipo"]
        D1 --> C3
        C4 --> D2["Capturar parámetros de entrada"]
        D2 --> D3["Construir Solicitud SOAP con Token de Sesión, Tipo, Origen, Destino y Valor"]
        D3 --> D4["Enviar petición SOAP a ServicioConversión"]
        D5["Deserializar respuesta SOAP"] --> D6{"¿Éxito == true?"}
        D6 -- Sí --> D7["Mostrar el valor convertido en sección de destino"]
        D7 --> C11
        D6 -- No --> D8["Mostrar mensaje de error recibido"]
        D8 --> C13
    end

    subgraph LaneServer ["Servidor SOAP (JAX-WS / Payara)"]
        D4 --> E1["Recibir Solicitud de Conversión y extraer parámetros"]
        E1 --> E2{"¿Token presente en Tokens Activos?"}
        E2 -- Sí --> E3["Convertir unidad origen a unidad base"]
        E3 --> E4["Convertir unidad base a unidad destino"]
        E4 --> E5["Retornar Respuesta SOAP con Valor Convertido y Éxito = true"]
        E2 -- No --> E6["Retornar Respuesta SOAP con mensaje 'Token no autorizado' y Éxito = false"]
        E5 --> D5
        E6 --> D5
    end
```

---

## 3. Diagramas de Secuencia (`03_SECUENCIA`)

**Modelo nativo:** `03_SECUENCIA/03_SECUENCIA.prj`, `Secuencia.oom`, `SecuenciaConvertirUnidades.oom`.

### 3.1. Secuencia: Iniciar Sesión
Interacción temporal entre las capas de presentación, control y servicios JAX-WS.

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI)
    participant C as AuthControlador
    participant P as ServicioAutenticacionClient (Proxy JAX-WS)
    participant S as ServicioAutenticación (Servidor SOAP)

    U->>V: Abrir Aplicación
    V-->>U: Formulario de Inicio de Sesión
    U->>V: Ingresa Usuario y Clave, clic "Iniciar Sesión"
    V->>C: iniciarSesion(usuario, clave)
    C->>P: crear SolicitudCredenciales(usuario, clave)
    P->>S: SOAP Request: ValidarCredenciales(solicitud) HTTP POST
    
    rect rgb(240, 248, 255)
        note over S: Validación de credenciales quemadas
        S->>S: compararUsuarioClave(solicitud.usuario, solicitud.clave)
        alt Credenciales válidas (MONSTER / MONSTER9)
            S->>S: TokensActivos.add(UUID.randomUUID())
            S-->>P: RespuestaCredenciales(Exito=true, TokenGenerado)
            P-->>C: (true, Sesión iniciada correctamente)
            C->>V: Navigation.NavigateTo("/conversion")
            V-->>U: Muestra pantalla de Conversión
        else Credenciales inválidas
            S-->>P: RespuestaCredenciales(Exito=false, "Credenciales inválidas")
            P-->>C: (false, MensajeError)
            C->>V: resultado error
            V-->>U: Muestra mensaje de error
        end
    end
```

### 3.2. Secuencia: Conversión de Unidades
Cálculo de conversión en el servidor tras la validación del token de sesión en memoria.

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant V as Vista (UI)
    participant C as ConversionControlador
    participant P as ServicioConversionClient (Proxy JAX-WS)
    participant S as Servicio Conversión (Servidor SOAP)

    U->>V: Selecciona Tipo de Unidad (Longitud/Masa/Temperatura)
    V-->>U: Muestra unidades de medida según el tipo
    U->>V: Selecciona Unidad Origen, Unidad Destino, Ingresa Valor y clic "Convertir"
    V->>C: Convertir(tipo, unidadOrigen, unidadDestino, valorOrigen)
    C->>P: crear SolicitudConversion(token, valor, origen, destino)
    P->>S: SOAP Request: Convertir(solicitud)
    
    rect rgb(240, 248, 255)
        note over S: Validación de Token y Conversión Matemática
        S->>S: ValidarToken(token) en memoria
        alt Token válido y activo
            S->>S: convertir unidad origen -> unidad base
            S->>S: convertir unidad base -> unidad destino
            S-->>P: RespuestaConversion(Exito=true, ValorConvertido)
            P-->>C: (true, ValorConvertido, "")
            C->>V: actualizar valorDestino
            V-->>U: Muestra resultado convertido
        else Token inválido o ausente
            S-->>P: RespuestaConversion(Exito=false, "Token no autorizado")
            P-->>C: (false, 0, MensajeError)
            C->>V: resultado error
            V-->>U: Muestra mensaje de error
        end
    end
```

---

## 4. Clases del Dominio (`04_CLASES`)

Al tratarse de una arquitectura **SINBDD** (sin persistencia en base de datos), la solución no requiere un modelo relacional ni mapeo ORM. La estructura en memoria y las credenciales se encuentran documentadas en el archivo:
`TI.1.1. PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR09/01. UML/03. DIAGRAMAS UML/04_CLASES/README.txt`

---

## 5. Arquitectura de Componentes (`05_ARQUITECTURA`)

**Modelo nativo:** `05_ARQUITECTURA/05_ARQUITECTURA.prj`, `Arquitectura.eam`.

Muestra los cuatro consumidores cliente comunicándose mediante sobres SOAP / XML contra el servidor Payara con almacenamiento volátil en memoria.

```mermaid
flowchart TB
    subgraph Consumidores ["Capa de Consumidores (Clientes)"]
        direction LR
        CC["Cliente Consola<br/>(Java CLI)"]
        CE["Cliente Escritorio<br/>(Java Swing / JavaFX)"]
        CM["Cliente Móvil<br/>(Android Java)"]
        CW["Cliente Web<br/>(Java JSP / Servlets)"]
    end

    subgraph RedSOAP ["Capa de Integración y Red"]
        direction TB
        SOAP_EP["Endpoint SOAP / WSDL<br/>HTTP POST / XML Envelopes"]
        JAXWS_PROXY["Proxies JAX-WS del Cliente"]
    end

    subgraph ServidorSOAP ["Capa de Servidor (Payara Server / Java)"]
        direction TB
        subgraph Servicios ["Servicios Web JAX-WS"]
            WS_AUTH["ServicioAutenticación<br/>(login, validación)"]
            WS_CONV["ServicioConversión<br/>(conversor de unidades)"]
        end
        
        subgraph LogicaNegocio ["Lógica de Negocio"]
            MOD_UTIL["Módulo de Utilidades<br/>(factores y fórmulas de conversión)"]
        end
        
        subgraph Memoria ["Almacenamiento Volátil (SINBDD)"]
            RAM_TOKENS[("Tokens Activos en Memoria<br/>(HashSet / UUIDs)")]
            RAM_CREDS[("Credenciales Quemadas<br/>MONSTER / MONSTER9")]
        end
    end

    CC --> JAXWS_PROXY
    CE --> JAXWS_PROXY
    CM --> JAXWS_PROXY
    CW --> JAXWS_PROXY

    JAXWS_PROXY --> SOAP_EP
    SOAP_EP --> WS_AUTH
    SOAP_EP --> WS_CONV

    WS_AUTH --> RAM_CREDS
    WS_AUTH --> RAM_TOKENS
    WS_CONV --> RAM_TOKENS
    WS_CONV --> MOD_UTIL
```

---

> [!info] Enlaces relacionados
> - [index.md](index.md): Panel principal del taller TI.1.1.
> - [srs.md](srs.md): Especificación de Requisitos de Software de TI.1.1.
> - [technical-manual.md](technical-manual.md): Manual técnico de instalación y despliegue.
> - [01-uml](../../03-deliverables/01-uml.md): Catálogo general de UML en el vault.
