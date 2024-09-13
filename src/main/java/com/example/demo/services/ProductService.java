package com.example.demo.services;

import java.util.Map;
import java.util.Optional;

import com.example.demo.shared.dto.ProductDto;

public interface ProductService {
	ProductDto createProduct(ProductDto productdto);
	ProductDto updateProduct(int code,ProductDto productdto);
	java.util.List<ProductDto> getProducts(int page,int limit);
	void deleteProduct(int id);
	Optional<ProductDto> getProductById(int productId);
	 ProductDto patchProduct(int productId, Map<String, Object> updates);
}
