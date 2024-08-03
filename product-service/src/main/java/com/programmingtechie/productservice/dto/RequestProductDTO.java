package com.programmingtechie.productservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class RequestProductDTO {
    private String name;
    private String description;
    private BigDecimal price;

}
