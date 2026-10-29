---
tipo: arquitectura
tags: [requerimientos, decisiones, interfaz, precision, clientes]
---

# Requerimientos Adicionales y Criterios del Usuario

Este documento compila todos los **requerimientos, reglas y decisiones de diseño no formales** incorporados por el integrantes (**Julio Enrique Viche Castillo**, **Gabriel Omar Murillo Medina**) a lo largo del desarrollo. 

Aunque algunos de estos puntos no estaban desglosados en el documento formal de especificación original (ERS/SRS) de la cátedra, constituyen **directrices obligatorias** para mantener la uniformidad funcional y estética entre las cuatro variantes del conversor de unidades.

---

## 1. Precisión Visual, Separador Decimal y Formateo a 2 Decimales

> [!IMPORTANT]
> **Regla de presentación:** Todos los clientes (Consola `CLICON`, Escritorio `CLIESC`, Web `CLIWEB` y Móvil `CLIMOV`) deben presentar los resultados y valores de conversión formateados a **exactamente 2 decimales** utilizando el **punto decimal (`.`)** como separador oficial.

* **Símbolo Separador Decimal (Punto `.` vs Coma `,`):**
  - **Presentación oficial (Salida):** Se utiliza estrictamente el **punto decimal (`.`)** mediante `Locale.US` en todos los clientes (ej. `100.50 m`), evitando comas que causen discrepancias con la sintaxis de código y JavaScript.
  - **Límite de longitud exclusivo en el valor a convertir (16 caracteres):** Se impone un límite estricto de **máximo 16 caracteres** de escritura exclusivamente en el campo del valor numérico a convertir. En Swing se bloquea el tipeo/pegado mediante `DocumentFilter` al alcanzar 16 caracteres, en Web se aplica `maxlength="16"` y `.slice(0, 16)`, y en Consola y Servlet se valida el tamaño impidiendo valores superiores a 16 caracteres. Los demás campos (como credenciales de acceso) no tienen esta restricción.
  - **Restricción estricta de caracteres:** En el campo de captura del valor numérico **se admiten únicamente dígitos (`0-9`), el punto decimal (`.`) y el signo negativo (`-`) exclusivamente al inicio de la cifra (`^-?\d+(\.\d+)?$`)**. No se admiten comas (`,`), letras, signos negativos intermedios ni símbolos especiales. En las interfaces gráficas y web se bloquean activamente las teclas no válidas en tiempo real (`DocumentFilter`, `onkeypress`, `oninput`), y tanto la consola como el backend validan estrictamente mediante la expresión regular `^-?\d+(\.\d+)?$`.
* **Capa de Presentación (Clientes):**
  - Consola Java/.NET: `System.out.printf(Locale.US, "%.2f", ...)` / `"{0:F2}"`.
  - Escritorio Swing/WPF: `String.format(Locale.US, "%.2f %s = %.2f %s", ...)` / `"{0:N2}"`.
  - Web JSP/HTML/JS: `String.format(Locale.US, "%.2f", ...)` y `.toFixed(2)` en JavaScript.
  - Móvil Android: `String.format(Locale.US, "%.2f", ...)`.
* **Capa de Negocio y Red (Servidor):**
  - El servidor web (SOAP / REST) **no trunca ni redondea prematuramente** los números; procesa y transfiere los cálculos con precisión de punto flotante completa (`double`) para garantizar máxima fidelidad matemática en el canal de comunicación.
* **Justificación:**
  - Evitar desbordamientos visuales o textos antiestéticos provocados por decimales periódicos infinitos (ej. $0.3333333333333333$) o notación científica ininteligible en las pantallas, manteniendo uniformidad internacional en el separador decimal sin frustrar al usuario al teclear.

---

## 2. Catálogo Estricto de Unidades ("Ni una más, ni una menos")

> [!WARNING]
> Ningún cliente ni servicio debe agregar unidades ajenas a la especificación oficial, ni omitir ninguna de las 5 requeridas por categoría.

El sistema debe operar con un catálogo cerrado de exactamente **5 unidades por magnitud física** (15 unidades en total):

| Magnitud | Unidades Permitidas (Exactamente 5) | Unidades Prohibidas / Excluidas |
| :--- | :--- | :--- |
| **Longitud** | `metro`, `centimetro`, `pie`, `yarda`, `milla` | *Kilómetro, milímetro, pulgada* (eliminadas) |
| **Masa** | `gramo`, `kilogramo`, `libra`, `onza`, `quintal` | *Miligramo, tonelada* (eliminadas; quintal estandarizado a $45.3592\text{ kg}$) |
| **Temperatura** | `celsius`, `fahrenheit`, `kelvin`, `newton`, `reaumur` | *Rankine* u otras escalas |

---

## 3. Autenticación Obligatoria Centralizada

* **Acceso bloqueado sin login:** Ningún usuario puede acceder al menú de conversión ni invocar el servicio de cálculo sin haberse autenticado con éxito.
* **Credenciales fijas en código (Hardcoded):**
  - **Usuario:** `MONSTER`
  - **Contraseña:** `MONSTER9`
* **Manejo de Sesión SINBDD:**
  - El servidor genera un token pseudo-UUID en memoria con tiempo de expiración (5 minutos).
  - Cada petición de conversión exige la validación previa del token de sesión.

---

## 4. Ergonomía e Interacción de Usuario en Clientes Gráficos

Para maximizar la usabilidad y coherencia visual en las interfaces (`CLIESC`, `CLIWEB`, `CLIMOV`), se definen los siguientes criterios:

1. **Navegación Segmentada:**
   - La selección de magnitud física se realiza mediante botones o pestañas segmentadas (`Longitud | Masa | Temperatura`), actualizando dinámicamente los selectores de unidades sin recargar toda la ventana.
2. **Botón de Intercambio (Swap ⇄):**
   - Se debe incluir un botón interactivo que intercambie rápidamente la unidad de origen con la de destino sin obligar al usuario a reconfigurar ambos desplegables manualmente.
3. **Tarjeta de Resultados (Dashboard Card):**
   - El resultado debe destacarse en un panel o tarjeta visual con color distintivo (verde menta para éxito, carmesí para error).
4. **Acción Rápida de Copiado:**
   - En clientes visuales (Swing y Web) se provee un botón para copiar el texto formateado (`%.2f`) directamente al portapapeles del sistema operativo.
5. **Visibilidad de Sesión:**
   - En la cabecera o pie de interfaz debe ser visible el usuario logueado (`MONSTER`) y un resumen abreviado del token activo.

---

## 5. Estándares de Documentación y Vault

* **Estructura en inglés kebab-case:** Todas las carpetas y archivos dentro del directorio [`vault/`](file:///c:/Users/Usuario01/Documents/JULIO%20VICHE/u1-arch/vault/) deben nombrarse en inglés y minúsculas con guiones medios (ej. `ti-1-1-soap-java`, `technical-manual.md`, `additional-requirements.md`).
* **Idioma de redacción:** Todo el contenido técnico, manuales y casos de uso deben estar redactados en **español formal**.
* **Trazabilidad:** Cada cambio en el contrato de servicio o comportamiento de los clientes debe quedar reflejado en su respectivo manual y en este registro.
