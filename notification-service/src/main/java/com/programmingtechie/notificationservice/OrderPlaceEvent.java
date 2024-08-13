package com.programmingtechie.notificationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlaceEvent {
    private String orderNumber;
}
