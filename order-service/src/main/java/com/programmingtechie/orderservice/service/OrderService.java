package com.programmingtechie.orderservice.service;


import com.programmingtechie.orderservice.dto.InventoryResponseDto;
import com.programmingtechie.orderservice.dto.RequestOrderDto;
import com.programmingtechie.orderservice.dto.ResponseOrderDto;
import com.programmingtechie.orderservice.mapper.OrderMapper;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WebClient.Builder webClientBuilder;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.webClientBuilder = webClientBuilder;
    }

    public void createOrder(RequestOrderDto requestOrderDto) throws IllegalAccessException {

        Order order = orderMapper.RequestOrderDtoToOrder(requestOrderDto);
        // Check if the product is in stock
//        Boolean result =  webClientBuilder.get()
//                .uri("http://localhost:8083/api/inventory")
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .block();
//        if (Boolean.TRUE.equals(result)){
//            orderRepository.save(order);
//        }else {
//            throw new IllegalAccessException("Product is not in stocks.");
//        }

        List<String>skuCodes =  order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();
        System.out.println(skuCodes);
        InventoryResponseDto[] inventoryResponseList =  webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponseDto[].class)
                .block();


        boolean allProductInStock = false;
        // inventoryResponseList will be a list, and it won't return anything for those sku which are not present
        if (inventoryResponseList != null && inventoryResponseList.length == skuCodes.size()) {
            System.out.println(inventoryResponseList.length);
            for(InventoryResponseDto inventoryResponseDto: inventoryResponseList){
                System.out.println(inventoryResponseDto.toString());
            }
            allProductInStock =  Arrays.stream(inventoryResponseList).allMatch(InventoryResponseDto::getIsInStock);
        }

        if (allProductInStock){
            orderRepository.save(order);
        }else {
            throw new IllegalAccessException("Product is not in stocks.");
        }
    }

    public List<ResponseOrderDto> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderMapper.OrderToResponseOrderDto(orderList);
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
