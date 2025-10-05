# 🍽️ **Canteen Ordering System**

A modern, full-stack canteen ordering system with real-time inventory management and automated order processing.

## 🚀 **ONE-CLICK START**

### **Prerequisites:**
- ✅ **Java 11+** - [Download here](https://www.oracle.com/java/technologies/downloads/)
- ✅ **Node.js 18+** - [Download here](https://nodejs.org/)

### **Run Application:**
```bash
# Simply double-click or run:
START.bat
```

**That's it!** The application will:
- ✅ Set up Java environment automatically
- ✅ Start the backend server
- ✅ Install frontend dependencies
- ✅ Start the frontend server
- ✅ Open the application in browser

---

## 🌐 **Application URLs**

| Service | URL | Description |
|---------|-----|-------------|
| **🏠 Main App** | http://localhost:3000 | React.js frontend |
| **🔧 Backend API** | http://localhost:8080 | Spring Boot REST API |
| **📚 API Docs** | http://localhost:8080/swagger-ui.html | Swagger documentation |
| **🗄️ Database** | http://localhost:8080/h2-console | H2 Database console |

---

## 🎯 **Key Features**

### **Backend:**
- ✅ **RESTful APIs** with Spring Boot
- ✅ **Real-time inventory** management with pessimistic locking
- ✅ **Automated order cancellation** (15-minute timeout)
- ✅ **Transaction-safe** stock management
- ✅ **Swagger documentation** for API testing
- ✅ **H2 in-memory database** for development

### **Frontend:**
- ✅ **React.js** with modern hooks and functional components
- ✅ **Responsive design** with TailwindCSS
- ✅ **Real-time updates** (10-second polling)
- ✅ **Shopping cart** with quantity management
- ✅ **Order management** with countdown timers
- ✅ **Admin dashboard** for system management
- ✅ **Modern UI** with shadcn/ui components

---

## 🧪 **Testing the Application**

### **1. Backend API Test:**
```bash
curl http://localhost:8080/api/menu
# Should return JSON array of menu items
```

### **2. Frontend Test:**
1. Open http://localhost:3000
2. Browse menu items
3. Add items to cart
4. Create orders
5. Check order status

### **3. Full Integration Test:**
1. **Browse Menu** - View available items
2. **Add to Cart** - Add items to shopping cart
3. **Checkout** - Create orders
4. **View Orders** - Check order status with countdown timer
5. **Admin Panel** - Manage menu items and view statistics

---

## 🔧 **Manual Setup (If Needed)**

### **Backend:**
```bash
# Set Java environment
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

# Navigate to backend
cd Backend

# Start Spring Boot
mvnw.cmd spring-boot:run
```

### **Frontend:**
```bash
# Navigate to frontend
cd Frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

---

## 🛠️ **Troubleshooting**

### **Java Issues:**
```bash
# Check Java version
java -version

# If not found, install Java 11+ from:
# https://www.oracle.com/java/technologies/downloads/
```

### **Node.js Issues:**
```bash
# Check Node.js version
node --version

# If not found, install from:
# https://nodejs.org/
```

### **Port Conflicts:**
- **Backend (8080):** Change in `Backend/src/main/resources/application.properties`
- **Frontend (3000):** Change in `Frontend/vite.config.js`

---

## 📊 **Technical Stack**

### **Backend:**
- **Java 11+** with Spring Boot
- **Spring Data JPA** with Hibernate
- **H2 Database** (in-memory for development)
- **Maven** for dependency management
- **Swagger/OpenAPI** for documentation

### **Frontend:**
- **React 18** with JavaScript
- **Vite** for fast development
- **TailwindCSS** for styling
- **shadcn/ui** for components
- **Axios** for API calls
- **React Router** for navigation

---

## 🎉 **Success Criteria**

The application is working correctly if:
- ✅ Backend starts without errors on port 8080
- ✅ Frontend starts without errors on port 3000
- ✅ API endpoints return JSON data
- ✅ Frontend displays the homepage
- ✅ Menu items load and display
- ✅ Cart functionality works
- ✅ Orders can be created and managed

---

**🎊 Your Canteen Ordering System is ready to use! Just run `START.bat` and you're good to go!** 🚀