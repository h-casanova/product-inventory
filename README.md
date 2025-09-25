# Product Inventory API

A REST API built with **Quarkus** and **Maven** to manage a product inventory.  
This project implements CRUD operations for products, with pagination, validation, and optimistic locking to prevent concurrent update issues.

---

# Features

### Core Endpoints
- **POST /products** — Create a new product
- **GET /products** — Retrieve all products with pagination (10 products per page)
- **GET /products/{id}** — Retrieve a specific product by ID
- **PUT /products/{id}** — Update an existing product with optimistic locking
- **DELETE /products/{id}** — Delete a product

### Request Example: Create a Product

POST /products
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1200.50,
  "quantity": 5
}

### Response Example

{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1200.50,
  "quantity": 5,
  "version": 0
}

### Advanced Features (Expected)

	Filtering products by name or price range

	Sorting products by name or price

	Category entity with one-to-many relationship to Product

	Authentication and role-based access control with JWT

	Database initialization and versioning with Flyway

### Tech Stack

	Java 17

	Quarkus 3.26.4

	H2 Database (in-memory or file-based)

	Hibernate ORM with Panache

	OpenAPI + Swagger UI for API documentation

	JUnit 5 + Rest Assured for testing

### Folder Structure

	pom.xml

	src/main/java/com/hcasanova/product_inventory/

		domain/model/

		application/service/

		infrastructure/persistence/repository/

		infrastructure/rest/controller/

		infrastructure/rest/dto/

	src/main/resources/

		application.properties

		db/initialDataFeed.sql

	src/test/resources/
	
		application-test.properties
		
		db/initialDataFeed.sql

### Running the Project
##### Prerequisites

	Java 17+

	Maven 3.9+

	Git

### Clone the Repository

	git clone https://github.com/hcasanova/product-inventory.git
	cd product-inventory

### Run in Dev Mode

	./mvnw quarkus:dev

    NOTE: Quarkus ships with a Dev UI available at http://localhost:8080/q/dev/

The app will be available at: http://localhost:8080

### Swagger API Documentation

    Swagger UI: http://localhost:8080/q/swagger-ui/

OpenAPI Spec: http://localhost:8080/q/openapi

### Testing

Run unit and integration tests:

	./mvnw test
	
Run only integration tests:

	./mvnw verify

Includes positive (2xx) and negative (4xx/5xx) scenarios.

### Packaging and Running

##### Package the application:

	./mvnw package

The application produces quarkus-run.jar in target/quarkus-app/ and can be run with:

	java -jar target/quarkus-app/quarkus-run.jar

##### To create an über-jar:

	./mvnw package -Dquarkus.package.jar.type=uber-jar

##### Run with:

	java -jar target/*-runner.jar

##### Creating a Native Executable

	./mvnw package -Dnative

##### Or with container build if GraalVM is not installed:

	./mvnw package -Dnative -Dquarkus.native.container-build=true

##### Run the native executable:

	./target/product-inventory-1.0.0-SNAPSHOT-runner

### H2 Database Configuration

Default configuration (src/main/resources/application.properties):

	quarkus.datasource.db-kind=h2
	quarkus.datasource.jdbc.url=jdbc:h2:./productDB
	quarkus.datasource.username=desiredUser
	quarkus.datasource.password=desiredPassword

	quarkus.hibernate-orm.database.generation=drop-and-create

### Optimistic Locking

Products use a @Version field to prevent overwriting changes made by another process.
If two updates occur simultaneously, an error is returned when the version is outdated.


### License
MIT License

Copyright (c) 2025 Humberto Casanova Lebrero

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

This project is for evaluation purposes only. You retain full ownership of the code.