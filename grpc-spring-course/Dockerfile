FROM maven:3-amazoncorretto-11 AS builder

WORKDIR /usr/src/app/

COPY ./ /usr/src/app/

RUN mvn clean install





FROM amazoncorretto:11-alpine

WORKDIR /usr/src/app/

COPY --from=builder /usr/src/app/target/grpc-spring-course-0.0.1-SNAPSHOT.jar ./app.jar

RUN apk add --no-cache openssl
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

ENTRYPOINT [ "java","-jar","app.jar" ]
CMD [ "dockerize","-wait","tcp://db:5432","-timeout","20s"]

EXPOSE 9090