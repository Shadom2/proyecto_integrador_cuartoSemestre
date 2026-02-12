# Script para iniciar la aplicacion completa
# Ejecuta: .\start-app.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Iniciando UniWork Notifier App" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. Iniciar Docker (PostgreSQL)
Write-Host "[1/3] Iniciando Docker PostgreSQL..." -ForegroundColor Yellow
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "Docker iniciado correctamente" -ForegroundColor Green
} else {
    Write-Host "Error al iniciar Docker" -ForegroundColor Red
    Write-Host "Asegurate de que Docker Desktop este ejecutandose" -ForegroundColor Red
    exit 1
}

Write-Host ""
Start-Sleep -Seconds 3

# 2. Iniciar Backend (Spring Boot) en nueva ventana
Write-Host "[2/3] Iniciando Backend (Spring Boot)..." -ForegroundColor Yellow
$backendPath = (Get-Location).Path
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$backendPath'; Write-Host 'Iniciando Spring Boot Backend...' -ForegroundColor Cyan; .\gradlew bootRun"
Write-Host "Backend iniciando en nueva ventana..." -ForegroundColor Green
Write-Host "  URL: http://localhost:8080" -ForegroundColor Gray

Write-Host ""
Start-Sleep -Seconds 2

# 3. Iniciar Frontend (Angular/Ionic) en nueva ventana
Write-Host "[3/3] Iniciando Frontend (Angular/Ionic)..." -ForegroundColor Yellow
$frontendPath = Join-Path (Get-Location).Path "notifier-frontend"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$frontendPath'; Write-Host 'Iniciando Angular/Ionic Frontend...' -ForegroundColor Cyan; npm start"
Write-Host "Frontend iniciando en nueva ventana..." -ForegroundColor Green
Write-Host "  URL: http://localhost:4200" -ForegroundColor Gray

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Aplicacion iniciada correctamente" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "IMPORTANTE:" -ForegroundColor Yellow
Write-Host "- Docker:   http://localhost:5432 (PostgreSQL)" -ForegroundColor White
Write-Host "- Backend:  http://localhost:8080 (API Spring Boot)" -ForegroundColor White
Write-Host "- Frontend: http://localhost:4200 (Ionic App)" -ForegroundColor White
Write-Host ""
Write-Host "El backend tardara ~3-5 minutos en iniciar completamente." -ForegroundColor Yellow
Write-Host "Espera a que veas 'Started NotifierApplicationKt' en la terminal del backend." -ForegroundColor Yellow
Write-Host ""
Write-Host "Presiona cualquier tecla para cerrar esta ventana..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
