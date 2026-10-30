---
tipo: plantilla
tags: [plantilla, ecud, casos-de-uso, uml]
---

# Plantilla Maestra: Especificación de Casos de Uso Detallados (ECUD)

Esta plantilla define la estructura formal reutilizable para generar el documento `ECUD_<PROTOCOLO>_<PLATAFORMA>_SINBDD_<GRUPO>.docx`.

---

## Metadatos del Documento

| Campo | Valor Parametrizado |
| :--- | :--- |
| **Institución** | UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE |
| **Carrera / Departamento** | Ingeniería en Tecnologías de la Información / DECIM |
| **Asignatura** | Arquitectura de Software |
| **Tarea** | {{CODIGO_TAREA}} (Ej. TI.1.1) |
| **NRC** | {{NRC}} (Ej. 36884) |
| **Grupo** | {{GRUPO}} (Ej. GR09) |
| **Autores / Integrantes** | {{INTEGRANTES}} (Ej. Julio Enrique Viche Castillo, Gabriel Omar Murillo Medina) |
| **Protocolo / Plataforma** | {{PROTOCOLO}} / {{PLATAFORMA}} |
| **Base de Datos** | Sin Base de Datos (`SINBDD` - Credenciales y datos en memoria) |

---

## Fichas de Casos de Uso Detallados

### CU-01: Iniciar Sesión en el Sistema
| Elemento | Descripción |
| :--- | :--- |
| **Identificador** | `CU-01` |
| **Nombre** | Iniciar Sesión / Autenticar Usuario |
| **Actor Principal** | Usuario Final (Consola, Escritorio, Web, Móvil) |
| **Propósito** | Validar las credenciales fijas y otorgar un token de sesión para autorizar conversiones. |
| **Precondiciones** | Servidor web ({{PROTOCOLO}}) en ejecución y accesible en la red. |
| **Entradas** | `usuario` (Texto), `clave` (Texto). |
| **Flujo Principal** | 1. El usuario ingresa usuario y contraseña en el cliente.<br>2. El cliente envía la solicitud al endpoint de autenticación.<br>3. El servidor compara contra las credenciales maestras (`MONSTER` / `MONSTER9`).<br>4. Si coinciden, genera un token GUID/UUID y lo retorna con código de éxito.<br>5. El cliente almacena el token y habilita la vista de conversión. |
| **Flujos Alternos** | **3a. Credenciales inválidas:** El servidor retorna `exito=false` con mensaje descriptivo; el cliente muestra error y bloquea la navegación.<br>**2a. Servidor inalcanzable:** El cliente activa fallback automático entre URLs candidatas (Emulador / Hotspot) o reporta fallo de red tras timeout. |
| **Postcondiciones** | Sesión iniciada con token activo en memoria del cliente. |

---

### CU-02: Convertir Unidades de Longitud
| Elemento | Descripción |
| :--- | :--- |
| **Identificador** | `CU-02` |
| **Nombre** | Convertir Magnitud Longitud |
| **Actor Principal** | Usuario Autenticado |
| **Propósito** | Realizar la transformación numérica entre dos unidades del catálogo estricto de longitud. |
| **Catálogo Estricto** | Exactamente 5 unidades: `Metro`, `Centímetro`, `Pie`, `Yarda`, `Milla`. |
| **Restricciones de Entrada** | Formato numérico regex `^-?\d+(\.\d+)?$`, máximo 16 caracteres. |
| **Flujo Principal** | 1. El usuario selecciona la magnitud Longitud.<br>2. Elige unidad de origen y destino.<br>3. Digita el valor numérico.<br>4. El cliente envía la solicitud adjuntando el token activo.<br>5. El servidor valida el token, convierte el valor al factor base (metro) y calcula el destino.<br>6. Retorna el resultado redondeado a 2 decimales (`%.2f` / `.toFixed(2)`). |
| **Flujos Alternos** | **5a. Token inválido/ausente:** Retorna error de autenticación 401/SOAP Fault.<br>**3a. Entrada no numérica o >16 caracteres:** Validación local bloquea el envío. |
| **Postcondiciones** | Resultado presentado en pantalla formateado a 2 decimales con punto (`.`). |

---

### CU-03: Convertir Unidades de Masa
| Elemento | Descripción |
| :--- | :--- |
| **Identificador** | `CU-03` |
| **Nombre** | Convertir Magnitud Masa |
| **Actor Principal** | Usuario Autenticado |
| **Propósito** | Transformar valores numéricos de masa entre unidades oficiales. |
| **Catálogo Estricto** | Exactamente 5 unidades: `Gramo`, `Kilogramo`, `Libra`, `Onza`, `Quintal`. |
| **Flujo Principal** | Idéntico a CU-02 utilizando el factor base estándar (gramo o kilogramo). |

---

### CU-04: Convertir Unidades de Temperatura
| Elemento | Descripción |
| :--- | :--- |
| **Identificador** | `CU-04` |
| **Nombre** | Convertir Magnitud Temperatura |
| **Actor Principal** | Usuario Autenticado |
| **Propósito** | Transformar escalas térmicas considerando fórmulas algebraicas no proporcionales y valores negativos. |
| **Catálogo Estricto** | Exactamente 5 escalas: `Celsius`, `Fahrenheit`, `Kelvin`, `Newton`, `Réaumur`. |
| **Particularidad** | Permite explícitamente el signo negativo (`-`) al inicio de la cifra. |
| **Flujo Principal** | Conversión directa o mediante escala Kelvin/Celsius como pivote térmico central. |
