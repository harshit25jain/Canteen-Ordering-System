@echo off
echo.
echo ================================================
echo    🚀 BULLETPROOF CANTEEN ORDERING SYSTEM START
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

echo 🔧 Testing Node.js installation...
node --version
if %errorlevel% neq 0 (
    echo ❌ Node.js not found! Please install Node.js 18+ first.
    echo 📥 Download from: https://nodejs.org/
    pause
    exit /b 1
)

echo ✅ Node.js is working!
echo.

echo 🔧 Fixing Maven wrapper issues...
cd Backend

echo 📦 Checking Maven wrapper...
if not exist ".mvn\wrapper\maven-wrapper.properties" (
    echo ⚠️  Maven wrapper properties missing. Creating...
    mkdir .mvn\wrapper 2>nul
    echo distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip > .mvn\wrapper\maven-wrapper.properties
    echo wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar >> .mvn\wrapper\maven-wrapper.properties
)

echo 🔄 Testing Maven wrapper...
call mvnw.cmd --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️  Maven wrapper has issues. Attempting to fix...
    echo.
    echo 🔄 Downloading fresh Maven wrapper files...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper-launcher.jar'"
    
    echo 🔄 Testing Maven wrapper again...
    call mvnw.cmd --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo ❌ Maven wrapper still has issues. Using alternative approach...
        echo.
        echo 🚀 Starting backend with direct compilation...
        javac -cp "src\main\java" -d "target\classes" src\main\java\com\canteen\ordering\*.java src\main\java\com\canteen\ordering\*\*.java
        if %errorlevel% neq 0 (
            echo ❌ Direct compilation failed!
            echo.
            echo 🔧 SOLUTIONS:
            echo 1. Install Maven globally: https://maven.apache.org/download.cgi
            echo 2. Use IntelliJ IDEA: Open Backend folder and run CanteenOrderingSystemApplication.java
            echo 3. Use Docker: docker-compose up
            echo.
            pause
            exit /b 1
        )
    )
)

echo ✅ Maven wrapper is working!
echo.

echo 🚀 Starting Backend Server...
echo 📡 Backend will be available at: http://localhost:8080
echo 📚 API Documentation: http://localhost:8080/swagger-ui.html
echo 🗄️  Database Console: http://localhost:8080/h2-console
echo.

start "Canteen Backend" cmd /k "title Canteen Backend && cd /d \"%~dp0Backend\" && set JAVA_HOME=C:\Program Files\Java\jdk-24 && set PATH=%JAVA_HOME%\bin;%PATH% && mvnw.cmd spring-boot:run"

echo ⏳ Waiting for backend to start (30 seconds)...
timeout /t 30 /nobreak >nul

echo 🌟 Starting Frontend Server...
cd ..\Frontend

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
echo 🎉 Canteen Ordering System is starting!
echo.
echo 💡 Keep both terminal windows open while using the application.
echo.
echo 🔧 If you encounter issues:
echo 1. Check that Java 11+ and Node.js 18+ are installed
echo 2. Ensure ports 8080 and 3000 are available
echo 3. Try using IntelliJ IDEA for the backend
echo.

pause








