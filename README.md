# Airlines

Microservices architecture simulating air traffic

## Technologies

- Java 11
- Gradle
- Docker
- Spring Boot
- Kafka
- Redis

## How to use

Build with Gradle and Docker
```
./gradlew build -x test
docker compose up --build -d 
```

Launch an airplane
```
curl -X POST http://localhost:8080/api/v1/airplanes
```

List current airplanes
```
curl http://localhost:8080/api/v1/airplanes
```

Stop
```
docker compose down
```