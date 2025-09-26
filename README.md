# Product Inventory API

A REST API built with Quarkus and Maven to manage a product inventory with hierarchical categories, allowing creation, modification, and deletion of both products and categories.

The project includes secured endpoints using Keycloak for authentication and Role-Based Access Control (RBAC), supporting roles like user and admin.
It follows Clean Architecture principles, clearly separating the domain, application, and infrastructure layers.

Unit and integration tests are provided to ensure code quality and reliability.


### Product

- Manage products with full CRUD operations.
- Support for bulk creation of products.
- Pagination, filtering, and sorting on product listings.
- Optimistic locking using @Version to prevent concurrent update issues.
- Validation using Jakarta Bean Validation (@Valid and @NotNull annotations).
- Products can belong to up to three hierarchical category levels (category_1, category_2, category_3).

#### Category 

- Hierarchical category management with up to three nested levels.
- Add or remove products dynamically from any category.
- Fetch categories with their associated product IDs.
- Bulk delete and category filtering support.

### Security & Authentication

- Integrated with Keycloak for authentication and authorization.
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
	Keycloak 26.3.5
	Postman
	Git

### Clone the Repository

	git clone https://github.com/hcasanova/product-inventory.git
	
	cd product-inventory

### Keycloak Setup

Download Keycloak 26.3.5. To run Keycloak access the /bin through PowerShell in Windows folder inside and execute:

	.\kc.bat start-dev
	
Or in Linux (untested):

	chmod +x kc.sh
	
Keycloak starts by default at http://localhost:8080	

Create a Realm by selecting the "Manage Realms" option in the menu → click Create realm and enter the name of your realm. In this case, we will use "product-inventory-realm".

Next, create a Client: go to the "Clients" option in the menu → click Create client → in the "Client ID" field, enter "product-app" → press the Next button. Then, enable Client authentication. In "Authentication flow," make sure Standard Flow and Direct Access Grants are active.

Still in the Client section, find your "product-app" client and go to the Roles tab → click Create role → in "Role name," enter "admin". Repeat the process for "user".

Now, create the desired users. In this case, we want "admin" and "user": go to Users in the menu → click Add user → enter "admin" in the "Username" field → click Create.

Users are created, but no passwords are assigned yet. To set a password, go to Users → select the user you created → go to the Credentials tab → click Set password. Enter "admin123", uncheck the Temporary option, and click Save.

Next, assign roles to the users: go to Users → select "admin" → go to Role Mapping → click Assign Role → Realm Roles → select "admin" and "user" → click Assign. Now the bearer token will contain these roles, which can be decrypted in your API.

If login attempts from clients like Postman fail, there is a workaround: go to Authentication in the menu → select Required Actions → disable Verify profile, Update profile, and Verify email.

To get the token with Postman or another similar application, you will need the Client Secret. To obtain it: go to Clients, select your "product-app" client → go to the Credentials tab. The Client Secret will be available there.

Finally, create a new POST request and add the following parameters in the body, selecting x-www-form-urlencoded:
	
	grant_type: password
	client_id: product-app
	client_secret: yourclientsecret
	username: admin
	password: admin123

The response should have the access token with some additional information.


### Run in Dev Mode

	mvn quarkus:dev

The app will be available at: **http://localhost:8085**

### Testing

Run unit and integration tests:

	mvnw test
	
Run only integration tests:

	mvnw verify

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