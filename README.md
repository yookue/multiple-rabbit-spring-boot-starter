# Multiple Rabbit Spring Boot Starter

Spring Boot application integrates multiple `RabbitMQ` quickly.

## Quickstart

- Import dependencies

```xml
    <dependency>
        <groupId>com.yookue.springstarter</groupId>
        <artifactId>multiple-rabbit-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> By default, this starter will auto take effect, you can turn it off by `spring.multiple-rabbit.enabled = false`

- Configure Spring Boot `application.yml` with prefix `spring.multiple-rabbit`

```yml
spring:
    multiple-rabbit:
        primary:
            host: '192.168.0.1'
            delayed-exchange:
                name: 'delayed-exchange'
                type: direct
                durable: true
                auto-delete: true
        secondary:
            host: '192.168.0.2'
        tertiary:
            host: '192.168.0.3'
```

> This starter supports 3 RabbitMQ `ConnectionFactory` at most. (Three strikes and you're out)

- **Optional feature**: If you want to use rabbitmq delayed message exchange, you should download the `rabbitmq_delayed_message_exchange` plugin and install it first like this:
```
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

> The plugin download page: https://github.com/rabbitmq/rabbitmq-delayed-message-exchange

- Configure your beans with the following beans by `@Autowired`/`@Resource` annotation, combined with `@Qualifier` annotation (take `primary` as an example)

| Bean Type                | Qualifier                                                |
|--------------------------|----------------------------------------------------------|
| CachingConnectionFactory | PrimaryRabbitAutoConfiguration.CONNECTION_FACTORY        |
| RabbitTemplate           | PrimaryRabbitAutoConfiguration.RABBIT_TEMPLATE           |
| RabbitMessagingTemplate  | PrimaryRabbitAutoConfiguration.RABBIT_MESSAGING_TEMPLATE |
| AmqpAdmin                | PrimaryRabbitAutoConfiguration.AMQP_ADMIN                |
| CustomExchange           | PrimaryRabbitAutoConfiguration.DELAYED_EXCHANGE          |

## Document

- Github: https://github.com/yookue/multiple-rabbit-spring-boot-starter
- RabbitMQ github: https://github.com/rabbitmq/rabbitmq-java-client
- RabbitMQ homepage: https://rabbitmq.com

## Requirement

- jdk 17+

## License

This project is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

See the `NOTICE.txt` file for required notices and attributions.

## Donation

You like this package? Then [donate to Yookue](https://yookue.com/public/donate) to support the development.

## Website

- Yookue: https://yookue.com
