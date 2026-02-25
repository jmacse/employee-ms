# Employee Microservice

Microservicio REST para la **gestión de empleados**, desarrollado con **Quarkus 3** y **Java 17**, siguiendo los principios de **arquitectura hexagonal** (Ports & Adapters).

---

## Tabla de contenido

- [Descripción](#descripción)
- [Features](#features)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Requisitos previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración de properties](#configuración-de-properties)
- [Ejecución local](#ejecución-local)
- [Endpoints disponibles](#endpoints-disponibles)
- [Pruebas con Postman](#pruebas-con-postman)
- [Health Checks](#health-checks)
- [Documentación OpenAPI / Swagger](#documentación-openapi--swagger)

---

## Descripción

`employee-ms` es un microservicio que expone una API REST para crear, consultar, actualizar y eliminar empleados. Aplica validaciones de dominio, cálculo automático de edad a partir de la fecha de nacimiento, gestión del estado activo/inactivo, y búsqueda por nombre. Toda la lógica de negocio reside en el dominio, desacoplada de la infraestructura.

---

## Features

| Feature | Descripción |
|---|---|
| **CRUD de empleados** | Crear, listar, obtener por ID, actualizar y eliminar empleados. |
| **Creación en lote** | El endpoint `POST /employees` acepta una lista de empleados en una sola petición. |
| **Búsqueda por nombre** | Búsqueda parcial por nombre o apellido con `GET /employees/search?name=`. |
| **Cálculo automático de edad** | La edad se calcula automáticamente a partir de la fecha de nacimiento en cada operación. |
| **Nombre completo** | El campo `fullName` se compone dinámicamente concatenando nombre, segundo nombre y apellidos. |
| **Gestión de estado** | Los empleados pueden ser activados o desactivados; el dominio valida transiciones inválidas. |
| **Promoción de cargo** | Actualizar el `position` de un empleado valida que el nuevo cargo sea diferente al actual. |
| **Validaciones de dominio** | Campos obligatorios, formato de fecha `dd-MM-yyyy`, y reglas de negocio encapsuladas en el modelo `Employee`. |
| **Manejo global de errores** | `GlobalExceptionMapper` devuelve respuestas estructuradas con códigos HTTP apropiados (`400`, `404`, `409`, `500`). |
| **Health Checks** | Liveness y Readiness checks con datos de contexto (total de empleados, estado de BD). |
| **Documentación OpenAPI** | Swagger UI integrado, disponible en `/swagger-ui`. |
| **Logging de requests** | `LoggingFilter` registra método, path y tiempo de respuesta de cada petición. |
| **Base de datos Oracle** | Persistencia con Hibernate ORM + Panache sobre Oracle Database Free. |
| **Docker Compose** | Levanta Oracle DB lista para usar en entorno local con un solo comando. |

---

## Arquitectura

```
src/main/java/com/jmacse/ms/employee/
├── domain/                  # Núcleo del negocio (sin dependencias externas)
│   ├── model/               # Entidad Employee, enum Gender
│   ├── exception/           # Excepciones de dominio
│   └── repository/          # Interfaces (puertos de salida)
├── application/             # Casos de uso y DTOs
│   ├── usecase/             # EmployeeCommandUseCase, EmployeeQueryUseCase
│   ├── dto/                 # Request / Response records
│   └── EmployeeService.java # Implementación de los casos de uso
├── infrastructure/          # Adaptadores (entrada y salida)
│   ├── adapter/             # EmployeeResource (REST), LoggingFilter, GlobalExceptionMapper
│   ├── persistence/         # EmployeeEntity, EmployeeRepositoryAdapter, EmployeeMapper
│   └── health/              # ApplicationHealthCheck, DatabaseHealthCheck
└── shared/                  # Constantes compartidas (DateConstants)
```

---

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Quarkus | 3.27.2 |
| Hibernate ORM + Panache | BOM Quarkus |
| Oracle JDBC | BOM Quarkus |
| SmallRye OpenAPI | BOM Quarkus |
| SmallRye Health | BOM Quarkus |
| Hibernate Validator | BOM Quarkus |
| JUnit 5 + REST Assured | BOM Quarkus |
| Docker / Docker Compose | >= 24 |

---

## Requisitos previos

- **Java 17** o superior instalado y en el `PATH`.
- **Maven 3.9+** (o usar el wrapper incluido `./mvnw`).
- **Docker** y **Docker Compose** para levantar Oracle DB localmente.
- (Opcional) **Postman** para ejecutar la colección de pruebas.

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd employee-ms
```

### 2. Levantar la base de datos Oracle con Docker

```bash
docker-compose up -d
```

> ⚠️ La primera vez que se descarga la imagen de Oracle puede tardar varios minutos.  
> Espera a que el healthcheck reporte `healthy` antes de iniciar la aplicación:

```bash
docker ps   # verifica que la columna STATUS muestre "(healthy)"
```

Credenciales configuradas por defecto:

| Campo | Valor |
|---|---|
| Usuario | `system` |
| Contraseña | `Oracle123` |
| SID / Service | `FREEPDB1` |
| Puerto | `1521` |

---

## Configuración de properties

El archivo de configuración principal es `src/main/resources/application.properties`.

### Servidor HTTP

| Property | Valor por defecto | Descripción |
|---|---|---|
| `quarkus.http.port` | `8081` | Puerto en el que escucha la aplicación. |

### Datasource (Oracle)

| Property | Valor por defecto | Descripción |
|---|---|---|
| `quarkus.datasource.db-kind` | `oracle` | Tipo de base de datos. |
| `quarkus.datasource.username` | `system` | Usuario de la base de datos. |
| `quarkus.datasource.password` | `Oracle123` | Contraseña de la base de datos. |
| `quarkus.datasource.jdbc.url` | `jdbc:oracle:thin:@localhost:1521/FREEPDB1` | URL de conexión JDBC. |

### Hibernate ORM

| Property | Valor por defecto | Descripción |
|---|---|---|
| `quarkus.hibernate-orm.schema-management.strategy` | `update` | Estrategia de gestión del esquema (`update`, `create`, `drop-and-create`, `validate`). |
| `quarkus.hibernate-orm.log.sql` | `true` | Habilita el log de sentencias SQL en consola. |
| `quarkus.hibernate-orm.log.format-sql` | `true` | Formatea el SQL en el log para mejor legibilidad. |
| `quarkus.hibernate-orm.jdbc.statement-fetch-size` | `50` | Número de filas recuperadas por fetch en las consultas. |

### OpenAPI / Swagger UI

| Property | Valor por defecto | Descripción |
|---|---|---|
| `quarkus.smallrye-openapi.info-title` | `Employee Microservice API` | Título de la especificación OpenAPI. |
| `quarkus.smallrye-openapi.info-version` | `1.0.0` | Versión de la API en la especificación. |
| `quarkus.smallrye-openapi.info-description` | _(descripción)_ | Descripción de la API en la especificación. |
| `quarkus.swagger-ui.always-include` | `true` | Incluye Swagger UI incluso en el perfil `prod`. |
| `quarkus.swagger-ui.path` | `/swagger-ui` | Ruta donde se sirve Swagger UI. |

### Health Checks

| Property | Valor por defecto | Descripción |
|---|---|---|
| `quarkus.smallrye-health.ui.always-include` | `true` | Incluye la UI de Health incluso en `prod`. |
| `quarkus.smallrye-health.root-path` | `/health` | Ruta raíz de los health checks. |
| `quarkus.smallrye-health.liveness-path` | `/health/live` | Ruta del liveness check. |
| `quarkus.smallrye-health.readiness-path` | `/health/ready` | Ruta del readiness check. |

### Logging

| Property | Valor por defecto | Descripción |
|---|---|---|
| `quarkus.log.console.format` | `%d{yyyy-MM-dd HH:mm:ss} ...` | Formato de los mensajes en consola. |
| `quarkus.log.console.level` | `INFO` | Nivel mínimo de log para la consola. |
| `quarkus.log.category."com.jmacse.ms".level` | `DEBUG` | Nivel de log específico para las clases del microservicio. |

---

## Ejecución local

### Modo desarrollo (hot reload)

```bash
./mvnw quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8081`

### Compilar y ejecutar el JAR

```bash
# Compilar
./mvnw clean package -DskipTests

# Ejecutar
java -jar target/quarkus-app/quarkus-run.jar
```

### Compilar imagen nativa (requiere GraalVM)

```bash
./mvnw clean package -Pnative
```

### Ejecutar pruebas unitarias

```bash
./mvnw test
```

---

## Endpoints disponibles

Base URL: `http://localhost:8081`

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/employees` | Lista todos los empleados. |
| `GET` | `/employees/{id}` | Obtiene un empleado por UUID. |
| `POST` | `/employees` | Crea uno o más empleados (acepta array). |
| `PUT` | `/employees/{id}` | Actualiza `position`, `birthDate` y/o `active`. |
| `DELETE` | `/employees/{id}` | Elimina un empleado por UUID. |
| `GET` | `/employees/search?name=` | Búsqueda parcial por nombre o apellido. |

### Ejemplo: Crear empleados

```bash
curl -X POST http://localhost:8081/employees \
  -H "Content-Type: application/json" \
  -d '[
    {
      "firstName": "Jorge",
      "middleName": "Luis",
      "paternalLastName": "Martinez",
      "maternalLastName": "Cruz",
      "birthDate": "15-06-1990",
      "gender": "MALE",
      "position": "Software Engineer"
    }
  ]'
```

> **Formato de fecha:** `dd-MM-yyyy`  
> **Valores de género:** `MALE`, `FEMALE`, `OTHER`

---

## Pruebas con Postman

La carpeta `postman/` contiene una colección y un environment listos para importar:

```
postman/
├── employee-ms.collection.json   # Colección con todos los endpoints y tests automáticos
└── employee-ms.environment.json  # Variables de entorno (base_url, employees_path, employee_id)
```

### Pasos para importar

1. Abre **Postman**.
2. Ve a **File → Import** (o arrastra los archivos directamente).
3. Selecciona ambos archivos: `employee-ms.collection.json` y `employee-ms.environment.json`.
4. En la esquina superior derecha, selecciona el environment **"Employee MS - Local"**.

### Variables de entorno

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `base_url` | `http://localhost:8081` | URL base del microservicio. |
| `employees_path` | `/employees` | Path base de los endpoints de empleados. |
| `employee_id` | _(vacío)_ | UUID auto-rellenado tras ejecutar `POST /employees`. |

> 💡 **Tip:** Ejecuta primero `POST create employees`. El script de test guarda automáticamente el `id` del primer empleado creado en la variable `employee_id`, que luego usarán `GET by ID`, `PUT update` y `DELETE`.

### Tests automáticos incluidos

Cada request de la colección incluye tests de Postman que verifican:
- Código de estado HTTP correcto (`200`, `201`, `204`).
- Estructura del body de respuesta.
- Tiempo de respuesta menor a 2000 ms.

Para correr todos los tests en secuencia, usa el **Collection Runner** de Postman:

1. Haz clic derecho sobre la colección → **Run collection**.
2. Selecciona el environment **"Employee MS - Local"**.
3. Haz clic en **Run Employee Microservice**.

---

## Health Checks

| Endpoint | Descripción |
|---|---|
| `GET /health` | Estado general (liveness + readiness). |
| `GET /health/live` | Liveness: verifica que la aplicación está corriendo e informa el total de empleados. |
| `GET /health/ready` | Readiness: verifica la conexión a la base de datos. |
| `GET /q/health-ui` | UI visual de los health checks (habilitada en todos los perfiles). |

---

## Documentación OpenAPI / Swagger

| URL | Descripción |
|---|---|
| `http://localhost:8081/swagger-ui` | Interfaz visual de Swagger UI. |
| `http://localhost:8081/q/openapi` | Especificación OpenAPI en formato YAML. |
| `http://localhost:8081/q/openapi?format=json` | Especificación OpenAPI en formato JSON. |

