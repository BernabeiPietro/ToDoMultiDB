[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=bugs)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BernabeiPietro_todomultidb&metric=coverage)](https://sonarcloud.io/summary/new_code?id=BernabeiPietro_todomultidb) [![Coverage Status](https://coveralls.io/repos/github/BernabeiPietro/todomultidb/badge.svg)](https://coveralls.io/github/BernabeiPietro/todomultidb) [![Java CI with Maven in Linux](https://github.com/BernabeiPietro/todomultidb/actions/workflows/pll-pr-ubuntu.yml/badge.svg?branch=master)](https://github.com/BernabeiPietro/todomultidb/actions/workflows/pll-pr-ubuntu.yml)
# ToDo Multi DB 
ToDoMultiDB, is a web application that allows information to be distributed across two separate relational databases. Specifically, the web application is structured according to a layered architecture (repository, service, REST controller and web controller) that uses two independent, non-duplicated MySQL databases to persist the information submitted by the user. As mentioned earlier, the client can interact with the application backend through two interfaces: one graphical, through a common HTML interface, and one through the REST API. The shared information is structured in two simple models in a 1 to n relationship: User and Todo respectively.


## Interface 
### Web side
Using the Thymeleaf engine, the site was developed mainly in HTML and CSS with some injections of scripts written on the Thymeleaf dialect.
Below a map of the web structure:
![mappa_sito_web](https://user-images.githubusercontent.com/25842408/233120206-14121921-b2ac-4bbb-af0c-fc418eb401fe.png)

### REST API

**User format for REST Request**
- id: integer string
- name: string
- email: string
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
    /api/users/{db}             - Get all users in db {db}
    /api/users/{db}/id/{id}     - Get single user with id {id} in db {db}
PUT
    /api/users/{db}/update/{id} - Override user with id {id}
POST
    /api/users/{db}/new         - Enter a new user.
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


## Technology
The following was used for development.
- Tools
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
And other tools ...
## Execute it
The application, by default, is reachable at the address:
```
http://localhost:8080/
```
Using docker-compose 
```
mvn clean package -Plocal-prod
docker-compose build
docker-compose up
  ```
## Try the test
```
Unit e IT Test:
    mvn clean verify
E2E Test:
    mvn clean verify -Pe2e-tests
```

## Distributed execution
- Initialize two MySQL databases.
- Create the JAR with information about the two databases:
```
mvn clean package -Ddb.docker.dns1=DB1_DNS_NAME -Ddb.docker.dns2=DB2_DNS_NAME -Ddb.docker.port1=DB1_port -Ddb.docker.port2=DB2_port 
```
- Example compatible with docker-compose file:
```
mvn clean package -Ddb.docker.dns1=db1 -Ddb.docker.dns2=db2 -Ddb.docker.port1=3306 -Ddb.docker.port2=3306
```

# More information
[Report_of_ToDoMultiDB_Project.pdf_(italian_version)](https://github.com/BernabeiPietro/ToDoMultiDB/files/11301962/Report_of_ToDoMultiDB_Project_Pietro_Bernabei.pdf)
