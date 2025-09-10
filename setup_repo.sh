#!/bin/bash

# Create .gitignore
cat > .gitignore <<EOL
# Kotlin / Java build
/build/
/.gradle/
!gradle/wrapper/gradle-wrapper.jar
!gradle/wrapper/gradle-wrapper.properties

# IntelliJ / Android Studio
.idea/
*.iml
*.ipr
*.iws
out/

# Logs
*.log

# OS files
.DS_Store
Thumbs.db

# Environment
.env
EOL

echo ".gitignore created ✅"

# Create README.md
cat > README.md <<'EOL'
# 🚀 Ktor Backend Application

A backend service built with [Ktor](https://ktor.io), Kotlin, and Gradle.  
This project is designed as a foundation for modern server-side applications with REST APIs, database integration, and clean architecture principles.

---

## 📖 Features
- ⚡ Built with **Ktor** (Kotlin server framework)
- 📦 RESTful API endpoints
- 🗄️ Database support (PostgreSQL / configurable)
- 🔑 Authentication & session handling (JWT-ready)
- 🧪 Easy testing with Ktor Test
- 🛠️ Gradle build system

---

## 🏗️ Project Structure
\`\`\`
.
├── src
│   ├── main
│   │   ├── kotlin        # Application source code
│   │   └── resources     # Application resources (config, db)
│   └── test              # Unit and integration tests
├── build.gradle.kts      # Gradle build script
├── gradlew               # Gradle wrapper (Unix)
├── gradlew.bat           # Gradle wrapper (Windows)
└── settings.gradle.kts
\`\`\`

---

## ⚙️ Setup & Installation

### 1. Clone the repository
\`\`\`bash
git clone https://github.com/yourusername/ktor-app.git
cd ktor-app
\`\`\`

### 2. Install dependencies
\`\`\`bash
./gradlew build
\`\`\`

### 3. Configure environment
Create a \`.env\` file in the root directory:
\`\`\`env
PORT=5000
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=yourpassword
DB_NAME=ktor_app
\`\`\`

### 4. Run migrations (if using Sequelize-like tools or Flyway)
\`\`\`bash
./gradlew runMigrations
\`\`\`

### 5. Start the server
\`\`\`bash
./gradlew run
\`\`\`

Server will run at: [http://localhost:5000](http://localhost:5000)

---

## 📡 API Endpoints (Sample)
- \`POST /api/v1/auth/signup\` → Register a new user  
- \`POST /api/v1/auth/signin\` → Login  
- \`GET /api/v1/user/{id}\` → Fetch user by ID  
- \`POST /api/v1/goal\` → Create a goal  

---

## 🧪 Running Tests
\`\`\`bash
./gradlew test
\`\`\`

---

## 🤝 Contributing
Contributions, issues, and feature requests are welcome!  
Feel free to fork this repo and submit a PR.

---

## 📜 License
This project is licensed under the MIT License.

---

## 👨‍💻 Author
**Your Name**  
- GitHub: [@yourusername](https://github.com/yourusername)  
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/your-link)  
EOL

echo "README.md created ✅"

echo "All done! Now run: git add . && git commit -m 'Add README and gitignore' && git push"