# url-swift
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=bugs)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=code_smells)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=coverage)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=ncloc)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=alert_status)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=security_rating)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=sqale_index)](https://sonarcloud.io/dashboard?id=event-io_url-swift)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=event-io_url-swift&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=event-io_url-swift)

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/url-swift-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- YAML Configuration ([guide](https://quarkus.io/guides/config-yaml)): Use YAML to configure your Quarkus application
- Logging JSON ([guide](https://quarkus.io/guides/logging#json-logging)): Add JSON formatter for console logging
- Reactive PostgreSQL client ([guide](https://quarkus.io/guides/reactive-sql-clients)): Connect to the PostgreSQL database using the reactive pattern

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
