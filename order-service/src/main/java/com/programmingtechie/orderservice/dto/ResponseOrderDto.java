package com.programmingtechie.orderservice.dto;


import com.programmingtechie.orderservice.model.OrderLineItems;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseOrderDto {
    private long id;
    private String orderNumber;
    private List<ResponseOrderLineItems> orderLineItems;
}
