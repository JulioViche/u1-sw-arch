---
tipo: requisito
tags: [requisito, login, autenticacion]
---

# Autenticación (login)

> [!danger] Requisito obligatorio
> **Todo proyecto debe iniciar con un login.** Ninguna conversión se ejecuta sin
> autenticación previa. Aplica a las **cuatro variantes** y a los **cuatro clientes**
> (consola, escritorio, web y móvil).

## Credenciales de esta unidad (quemadas)

Para esta unidad las credenciales están **quemadas en el código** (hardcoded), no en base
de datos:

| Usuario | Contraseña |
| --- | --- |
| `MONSTER` | `MONSTER9` |

- Se escriben **literalmente en el código** como constantes (o archivo de configuración).
- No hay tabla de usuarios, ni hash, ni persistencia (el proyecto es [[vault/03 - Entregables/03 BDD|SINBDD]]).
- La comparación de credenciales es directa contra esos valores fijos.

## Flujo esperado

```mermaid
sequenceDiagram
    actor U as Usuario
    participant C as Cliente
    participant S as Servidor
    U->>C: ingresa usuario y contraseña
    C->>S: solicitar login
    S->>S: comparar con MONSTER / MONSTER9
    alt credenciales válidas
        S-->>C: OK + sesión/token
        C-->>U: acceso al conversor
    else credenciales inválidas
        S-->>C: error de autorización
        C-->>U: mensaje de credenciales inválidas
    end
```

## Reglas para agentes

1. **Primer caso de uso** de cada cliente: iniciar sesión.
2. Proteger el módulo de conversión: si no hay sesión válida, devolver error de autorización.
3. Mantener la misma credencial quemada en las cuatro variantes para coherencia.
4. Ante error de credenciales, mostrar un mensaje claro y **no** permitir continuar.

> [!note] Base de este requisito
> Proviene del caso de estudio del ejemplo de referencia
> (`TALLER CASOS DE USO CONVERSION UNIDADES.docx`): "El acceso al sistema está restringido
> por un único usuario... El acceso al módulo de conversión debe estar protegido por un
> mecanismo de autenticación."

> [!info] Relacionado
> - [[vault/03 - Entregables/04 Clientes y servidor]]
> - [[vault/02 - Variantes/RESTful vs SOAP]]
> - [[vault/05 - Referencia/Datos académicos]]
