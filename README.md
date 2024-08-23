## I was not able able to upload some screenshots. So I published the notion here: https://rebel-handsaw-0e7.notion.site/Java-Microservices-Concept-3a75de4acc2f43b08488cd6ccca03359

Please free to ask any question and suggestion.

![Screenshot 2024-07-31 at 8.33.25 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/f481add8-611f-43c0-bb8e-10a82202c980/Screenshot_2024-07-31_at_8.33.25_PM.png)

![Screenshot 2024-07-31 at 8.33.17 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/dad05f47-0d3d-45ec-92bd-330f46f3b17d/Screenshot_2024-07-31_at_8.33.17_PM.png)

![Screenshot 2024-07-31 at 8.33.47 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/90011572-a0ce-42c4-8d00-b37fe469a584/Screenshot_2024-07-31_at_8.33.47_PM.png)

Basics packages About services:

1. Controller layer
2. DAO Layer (Request, Response)
3. Model Layer
4. Service Layer
5. Repository Layer

### Spring Boot Application Starting code

```bash
@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
```

### H2 Database Settings

```bash
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

# Add @Document(value = “table-name”) on model class for mongo db 

### Repo code

```bash
public interface ProductRepository extends JpaRepository<Product, String> {
}
```

### Constructor Injection

```bash
private final ProductService productService;

public ProductController(ProductService productService) {
    this.productService = productService;
}

# You can use @RequiredArgsConstructor
```

### Java Mapstruct(https://mapstruct.org/)

https://www.baeldung.com/mapstruct

```bash
@Mapper(componentModel = "spring")
public interface ProductMapper {
    //ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ResponseProductDTO productToResponseProductDTO(Product product);

    Product requestProductDTOToProduct(RequestProductDTO productDTO);
}

# Mapper can do the implementation via builder or getter/setter.
# Remember to add correct depencies, check baeldung page
```

### MySQL Config

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/order-service
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

# create db first, otherwise you will get the error
```

### Add data in application at run time

```bash
@Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository){
			return args ->{
				# create model obj and save
			};
    }
```

Next Step is move all service to a parent project and use them as a new module.

### IPC

https://www.baeldung.com/spring-5-webclient

1. Add dependency
2. Add new dir → config for external beans

```bash
@Configuration
public class webClientConfig {
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}
```

![Screenshot 2024-08-02 at 8.07.40 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/88064bc7-b580-4043-9532-488a9dccfa62/Screenshot_2024-08-02_at_8.07.40_PM.png)

# If we have order with 100s of orderlineitem, it is not good to make hundreds call. Just make one call 

```bash
Single Request:
Boolean result =  webClient.get()
                .uri("http://localhost:8083/api/inventory/sku-code")
                .retrieve() -> to get the response
                .bodyToMono(Boolean.class) -> need it to tell the return type
                .block(); -> for synchronous conn.
```

```bash
// inventoryResponseList will be a list, and it won't return anything for those // sku which are not present
        if (inventoryResponseList != null && inventoryResponseList.length == skuCodes.size()) {
        # it will check for all product quantity is present
            allProductInStock =  Arrays.stream(inventoryResponseList).allMatch(InventoryResponseDto::getIsInStock);
        }
```

```bash
# Sending a list of request
List<String>skuCodes =  order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponseDto[] inventoryResponseList =  webClient.get()
                .uri("http://localhost:8083/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponseDto[].class)
                .block();

        boolean allProductInStock =  Arrays.stream(Objects.requireNonNull(inventoryResponseList)).allMatch(InventoryResponseDto::getIsInStock);
        if (allProductInStock){
            orderRepository.save(order);
        }else {
            throw new IllegalAccessException("Product is not in stocks.");
        }

```

### Service Discovery [https://www.baeldung.com/spring-cloud-netflix-eureka]

Discovery Server is a place where all the services  register themselves with server.

![Screenshot 2024-08-03 at 12.21.58 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/82fbfee8-dadd-4564-8b22-0d225e5c267c/Screenshot_2024-08-03_at_12.21.58_PM.png)

Client also store the local copy of discovery server registory. If, for some reason, discovery server is not available, it first check it local copy and make the call.

