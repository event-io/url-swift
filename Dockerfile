## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS build

ARG URL_SWIFT_HOST=$URL_SWIFT_HOST
ARG URL_SWIFT_API_KEY=$URL_SWIFT_API_KEY
ARG URL_SWIFT_SCHEMA=$URL_SWIFT_SCHEMA
ARG URL_SWIFT_EMAIL=$URL_SWIFT_EMAIL
ARG URL_SWIFT_PWD=$URL_SWIFT_PWD
ARG B62KG_HOST=$B62KG_HOST
ARG B62KG_API_KEY=$B62KG_API_KEY
ARG B62KG_SCHEMA=$B62KG_SCHEMA
ARG B62KG_EMAIL=$B62KG_EMAIL
ARG B62KG_PWD=$B62KG_PWD

ENV URL_SWIFT_HOST=$URL_SWIFT_HOST
ENV URL_SWIFT_API_KEY=$URL_SWIFT_API_KEY
ENV URL_SWIFT_SCHEMA=$URL_SWIFT_SCHEMA
ENV URL_SWIFT_EMAIL=$URL_SWIFT_EMAIL
ENV URL_SWIFT_PWD=$URL_SWIFT_PWD
ENV B62KG_HOST=$B62KG_HOST
ENV B62KG_API_KEY=$B62KG_API_KEY
ENV B62KG_SCHEMA=$B62KG_SCHEMA
ENV B62KG_EMAIL=$B62KG_EMAIL
ENV B62KG_PWD=$B62KG_PWD

COPY --chown=quarkus:quarkus mvnw /code/mvnw
# make /code/mvnw executable by quarkus user
RUN chmod +x /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -q
COPY src /code/src
USER root
# Make target dir and subdirs writable by quarkus user
RUN mkdir -p /code/target && chown -R quarkus:quarkus /code/target
USER quarkus
RUN ./mvnw package -Dnative -q

## Stage 2 : create the docker final image
FROM quay.io/quarkus/quarkus-micro-image:2.0

COPY --from=build /code/target/*-runner /work/application

# set up permissions for user `1001`
RUN chmod 775 /work /work/application \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 8080
USER 1001

CMD ["./work/application", "-Dquarkus.http.host=0.0.0.0"]
