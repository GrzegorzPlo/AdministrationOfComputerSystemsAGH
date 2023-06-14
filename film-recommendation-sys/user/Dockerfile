FROM openjdk:17
EXPOSE 8081
ADD target/user-0.0.1-SNAPSHOT.jar user.jar
ENTRYPOINT ["java","-jar","/user.jar"]