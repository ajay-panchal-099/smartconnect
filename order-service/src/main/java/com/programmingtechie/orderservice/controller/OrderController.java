package com.programmingtechie.orderservice.controller;

import com.programmingtechie.orderservice.dto.RequestOrderDto;
import com.programmingtechie.orderservice.dto.ResponseOrderDto;
import com.programmingtechie.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody RequestOrderDto requestOrderDto) throws IllegalAccessException {
        orderService.createOrder(requestOrderDto);
        return "Order has been Created";
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
