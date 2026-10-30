---
tipo: deliverable
tags: [deliverable, uml, ecud, use-cases, requirements, sinbdd]
---

# Especificación de Casos de Uso Detallados (ECUD) - Conversor de Unidades

> [!NOTE]
> Documento técnico que compila la **Especificación de Casos de Uso Detallados (ECUD)** para el sistema distribuido **Conversor de Unidades** bajo la arquitectura **SINBDD** (Sin Base de Datos).
> Aplica transversalmente a las cuatro variantes: **SOAP Java (TI.1.1)**, **SOAP .NET (TI.1.2)**, **RESTful Java (TI.1.3)** y **RESTful .NET (TI.1.4)**.

---

## 1. Portada y Metadatos Institucionales

| Campo | Detalle Institucional |
| :--- | :--- |
| **Institución** | Universidad de las Fuerzas Armadas ESPE |
| **Carrera** | Ingeniería de Software / Tecnologías de la Información |
| **Asignatura** | Arquitectura de Software |
| **Tarea / Taller** | Entregable UML: 02. ECUD |
| **NRC** | 36884 |
| **Grupo** | GR09 |
| **Autores / Integrantes** | **Julio Enrique Viche Castillo**, **Gabriel Omar Murillo Medina** |
| **Docente / Tutor** | Ing. Eduardo Mauricio Campaña Ortega, MIS.MDU.CCNA.CCIA. PhD. (c) |
| **Fecha** | 28 de Octubre de 2026 |

---

## 2. Diagrama General de Casos de Uso del Sistema

```mermaid
flowchart TD
    ActorUser(["fa:fa-user Usuario Final"])

    subgraph Sistema ["Sistema Conversor de Unidades (SINBDD)"]
        CU01(["CU-01: Iniciar Sesión (Login)"])
        CU02(["CU-02: Convertir Longitud"])
        CU03(["CU-03: Convertir Masa"])
        CU04(["CU-04: Convertir Temperatura"])
        CU05(["CU-05: Validar Token (RAM)"])
        CU06(["CU-06: Validar Unidades"])
    end

    ActorUser --> CU01
    ActorUser --> CU02
    ActorUser --> CU03
    ActorUser --> CU04

    CU02 -.->|<< include >>| CU05
    CU02 -.->|<< include >>| CU06
    CU03 -.->|<< include >>| CU05
    CU03 -.->|<< include >>| CU06
    CU04 -.->|<< include >>| CU05
    CU04 -.->|<< include >>| CU06

    CU05 -.->|<< extend (token expirado) >>| CU01
```

---

## 3. Matriz de Relaciones entre Casos de Uso

| Caso de Uso | Tipo | Invocador | Dependencias (`<<include>>`) | Extensiones (`<<extend>>`) |
| :--- | :--- | :--- | :--- | :--- |
| **CU-01: Iniciar Sesión** | Primario | Usuario Final | Ninguna | Es extendido por CU-05 ante fallo de sesión |
| **CU-02: Convertir Longitud** | Primario | Usuario Final | CU-05 (Validar Token), CU-06 (Validar Unidades) | Ninguna |
| **CU-03: Convertir Masa** | Primario | Usuario Final | CU-05 (Validar Token), CU-06 (Validar Unidades) | Ninguna |
| **CU-04: Convertir Temperatura**| Primario | Usuario Final | CU-05 (Validar Token), CU-06 (Validar Unidades) | Ninguna |
| **CU-05: Validar Token** | Interno | CU-02, CU-03, CU-04 | Ninguna | Extiende a CU-01 si el token no existe/expiró |
| **CU-06: Validar Unidades** | Interno | CU-02, CU-03, CU-04 | Ninguna | Ninguna |

---

## 4. Fichas de Casos de Uso Detallados

---

### CU-01: Iniciar Sesión (Login)

