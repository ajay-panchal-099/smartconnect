package com.programmingtechie.productservice.mapper;

import com.programmingtechie.productservice.dto.RequestProductDTO;
import com.programmingtechie.productservice.dto.ResponseProductDTO;
import com.programmingtechie.productservice.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    //ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ResponseProductDTO productToResponseProductDTO(Product product);

    Product requestProductDTOToProduct(RequestProductDTO productDTO);

    List<ResponseProductDTO> productListToResponseProductDTOList(List<Product> products);

    List<Product> requestProductDTOListToProductList(List<RequestProductDTO> requestProductDTOs);
}
