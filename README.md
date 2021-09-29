# M223 Punchclock

## Dependencies

* Apache Maven 3.8.1 or newer.
* OpenJDK 17 (may work with older versions > 11 too.)

## Setup and Run

1. Clone the github repository (https://github.com/cvanloo/m223-punchclock-quarkus).
```sh
git clone git@github.com:cvanloo/m223-punchclock-quarkus.git
cd 223-punchclock-quarkus.git
```

2. Configure Java home
```sh
export JAVA_HOME="$HOME/.jdks/openjdk-17"
```
The path might be different on your system. The application was only tested with OpenJDK 17, but should
run with any version newer than 11.

3. Execute
```sh
./mvnw compile quarkus:dev
```

## Services

During execution of the application, the database can be accessed via: [http://localhost:8080/h2/].

Datasource: jdbc:h2:mem:punchclock\
Username: zli\
Password: zli

___All content of the database gets lost when the application is recompiled.___

Swagger API: [http://localhost:8080/q/swagger-ui/]

## URLs

The following URLs can be accessed over [http://localhost:8080/] when the application is running.

URI            | Description
-------------- | -----------
/register.html | Users can sign up here.
/login.html    | Login for users and admins.
/index.html    | Punchclock interface for users.
/admin.html    | Admin page.

## User Roles

There are two roles: `User` and `Admin`. When creating an account from the
admin page, the role has to be manually entered into the text field.

When creating an account by signing up on the register page, the account
will be automatically created using the role `User`.

## Admin Accounts

Admin accounts need to be manually created using the H2 interface or on the Admin-Page using a different
admin account.

### Create Admin account using H2

1. Navigate to [http://localhost:8080/h2/]
2. Enter the correct credentials:\
JDBC URL: jdbc:h2:mem:punchclock\
Username: zli\
Password: zli
3. In the "SQL Statement"-field enter:
```SQL
INSERT INTO user (accountname, passwdhash, role)
VALUES ('admin', '$2a$10$d1ZG88VwmtAgE0zs0wVXR.J.wEofuZ7Adl2LnDHiugLx2z4iLhZLG', 'Admin');
```
Then click on "Run".
This creates an account named `admin` using the password `test`.

### Create Admin account using the Admin interface

For that to work, at least one admin account already has to exist.

1. Log into an admin account.
2. Navigate to [http://localhost:8080/admin.html].
3. In the form enter a Username and Password.
4. In Role enter "Admin".
5. Click on `Create`.

## Password Hashing

Bcrypt is used for password hashing.

This requires Quarkus' security-jpa extension:
```sh
./mvnw quarkus:add-extension -Dextensions="security-jpa"
```

___It should already/automatically be installed.___ Otherwise try executing the command above.

Quarkus only has an API to create Bcrypt hashes, but no option to valide them. Instead,
`org.wildfly.security.password` is used for validating hashes.

For convenience, this functionality has been directly implemented into the
`ch.zli.m223.punchclock.domain.User` entity.