@enableEurekaServer  → On Discovery Server app

@enableEurekaClient → On all other microservices

![Screenshot 2024-08-03 at 12.24.44 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/de08d146-5799-4bd6-9dd0-75eab9f46aea/Screenshot_2024-08-03_at_12.24.44_PM.png)

### Eureka Server dependency and properties:

### Eureka Client dependency and properties:

```bash
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>

#common for both server and client
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-parent</artifactId>
            <version>2023.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

```bash
@EnableDiscoveryServer
# application properties
spring.application.name=discovery-server

eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

server.port=8761
```

```bash
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

```

```bash
@EnableDiscoveryClient

# application.properties
eureka.client.service-url.defaultZone = http://localhost:8761/eureka

# eureka url for UI
url: http://localhost:8761

```

```bash
# help eureka to recognize muliple instances of a service
eureka.instance.instance-id=${spring.application.name}:${spring.application.instance_id:${random.value}}
```

Other way:

https://stackoverflow.com/questions/76754399/eureka-server-doesnt-discover-multiple-instances-of-the-same-client

### Client Side Load Balancing

```bash
@Configuration
public class webClientConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClient(){
        return WebClient.builder(); -> Builder will create and take the instance at run time
    }
}

# Use service-name to call 
```

### API Gateway

API Gateway Can do multiple things:

1. Route Traffic (Based on Request Headers)
2. Authentication
3. Load Balancing
4. Security
5. SSL Termination

Note: Remember to add api-gateway service as a discovery client 

![Screenshot 2024-08-03 at 5.54.45 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/8256d183-cbb3-49ff-aca7-46f5106da853/Screenshot_2024-08-03_at_5.54.45_PM.png)

```bash
# application.properties
spring.application.name=api-gateway

eureka.client.service-url.defaultZone = http://localhost:8761/eureka
eureka.instance.instance-id=${spring.application.name}:${spring.application.instance_id:${random.value}}

logging.level.root=INFO
logging.level.org.springframework.cloud.gateway.route.RouteDefinitionLayer= INFO
logging.level.org.springframework.cloud.gateway= TRACE

# Product Service Route
spring.cloud.gateway.routes[0].id= product-service
spring.cloud.gateway.routes[0].uri= lb://product-service
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/product

# Inventory Service Route
spring.cloud.gateway.routes[1].id= inventory-service
spring.cloud.gateway.routes[1].uri= lb://inventory-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/api/inventory

# Order Service Route
spring.cloud.gateway.routes[2].id= order-service
spring.cloud.gateway.routes[2].uri= lb://order-service
spring.cloud.gateway.routes[2].predicates[0]=Path=/api/order

```

### Authentication Using KeyClock

Little hectic, learn here: https://www.youtube.com/watch?v=t9O99l4gjAc

Read env var from Application.properties:

![Screenshot 2024-08-07 at 9.51.45 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/d5366bbf-8d68-4d63-950d-22c2dff30c3a/Screenshot_2024-08-07_at_9.51.45_PM.png)

![Screenshot 2024-08-07 at 9.52.13 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/1f8ebf5d-061a-4d26-9c9b-38dbd0b1a5f7/Screenshot_2024-08-07_at_9.52.13_PM.png)

### Circuit Breaker(Resilence4j)

https://www.baeldung.com/spring-cloud-circuit-breaker

https://programmingtechie.com/2024/05/26/spring-boot-microservices-tutorial-part-6/

At any given point of time, a circuit breaker will be in different states like:

- **Open**: This states indicates that the Circuit Breaker is open, and all the traffic going through the Circuit Breaker will be blocked.
- **Half-Open**: In this state, the Circuit Breaker will start allowing gradually the traffic to the remote service **R**
- **Closed**: In this state, the Circuit Breaker will allow all the requests to the service, which means that the service **R** is working well without any problems.

```bash
#Resilinece4j Properties
resilience4j.circuitbreaker.instances.inventory.registerHealthIndicator=true
resilience4j.circuitbreaker.instances.inventory.event-consumer-buffer-size=10
resilience4j.circuitbreaker.instances.inventory.slidingWindowType=COUNT_BASED
resilience4j.circuitbreaker.instances.inventory.slidingWindowSize=5
resilience4j.circuitbreaker.instances.inventory.failureRateThreshold=50
resilience4j.circuitbreaker.instances.inventory.waitDurationInOpenState=5s
resilience4j.circuitbreaker.instances.inventory.permittedNumberOfCallsInHalfOpenState=3
resilience4j.circuitbreaker.instances.inventory.automaticTransitionFromOpenToHalfOpenEnabled=true
resilience4j.circuitbreaker.instances.inventory.minimum-number-of-calls=5
 
