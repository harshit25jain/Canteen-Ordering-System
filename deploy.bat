@echo off
echo.
echo ================================================
echo    🚀 DEPLOYING CANTEEN ORDERING SYSTEM
echo ================================================
echo.

echo 🔨 Building Backend...
cd Backend
call mvnw.cmd clean package -DskipTests

if %errorlevel% neq 0 (
    echo ❌ Backend build failed.
    pause
    exit /b 1
)

echo ✅ Backend built successfully!
echo.

echo 🎨 Building Frontend...
cd ..\Frontend
call npm run build

if %errorlevel% neq 0 (
    echo ❌ Frontend build failed.
    pause
    exit /b 1
)

echo ✅ Frontend built successfully!
echo.

echo 🐳 Building Docker Images...
cd ..
docker-compose build

if %errorlevel% neq 0 (
    echo ❌ Docker build failed. Make sure Docker is installed.
    pause
    exit /b 1
)

echo ✅ Docker images built successfully!
echo.

echo 🚀 Starting Production Services...
docker-compose up -d

echo.
echo ✅ Deployment complete!
echo.
echo 📱 Application URLs:
echo    • Frontend: http://localhost:3000
echo    • Backend API: http://localhost:8080
echo    • Swagger UI: http://localhost:8080/swagger-ui.html
echo.
echo To stop services: docker-compose down
echo.

pause
