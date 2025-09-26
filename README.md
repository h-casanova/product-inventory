# Product Inventory API

A REST API built with Quarkus and Maven to manage a product inventory with hierarchical categories, allowing creation, modification, and deletion of both products and categories.

The project includes secured endpoints using JWT for authentication and Role-Based Access Control (RBAC), supporting roles like user and admin.
It follows Clean Architecture principles, clearly separating the domain, application, and infrastructure layers.

Unit and integration tests are provided to ensure code quality and reliability.


### Product

- Manage products with full CRUD operations.
- Support for bulk creation of products.
- Pagination, filtering, and sorting on product listings.
- Optimistic locking using @Version to prevent concurrent update issues.
- Validation using Jakarta Bean Validation (@Valid and @NotNull annotations).

#### Category 

- Hierarchical category management with up to three nested levels.
- Add or remove products dynamically from any category.
- Fetch categories with their associated product IDs.

### Security & Authentication

- Integrated with JWT for authentication and authorization.
- Supports admin and user roles
- Two protected endpoints under /protected/*.

### Documentation

- Automatically generated OpenAPI/Swagger documentation (/q/swagger-ui).

### Database

- Uses H2 in-memory database in development and testing environments.

### Optimistic locking

- Products use a @Version field to prevent overwriting changes made by another process.
If two updates occur simultaneously, an error is returned when the version is outdated.

---

# Features

### Core Endpoints
- **POST /products/create-single** — Create a single product
- **POST /products/create-bulk** — Create multiple products
- **GET /products** — Retrieve all products with optional pagination, filtering and sorting
- **GET /products/{id}** — Retrieve a specific product by ID
- **PUT /products/{id}** — Update an existing product (requires version for optimistic locking)
- **DELETE /products/{id}** — Delete a product

- **POST /categories** — Create a new category
- **GET /categories** — Retrieve all categories with their product IDs
- **GET /categories/{id}** — Retrieve a specific category by ID
- **PUT /categories/{id}** — Update the name of an existing category
- **DELETE /categories/{id}** — Delete a category
- **POST /categories/{categoryId}/add-product/{productId}** — Add an existing product to the specified category
- **POST /categories/{categoryId}/remove-product/{productId}** — Remove a product from the specified category

- **GET /protected/admin/products** — Retrieve all products (admin only)
- **GET /protected/user/products** — Retrieve all products (user only)

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
		
		application/service
		
		domain/model/
		
		infrastructure/mapper
		
		infrastructure/persistence/repository/
		
		infrastructure/rest/controller/
		
		infrastructure/rest/dto/
		
		infrastructure/rest/security/

	src/main/resources/
		
		application.properties
		
		application-dev.properties
		
		db/migration/
		
		postman/
		
		security/

	src/test/resources/
		
		application.properties
		
		application-test.properties
		
		db/migration/
		

### Running the Project
##### Prerequisites

	Java 17+
	Maven 3.9+
	Postman
	Git

### Clone the Repository

	git clone https://github.com/h-casanova/product-inventory.git
	
	cd product-inventory

### Setting up JWT

Create the file "publicKey.pem" and "privateKey.pem" in src/main/resources/security/ and put these lines inside both files with the following format:
	
	-----BEGIN PUBLIC KEY-----
	-----END PUBLIC KEY-----
	
Browse into de directory /security through the terminal and generate the privateKey using this command:

	openssl genpkey -algorithm RSA -out privateKey.pem -pkeyopt rsa_keygen_bits:2048
	
Now your "privateKey.pem" should contain a correct value. To do the same with the "publicKey.pem" based on the private we created you must use:

	openssl rsa -pubout -in privateKey.pem -out publicKey.pem
	
Now we have correct keys on our files and credentials can be signed and verified.

The H2 database includes two users with their passwords already hashed with Argon2

	username: admin
	password: admin123
	
and

	username: user
	password: user123
	
The route http://localhost:8085/auth/login expects a header "Content-Type: application/json" and the body with the following format:

	{
	  "username": "admin",
	}
	
Once done the API replies with the user token and additional data for the user logged.

### Run in Dev Mode

	mvn quarkus:dev

The app will be available at: **http://localhost:8085**

### Testing

Run unit and integration tests:

	mvn test
	
Run only integration tests:

	mvn verify

Includes positive (2xx) and negative (4xx/5xx) scenarios.

### Swagger API Documentation

    Swagger UI: http://localhost:8085/openapi/

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