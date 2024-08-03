package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryResponseDto;
import com.programmingtechie.inventoryservice.dto.RequestInventoryDto;
import com.programmingtechie.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {

    InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    //http://localhost:8083/api/inventory/iphone-13,<iphone-13-red>
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode){
        return inventoryService.isInStock(skuCode);
    }

    //http://localhost:8083/api/inventory?sku-code=iphone-13&sku-code=iphone-13-red
    //variables as a req perimeter
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> isInStockList(@RequestParam List<String> skuCode){
        return inventoryService.isInStockList(skuCode);
    }



    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody List<RequestInventoryDto> requestInventories){
        inventoryService.createInventory(requestInventories);
        return "Inventory has been Updated.";
    }

//    @GetMapping()
//    @ResponseStatus(HttpStatus.OK)
//    public List<InventoryResponseDto> getAllInventory(){
//        return inventoryService.getAllInventory();
//
//    }
}
