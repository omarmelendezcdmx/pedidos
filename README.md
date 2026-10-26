# pedidos-api

API REST (Java 21, Spring Boot 3, MongoDB) para gestionar los pedidos realizados desde la aplicación y el sitio web. Consolida dos servicios externos (`/pedidos` e `/items`), persiste la información en MongoDB y expone CRUD de clientes, entregas y pedidos, además de una búsqueda type-ahead flexible.


# arquitectura propuesta (hexagonal)

```
Clientes / REST
        ↓
Inbound Adapters
        ↓
Inbound Ports
        ↓
Application / Domain
        ↓
Outbound Ports
        ↓
Outbound Adapters
        ↓
MOngoDB / APIs externas 
```

La idea es que la lógica de negocio sea independiente de tecnologías externas. El negocio esta en la parte central (Domain) y las tecnologías externas se conectan mediante puertos y adaptadores. 
A mi entender esto hace a la aplicación más fácil de mantener, se cumple uno de los principios de SOLID (DIP: Dependency Inversion Principle) y es más fácil de testear así como de cambiar su infraestructura

**Regla de dependencias:** `infrastructure → application → domain`. Nunca al revés. Se verifica
automáticamente en cada build con ArchUnit (`ArquitecturaTest`).

### Patrones de diseño aplicados

| Patrón | Dónde |
|---|---|
| Ports & Adapters (Hexagonal) | Estructura general del proyecto |
| Repository | Puertos `*Repositorio` con adaptadores MongoDB |
| Command | `ClienteCommand`, `PedidoCommand`, `EntregaCommand` |

## Pruebas unitarias

JUnit 5, AssertJ, Mockito

## Documentación de código

Javadoc en todos los tipos y métodos públicos (tal como lo pide checkstyle) 
OpenAPI/Swagger

## Checkstyle

`config/checkstyle/checkstyle.xml`; corre en `mvn validate` y rompe el build si hay violaciones


## Instalación de tools en windows para desarrollo 

```powerShell (admin)
winget install EclipseAdoptium.Temurin.21.JDK
winget install Git.Git
winget install OpenJS.NodeJS.LTS
winget install Microsoft.VisualStudioCode
winget install JetBrains.IntelliJIDEA.Community
winget install MongoDB.Server
winget install MongoDB.Shell
winget install MongoDB.Compass.Full
```


## Ejecutar en local

Requisitos: JDK 21 y MongoDB.



```terminal
mvn spring-boot:run
```

| Variable | Default | Descripción |
|---|---|---|
| `MONGODB_URI` | `mongodb://localhost:27017/pedidos_db` | Conexión a MongoDB |
| `CORS_ORIGINS` | `http://localhost:4200` | Orígenes permitidos (Angular) |
| `PORT` | `8080` | Puerto  |



Documentación interactiva: http://localhost:8080/swagger-ui.html (especificación en `/v3/api-docs`).

## Endpoints

| API | Método y ruta |
|---|---|
| Sincronización | `POST /api/sync` |
| Clientes | `POST/GET /api/clientes`, `GET/PUT/DELETE /api/clientes/{userId}`, `GET /api/clientes/{userId}/pedidos` |
| Entregas | `POST/GET /api/entregas`, `GET/PUT/DELETE /api/entregas/{id}` |
| Pedidos | `POST/GET /api/pedidos`, `GET/PUT/DELETE /api/pedidos/{orderRef}` |
| Búsqueda | `GET /api/busqueda/pedidos`, `GET /api/busqueda/sugerencias` |

### Flujo de prueba

```bash
curl -X POST localhost:8080/api/clientes -H "Content-Type: application/json" -d '{
  "userId":"75c97531-abf5-4524-8107-90aa48d08efc",
  "nombre":"Ana","apellidoPaterno":"López","apellidoMaterno":"Ruiz","email":"ana@correo.com"}'

curl -X POST localhost:8080/api/sync
curl localhost:8080/api/clientes/75c97531-abf5-4524-8107-90aa48d08efc     # orders: 3 pedidos
```

### Búsqueda type-ahead

```bash
curl "localhost:8080/api/busqueda/pedidos?displayName=pantalon levis"      # sin acentos ni apóstrofo
curl "localhost:8080/api/busqueda/pedidos?displayName=laptp lenovo"        # error de ortografía
curl "localhost:8080/api/busqueda/pedidos?storeName=GALERIAS, toluca"      # mayúsculas y comas
curl "localhost:8080/api/busqueda/pedidos?orderRef=30100916"               # prefijo
curl "localhost:8080/api/busqueda/pedidos?orderStatus=2025-12"             # fecha estimada
curl "localhost:8080/api/busqueda/sugerencias?campo=displayName&q=pant"    # autocompletado
```

Reglas: se ignoran mayúsculas, acentos, comas y apóstrofos; cada palabra coincide por prefijo; se toleran
errores mínimos de ortografía en texto (no en `orderRef` ni fechas); `playstation` encuentra `Play Station`.
Con `displayName` solo se devuelven los items que coinciden.

## Calidad

```bash
mvn clean verify            # Checkstyle + pruebas unitarias + cobertura
```

- Reporte de cobertura: `target/site/jacoco/index.html`
- Reglas de estilo: `config/checkstyle/checkstyle.xml` (saltar temporalmente con `-Dcheckstyle.skip`)
- Integración continua: `.github/workflows/ci.yml` ejecuta `mvn verify` en cada push y pull request

## Autor

Omar Meléndez López
https://www.linkedin.com/in/omarmelendezlopez/
