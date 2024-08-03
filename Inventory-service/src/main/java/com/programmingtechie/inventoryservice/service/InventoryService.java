package com.programmingtechie.inventoryservice.service;

import com.programmingtechie.inventoryservice.dto.InventoryResponseDto;
import com.programmingtechie.inventoryservice.dto.RequestInventoryDto;
import com.programmingtechie.inventoryservice.mapper.InventoryMapper;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    InventoryRepository inventoryRepository;

    InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper){
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    public boolean isInStock(String skuCode){
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    public List<InventoryResponseDto> isInStockList(List<String> skuCode){
        List<Inventory>  inventories =  inventoryRepository.findAllBySkuCodeIn(skuCode);
        for (Inventory inventory: inventories){
            System.out.println("Returned data");
            System.out.println(inventory.toString());
        }
        return inventoryMapper.InventoryListToInventoryResponseDtoList(inventories);
    }

    public void createInventory(List<RequestInventoryDto> requestInventories) {
       List<Inventory> inventories = inventoryMapper.RequestInventoryDtoListToInventoryList(requestInventories);
       inventoryRepository.saveAll(inventories);
    }

    public List<InventoryResponseDto> getAllInventory() {
        List<Inventory> inventories =  inventoryRepository.findAll();
        return inventoryMapper.InventoryListToInventoryResponseDtoList(inventories);
    }
}
