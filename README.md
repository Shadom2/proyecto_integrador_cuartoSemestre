# Uniwork Notifier API 📚

Proyecto integrador final de Arquitectura de Software - API REST desarrollada con Kotlin + Spring Boot + PostgreSQL.

## 📋 Descripción

Sistema de gestión de tareas para estudiantes que permite organizar proyectos y subtareas con notificaciones. Implementa una arquitectura robusta siguiendo las mejores prácticas de desarrollo backend.

### Modelo de Datos

El proyecto implementa 3 entidades relacionadas:

```
Student (1) ──────> (N) Task (1) ──────> (N) SubTask
```

- **Student**: Estudiantes del sistema
- **Task**: Tareas asignadas a estudiantes con prioridad y fechas de vencimiento
- **SubTask**: Subtareas que componen una tarea principal

## 🚀 Tecnologías Utilizadas

- **Lenguaje**: Kotlin 2.2.21
- **Framework**: Spring Boot 4.0.2
- **Base de Datos**: PostgreSQL 15
- **ORM**: Spring Data JPA + Hibernate
- **Testing**: JUnit 5 + Mockito
- **Coverage**: JaCoCo (100% en capa Service)
- **Contenedores**: Docker + Docker Compose
- **Build Tool**: Gradle

## 📁 Estructura del Proyecto

```
src/main/kotlin/com/uniwork/notifier/
├── controller/          # Capa de controladores REST
│   ├── StudentController.kt
│   ├── TaskController.kt
│   └── SubTaskController.kt
├── service/            # Lógica de negocio
│   ├── StudentService.kt
│   ├── TaskService.kt
│   └── SubTaskService.kt
├── repository/         # Acceso a datos
│   ├── StudentRepository.kt
│   ├── TaskRepository.kt
│   └── SubTaskRepository.kt
├── entity/            #  Entidades JPA
│   ├── Student.kt
│   ├── Task.kt
│   └── SubTask.kt
├── dto/               # Data Transfer Objects
│   ├── request/       # DTOs para requests
│   └── response/      # DTOs para responses
└── mapper/            # Conversión Entity ↔ DTO
    ├── StudentMapper.kt
    ├── TaskMapper.kt
    └── SubTaskMapper.kt

src/test/kotlin/com/uniwork/notifier/
└── service/           # Tests unitarios (100% coverage)
    ├── StudentServiceTest.kt
    ├── TaskServiceTest.kt
    └── SubTaskServiceTest.kt
```

## 🐳 Configuración y Ejecución

### Prerrequisitos

- Java 21+
- Docker y Docker Compose
- Gradle (o usar el wrapper incluido)

### 1. Levantar la Base de Datos

```bash
docker compose up -d
```

Esto iniciará un contenedor PostgreSQL con la siguiente configuración:
- **Host**: localhost
- **Puerto**: 5432
- **Base de datos**: uniwork_db
- **Usuario**: uniwork_user
- **Contraseña**: uniwork_password

**Verificar que el contenedor esté corriendo:**
```bash
docker ps
```

### 2. Ejecutar la Aplicación

**Con Gradle wrapper (recomendado):**
```bash
./gradlew bootRun
```

**O compilar y ejecutar el JAR:**
```bash
./gradlew build
java -jar build/libs/notifier-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

### 3. Verificar que la API esté funcionando

```bash
curl http://localhost:8080/api/students
```

## 🧪 Testing

### Ejecutar todos los tests

```bash
./gradlew test
```

### Generar reporte de coverage

```bash
./gradlew test jacocoTestReport
```

El reporte HTML se generará en:
```
build/reports/jacoco/test/html/index.html
```

### Verificar coverage mínimo (100% en Services)

```bash
./gradlew jacocoTestCoverageVerification
```

### Ver resultados de tests

Después de ejecutar los tests, el reporte estará disponible en:
```
build/reports/tests/test/index.html
```

## 📮 Endpoints de la API

### Base URL
```
http://localhost:8080/api
```

### Students

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/students` | Obtener todos los estudiantes |
| GET | `/students/{id}` | Obtener estudiante por ID |
| POST | `/students` | Crear nuevo estudiante |
| PUT | `/students/{id}` | Actualizar estudiante |
| DELETE | `/students/{id}` | Eliminar estudiante |

