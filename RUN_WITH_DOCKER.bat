@echo off
echo.
echo ================================================
echo    🐳 RUNNING CANTEEN SYSTEM WITH DOCKER
echo ================================================
echo.

echo 🔧 Checking Docker installation...
docker --version
if %errorlevel% neq 0 (
    echo ❌ Docker not found! Please install Docker Desktop first.
    echo 📥 Download from: https://www.docker.com/products/docker-desktop/
    pause
    exit /b 1
)

echo ✅ Docker is working!
echo.

echo 🚀 Starting Canteen Ordering System with Docker...
echo This will start both backend and frontend in containers.
echo.

docker-compose up --build

echo.
echo 🎉 Canteen Ordering System is running with Docker!
echo.
echo 🌐 APPLICATION URLs:
echo    📱 Frontend: http://localhost:3000
echo    🔧 Backend:  http://localhost:8080
echo    📚 API Docs: http://localhost:8080/swagger-ui.html
echo.

pause








