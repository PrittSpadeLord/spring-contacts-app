# Generate tester
FROM amazoncorretto:25.0.1-al2023 AS tester

WORKDIR /app

RUN dnf install -y tar gzip && dnf clean all

COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src src

# Generate builder
FROM amazoncorretto:25.0.1-al2023 AS builder

WORKDIR /app

RUN dnf install -y tar gzip binutils && dnf clean all

COPY --from=tester /app /app

RUN ./mvnw -DskipTests clean package \
    && MODULES=$(jdeps --multi-release 25 -cp "target/lib/*" --ignore-missing-deps --print-module-deps target/spring-contacts-app-1.0.0.jar) \
    && jlink --compress=zip-9 --strip-debug --no-header-files --no-man-pages --add-modules "${MODULES}" --output /app/jlink-runtime

# Set up the runtime
FROM scratch

COPY --from=builder /app/jlink-runtime /usr/lib/jvm/jre-min
COPY --from=builder /app/target/spring-contacts-app-1.0.0.jar /app/app.jar
COPY --from=builder /app/target/lib /app/lib

COPY --from=builder /usr/lib64/ld-linux-x86-64.so.2 /lib64/
COPY --from=builder /usr/lib64/libc.so.6 /lib64/
COPY --from=builder /usr/lib64/libm.so.6 /lib64/
COPY --from=builder /usr/lib64/libz.so.1 /usr/lib64/
COPY --from=builder /usr/lib64/libstdc++.so.6 /usr/lib64/
COPY --from=builder /usr/lib64/libgcc_s.so.1 /usr/lib64/

ENV JAVA_HOME=/usr/lib/jvm/jre-min

ENTRYPOINT ["/usr/lib/jvm/jre-min/bin/java", "-jar", "/app/app.jar"]