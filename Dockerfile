##rename to test later

FROM amazoncorretto:25.0.1-al2023

WORKDIR /app

#RUN yum update -y && yum install tar gzip binutils -y && rm -rf /var/cache/yum
RUN dnf install -y tar gzip && dnf clean all

COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src src

ENTRYPOINT ["./mvnw", "test"]