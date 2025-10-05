@echo off
echo.
echo ================================================
echo    🎯 FINAL SOLUTION - CANTEEN ORDERING SYSTEM
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

echo 🔧 FIXING MAVEN WRAPPER ISSUES...
cd Backend

echo 📦 Recreating Maven wrapper files...
rmdir /s /q .mvn 2>nul
mkdir .mvn\wrapper

echo 📝 Creating maven-wrapper.properties...
echo distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip > .mvn\wrapper\maven-wrapper.properties
echo wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar >> .mvn\wrapper\maven-wrapper.properties

echo 📥 Downloading Maven wrapper jar files...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar' -UseBasicParsing } catch { Write-Host 'Download failed' }"
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper-launcher.jar' -UseBasicParsing } catch { Write-Host 'Download failed' }"

echo 🔄 Testing Maven wrapper...
call mvnw.cmd --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven wrapper still has issues. Using IntelliJ IDEA approach...
    echo.
    echo 🔧 ALTERNATIVE SOLUTIONS:
    echo 1. Use IntelliJ IDEA: Open Backend folder and run CanteenOrderingSystemApplication.java
    echo 2. Install Maven globally: https://maven.apache.org/download.cgi
    echo 3. Use Docker: docker-compose up
    echo.
    echo 🚀 Starting backend with Maven wrapper anyway...
    start "Canteen Backend" cmd /k "title Canteen Backend && cd /d \"%~dp0Backend\" && set JAVA_HOME=C:\Program Files\Java\jdk-24 && set PATH=%JAVA_HOME%\bin;%PATH% && mvnw.cmd spring-boot:run"
) else (
    echo ✅ Maven wrapper is working!
    echo.
    echo 🚀 Starting Backend Server...
    start "Canteen Backend" cmd /k "title Canteen Backend && cd /d \"%~dp0Backend\" && set JAVA_HOME=C:\Program Files\Java\jdk-24 && set PATH=%JAVA_HOME%\bin;%PATH% && mvnw.cmd spring-boot:run"
)

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
echo 💡 If backend fails to start:
echo 1. Use IntelliJ IDEA: Open Backend folder and run CanteenOrderingSystemApplication.java
echo 2. Install Maven globally: https://maven.apache.org/download.cgi
echo 3. Use Docker: docker-compose up
echo.

pause








