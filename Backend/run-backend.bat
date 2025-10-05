@echo off
echo.
echo ================================================
echo    🚀 STARTING CANTEEN ORDERING SYSTEM BACKEND
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
    pause
    exit /b 1
)

echo ✅ Java is working!
echo.

echo 🔨 Building the application...
echo This may take a few minutes on first run...
call mvnw.cmd clean compile -DskipTests
if %errorlevel% neq 0 (
    echo ❌ Build failed! Trying alternative approach...
    echo.
    echo 🔄 Attempting to run with Maven wrapper...
    call mvnw.cmd spring-boot:run
    if %errorlevel% neq 0 (
        echo ❌ Maven wrapper failed! Please check Java installation.
        echo.
        echo 🔧 Troubleshooting steps:
        echo 1. Ensure Java 11+ is installed
        echo 2. Check JAVA_HOME is set correctly
        echo 3. Try running: mvnw.cmd clean install
        pause
        exit /b 1
    )
)

echo ✅ Build successful!
echo.

echo 🌟 Starting Spring Boot application...
echo 📡 Backend will be available at: http://localhost:8080
echo 📚 API Documentation: http://localhost:8080/swagger-ui.html
echo 🗄️  Database Console: http://localhost:8080/h2-console
echo.

call mvnw.cmd spring-boot:run

pause








