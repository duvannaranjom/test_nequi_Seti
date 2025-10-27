# Proyecto Reactivo - Gestión de Franchese (Clean Architecture)

Este proyecto implementa una **API Reactiva** para la gestión de *Franchese*, *Branches* (sucursales) y *Products*, basada en la **Arquitectura Limpia (Clean Architecture)** propuesta por **Bancolombia**, con **Spring WebFlux**, **MongoDB Reactivo** y **JaCoCo** para cobertura de pruebas.

## 🚀 Características principales

- 🧩 **Arquitectura Limpia Modularizada** (Dominio, Casos de Uso, Infraestructura, Entradas/Salidas).
- ⚡ **Spring WebFlux** con Project Reactor (Mono / Flux).
- 🗄️ **MongoDB Reactivo** como base de datos.
- 🧠 **Validaciones con Jakarta Validation** (`@Valid`, `@NotBlank`, `@Min`).
- 🧭 **MapStruct** para mapear DTOs ↔ Entidades de dominio.
- 🧪 **JUnit 5 + Mockito + Reactor Test** para pruebas unitarias.
- 📊 **JaCoCo** integrado para cobertura de código (>80%).
- 🧰 **Docker Compose** para levantar MongoDB y la app.
- 🧾 **GlobalExceptionHandler** centralizado para manejar errores.
* **Logs estructurados con SLF4J.**

---

## ⚙️ Requisitos previos

* JDK 17+
* Maven 3.8+
* Docker (opcional)
* MongoDB (local o en contenedor)

---

## 🧩 Estructura del proyecto

```
test_nequi_Seti/
├── domain/                           # Reglas de negocio puras
│   ├── model/                        # Entidades y excepciones de dominio
│   └── usecase/                      # Casos de uso (aplicación)
│
├── infrastructure/
│   ├── driven-adapters/              # Adaptadores hacia el exterior
│   │   └── mongo-repository/         # Implementaciones reactivas (MongoDB)
│   └── entry-points/                 # Entradas al sistema
│       └── webflux-rest/             # Controladores REST (Spring WebFlux)
│
├── application/app-service/                      # Orquestador (Spring Boot Application)
│   ├── src/main/java/com/seti/nequi/test/appservice/NequiTestApplication.java
│   └── resources/application.yml
│
└── pom.xml                           # Proyecto multimódulo Maven
```

---

## 🧪 Instalación y ejecución

### Usando Maven

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en:
👉 [http://localhost:8080](http://localhost:8080)

---

## 📖 Documentación de la API

Una vez ejecutada la aplicación:

* Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🌍 Endpoints disponibles

### Endpoints

#### 🧱 Franchese
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| `POST` | `/api/v1/franchese` | Crear una nueva franquicia |
| `GET`  | `/api/v1/franchese` | Listar todas las franquicias |
| `GET`  | `/api/v1/franchese/{id}` | Obtener franquicia por ID |
| `PATCH` | `/api/v1/franchese/{id}/name` | Actualizar nombre de franquicia |
| `DELETE` | `/api/v1/franchese/{id}` | Eliminar una franquicia |

#### 🏢 Branch
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| `POST` | `/api/v1/franchese/{franchiseId}/branches` | Agregar una sucursal |
| `PATCH` | `/api/v1/franchese/{franchiseId}/branches/{branchId}/name` | Actualizar nombre de sucursal |
| `DELETE` | `/api/v1/franchese/{franchiseId}/branches/{branchId}` | Eliminar una sucursal |

#### 📦 Product
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| `POST` | `/api/v1/franchese/{franchiseId}/branches/{branchId}/products` | Agregar un producto |
| `PATCH` | `/api/v1/franchese/{franchiseId}/branches/{branchId}/products/{productId}/name` | Actualizar nombre del producto |
| `PATCH` | `/api/v1/franchese/{franchiseId}/branches/{branchId}/products/{productId}/count` | Actualizar cantidad del producto |
| `DELETE` | `/api/v1/franchese/{franchiseId}/branches/{branchId}/products/{productId}` | Eliminar un producto |

#### 📊 Reporte
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| `GET` | `/api/v1/franchese/{franchiseId}/products/max-stock` | Productos con mayor stock por sucursal |


---

## 💻 Ejemplos con cURL

## 📄 Ejemplo de request

### Crear una franquicia
```bash
curl -X POST http://localhost:8080/api/v1/franchese   -H "Content-Type: application/json"   -d '{
    "name": "TechStore"
  }'
```

### Agregar sucursal
```bash
curl -X POST http://localhost:8080/api/v1/franchese/{franchiseId}/branches   -H "Content-Type: application/json"   -d '{
    "name": "Sucursal Norte"
  }'
```

### Agregar producto
```bash
curl -X POST http://localhost:8080/api/v1/franchese/{franchiseId}/branches/{branchId}/products   -H "Content-Type: application/json"   -d '{
    "name": "Laptop Dell",
    "count": 25
  }'
```

---

## 🧪 Pruebas y cobertura JaCoCo

### Ejecutar pruebas
```bash
mvn clean verify
```

### Generar reporte de cobertura
```bash
mvn jacoco:report
```

---
## 🧱 Tecnologías principales

| Capa | Tecnología |
|------|-------------|
| Dominio | Java 17, POJOs, Exceptions |
| Casos de uso | Spring Reactor, Mockito |
| Entradas | Spring WebFlux, Validation, DTOs |
| Salidas | MongoDB Reactive, Spring Data |
| Tests | JUnit 5, Mockito, Reactor Test |
| Cobertura | JaCoCo |
| Build | Maven multimódulo |
| Infraestructura | Docker, Docker Compose |

---

## 🧰 Docker Compose (Mongo + App)

```bash
docker-compose up --build
```

Acceder a:
- API → [http://localhost:8080](http://localhost:8080)
- Mongo Express → [http://localhost:8081](http://localhost:8081)

---
## 🧠 Patrones y principios aplicados

* **SOLID**
* **Arquitectura Hexagonal**
* **DTO Pattern**
* **Repository Pattern**
* **Programación Funcional (Reactor)**
* **Validaciones con Bean Validation**

---

## 🐳 Docker (opcional)

Ejecuta la aplicación junto con MongoDB:

```bash
docker-compose up --build
```

Accede en [http://localhost:8080](http://localhost:8080)

---

## 🧩 Logs y monitoreo

Para seguir logs en ejecución:

```bash
docker-compose logs -f app
```

---


## 🚀 Próximas mejoras

- 🔐 Autenticación y autorización (JWT).
- 📊 Métricas y observabilidad (Micrometer / Prometheus).
- ☁️ Pipeline CI/CD (GitHub Actions).
- 🧩 Integración con HATEOAS para navegabilidad entre recursos.

© 2025 - Proyecto Reactivo de Gestión de **Franchese**
