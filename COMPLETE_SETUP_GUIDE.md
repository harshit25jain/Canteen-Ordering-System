# 🚀 **COMPLETE SETUP GUIDE - CANTEEN ORDERING SYSTEM**

## 📋 **PREREQUISITES TO INSTALL**

### **1. Java 11 or Higher (REQUIRED)**
- **Download:** https://www.oracle.com/java/technologies/downloads/
- **Alternative:** https://openjdk.org/
- **Version:** Java 11, 17, 21, or 24
- **Installation:** Follow the installer, it will set JAVA_HOME automatically

### **2. Node.js 18 or Higher (REQUIRED)**
- **Download:** https://nodejs.org/
- **Version:** 18.x or higher
- **Installation:** Follow the installer, it will add npm automatically

### **3. Maven (OPTIONAL - for manual builds)**
- **Download:** https://maven.apache.org/download.cgi
- **Extract to:** `C:\Program Files\Apache\maven`
- **Add to PATH:** `C:\Program Files\Apache\maven\bin`

### **4. IntelliJ IDEA (RECOMMENDED)**
- **Download:** https://www.jetbrains.com/idea/
- **Community Edition:** Free
- **Professional Edition:** 30-day trial

---

## 🔧 **STEP-BY-STEP SETUP**

### **STEP 1: Verify Prerequisites**
```cmd
# Check Java
java -version

# Check Node.js
node --version
npm --version
```

### **STEP 2: Fix Maven Wrapper (If Needed)**
The Maven wrapper might be corrupted. Here's how to fix it:

```cmd
cd "C:\Users\hj235\Desktop\Canteen Ordering System\Backend"
# Delete corrupted wrapper
rmdir /s .mvn
# Recreate wrapper
mkdir .mvn\wrapper
# Download fresh wrapper
curl -o .mvn\wrapper\maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
```

### **STEP 3: Run the Application**

#### **Option A: Automated Script (Easiest)**
```cmd
# Just run this:
SIMPLE_RUN.bat
```

#### **Option B: Manual Commands**
```cmd
# Terminal 1: Backend
cd "C:\Users\hj235\Desktop\Canteen Ordering System\Backend"
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%
mvnw.cmd spring-boot:run

# Terminal 2: Frontend
cd "C:\Users\hj235\Desktop\Canteen Ordering System\Frontend"
npm install
npm run dev
```

#### **Option C: IntelliJ IDEA (Recommended)**
1. **Open IntelliJ IDEA**
2. **File → Open** → Select `Backend` folder
3. **Wait for Maven import**
4. **Run:** `CanteenOrderingSystemApplication.java`

---

## 🌐 **APPLICATION URLs**

| Service | URL | Description |
|---------|-----|-------------|
| **🏠 Frontend** | http://localhost:3000 | React.js application |
| **🔧 Backend** | http://localhost:8080 | Spring Boot REST API |
| **📚 API Docs** | http://localhost:8080/swagger-ui.html | Swagger documentation |
| **🗄️ Database** | http://localhost:8080/h2-console | H2 Database console |

---

## 🧪 **TESTING THE APPLICATION**

### **1. Backend Test:**
```bash
# Test API endpoint
curl http://localhost:8080/api/menu

# Expected: JSON array of menu items
```

### **2. Frontend Test:**
1. Open http://localhost:3000
2. You should see the Canteen Ordering System homepage
3. Browse menu items, add to cart, create orders

### **3. Database Test:**
1. Open http://localhost:8080/h2-console
2. **JDBC URL:** `jdbc:h2:mem:testdb`
3. **Username:** `sa`
4. **Password:** (leave empty)
5. Click "Connect"

---

## 🛠️ **TROUBLESHOOTING**

### **Java Issues:**
```cmd
# Check Java installation
java -version

# If not found, install Java 11+ from:
# https://www.oracle.com/java/technologies/downloads/
```

### **Node.js Issues:**
```cmd
# Check Node.js installation
node --version

# If not found, install from:
# https://nodejs.org/
```

### **Maven Wrapper Issues:**
```cmd
# If mvnw.cmd fails, use IntelliJ IDEA instead
# Or install Maven globally and use: mvn spring-boot:run
```

### **Port Conflicts:**
- **Backend (8080):** Change in `Backend/src/main/resources/application.properties`
- **Frontend (3000):** Change in `Frontend/vite.config.js`

---

## 🎯 **QUICK START (RECOMMENDED)**

### **For Immediate Results:**
1. **Install Java 11+** and **Node.js 18+**
2. **Run:** `SIMPLE_RUN.bat`
3. **Open:** http://localhost:3000

### **For Professional Development:**
1. **Install IntelliJ IDEA**
2. **Open Backend folder** in IntelliJ
3. **Run:** `CanteenOrderingSystemApplication.java`
4. **Run Frontend:** `npm run dev` in Frontend folder

---

## ✅ **SUCCESS CRITERIA**

The application is working correctly if:
- ✅ Backend starts without errors on port 8080
- ✅ Frontend starts without errors on port 3000
- ✅ API endpoints return JSON data
- ✅ Frontend displays the homepage
- ✅ Menu items load and display
- ✅ Cart functionality works
- ✅ Orders can be created and managed

---

**🎉 Your Canteen Ordering System is ready to use!** 🚀








