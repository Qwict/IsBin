# The IsBin application

This application will allow users to add books to a favorite list.
It also allows administrators to add books to the database.

## Installation

Create the application.properties file in the src/main/resources folder.
Add the following properties to the file:

It is also important to have a running MySQL database. I configure my databse to always use SSL.
This means that it is important to have the following files:
#src/main/resources/keys/client-key.pem
#src/main/resources/certs/client-cert.pem
#src/main/resources/certs/ca-cert.pem


```properties
spring.datasource.url=jdbc:mysql://{example.com}/{schemeName}?useSSL=true&serverTimezone=UTC
spring.datasource.sslCa=ca.pem
spring.datasource.sslCert=client-cert.pem
spring.datasource.privateKey=client-key.pem
spring.datasource.username={schemeDatabaseUser}
spring.datasource.password={passwordForSchemeDatabaseUser

application.port={portNumber}

spring.jpa.hibernate.ddl-auto=update
```