| Atributo | Especificación |
| :--- | :--- |
| **Identificador** | **CU-01** |
| **Nombre** | Iniciar Sesión (Login) |
| **Descripción** | Permite al usuario autenticarse en el sistema enviando sus credenciales fijas (`MONSTER` / `MONSTER9`). Si son válidas, el servidor genera un token pseudo-UUID en memoria RAM, lo almacena en la colección de sesiones activas y lo retorna al cliente. |
| **Actor(es)** | Usuario Final |
| **Meta** | El usuario obtiene un token de autenticación válido y vigente para realizar conversiones. |
| **Precondiciones** | 1. El servicio web backend (SOAP o REST) debe encontrarse activo y en ejecución.<br/>2. El usuario debe ingresar credenciales válidas. |
| **Postcondiciones (Éxito)** | El sistema genera el token UUID en memoria y retorna `success = true`, mensaje de bienvenida y el valor del token. |
| **Postcondiciones (Fallo)** | El sistema no genera token, responde con código HTTP 401 Unauthorized y mensaje: `"Usuario o clave fallidos, intenta de nuevo."` |
| **Datos de Entrada** | - `usuario`: `"MONSTER"`<br/>- `clave`: `"MONSTER9"` |
| **Datos de Salida** | - `token`: Cadena alfanumérica UUID<br/>- `success`: `true` / `false`<br/>- `message`: Mensaje descriptivo |

#### Flujo Básico (CU-01)
1. El usuario ingresa sus credenciales en el cliente (Consola, Escritorio, Web o Móvil) y solicita inicio de sesión.
2. El cliente empaqueta la solicitud y la transmite al servidor (vía HTTP POST JSON o SOAP Envelope).
3. El servidor recibe los datos y valida el usuario y contraseña contra las credenciales maestras del sistema (`MONSTER` / `MONSTER9`).
4. Al coincidir exitosamente, el servidor genera un identificador único de sesión (token UUID).
5. El servidor registra el token en su estructura de memoria volátil (SINBDD) con marca de tiempo.
6. El servidor retorna la respuesta confirmando el éxito con el token asignado.
7. El cliente almacena el token para asociarlo a las operaciones de conversión subsecuentes.

#### Flujos Alternativos (CU-01)
* **A1 (Credenciales Inválidas):** En el paso 3, si el usuario o contraseña no coinciden con `MONSTER`/`MONSTER9`, el servidor rechaza la solicitud retornando código 401 / Fault SOAP y no genera token.
* **A2 (Campos Vacíos o Nulos):** Si el cliente envía datos en blanco o incompletos, el servidor retorna HTTP 400 Bad Request exigiendo ambos parámetros.

---

### CU-02: Convertir Longitud

| Atributo | Especificación |
| :--- | :--- |
| **Identificador** | **CU-02** |
| **Nombre** | Convertir Longitud |
| **Descripción** | Permite a un usuario autenticado transformar un valor numérico de longitud entre las 5 unidades autorizadas del catálogo estricto: `metro`, `centimetro`, `pie`, `yarda` y `milla`. |
| **Actor(es)** | Usuario Final |
| **Meta** | Obtener el valor numérico equivalente en la unidad de destino deseada, formateado a 2 decimales con punto. |
| **Precondiciones** | 1. El usuario debe haber iniciado sesión exitosamente (**CU-01**) y contar con un token vigente.<br/>2. Los campos `from`, `to`, `value` y `token` deben estar presentes y válidos en la petición. |
| **Postcondiciones (Éxito)** | Retorna `success = true`, `result` con el cálculo matemático y mensaje de confirmación. |
| **Postcondiciones (Fallo)** | - Token inválido o ausente: HTTP 401 Unauthorized.<br/>- Unidad no reconocida: HTTP 400 Bad Request. |
| **Catálogo Estricto (5 unidades)** | `metro` (base: $1.0$), `centimetro` ($0.01$), `pie` ($0.3048$), `yarda` ($0.9144$), `milla` ($1609.34$). |
| **Restricción de Entrada** | Máximo 16 caracteres numéricos (`0-9` y `.`). |

#### Flujo Básico (CU-02)
1. El usuario selecciona la unidad de origen, la unidad de destino e ingresa el valor a convertir.
2. El cliente adjunta el token activo e invoca el servicio de cálculo de longitud.
3. El sistema invoca **CU-05 (Validar Token)**; se verifica que el token esté activo en la memoria RAM del servidor.
4. El sistema invoca **CU-06 (Validar Unidades)**; se comprueba que `from` y `to` pertenezcan al catálogo de longitud.
5. El sistema ejecuta el algoritmo de conversión mediante el patrón Strategy (`ConversorLongitudStrategy`):
   $$\text{valorEnMetros} = \text{valor} \times \text{factor}(\text{from})$$
   $$\text{resultado} = \frac{\text{valorEnMetros}}{\text{factor}(\text{to})}$$
