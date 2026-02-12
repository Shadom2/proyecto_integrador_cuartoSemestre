# 📱 UniWork Notifier - Sistema de Gestión de Tareas

Sistema completo de gestión de tareas para estudiantes con notificaciones y organización por prioridades.

## 🏗️ Arquitectura

### Backend
- **Framework:** Spring Boot 3.x + Kotlin
- **Base de Datos:** PostgreSQL 16
- **Seguridad:** JWT Authentication
- **ORM:** JPA/Hibernate

### Frontend
- **Framework:** Angular 18 + Ionic 8
- **UI:** Ionic Components
- **Storage:** Ionic Storage (persistencia local)
- **HTTP:** Interceptores para JWT automático

---

## 🚀 Inicio Rápido

### Requisitos Previos
- ✅ **Java 21** o superior
- ✅ **Node.js 18+** y npm
- ✅ **Docker Desktop** (para PostgreSQL)
- ✅ **Git**

### Instalación

1. **Clonar repositorio**
```bash
git clone https://github.com/Shadom2/proyecto_integrador_cuartoSemestre.git
cd proyecto_integrador_cuartoSemestre
```

2. **Instalar dependencias del frontend**
```bash
cd notifier-frontend
npm install
cd ..
```

3. **Iniciar aplicación**

**Opción A - Script Automático (Recomendado):**
```powershell
.\start-app.ps1
```

**Opción B - Manual (3 terminales):**
```bash
# Terminal 1: Docker
docker-compose up -d

# Terminal 2: Backend
.\gradlew bootRun

# Terminal 3: Frontend
cd notifier-frontend
npm start
```

4. **Acceder a la aplicación**
- 🌐 Frontend: http://localhost:4200
- 🔧 Backend API: http://localhost:8080
- 🗄️ PostgreSQL: localhost:5432

---

## 📋 Funcionalidades

### ✨ Características Principales
- 🔐 **Autenticación JWT** - Login y registro seguro
- 📝 **Gestión de Tareas** - CRUD completo
- 🏷️ **Prioridades** - Alto, Medio, Bajo (ordenamiento automático)
- 📊 **Estados** - Pendiente, En Progreso, Completada
- 📅 **Fechas de Vencimiento** - Selector de fecha y hora
- 🔔 **Notificaciones Visuales** - Badges y colores por estado
- 📱 **Responsive Design** - Funciona en móvil y escritorio
- 💾 **Persistencia** - Sesión guardada localmente

### 🎨 Interfaz
- Diseño moderno con gradientes
- Pestañas de filtrado por estado
- Cards con información detallada
- Selector de fecha/hora intuitivo
- Indicadores visuales de prioridad y estado

---

## 🗂️ Estructura del Proyecto

```
notifier/
├── src/                          # Backend (Kotlin/Spring Boot)
│   ├── main/kotlin/
│   │   └── com/uniwork/notifier/
│   │       ├── config/           # Configuración de seguridad
│   │       ├── controller/       # REST Controllers
│   │       ├── dto/              # Data Transfer Objects
│   │       ├── entity/           # JPA Entities
│   │       ├── mapper/           # Entity ↔ DTO mappers
│   │       ├── repository/       # Spring Data JPA
│   │       ├── security/         # JWT + Filters
│   │       └── service/          # Lógica de negocio
│   └── resources/
│       └── application.properties
├── notifier-frontend/            # Frontend (Angular/Ionic)
│   ├── src/
│   │   ├── app/
│   │   │   ├── guards/          # Auth guard
│   │   │   ├── interceptors/    # HTTP interceptor (JWT)
│   │   │   ├── models/          # TypeScript interfaces
│   │   │   ├── pages/           # Páginas de la app
│   │   │   │   ├── login/
│   │   │   │   ├── register/
│   │   │   │   ├── tasks/
│   │   │   │   ├── task-form/
│   │   │   │   └── task-detail/
│   │   │   └── services/        # API services
│   │   └── environments/        # Config de entorno
│   └── package.json
├── docker-compose.yml            # PostgreSQL container
├── start-app.ps1                 # Script de inicio automático
└── README.md
```

---

## 🔧 Configuración

### Backend (`application.properties`)
```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/notifier
spring.datasource.username=notifier
spring.datasource.password=notifier123

# JWT Secret
jwt.secret=your-secret-key-change-this-in-production
jwt.expiration=86400000
```

### Frontend (`environment.ts`)
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'
};
```

---

## 🧪 Testing

### Backend
```bash
.\gradlew test
```

### Frontend
```bash
cd notifier-frontend
npm test
```

---

## 📦 Build para Producción

### Backend
```bash
.\gradlew build
java -jar build/libs/notifier-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd notifier-frontend
npm run build
# Los archivos estarán en: www/
```

---

## 🛑 Detener la Aplicación

1. **Backend y Frontend:** Presiona `Ctrl + C` en cada terminal
2. **Docker:**
```bash
docker-compose down
```

---

## 📚 API Endpoints

### Auth
- `POST /auth/register` - Registro de usuario
- `POST /auth/login` - Login (recibe JWT)

### Students
- `GET /api/students` - Listar estudiantes
- `GET /api/students/{id}` - Obtener estudiante

### Tasks
- `GET /api/tasks` - Listar todas las tareas
- `GET /api/tasks/{id}` - Obtener tarea
- `GET /api/tasks/student/{studentId}` - Tareas de un estudiante
- `POST /api/tasks` - Crear tarea
- `PUT /api/tasks/{id}` - Actualizar tarea
- `DELETE /api/tasks/{id}` - Eliminar tarea

### Subtasks
- `GET /api/subtasks/task/{taskId}` - Subtareas de una tarea
- `POST /api/subtasks` - Crear subtarea
- `PUT /api/subtasks/{id}` - Actualizar subtarea
- `DELETE /api/subtasks/{id}` - Eliminar subtarea

---

## 🐛 Solución de Problemas

### Docker no inicia
```bash
# Verificar que Docker Desktop esté corriendo
docker --version
docker ps
```

### Backend - Error de compilación
```bash
.\gradlew clean build --refresh-dependencies
```

### Frontend - Dependencias
```bash
rm -rf node_modules package-lock.json
npm install
```

### Puerto ocupado
```bash
# Windows: Ver qué usa el puerto 8080
netstat -ano | findstr :8080
# Matar proceso: taskkill /PID <number> /F
```

---

## 👥 Usuarios de Prueba

Después de iniciar la aplicación, puedes registrar usuarios o usar estos de ejemplo:

```json
{
  "email": "pablo@gmailxd.com",
  "password": "password123",
  "firstName": "Pablo",
  "lastName": "Estudiante"
}
```

---

## 📄 Licencia

Este proyecto es parte de un trabajo académico - PUCE Cuarto Semestre.

---

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama: `git checkout -b feature/nueva-funcionalidad`
3. Commit: `git commit -m 'Agrega nueva funcionalidad'`
4. Push: `git push origin feature/nueva-funcionalidad`
5. Abre un Pull Request

---

## 📞 Soporte

Para problemas o preguntas, abre un issue en GitHub.

---

**Desarrollado con ❤️ por Pablo - PUCE 2026**
