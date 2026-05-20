# MS-Reportes — Microservicio de Reportes

## Resumen
MS-Reportes es un microservicio Spring Boot que gestiona reportes relacionados con mascotas (por ejemplo: mascotas perdidas o vistas). Expone una API REST para crear, consultar, actualizar y eliminar reportes; persiste los datos en PostgreSQL y publica eventos en RabbitMQ para notificar a otros microservicios (por ejemplo, servicio de coincidencias).

## Responsabilidades principales
- Recibir y validar reportes enviados por clientes (aplicación web).
- Enriquecer la información de ubicación usando LocationIQ cuando se entrega sólo la dirección.
- Persistir reportes en una base de datos PostgreSQL mediante Spring Data JPA.
- Publicar eventos de nuevo reporte en RabbitMQ para integración con otros servicios.

## Flujo de ejecución (petición típica)
1. Cliente realiza `POST /reportes` con un `ReporteRequestDTO`.
2. `ReporteController` delega en `ReporteService`.
3. `ReporteServiceImpl` arma la entidad `ReporteModel` y, si es necesario, llama a `GeoService.obtenerCoordenadas(direccion)` para obtener lat/lon desde LocationIQ.
4. Se guarda el `ReporteModel` en la base de datos via `ReporteRepository` (Spring Data JPA).
5. Tras persistir, se convierte a `ReporteResponseDTO` y se publica el evento usando `ReportePublisher` en la cola `reporte.creado.queue` de RabbitMQ.
6. El endpoint retorna el `ReporteResponseDTO` con el estado HTTP adecuado (ej. `201 Created`).

## Endpoints
- `POST /reportes` — Crear un nuevo reporte. Cuerpo: `ReporteRequestDTO`.
- `GET /reportes` — Listar todos los reportes. Retorna lista de `ReporteResponseDTO`.
- `GET /reportes/{id}` — Obtener reporte por ID.
- `PUT /reportes/{id}` — Actualizar un reporte existente.
- `DELETE /reportes/{id}` — Eliminar un reporte.

Ejemplo mínimo de body (crear):
```
{
	"idUsuario": 123,
	"tipoReporte": "PERDIDO",
	"tipoMascota": "Perro",
	"nombreMascota": "Firulais",
	"color": "marrón",
	"tamano": "MEDIANO",
	"raza": "Labrador",
	"fotoMascota": "https://.../foto.jpg",
	"descripcion": "Se perdió en el parque X",
	"direccion": "Av. Siempre Viva 123, Ciudad",
	"coordenadas": "", 
	"sexo": "M",
	"estado": "ACTIVO"
}
```

## Modelo de datos (resumen)
- Entidad principal: `ReporteModel` (tabla `reportes`). Campos clave: `idReporte`, `idUsuario`, `tipoReporte` (ENUM: PERDIDO/VISTO), `tipoMascota`, `nombreMascota`, `raza`, `tamano` (ENUM: PEQUENO/MEDIANO/GRANDE), `direccion`, `coordenadas`, `descripcion`, `fotoMascota`, `estado`, `sexo`.
- Repositorio: `ReporteRepository` extiende `JpaRepository<ReporteModel, Long>` y contiene consultas personalizadas (ej. `findReportesByUsuarioId`).

## Integraciones externas
- LocationIQ: usado por `GeoService` para convertir una dirección en coordenadas (lat,lon). La API key se configura en `application.properties` bajo `locationiq.api-key`.
- PostgreSQL: persistencia principal (configurada en `application.properties`).
- RabbitMQ: transporte de eventos; la app publica objetos serializados JSON en la cola `reporte.creado.queue` usando `RabbitTemplate` y `Jackson2JsonMessageConverter`.

## Arquitectura y patrones de diseño
- Arquitectura en capas (Layered Architecture): Controller → Service → Repository.
- Inyección de dependencias (constructor-based DI) proveniente de Spring — facilita testeo y separación de responsabilidades.
- DTO Pattern: `ReporteRequestDTO` y `ReporteResponseDTO` para separar la representación externa de la entidad persistida.
- Publicador de eventos (Event-driven): `ReportePublisher` publica eventos en RabbitMQ para comunicación asíncrona entre microservicios.
- Uso de transacciones en capa de servicio (`@Transactional`) para garantizar consistencia al persistir y publicar.
- Uso de `Enum` para valores limitados (`TipoReporte`, `TamanoMascota`) para mejorar validación y estabilidad de datos.

## Manejo de errores y validaciones
- Validaciones básicas aplicadas mediante `@Valid` en el controlador y validadores de Spring (dependencias incluidas en `pom.xml`).
- Actualmente se utilizan excepciones runtime simples (p.ej. `RuntimeException`) para notificar errores; se recomienda implementar un `@ControllerAdvice` centralizado para manejar errores y devolver responses HTTP consistentes.

## Dependencias principales
- Spring Boot Web, Data JPA, AMQP (RabbitMQ), Validation, Spring Integration (algunas dependencias de integración están presentes).
- PostgreSQL JDBC driver y Lombok para reducir boilerplate.
- `springdoc-openapi` para documentación automática (UI Swagger disponible si se habilita).

## Configuración y despliegue
- Variables principales en `src/main/resources/application.properties`:
	- `locationiq.api-key` — API key de LocationIQ
	- `spring.datasource.*` — URL, usuario y contraseña de PostgreSQL
	- `spring.rabbitmq.*` — host, puerto y credenciales de RabbitMQ

- Docker: el proyecto incluye `dockerfile` y `docker-compose.yml` (en la carpeta `reporte/`) para levantar contenedores de PostgreSQL y RabbitMQ junto al servicio.
- Ejecutar localmente (maven):
```
./mvnw spring-boot:run
```

O con Docker Compose (desde la carpeta que contiene `docker-compose.yml`):
```
docker compose up --build
```

## Archivos clave
- Código principal: `src/main/java/cl/sanosysalvos/reporte` (controladores, servicios, repositorios, modelos, mensajería).
- Config: `src/main/resources/application.properties`.
- Docker: `dockerfile`, `docker-compose.yml` (en `reporte/`).

