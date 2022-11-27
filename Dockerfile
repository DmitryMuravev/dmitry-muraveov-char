FROM openjdk:11

COPY target/chat-1.0.0.jar chat-1.0.0.jar

CMD ["java", "-jar", "/chat-1.0.0.jar"]
