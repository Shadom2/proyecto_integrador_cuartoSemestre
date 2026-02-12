---
description: Cómo iniciar la aplicación completa (Docker + Backend + Frontend)
---

# Iniciar Aplicación UniWork Notifier

## Método 1: Script Automático (Recomendado)

Ejecuta el script PowerShell desde la raíz del proyecto:

```powershell
.\start-app.ps1
```

Este script:
- ✅ Inicia Docker (PostgreSQL) en segundo plano
- ✅ Abre una terminal con el Backend (Spring Boot)
- ✅ Abre una terminal con el Frontend (Angular/Ionic)

---

## Método 2: Manual (3 Terminales)

### Terminal 1: Docker (PostgreSQL)
```bash
docker-compose up -d
```

**Verificar:**
```bash
docker ps
```
Deberías ver un contenedor con PostgreSQL corriendo.

---

### Terminal 2: Backend (Spring Boot)
```bash
cd d:\Deberes U\Arquitectura\notifier
.\gradlew bootRun
```

**Esperar hasta ver:**
```
Started NotifierApplicationKt in X.XXX seconds
```

**URL:** http://localhost:8080

---

### Terminal 3: Frontend (Angular/Ionic)
```bash
cd d:\Deberes U\Arquitectura\notifier\notifier-frontend
npm start
```

**Esperar hasta ver:**
```
✔ Browser application bundle generation complete.
```

**URL:** http://localhost:4200

---

## Verificación

1. **Docker:** `docker ps` → Ver PostgreSQL corriendo
2. **Backend:** Abrir http://localhost:8080 → Ver API funcionando
3. **Frontend:** Abrir http://localhost:4200 → Ver aplicación cargando

---

## Detener Aplicación

### Detener Backend y Frontend
En cada terminal, presiona **Ctrl + C**

### Detener Docker
```bash
docker-compose down
```

---

## Solución de Problemas

### Docker no inicia
- Verificar que Docker Desktop esté ejecutándose
- Verificar puerto 5432 no esté ocupado: `netstat -ano | findstr :5432`

### Backend no compila
- Limpiar build: `.\gradlew clean build`
- Verificar Java 21 instalado: `java -version`

### Frontend no inicia
- Reinstalar dependencias: `npm install`
- Limpiar caché: `npm cache clean --force`

### Base de datos
- Ver logs: `docker logs <container_id>`
- Reiniciar: `docker-compose restart`
