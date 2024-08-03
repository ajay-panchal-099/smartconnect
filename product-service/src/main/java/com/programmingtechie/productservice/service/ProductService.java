package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.RequestProductDTO;
import com.programmingtechie.productservice.dto.ResponseProductDTO;
import com.programmingtechie.productservice.mapper.ProductMapper;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void createProduct(RequestProductDTO requestProductDTO) {
        Product product = productMapper.requestProductDTOToProduct(requestProductDTO);
        productRepository.save(product);
    }

    public ResponseProductDTO getProductById(long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(productMapper::productToResponseProductDTO).orElse(null);
    }

    public List<ResponseProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.productListToResponseProductDTOList(products);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }

    public void createProducts(List<RequestProductDTO> requestProductDTOs) {
        List<Product> products = productMapper.requestProductDTOListToProductList(requestProductDTOs);
        productRepository.saveAll(products);
    }
}
