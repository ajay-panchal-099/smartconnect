package com.programmingtechie.inventoryservice.mapper;

import com.programmingtechie.inventoryservice.dto.InventoryResponseDto;
import com.programmingtechie.inventoryservice.dto.RequestInventoryDto;
import com.programmingtechie.inventoryservice.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    List<Inventory> RequestInventoryDtoListToInventoryList(List<RequestInventoryDto> requestInventories);

    Inventory requestInventoryDtoToInventory(RequestInventoryDto requestInventoryDto);

    List<InventoryResponseDto> InventoryListToInventoryResponseDtoList(List<Inventory> inventories);

    @Mapping(target = "isInStock", expression = "java(inventory.getQuantity() > 0)")
    InventoryResponseDto InventoryToInventoryResponseDto(Inventory inventory);

}
