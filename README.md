# API de Gestión de Franchese

Este proyecto implementa una **API RESTful Reactiva** para la gestión de *franchese*, sus *branches* (sucursales) y *products*, siguiendo una **arquitectura hexagonal** y principios **SOLID**.

## 🚀 Características principales

* **Arquitectura Hexagonal**: Separación clara entre dominio, aplicación e infraestructura.
* **Programación Reactiva**: Basada en Project Reactor (Mono y Flux) para operaciones no bloqueantes.
* **MongoDB**: Persistencia de datos reactiva.
* **Spring WebFlux**: Framework base para endpoints no bloqueantes.
* **Validaciones con Jakarta Validation** (`@Valid`, `@NotBlank`, `@Min`).
* **Mappers DTO → Domain → DTO** con MapStruct.
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
src/main/java/com/seti/nequi/test/Nequi/Test/
├── application
│   ├── ports
│   │   ├── input (servicios de dominio)
│   │   └── output (repositorios)
│   └── services (casos de uso)
├── domain
│   └── model (entidades de negocio)
├── infrastructure
│   ├── adapters
│   │   ├── input/rest (controladores)
│   │   └── output/persistence (repositorios)
│   └── config (configuración general)
└── NequiTestApplication.java
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

### Franchese (entidad principal)

```
POST   /api/v1/reactive/franchese                          → Crear una franchese
GET    /api/v1/reactive/franchese                          → Obtener todas las franchese
GET    /api/v1/reactive/franchese/{franchiseId}            → Obtener franchese por ID
PUT    /api/v1/reactive/franchese/{franchiseId}            → Actualizar franchese
DELETE /api/v1/reactive/franchese/{franchiseId}            → Eliminar franchese
```

### Actualización de nombres

```
PATCH  /api/v1/reactive/franchese/{franchiseId}/nombre                         → Actualizar nombre de franchese
PATCH  /api/v1/reactive/franchese/{franchiseId}/branches/{branchId}/nombre     → Actualizar nombre de branch
PATCH  /api/v1/reactive/franchese/{franchiseId}/branches/{branchId}/products/{productId}/nombre → Actualizar nombre de producto
```

### Branch (sucursal)

```
POST   /api/v1/reactive/franchese/{franchiseId}/branches                      → Agregar branch
DELETE /api/v1/reactive/franchese/{franchiseId}/branches/{branchId}           → Eliminar branch
```

### Product (producto)

```
POST   /api/v1/reactive/franchese/{franchiseId}/branches/{branchId}/products  → Agregar producto
DELETE /api/v1/reactive/franchese/{franchiseId}/branches/{branchId}/products/{productId} → Eliminar producto
PATCH  /api/v1/reactive/franchese/{franchiseId}/branches/{branchId}/products/{productId}/stock → Actualizar stock
```

### Consultas especiales

```
GET /api/v1/reactive/franchese/{franchiseId}/products/mas-stock → Obtener productos con más stock por branch
```

---

## 💻 Ejemplos con cURL

### Crear una franchese

```bash
curl -X POST http://localhost:8080/api/v1/reactive/franchese \
  -H "Content-Type: application/json" \
  -d '{
    "name": "TechStore"
}'
```

### Agregar un branch

```bash
curl -X POST http://localhost:8080/api/v1/reactive/franchese/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Branch Norte"
}'
```

### Agregar un producto

```bash
curl -X POST http://localhost:8080/api/v1/reactive/franchese/{franchiseId}/branches/{branchId}/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell",
    "count": 25
}'
```

### Actualizar stock de producto

```bash
curl -X PATCH http://localhost:8080/api/v1/reactive/franchese/{franchiseId}/branches/{branchId}/products/{productId}/stock \
  -H "Content-Type: application/json" \
  -d '{
    "newCount": 100
}'
```

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

## 🚀 Posibles Mejoras Futuras

### 🧱 Implementación de manejo de errores global
- Agregar una clase `@RestControllerAdvice` para capturar excepciones globales.
- Responder con estructuras uniformes de error (`timestamp`, `status`, `message`, `path`).
- Definir errores personalizados como `BusinessException` y `NotFoundException`.

### 🧪 Integración de JaCoCo y pruebas unitarias
- Configurar el plugin **JaCoCo** en el `pom.xml` para generar reportes de cobertura.
- Implementar pruebas unitarias con **JUnit5** y **Mockito** en servicios y controladores.
- Configurar un umbral mínimo de cobertura (por ejemplo, 90%).

### 🔄 Configuración de despliegue continuo (CI/CD)
- Crear un workflow en **GitHub Actions** o **Jenkinsfile** para:
    - Ejecutar pruebas automáticas en cada `push` o `pull request`.
    - Generar y publicar reportes de cobertura con JaCoCo.
    - Construir y desplegar automáticamente la aplicación en entornos de prueba o producción usando Docker o Kubernetes.

---

### 📈 Implementación de HATEOAS para monitoreo
- Incorporar **HATEOAS** (Hypermedia as the Engine of Application State) para enriquecer las respuestas de la API.
- Añadir enlaces contextuales que permitan navegar entre recursos relacionados (por ejemplo, `self`, `branches`, `products`).
- Utilizar **Spring HATEOAS** para mejorar la observabilidad y trazabilidad de las relaciones entre entidades.

---

© 2025 - Proyecto Reactivo de Gestión de **Franchese**
