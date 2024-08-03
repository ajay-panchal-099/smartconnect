package com.programmingtechie.productservice.controller;

import com.programmingtechie.productservice.dto.RequestProductDTO;
import com.programmingtechie.productservice.dto.ResponseProductDTO;
import com.programmingtechie.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseProductDTO> getAllProduct(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseProductDTO getProductById(@PathVariable long id){
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody RequestProductDTO requestProductDTO){
        productService.createProduct(requestProductDTO);
        return "Product has been Created.";
    }

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.CREATED)
    public String createProducts(@RequestBody List<RequestProductDTO> requestProductDTOs){
        productService.createProducts(requestProductDTOs);
        return "All Products has been Created.";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteByIdProduct(@PathVariable long id){
        System.out.println("Received request to delete product by id=" + id);
        productService.deleteProductById(id);
        return "Product is Deleted with id: " + id;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllProducts(){
        System.out.println("Received request to delete all products");
        productService.deleteAllProducts();
        return "All Products has been Deleted.";
    }
}
