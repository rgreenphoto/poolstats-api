FROM openjdk:12
ADD poolstats-api-*.jar /app.war
CMD sleep 1 && java -jar /app.war -Dspring-boot.run.arguments=--logging.level.org.booj=TRACE