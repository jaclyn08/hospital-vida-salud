# Hospital Vida Salud - Microservicio de Citas Médicas (`citas-service`)

## 1. Descripción del Proyecto
Este microservicio forma parte del ecosistema de arquitectura distribuida para el **Hospital Vida Salud** (Evaluación T2 - Desarrollo de Aplicaciones Web II - CIBERTEC). Está diseñado con un enfoque 100% reactivo no bloqueante, implementando la gestión y programación de citas médicas, validación síncrona con el servicio de pacientes y publicación asíncrona de eventos a RabbitMQ.

## 2. Tecnologías Empleadas
- **Java 21 / 25**
- **Spring Boot 3.3.4**
- **Spring WebFlux (Reactor - Mono / Flux)**: Procesamiento asíncrono y reactivo no bloqueante.
- **Spring Data R2DBC**: Persistencia reactiva a base de datos relacional (PostgreSQL / H2).
- **RabbitMQ (Spring AMQP)**: Comunicación orientada a eventos para notificaciones y auditoría (`CitaConfirmadaEvent`).
- **Spring Cloud Eureka Client**: Registro y descubrimiento dinámico de microservicios.
- **JUnit 5, Mockito & WebTestClient**: Pruebas unitarias y de integración automatizadas.
- **GitHub Actions**: Pipeline de Integración Continua (CI).

## 3. Arquitectura y Responsabilidades
- **Endpoints reactivos**: Expone endpoints bajo `/api/citas/**`.
- **Comunicación Síncrona**: Consulta vía `WebClient` al microservicio de Pacientes para verificar la existencia del paciente antes de agendar la cita.
- **Comunicación Asíncrona**: Al cambiar el estado de la cita a `CONFIRMADA`, se despacha un evento a RabbitMQ hacia el Exchange `hospital.direct.exchange` con routing key `cita.confirmada.key`. Las colas de `notificaciones` y `auditoria` consumen dicho evento para notificar al paciente y registrar trazabilidad.
- **Control de Estados y Transiciones**:
  - `PENDIENTE` -> `CONFIRMADA` -> `ATENDIDA`
  - Transición a `CANCELADA` desde estados activos.
  - No se permite alterar citas canceladas ni marcar como atendida una cita sin previa confirmación.

## 4. Endpoints Principales
| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/citas` | Registra una nueva cita médica (valida paciente y horarios) |
| `GET` | `/api/citas` | Retorna el flujo reactivo (`Flux`) de todas las citas |
| `GET` | `/api/citas/{id}` | Consulta una cita por ID (`Mono`) |
| `GET` | `/api/citas/codigo/{codigoCita}` | Consulta una cita por código único |
| `PATCH` | `/api/citas/{id}/estado` | Actualiza estado (publica evento al confirmar) |
| `DELETE` | `/api/citas/{id}` | Cancela la cita médica |

## 5. Instrucciones de Ejecución Local
### Prerrequisitos
- JDK 21 o superior
- Maven Wrapper (incluido `./mvnw`)

### Ejecución de Pruebas
```bash
./mvnw clean test
```

### Ejecución del Microservicio
```bash
./mvnw spring-boot:run
```
El microservicio iniciará en el puerto `8082`.
