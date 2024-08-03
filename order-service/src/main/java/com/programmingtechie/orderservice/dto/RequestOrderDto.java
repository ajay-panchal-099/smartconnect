package com.programmingtechie.orderservice.dto;


import com.programmingtechie.orderservice.model.OrderLineItems;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderDto {
    private String orderNumber;
    private List<RequestOrderLineDto> orderLineItems;
}
