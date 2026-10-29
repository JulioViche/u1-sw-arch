# 03. BDD — Base de Datos

Esta carpeta se mantiene sin scripts SQL, esquemas ni migraciones debido a que este proyecto corresponde estrictamente a la modalidad **SINBDD (Sin Base de Datos)**.

### Justificación Técnica:
- **Sin Persistencia:** No se utiliza ninguna base de datos (relacional o NoSQL) en la arquitectura de la solución.
- **Autenticación en Memoria:** Las credenciales quemadas (MONSTER / MONSTER9) y tokens de sesión se validan en memoria volátil.
- **Lógica de Negocio:** La conversión de magnitudes y unidades se gestiona íntegramente en memoria en el servidor.
