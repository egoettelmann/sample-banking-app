##################
# Build image
##################
FROM maven:3.6.3-openjdk-8 AS build-img

# Defining build folder
WORKDIR /build

# Adding pom.xml and getting dependencies
COPY ./pom.xml .
RUN mvn dependency:go-offline

# Adding sources and building
COPY ./src ./src
RUN mvn clean package

##################
# Run image
##################
FROM openjdk:8-jre-alpine

# Defining runtime folder
WORKDIR /srv/app

# Adding the app
COPY --from=build-img /build/target/*.jar ./app.jar

# Defining default environment
ENV SPRING_PROFILES_ACTIVE=docker

# Running the app
ENTRYPOINT ["java", "-jar", "./app.jar"]
