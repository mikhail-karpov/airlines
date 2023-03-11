# Airlines

Microservices architecture simulating air traffic

## Technologies

- Java 11
- Gradle
- Docker
- Spring Boot
- Kafka
- Redis
- Thymeleaf

## Running

Build with Gradle and Docker
```
./gradlew build -x test
docker compose up --build -d 
```

## Monitoring

Start Prometheus and Grafana
```
docker compose -f docker-compose-monitoring.yaml up -d
```

Grafana dashboards are available at http://localhost:3000 with ```admin:pa55word```