#Resilience4J Timeout Properties
resilience4j.timelimiter.instances.inventory.timeout-duration=3s
 
#Resilience4J Retry Properties
resilience4j.retry.instances.inventory.max-attempts=3
resilience4j.retry.instances.inventory.wait-duration=5s
```

!https://cdn-jflfd.nitrocdn.com/XWvMVfzfEJrkgcycxrvewQaIXHcwHADn/assets/images/optimized/rev-af26b99/i0.wp.com/programmingtechie.com/wp-content/uploads/2024/05/4f84c67af7982ed85cb3c10a964101de.image-3.png

### Distributed Tracing

It help us to track the req from start to end.

![Screenshot 2024-08-09 at 2.34.34 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/c116d34b-33aa-4bf4-9efe-3adbee42de09/6121125e-47cd-4a9c-8288-7ccd24a3ce9e/Screenshot_2024-08-09_at_2.34.34_PM.png)

Note: Need to install docker for zipkin server to run on your machine: 

`docker run -d -p 9411:9411 openzipkin/zipkin`

UI: http://localhost:9411/zipkin/

- [ ]  Added the config but traces still not showing up on zipkin server. Need more debugging here.

### Event Driven Architecture

PreReq: https://spring.io/projects/spring-kafka,  https://medium.com/@erkndmrl/kafka-cluster-with-docker-compose-5864d50f677e

Setting up docker compose:

https://www.baeldung.com/ops/kafka-docker-setup

```bash
version: '2'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.4
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181

  kafka:
    image: confluentinc/cp-kafka:7.4.4
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

```bash
cmd:

 docker-compose up -d
 
 docker ps

 docker logs -f <id>
 
 
```

### Kafka[https://spring.io/projects/spring-kafka#overview]

Common

```bash
<dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
```

https://stackoverflow.com/questions/72798060/spring-for-apache-kafka-json-deserialization-exception-class-not-found/72802278#72802278

https://stackoverflow.com/questions/70252047/spring-kafka-error-this-error-handler-cannot-process-serializationexceptions

https://stackoverflow.com/questions/55109508/spring-kafka-no-type-information-in-headers-and-no-default-type-provided

https://docs.spring.io/spring-kafka/reference/kafka/serdes.html

https://stackoverflow.com/questions/53955613/working-with-eureka-clients-programmatically-issue-completed-shut-down-of-disc

https://stackoverflow.com/questions/73747314/how-to-configure-kafka-type-mapping-using-spring-kafka

https://programmingtechie.com/articles/spring-boot-microservices-tutorial-part-8

Order Service

```bash
#kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.template.default-topic=order-placed
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.properties.spring.json.type.mapping=event:com.programmingtechie.orderservice.event.OrderPlacedEvent
spring.kafka.producer.properties.spring.json.add.type.headers=true
spring.kafka.producer.properties.spring.json.trusted.packages=*

### 
private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
kafkaTemplate.send("order-placed", new OrderPlacedEvent(order.getOrderNumber()));
```

Notification Service

```bash
# Kafka Config
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=notificationService
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer

spring.kafka.consumer.properties.spring.json.type.mapping=event:com.programmingtechie.notificationservice.OrderPlaceEvent
spring.kafka.consumer.properties.spring.json.use.type.headers=false
spring.kafka.consumer.properties.spring.json.value.default.type=com.programmingtechie.notificationservice.OrderPlaceEvent

# Type mapping and trusted packages
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.properties.spring.json.add.type.headers=false
```

### Dockerise the Project