6. El sistema formatea el resultado a dos decimales (`%.2f`) y lo devuelve al cliente.
7. El cliente presenta el resultado formateado en la interfaz de usuario.

#### Flujos Alternativos (CU-02)
* **A1 (Sesión Expirada / Token Inválido):** En el paso 3, si el token no existe o expiró, el sistema retorna HTTP 401 con mensaje `"Tu sesión expiró o el token no vale."`. El cliente redirige a la pantalla de login (**CU-01**).
* **A2 (Unidad No Reconocida):** En el paso 4, si se ingresa una unidad fuera del catálogo (ej. `kilómetro`), retorna HTTP 400 con mensaje `"Unidad de origen/destino no reconocida."`.

---

### CU-03: Convertir Masa

| Atributo | Especificación |
| :--- | :--- |
| **Identificador** | **CU-03** |
| **Nombre** | Convertir Masa |
| **Descripción** | Permite a un usuario autenticado transformar un valor de masa entre las 5 unidades autorizadas del catálogo estricto: `gramo`, `kilogramo`, `libra`, `onza` y `quintal`. |
| **Actor(es)** | Usuario Final |
| **Meta** | Obtener el valor numérico equivalente en la unidad de masa de destino, formateado a 2 decimales con punto. |
| **Precondiciones** | 1. Usuario autenticado (**CU-01**) con token activo en memoria.<br/>2. Parámetros de petición completos. |
| **Postcondiciones (Éxito)** | Retorna `success = true`, `result` numérico y mensaje `"Conversión exitosa."`. |
| **Postcondiciones (Fallo)** | Error 401 (token expirado) o Error 400 (unidad inválida). |
| **Catálogo Estricto (5 unidades)** | `kilogramo` (base: $1.0$), `gramo` ($0.001$), `libra` ($0.453592$), `onza` ($0.0283495$), `quintal` ($45.3592$). |
| **Restricción de Entrada** | Máximo 16 caracteres numéricos (`0-9` y `.`). |

#### Flujo Básico (CU-03)
1. El usuario selecciona unidades de masa (origen y destino) e introduce la cifra numérica.
2. El cliente despacha la petición incluyendo el token en la cabecera/cuerpo.
3. El sistema ejecuta **CU-05 (Validar Token)**. Token validado.
4. El sistema ejecuta **CU-06 (Validar Unidades)**. Unidades validadas.
5. El sistema procesa la conversión con `ConversorMasaStrategy` convirtiendo a la unidad base (kilogramo) y luego a la unidad de destino.
6. El sistema retorna la respuesta estructurada con el resultado a 2 decimales.
7. El cliente renderiza el valor en el panel de resultados.

---

### CU-04: Convertir Temperatura

| Atributo | Especificación |
| :--- | :--- |
| **Identificador** | **CU-04** |
| **Nombre** | Convertir Temperatura |
| **Descripción** | Permite a un usuario autenticado convertir una magnitud termométrica entre las 5 escalas admitidas: `celsius`, `fahrenheit`, `kelvin`, `newton` y `reaumur`. |
| **Actor(es)** | Usuario Final |
| **Meta** | Obtener la temperatura equivalente en la escala de destino. |
| **Precondiciones** | 1. Usuario con token válido (**CU-01**).<br/>2. Escalas origen/destino y valor numérico especificados. |
| **Postcondiciones (Éxito)** | Retorna `success = true`, `result` y mensaje de confirmación. |
| **Catálogo Estricto (5 escalas)** | `celsius` ($^\circ\text{C}$), `fahrenheit` ($^\circ\text{F}$), `kelvin` ($\text{K}$), `newton` ($^\circ\text{N}$), `reaumur` ($^\circ\text{Ré}$). |
| **Fórmulas de Cómputo (Base: Celsius)** | - Celsius $\rightarrow$ Fahrenheit: $F = (C \times \frac{9}{5}) + 32$<br/>- Celsius $\rightarrow$ Kelvin: $K = C + 273.15$<br/>- Celsius $\rightarrow$ Newton: $N = C \times \frac{33}{100}$<br/>- Celsius $\rightarrow$ Réaumur: $Ré = C \times \frac{4}{5}$ |

