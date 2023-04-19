[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=bugs)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=coverage)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Coverage Status](https://coveralls.io/repos/github/BernabeiPietro/todomultidb/badge.svg)](https://coveralls.io/github/BernabeiPietro/todomultidb) [![Java CI with Maven in Linux](https://github.com/BernabeiPietro/todomultidb/actions/workflows/pll-pr-ubuntu.yml/badge.svg?branch=master)](https://github.com/BernabeiPietro/todomultidb/actions/workflows/pll-pr-ubuntu.yml)
# todomultidb
todomultidb is a web application, developed with the Spring Boot framework. As we can undestand from the name of the project, its main scope is to provide a todo management function, storing the information collect into multiple database destination. More precisasly, the application was designed to manage two different simplier models, user and todo, in relaction with themself. From the point of view of the database, it was developed to manage two different database MYSQL, with the feature of permits to the client to define at runtime, where store the data. The choose of limit the application to only two database is only to maintain the simpleness of the code.
This repository is an exercise to experiment the TDD developement and different technology, framework and techniques.
To maintain more simplier the complexity of the application, it manage only two models: User and Todo. 

##Architecture
The application using a layer architecture, Repository, Service, Web Controller and REST Controller. 
To interact with the application, it's provide two interface: web based interface and REST api. 
### Web side
Using the Thymeleaf engive , the web side was developed mainly in HTML and CSS with some injections of scripts writed on the thymeleaf dialect.
Below a map of the web structure:
![mappa_sito_web](https://user-images.githubusercontent.com/25842408/233120206-14121921-b2ac-4bbb-af0c-fc418eb401fe.png)

### REST API

**User format for REST Request**
```
{
    "id": "2",
    "name": "prova",
    "email": "prova"
}
```
**User REST API**
```
GET
    /api/users/{db}             - Ottieni tutti gli user nel db {db}
    /api/users/{db}/id/{id}     - Ottieni un singolo user con id {id} nel db {db}
PUT
    /api/users/{db}/update/{id} - Sovrascrivi i campi del user con id {id} 
POST
    /api/users/{db}/new         - Inserisci un nuovo user.
```
**ToDo format for REST request:**
- id: integer string
- actions: couple of key (string), and value (boolean)
- date: string in ISO format
- idOfUser: integer string
```
{
        "id": "1",
        "actions": {
            "try": true,
            {String key}: {boolean}
        },
        "date": "2023-10-10T00:00:00", 
        "idOfUser": "1"
}
```
**ToDo REST API**
```
GET
    /api/todo/{db}             - Get all todos in db {db}
    /api/todo/{db}/id/{id}     - Get single todo with id {id} in db {db}
    /api/todo/{db}/ofuser/{id} - Get all todos of user with id {id} in db {db}
PUT
    /api/todo/{db}/update/{id} - Override fields altogether with id {id} 
POST
    /api/todo/{db}/new         - Insert a new todo.
```


##Technology
Insight, you can find:
- Technology
  - Spring
  - Maven
  - Docker
  - Docker-compose
  - Jacoco
  - PIT Mutation testing
  - Sonar
  - GitHub Actions
- Test
  - Unit test
    - Junit 4
    - Mockito
    - Hamcrest
    - [HtmlUnit](https://github.com/HtmlUnit/htmlunit)
  - IT test
    - Mockito
    - RestAssured 
    - Selenium
  - E2E test
    - Selenium
## Execute it
Using docker-compose 
```
mvn clean package -Plocal-prod
docker-compose build
docker-compose up
  ```
## Try the test
```
Unit e IT Test:
    mvn clear verify
E2E Test:
    mvn clean verify -PE2E-test
```

## Distributed execution
- Initialize two MySQL databases.
- Create the JAR with information about the two databases:
```
mvn clean package -Ddb.docker.dns1=DB1_DNS_NAME -Ddb.docker.dns2=DB2_DNS_NAME -Ddb.docker.port1=DB1_port -Ddb.docker.port2=DB2_port 
```

#More information
[ATTSW Exam.pdf](https://github.com/BernabeiPietro/todomultidb/files/11274808/ATTSW.Exam.pdf)

