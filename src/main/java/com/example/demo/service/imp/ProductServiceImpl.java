package com.example.demo.service.imp;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.exceptions.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;
import com.example.demo.shared.dto.ProductDto;
import com.example.demos.shared.ProductMapper;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import org.springframework.util.ReflectionUtils;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.ModelMapper;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductMapper productmapper;
	
	@Autowired
    private ModelMapper modelMapper;

	@Override
	public ProductDto createProduct(ProductDto productdto) {
		 // Vérifiez si le produit existe déjà
	    Product existingProduct = productRepository.findByCode(productdto.getCode());
	    if (existingProduct != null) {
	        throw new RuntimeException("Product Already Exists!");
	    }

	    // Utiliser ModelMapper pour mapper ProductDto vers Product
	    ModelMapper modelMapper = new ModelMapper();
	    Product productEntity = modelMapper.map(productdto, Product.class);

	    Product savedProduct = productRepository.save(productEntity);

	    ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);

	    return savedProductDto;
	}
	@Override
	public ProductDto updateProduct(int productId, ProductDto productDto) {
	    // Trouver le produit existant par ID
	    Optional<Product> optionalProduct = productRepository.findById(productId); // findById retourne Optional<Product>
	    
	    // Vérifier si le produit existe
	    if (!optionalProduct.isPresent()) {
	        throw new ProductNotFoundException("Product with ID " + productId + " not found");
	    }
	    
	    // Obtenir le produit existant
	    Product existingProduct = optionalProduct.get();
	    
	    // Mettre à jour les propriétés du produit existant avec les nouvelles valeurs
	    existingProduct.setNumber(productDto.getNumber());
	    existingProduct.setCode(productDto.getCode());
	    existingProduct.setName(productDto.getName());
	    existingProduct.setDescription(productDto.getDescription());
	    existingProduct.setPrice(productDto.getPrice());
	    existingProduct.setQuantity(productDto.getQuantity());
	    existingProduct.setInventoryStatus(productDto.getInventoryStatus());
	    existingProduct.setCategory(productDto.getCategory());
	    existingProduct.setImage(productDto.getImage());
	    existingProduct.setRating(productDto.getRating());

	    // Enregistrer les modifications
	    Product updatedProduct = productRepository.save(existingProduct); // La méthode save retourne un Product

	    // Convertir en DTO et retourner
	    return modelMapper.map(updatedProduct, ProductDto.class);
	}


	@Override
	public List<ProductDto> getProducts(int page, int limit) {
		if (page > 0)
			page = page - 1;
		Pageable pageable = PageRequest.of(page, limit);
		Page<Product> userPage = productRepository.findAll(pageable);
		// userPage.getContent contien la liste des users
		// Conversion des entités en DTOs
		return userPage.getContent().stream().map(productmapper::toDto).collect(Collectors.toList());
	}

	@Override
    public void deleteProduct(int productId) {
        // Vérifiez si le produit existe avant de tenter de le supprimer
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found");
        }
        // Supprimer le produit
        productRepository.deleteById(productId);
    }
	
	@Override
	public Optional<ProductDto> getProductById(int productId) {
	    Optional<Product> productOptional = productRepository.findById(productId);

	    if (productOptional.isPresent()) {
	        // Convertir le produit en DTO
	        ProductDto productDto = modelMapper.map(productOptional.get(), ProductDto.class);
	        return Optional.of(productDto);
	    }

	    // Retourner un Optional vide si le produit n'existe pas
	    return Optional.empty();
	}

	
	@Override
	public ProductDto patchProduct(int productId, Map<String, Object> updates) {
	    // Récupérer le produit par ID
	    Optional<Product> optionalProduct = productRepository.findById(productId);
	    
	    if (!optionalProduct.isPresent()) {
	        throw new ProductNotFoundException("Product with ID " + productId + " not found");
	    }

	    Product existingProduct = optionalProduct.get();

	    // Appliquer les mises à jour partielles
	    updates.forEach((key, value) -> {
	        Field field = ReflectionUtils.findField(Product.class, key);
	        if (field != null) {
	            field.setAccessible(true);
	            ReflectionUtils.setField(field, existingProduct, value);
	        }
	    });

	    // Enregistrer le produit mis à jour
	    Product updatedProduct = productRepository.save(existingProduct);

	    // Retourner le DTO mis à jour
	    return modelMapper.map(updatedProduct, ProductDto.class);
	}



	

	
}
