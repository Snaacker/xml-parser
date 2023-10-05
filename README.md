# XML Parser application
[![Java CI with Gradle](https://github.com/Snaacker/xml-parser/actions/workflows/gradle.yml/badge.svg)](https://github.com/Snaacker/xml-parser/actions/workflows/gradle.yml)

Sample application for parser xml file and import to DB
## Usage
- Upload and validate xml file
- Save objects to DB
- Query objects by offer feed id

## Prerequisite
- Java 17
- Docker

## How to start application
### 1. Use startup script
Run command ```./local-startup.sh```

This command will
- Start a Mysql docker container name `db`, expose port 3306
- Create an empty db with default user/password
- Export environment variables
- Start application with ```./gradlew bootRun```

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

**NOTE**: Be carefully since this will drop your local database
## How to build
Run command ```./gradlew clean build```

## How to use API Endpoint

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
### Get product by feed_id
Return 200 "OK" with list JSON object
- GET /api/v1/product/{id}
``` curl http://localhost:8080/api/v1/product/1```
## Project structure explanation
### Folders
- src/main
- src/test
- files-upload: storing upload file from local
- gradle: Gradle folder
- .github: Github action pipeline

### Packages explanation
- controller: handle HTTP request from client
- model: POJO objects for parsing xml file or HTTP response object
- persistent: declare entities use in the application
- repository: provides the mechanism for storage, retrieval, search, update and delete operation on objects
- service: handle application business
- exception: declare application exception + handle exception

## Libraries usage
- Spring/Spring Boot: create web application with minimum configuration
- Spring JPA: store data in a relational database
- JUnit 5: testing framework
- Lombok: minimize/remove the boilerplate code in POJO
- Jakarta XML Binding: automate the mapping between XML documents and Java objects
- Jacoco: code coverage
