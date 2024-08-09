package com.programmingtechie.orderservice.controller;

import com.programmingtechie.orderservice.dto.RequestOrderDto;
import com.programmingtechie.orderservice.dto.ResponseOrderDto;
import com.programmingtechie.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> createOrder(@RequestBody RequestOrderDto requestOrderDto) {
        return CompletableFuture.supplyAsync(()-> orderService.createOrder(requestOrderDto));
    }

    public CompletableFuture<String> fallbackMethod(RequestOrderDto requestOrderDto, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()-> "Oops! Something went wrong.");
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseOrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllOrders(){
        orderService.deleteAllOrders();
        return "All Orders has been Deleted.";
    }


}
