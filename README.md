# VendingMachineDemo

This is an implementation of RESTful API for using a vending machine.

## Requirements
- [Docker](https://www.docker.com/)

## Installation
1. Clone the repository
2. Build the project as a root user
```
docker build -t vending-machine-demo .
```
3. Run the project as a root user
```
docker run -p 8080:8080 vending-machine-demo
```

The app is running on http://localhost:8080/api/

## Installation without docker (not recommended)
### Requirements
- Java 11
- Maven 3.6.*

### Installation
1. Clone the repository
2. Build the project
```
mvn clean install
```
3. Run the project
```
mvn spring-boot:run
``` 

The app is running on http://localhost:8080/api/
## Endpoint documentation
After starting the application, a swagger documentation can be found on the following link:
http://localhost:8080/api/swagger-ui/index.html#/ .

## App usage
For easily testing and using the app, there is a postman collection, that might be found in the folder 
`postmanCollections`. Due to the fact, that no  persistent database is used, the `uuid`s in the collection should be 
updated after the application start.

<b>Example of using the application</b>
1. Get a list of the available products in the vending machine.
2. Insert coins in the machine.
3. Buy the chosen product.



