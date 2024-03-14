# How to run
* The application is written using Java 17 and Spring boot 3.2.3
* Database Migration is in place using liquibase
* Make sure you have Maven installed
### Docker way
* Install Docker Desktop
* Update .env file change REDIS_PASSWORD to the one sent with the email
* Update other .env file details (if you want)
* Run the following commands
* Build the application using
```  
mvn clean install  
```  
* Run the application using docker
```  
docker-compose up  
```  
### Non-docker
* Run the command below
* Install MySQL 8.0 above
* Configure your MySQL
* Configure .env to reflect the Mysql configuration
* REDIS_PASSWORD will be sent with email
* Run the command below
```  
mvn spring-boot:run  
```  


* Go to [How to test](#How to test) Section
* default admin credentials: admin@admin.com/hello123

### Documentation
1. ER Diagram can be found in the root folder of the project (erdiagram.png)
2. Open API documentation can be found in the root folder of the project (apispec.yaml)
3. Postman collection can be found in the root folder of the project (Lunchbunch.postman_collection.json)
4. See [Design Consideration](#Design Consideration)

### How to test
* Create your user using register endpoint
```  
POST http://localhost:8080/register  
{  
"email":"hello@world.com",  
"password":"hello123"  
"name":"hello"  
}  
```  
* Login using login endpoint
```  
POST http://localhost:8080/login  
{  
"email":"hello@world.com",  
"password":"hello123"  
}  
```  
* Copy the access token
* Create a new lunch bunch - paste the access token in the header
```  
POST http://localhost:8080/lunch-plan  
Authorization: Bearer <token>  
{  
"date":"2024-10-03",  
"description":"Birthday celebration"  
}  
```  
* View list of lunch plan
```  
GET localhost:8080/lunch-plan?page=0&size=20  
Authorization: Bearer <token>  
```  
* View Detailed Lunch Plan
```  
GET localhost:8080/lunch-plan/<uuid>  
```  


### Design Considerations
1. Authentication is required for users to create a lunch plan, while suggesting changes to a lunch plan does not necessitate authentication. This ensures convenience for users in deciding on lunch plans. However, authentication is necessary for the lunch plan creator to access their past lunch plans.
2. Lunch plan creation is facilitated through APIs, while suggestions are transmitted via websockets. Websockets are employed to provide real-time updates to other users accessing the same lunch plan.
3. Redis pub/sub is integrated as a downstream service of the application. In anticipation of potential scalability challenges due to increased user activity, horizontal scaling is considered as a solution. Horizontal scaling becomes crucial as different users may be contributing suggestions to a lunch plan while connected to various instances/servers. Without proper coordination, real-time propagation of messages may not occur across instances/servers. Therefore, the utilization of the Redis PUB/SUB feature becomes essential to ensure seamless communication among all instances/servers, regardless of the user's connection point.