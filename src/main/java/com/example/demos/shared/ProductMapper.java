package com.example.demos.shared;

import org.springframework.stereotype.Component;

import com.example.demo.entities.Product;
import com.example.demo.response.ProductResponse;
import com.example.demo.shared.dto.ProductDto;


@Component
public class ProductMapper {

	public ProductDto toDto(Product productEntity) {
		ProductDto productDto = new ProductDto();
		productDto.setId(productEntity.getId());
		productDto.setCode(productEntity.getCode());
		productDto.setName(productEntity.getName());
		productDto.setDescription(productEntity.getDescription());
		productDto.setImage(productEntity.getImage());
		productDto.setPrice(productEntity.getPrice());
		productDto.setCategory(productEntity.getCategory());
		productDto.setQuantity(productEntity.getQuantity());
		productDto.setRating(productEntity.getRating());
        return productDto;
    }
	
	public ProductResponse toUserResponse(ProductDto productDto) {
        ProductResponse productresponse = new ProductResponse();
        productresponse.setId(productDto.getId());
        productresponse.setCode(productDto.getCode());
        productresponse.setName(productDto.getName());
        productresponse.setDescription(productDto.getDescription());
        productresponse.setImage(productDto.getImage());
        productresponse.setPrice(productDto.getPrice());
        productresponse.setCategory(productDto.getCategory());
        productresponse.setQuantity(productDto.getQuantity());
        productresponse.setRating(productDto.getRating());       
        return productresponse;
    }
}
