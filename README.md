# XML Parser application
Sample application for parser xml file and import to DB

# Prerequisite
- Java 17
- Docker

# How to start application
### 1. Use startup script
Run command ```./local-startup.sh```

### 2.Manually start with Gradle
Set environment value for
- DATABASE_URL_CUSTOMIZE
- USER_NAME
- USER_PASSWORD
For example:
```
export DATABASE_URL_CUSTOMIZE="jdbc:mysql://localhost:3306/xmlparser"
export USER_NAME=admin
export USER_PASSWORD=admin
```
Run command ```./gradlew bootRun```

# How to build
Run command ```./gradlew clean build```

# How to use API Endpoint

# Application endpoints

### Check application health
Return 200 "OK" if application is up and running
- GET /healthCheck
``` curl http://localhost:8080/healthCheck```
### Upload xml file to application and load to DB
Return 200 "OK" if file was load successfully to DB
- POST /api/v1/product
```
curl -F file=@"[PATH_TO_XML_FILE]" http://localhost:8080/api/v1/product
 ```
### Get product by product id
Return 200 "OK" with response object
- GET /api/v1/product/{id}
``` curl http://localhost:8080/api/v1/product/1```
# Project structure explanation
## Folders
- src/main
- src/test
- files-upload: storing upload file from local
- gradle: Gradle folder
- .github: Github action pipeline

## Packages
- controller: Controller class
- model: - Model class
- persistent: - Persistent classes
- repository: - Repository class
- service: - Service class
- util: - Utilities class

# Libraries usage
- Spring/Spring Boot: create web application with minimum configuration
- Spring JPA: store data in a relational database
- JUnit 5: testing framework
- Lombok: minimize/remove the boilerplate code in POJO
- Jakarta XML Binding: automate the mapping between XML documents and Java objects
