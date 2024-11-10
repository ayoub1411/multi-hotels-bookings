FROM openjdk:19


WORKDIR /app

COPY target/Booking-0.0.1-SNAPSHOT.jar /app/booking.jar

EXPOSE 5001

CMD ["java","-jar","booking.jar"]




