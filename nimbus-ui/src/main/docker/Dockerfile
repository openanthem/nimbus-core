FROM nimbus/com.anthem.nimbus.platform.core.service.image
VOLUME /clientextensions
ADD com.anthem.nimbus.platform.web-1.0.0.M6-SNAPSHOT.jar app.jar
ADD run.sh run.sh
#COPY jrebel.jar /jrebel.jar
RUN chmod +x run.sh
#EXPOSE 8080
#ENTRYPOINT ["java","-jar", "/app.jar"]
ENTRYPOINT ["./run.sh"]
#ENTRYPOINT ["java","-jar", "/app.jar"]