### Tasks

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/tasks` | Obtener todas las tareas |
| GET | `/tasks/{id}` | Obtener tarea por ID |
| GET | `/tasks/student/{studentId}` | Obtener tareas de un estudiante |
| POST | `/tasks` | Crear nueva tarea |
| PUT | `/tasks/{id}` | Actualizar tarea |
| DELETE | `/tasks/{id}` | Eliminar tarea |

### SubTasks

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/subtasks` | Obtener todas las subtareas |
| GET | `/subtasks/{id}` | Obtener subtarea por ID |
| GET | `/subtasks/task/{taskId}` | Obtener subtareas de una tarea |
| POST | `/subtasks` | Crear nueva subtarea |
| PUT | `/subtasks/{id}` | Actualizar subtarea |
| DELETE | `/subtasks/{id}` | Eliminar subtarea |

## 📤 Uso de la Colección de Postman

### Importar la colección

1. Abre Postman
2. Click en "Import"
3. Selecciona el archivo `postman_collection.json` del proyecto
4. La colección "Uniwork Notifier API" aparecerá en tu sidebar

### Configuración

La colección incluye una variable de entorno:
- `base_url`: `http://localhost:8080/api`

### Ejemplos de uso

**Crear un estudiante:**
```json
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "firstName": "Pablo",
  "lastName": "Brito",
  "email": "pablo@test.com"
}
```

**Crear una tarea:**
```json
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Proyecto Final",
  "description": "Desarrollo del backend",
  "dueDate": "2024-12-31T23:59:59",
  "priority": "HIGH",
  "studentId": 1
}
```

**Crear una subtarea:**
```json
POST http://localhost:8080/api/subtasks
Content-Type: application/json

{
  "title": "Implementar entidades JPA",
  "description": "Crear Student, Task y SubTask",
  "taskId": 1
}
```

## 🔧 Convenciones de Código

### Naming Conventions

**Base de Datos (snake_case):**
- Tablas: `students`, `tasks`, `sub_tasks`
- Columnas: `first_name`, `last_name`, `created_at`, `student_id`

**Kotlin (camelCase):**
- Propiedades: `firstName`, `lastName`, `createdAt`, `studentId`
- Funciones: `findById()`, `createStudent()`

**Clases (PascalCase):**
- Entidades: `Student`, `Task`, `SubTask`
- Services: `StudentService`, `TaskService`
- Controllers: `StudentController`, `TaskController`

## 📊 Coverage Report

El proyecto mantiene **100% de cobertura** en la capa de servicios:

- ✅ **StudentService**: 100%
- ✅ **TaskService**: 100%
- ✅ **SubTaskService**: 100%

Los tests cubren:
- ✅ Casos exitosos
- ✅ Validaciones de negocio
- ✅ Manejo de errores (not found, duplicados, etc.)

## 🛠️ Comandos Útiles

### Limpiar build
```bash
./gradlew clean
```

### Compilar sin tests
```bash
./gradlew build -x test
```

### Ver dependencias
```bash
./gradlew dependencies
```

### Detener la base de datos
```bash
docker compose down
```

### Detener y eliminar volúmenes
```bash
docker compose down -v
```

## 📝 Notas Importantes

1. **Primera ejecución**: Hibernate creará automáticamente las tablas en PostgreSQL usando `ddl-auto=update`

2. **Puerto ocupado**: Si el puerto 8080 está en uso, cambia el puerto en `application.properties`:
   ```properties
   server.port=8081
   ```

3. **Base de datos**: Asegúrate de que Docker esté corriendo antes de iniciar la aplicación

4. **Tests**: Los tests usan mocks, NO requieren base de datos

## 👨‍💻 Autor

Proyecto Integrador - Arquitectura de Software  
Universidad: PUCE  
Año: 2024-2025

## 📄 Licencia

Este proyecto es con fines académicos.

---

**¿Problemas?** Revisa que:
- ✅ Docker esté corriendo
- ✅ Puerto 5432 esté disponible
- ✅ Java 21+ esté instalado
- ✅ Las variables de entorno en `application.properties` coincidan con `docker-compose.yml`
