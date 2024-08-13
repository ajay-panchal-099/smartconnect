package com.programmingtechie.notificationservice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;
@SpringBootApplication
@EnableDiscoveryClient
public class NotificationService {
    public static void main(String[] args) {
        SpringApplication.run(NotificationService.class, args);
    }

    @KafkaListener(topics = "order-placed")
    public void listen(ConsumerRecord<String, OrderPlaceEvent> record){
        try {
            OrderPlaceEvent message = record.value();
            // Process the message
            System.out.println("Notification Received for Order with id: " + message.getOrderNumber());
        } catch (Exception e) {
            // Handle the exception (e.g., log it)
            System.err.println("Error processing message: " + e.getMessage());
        }

    }
}
