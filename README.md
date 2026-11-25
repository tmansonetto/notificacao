# Notificacao

Notification microservice of a loan-proposal system. It is consumed by
[proposta-app](https://github.com/tmansonetto/proposta-app), which publishes proposal
events to RabbitMQ. This service listens to those events so that users can be notified
about the progress of their proposal.

> **Status:** early development. The project skeleton, RabbitMQ connection settings and
> dependencies are in place; the message listeners and the notification logic are the next steps.

## Role in the system

```
Front-end (proposta-web) -> proposta-app -> RabbitMQ -> notificacao
                                  ^
                                  |   proposal analysis result
                                  +-------- (other microservices)
```

- **proposta-app** creates the proposal, saves it in PostgreSQL and publishes it to the
  fanout exchange `proposta-pendente.ex`.
- **RabbitMQ** copies each message to every queue bound to that exchange.
- **notificacao** (this service) is intended to consume the queues dedicated to it:

| Queue | Direction | Purpose |
|---|---|---|
| `proposta-pendente.ms-notificacao` | consumed | A new proposal was received (pending analysis) |
| `proposta-concluida.ms-notificacao` | consumed | The analysis finished (approved or denied) |

The exchange and queues are declared by `proposta-app`
(`RabbitMQConfiguration`), so start it at least once before running this service.

## Tech stack

- Java 25
- Spring Boot 4.2.0 (`spring-boot-starter-rabbitmq`)
- RabbitMQ
- AWS SDK for Java v1 (`aws-java-sdk-sns`) for sending notifications through Amazon SNS
- Jackson (JSON messages)
- Lombok
- Maven (wrapper included)

## Prerequisites

- JDK 25
- A running RabbitMQ (management UI on port 15672):

```bash
docker run -d --name my-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

## Configuration

`src/main/resources/application.properties`:

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

`guest/guest` only works against a local RabbitMQ. For any other environment, provide the
credentials through environment variables (for example
`spring.rabbitmq.password=${RABBITMQ_PASSWORD}`) and never commit real secrets.

## Running

```bash
./mvnw spring-boot:run      # Linux / macOS
mvnw.cmd spring-boot:run    # Windows
```

Run the tests with:

```bash
./mvnw test
```

## Related projects

- [proposta-app](https://github.com/tmansonetto/proposta-app): proposal API that produces the
  events consumed here.

## Author

Thiago Mansonetto - [github.com/tmansonetto](https://github.com/tmansonetto)