#### Flujo Básico (CU-04)
1. El usuario selecciona las escalas de temperatura origen/destino e introduce la temperatura.
2. El cliente emite la solicitud al servicio con el token.
3. El sistema ejecuta **CU-05 (Validar Token)** y valida la sesión.
4. El sistema ejecuta **CU-06 (Validar Unidades)** y verifica que las escalas correspondan a las 5 admitidas.
5. El sistema aplica las ecuaciones termodinámicas correspondientes a través de `ConversorTemperaturaStrategy`.
6. El sistema formatea a 2 decimales y devuelve la respuesta.
7. El cliente muestra el resultado en pantalla.

---

### CU-05: Validar Token (Caso de Uso Interno)

| Atributo | Especificación |
| :--- | :--- |
| **Identificador** | **CU-05** |
| **Nombre** | Validar Token |
| **Tipo** | Interno (`<<include>>` por CU-02, CU-03, CU-04) |
| **Descripción** | Intercepta cada solicitud de conversión para verificar que el token recibido exista en la colección de sesiones activas en memoria RAM del servidor y no haya caducado. |
| **Meta** | Autorizar el acceso al motor de cálculo o bloquear la solicitud con error de autorización (HTTP 401). |
| **Precondiciones** | La solicitud contiene un campo `token` o encabezado de autorización. |
| **Postcondiciones (Éxito)** | El token se encuentra en memoria; la ejecución continúa hacia la validación de unidades y cálculo. |
| **Postcondiciones (Fallo)** | El token es nulo, inválido o expiró; se retorna HTTP 401 Unauthorized y se extiende a **CU-01** para forzar nuevo login. |

---

### CU-06: Validar Unidades (Caso de Uso Interno)

| Atributo | Especificación |
| :--- | :--- |
| **Identificador** | **CU-06** |
| **Nombre** | Validar Unidades |
| **Tipo** | Interno (`<<include>>` por CU-02, CU-03, CU-04) |
| **Descripción** | Comprueba que las unidades `from` y `to` enviadas en la petición pertenezcan al catálogo oficial de exactamente 5 unidades de la magnitud solicitada. |
| **Meta** | Prevenir errores matemáticos y garantizar el cumplimiento estricto del catálogo cerrado. |
| **Precondiciones** | Parámetros `from` y `to` recibidos en la petición. |
| **Postcondiciones (Éxito)** | Ambas unidades son válidas; se procede a invocar la estrategia de cálculo correspondiente. |
| **Postcondiciones (Fallo)** | Retorna HTTP 400 Bad Request indicando si la unidad de origen o destino no es reconocida. |

---

## 5. Trazabilidad con Requisitos del Sistema (ERS / SRS)

| Caso de Uso (ECUD) | Requisito Funcional (ERS) | Requisito No Funcional (ERS) | Componente en Código |
| :--- | :--- | :--- | :--- |
| **CU-01: Iniciar Sesión** | **RQF-001** (Autenticar Cliente) | **RNF-005** (Seguridad y Autorización) | `AuthController` / `AuthWebService` |
| **CU-02: Convertir Longitud** | **RQF-002** (Convertir Longitud) | **RNF-001**, **RNF-002** | `ConversorLongitudStrategy` |
| **CU-03: Convertir Masa** | **RQF-003** (Convertir Masa) | **RNF-001**, **RNF-002** | `ConversorMasaStrategy` |
| **CU-04: Convertir Temperatura**| **RQF-004** (Convertir Temperatura) | **RNF-001**, **RNF-002** | `ConversorTemperaturaStrategy` |
| **CU-05: Validar Token** | **RQF-001** | **RNF-005** | `ServicioAuth` / Filtro de Sesión |
| **CU-06: Validar Unidades** | **RQF-002**, **RQF-003**, **RQF-004** | **RNF-001** | `ConversionUtil` / Enumeraciones |
