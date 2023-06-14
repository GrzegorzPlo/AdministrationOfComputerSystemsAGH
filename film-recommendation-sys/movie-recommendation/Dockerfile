FROM openjdk:17
EXPOSE 8082
ADD target/movie-recommendation-0.0.1-SNAPSHOT.jar movie-recommendation.jar
ENTRYPOINT ["java","-jar","/movie-recommendation.jar"]