# points-api


### Softwares Needed

* Java 1.8
* Maven
* Eclipse IDE
* Docker
* Postman API

### Run the Project using Eclipse

- Import project in Eclipse as Maven Project and run the PointsApplication.java file.

### Run the Project using Maven

- Navigate to the project folder.
- Run using command : ```mvn spring-boot:run```

### Run pre-existing docker image

- Make sure docker is installed on your machine and docker process is running.
- Make sure docker is installed on your machine.
- Run using command : ```docker run -d -p 8085:8085 --name points-api akshaypawaskar/points-api```

### Build docker image

- Make sure docker is installed on your machine and docker process is running.
- Navigate to the project folder.
- Run using command : ```mvn clean package docker:build```

### Create and run docker container.

- Make sure docker is installed on your machine and docker process is running.
- Run using command : ```docker run -p 8085:8085 -d --name points-api points-api:0.0.1-SNAPSHOT```

### Documentation.
- I have used SwaggerUI to generate docs for REST APIs. It provides convenient UI to view rest endpoints and also test them.
- Once project is up and running, you can visit following link for documentation:<br/>
```
http://$hostname:8085/swagger-ui/#/
```

