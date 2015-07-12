Akka Slick REST service template
=========================

[![Build Status](https://travis-ci.org/ArchDev/akka-http-rest.svg?branch=master)](https://travis-ci.org/ArchDev/akka-http-rest)

Goal of example is to show how create reactive REST services on Typesafe stack with Akka and Slick.

Example contains complete REST service for entity interaction.

### Features:
* CRUD operations
* Entity partial updates
* Implemented authentication by token directive
* Test coverage with *ScalaTest*

### TODO:
* Add database evolution plugin (flyway or liquibase)
* Parallelize tests


## Requirements
* JDK 7+ (e.g. [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html));
* sbt ([http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html));
* PostgreSQL server

## Configuration
* Create database in PostgresSQL 
* Set database settings on application config or set enviroment variables

### Changing application config
There are two config files. Application config `src/main/resources/application.conf` and test config `src/test/resources/application.conf`.

### Enviroment variables
- `PSQL_URL` || `PSQL_TEST_URL` - database url by scheme `jdbc:postgresql://host:port/database-name`
- `PSQL_USER` || `PSQL_TEST_USER` - database user
- `PSQL_PASSWORD` || `PSQL_TEST_PASSWORD` - database password

## Run application
To run application, call:
```
sbt run
```
If you wanna restart your application without reloading of sbt, use:
```
sbt re-start
```

## Run tests
To run tests, call:
```
sbt test
```

## Live example
Application deployed on heroku and can be accessed by URL [http://akka-http-rest.herokuapp.com/](http://akka-http-rest.herokuapp.com/). First request can take some time, because heroku launch up project.

You can see documentation for this example on [Apiary](http://docs.akkahttprest.apiary.io).


## Copyright

Copyright (C) 2015 Arthur Kushka.

Distributed under the MIT License.
