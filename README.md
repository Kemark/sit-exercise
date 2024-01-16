# SIT Exercise

This exercise based on the newes Spring Boot version 3.2.1

## Used Tech Stack

- Java 19SE
- Spring Boot 3.2.1
- Spring Data JPA
- Spring Actuator
- MS SQL Driver 2022
- SpringDoc
- Mockito
- Instancio
- Mapstruct
- Swagger / SpringDoc
- Testcontainers

## Preconditions

Therefore, the scripts in the package.json may have to be adapted for windows.

### Installations

1. Install **nvm** by running ```curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash```

    see [installation guide](https://www.freecodecamp.org/news/node-version-manager-nvm-install-guide/) for further informations

2. Install **sdkman** by running ``` $ curl -s "https://get.sdkman.io" | bash ```

    To ensure that the installation succeeded run ``` $ sdk version ```

3. Install **Docker** ```https://www.docker.com/products/docker-desktop/```

### Getting started


- Run ```$ nvm use``` to use the correct npm version

  If the version is not yet installed you are prompted to run ```$ nvm install```

- Run ```$ npm install```

- Run ```$ sdk env install``` to install the correct versions of java and maven

- Start Docker

## Starting local development

The local environment starts all needed docker containers
- Check Preconditions and run ```$ npm run start:local:env```

    The ```start:local:env``` is starting ```sql server``` and ```jaeger``` locally. This services must be available 
    before you can start the local masterdata service.


- Run ```npm run start:local```


## Commands

To use the following commands you have to execute ```npm install``` once a time.

| Command                       | Description                                                                                                                                                                                                             | Pre-Condition                                                               |
|-------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| ```npm run start:local:env``` | Starts the local environment. Including                    | [docker](https://www.docker.com/products/docker-desktop/) must be installed  |
| ```npm run build```           | Build the project without testing                                                                                                                                               | ```npm run start:local-env```must be executed before                        |
| ```npm run start```           | Starts the application (<http://localhost:8090>,<br> swagger: <http://localhost:8080/swagger-ui/index.html>)                                                                                                                                | ```npm run start:local-env```must be executed before                        |–
| ```npm run start:debug```     | Starts the application in debug mode with the "local" profile. Debug-Port: 8000. Hotcode replacement is enabled.                                                                | ```npm run start:local-env```must be executed before                        |
| ```npm test```                | Executes all tests                                                                                                                                                              |                                                                             |
| ```npm run check```           | Run check style (using [./config/checkstyle/checkstyle.xml](.checkstyle.xml))                                                                                                   |                                                          |

# Notes

This are Notes to this exercise

- using Java

  I decided to use Java for the implementation, as this is where I currently expect to make the fastest progress in implementing this exercise. 

- using existing approaces
  
    A few approaches are from my last projects. It is actually intended for larger projects, but is also
    also suitable for such a small practice project. The advantage of this is that the features can always be structured in a very similar way
    and the CRUD operation is generic.

- using checkstyle

    checkstyle was not required, but I added it, because it makes implementation easier. Also, checkstyle or other checks
    should be integrated into a project as early as possible.

- using integration tests

    For the controller tests, I only used integration tests based on test containers.
    The individual units contain very little functionality. The complexity lies in the interaction of the components.

- using sql server

    I decided to use SQL Server for the database. This is mainly because I have worked with SQL Server in the last few projects 
    SQL Server and therefore currently have the experience to implement it quickly. H2 would also be conceivable for this small
    project, but is practically not used.

- database schema generation
  
    flyway or liquibase is usually used in projects for db schema generation. For the exercise, I used auto-generation.
    This has disadvantages, but is not feasible for the time frame.