Akka Slick REST service template
=========================

[![Build Status](https://travis-ci.org/ArchDev/akka-http-rest.svg?branch=master)](https://travis-ci.org/ArchDev/akka-http-rest)

Goal of example is to show how create reactive REST services on Lightband stack with Akka and Slick.

Example contains complete REST service for entity interaction.

### Features:
* CRUD operations
* Entity partial updates
* CORS support
* Authentication with *JWT* tokens
* Test coverage with *ScalaTest*
* Migrations with *FlyWay*
* Ready for *Docker*
* Testing with in-memory postgres instance that launch automatically
* *HikaryCP* as connection pool

## Requirements
* JDK 8 (e.g. [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html));
* sbt ([http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html));

## Development guide
This application is fully tested with Unit and IT tests.
You don't need to launch server locally for development.
My recommendation is to write a test before changes and work via TDD.
To ensure that application working properly, you should run it: `sbt test`.

### Structure
All business logic is located in `core` package, every package inside is related to some domain.
Service classes contains high level logic that related to data manipulation,
that means that service MUST NOT implement storaging and queering for the data.
For storaging there are Storage classes that always have interface with two implementation, production one and
in-memory one. That's needed to fasten tests of services and make it independent from each other.

### Checking code coverage
To generate code coverage report, please run: `sbt clean coverage test coverageReport`.
Then you will have HTML pages with reports in `/target/scala-2.12/scoverage-report`

### Packaging
Application packaging implemented via [sbt-native-packager](https://github.com/sbt/sbt-native-packager) plugin.
Currently in `build.sbt` enabled two types: docker and universal.

**Universal packager**  
To package application as a universal app, use: `sbt universal:packageBin`.
Application jar will be generated in `/target/scala-2.12/` folder.

**Docker packager**   
To package application as docker image, use `sbt docker:publishLocal`.
It will generate and push application image into your local docker store.
For information about publishing to external store, please, read [plugin documentation](http://www.scala-sbt.org/sbt-native-packager/formats/docker.html).

### Running
If you want to launch application locally (its not recommended) you need to start Postgres instance locally and fulfill
some env variables:
 - `JDBC_URL` - url to your database
 - `JDBC_USER` - database username
 - `JDBC_PASSWORD` - database password
 
After that, just run `sbt run` and enjoy hacking. For better expirience you can use `sbt reStart` that will give you ability to
restart application without restarting of sbt.

### Deployment on production
Easiest way to deliver your application, is to do it with docker. Publish image into the store and then use
docker-compose file with structure like in `docker-compose.yml`.

## Live example
Application deployed on heroku and can be accessed by URL [http://akka-http-rest.herokuapp.com/](http://akka-http-rest.herokuapp.com/).   
First request can take some time, because heroku launch up project.  
You can see documentation for this example on [Apiary](http://docs.akkahttprest.apiary.io).

## Copyright
Copyright (C) 2017 Arthur Kushka.  
Distributed under the MIT License.