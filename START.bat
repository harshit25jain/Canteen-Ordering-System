@echo off
echo.
echo ================================================
echo    🚀 CANTEEN ORDERING SYSTEM - ONE CLICK START
echo ================================================
echo.

echo 🔧 Setting up Java environment...
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo ✅ JAVA_HOME: %JAVA_HOME%
echo.

echo 🧪 Testing Java installation...
java -version
if %errorlevel% neq 0 (
    echo ❌ Java not found! Please install Java 11+ first.
    echo 📥 Download from: https://www.oracle.com/java/technologies/downloads/
    echo.
    echo 🔧 Alternative: Install OpenJDK from https://openjdk.org/
    pause
    exit /b 1
)

echo ✅ Java is working!
echo.

echo 🚀 Starting Backend Server...
cd /d "%~dp0Backend"
if not exist "mvnw.cmd" (
    echo ❌ Maven wrapper not found in Backend directory!
    echo Please ensure you're in the correct project directory.
    pause
    exit /b 1
)

echo 📁 Backend directory: %CD%
echo 🔨 Starting Spring Boot application...
start "Canteen Backend" cmd /k "title Canteen Backend && cd /d \"%~dp0Backend\" && set JAVA_HOME=C:\Program Files\Java\jdk-24 && set PATH=%JAVA_HOME%\bin;%PATH% && mvnw.cmd spring-boot:run"

echo ⏳ Waiting for backend to start (30 seconds)...
timeout /t 30 /nobreak >nul

echo 🌟 Starting Frontend Server...
cd /d "%~dp0Frontend"
if not exist "package.json" (
    echo ❌ package.json not found in Frontend directory!
    echo Please ensure you're in the correct project directory.
    pause
    exit /b 1
)

echo 📁 Frontend directory: %CD%
echo 📦 Installing dependencies...
call npm install
if %errorlevel% neq 0 (
    echo ❌ npm install failed! Please check Node.js installation.
    echo 📥 Download Node.js from: https://nodejs.org/
    pause
    exit /b 1
)

echo ✅ Dependencies installed!
echo 🚀 Starting React development server...
start "Canteen Frontend" cmd /k "title Canteen Frontend && cd /d \"%~dp0Frontend\" && npm run dev"

echo ⏳ Waiting for frontend to start (15 seconds)...
timeout /t 15 /nobreak >nul

echo.
echo ✅ Both services are starting!
echo.
echo 🌐 APPLICATION URLs:
echo    📱 Frontend: http://localhost:3000
echo    🔧 Backend:  http://localhost:8080
echo    📚 API Docs: http://localhost:8080/swagger-ui.html
echo    🗄️  Database: http://localhost:8080/h2-console
echo.

echo 🌐 Opening application in browser...
start http://localhost:3000

echo.
echo 🎉 Canteen Ordering System is ready!
echo.
echo 💡 Keep both terminal windows open while using the application.
echo.

pause
