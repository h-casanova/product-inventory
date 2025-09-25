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
	
	Keycloak 26.3.5

	Git

### Clone the Repository

	git clone https://github.com/hcasanova/product-inventory.git
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
	  "password": "admin123"
	}
	
Once done the API replies with the user token and additional data for the user logged.

### Keycloak

Download Keycloak 26.3.5 and start it in order for the login to work. To run Keycloak access the /bin through PowerShell in Windows folder inside and execute:

	.\kc.bat start-dev
	
Or in Linux (untested):

	chmod +x kc.sh
	
		
Keycloak starts by default in http://localhost:8080. Access the URL and setup the user you want for Keycloak use.

Create a "Realm" selecting the "Manage Realms" option in the menu -> Create realm and write the name of your realm, in this case we will be using "product-inventory-realm"

Now we need to create a "Client" by pressing the "Clients" option in the menu -> Create client -> and in the "Cliend ID" field write "product-app", then press "Next" button and now we need to enable "Client authentication" and in "Authentication flow" we need to have active "Standard flow" and "Direct access grants"

Still in the "Client" section, search for your "product-app" client and the "Roles" tab, then press "Create role" and in "Role name" use "admin". Do the same for "user"

Next, we will create the desired users, in this case we want "admin" and "user". Select "Users" in the options menu -> Add user and write "admin" in the "Username" field, then press "Create" button.

Users are created but no passwords are designed to them, so we acces the "Users" option in the menu, press over any of our newly created users and in the "Credentials" label we can press "Set password", we will use "admin123" and uncheck the "Temporary" option so it is "off" and then press on "Save".

Now go to "Users" in the menu, select "admin", click the "Role mapping" and click on "Assign role" -> "Realm roles" and select "admin" and "user" and click on "Assign". Now the token bearer will contain these roles to be decrypted in our API.

Login attempts from clients like Postman have trouble login, but there is a workaround. Go to the "Authentication" option in the menu, select the "Required actions" label you have to disable "Verify profile", "Update profile" and "Verify email".

To get the token with Postman or another similar application, we will also need the "Client Secret" in order to be able to log in. To get this secret we need to find the client we created in "Clients", search for your client "product-app" and click on it. Select the tab "Credentials" and there is the Client Secret available.

Create a new POST petition and write the following parameters in the body, selecting "x-www-form-urlencoded":
	
	grant_type: password
	client_id: product-app
	client_secret: yourclientsecret
	username: admin
	password: admin123

The response should have the access token with some additional information.


### Run in Dev Mode

	./mvnw quarkus:dev

NOTE: Quarkus ships with a Dev UI available at http://localhost:8085/q/dev/

The app will be available at: http://localhost:8085

### Swagger API Documentation

    Swagger UI: http://localhost:8085/q/swagger-ui/

OpenAPI Spec: http://localhost:8085/q/openapi

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