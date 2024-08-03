package com.programmingtechie.orderservice.mapper;

import com.programmingtechie.orderservice.dto.RequestOrderDto;
import com.programmingtechie.orderservice.dto.ResponseOrderDto;
import com.programmingtechie.orderservice.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order RequestOrderDtoToOrder(RequestOrderDto requestOrderDto);

    List<ResponseOrderDto> OrderToResponseOrderDto(List<Order> orderList);

//    ResponseOrderDto OrderToResponseOrderDto(Order order);
}
