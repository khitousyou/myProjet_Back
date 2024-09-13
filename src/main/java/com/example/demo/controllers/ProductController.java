package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.ApiResponse;
import com.example.demo.exceptions.ProductNotFoundException;
import com.example.demo.requests.ProductRequest;
import com.example.demo.response.ProductResponse;
import com.example.demo.services.ProductService;
import com.example.demo.shared.dto.ProductDto;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
    private ModelMapper modelMapper;

	@GetMapping("/get")
    public ResponseEntity<String> getProducts() {
        return ResponseEntity.ok("Liste des produits");
    }
	
	@GetMapping()
	public ResponseEntity<List<ProductResponse>> getAllProducts(
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        @RequestParam(value = "limit", defaultValue = "4") int limit) {

	    // Initialisation de la liste des réponses de produits
	    List<ProductResponse> productsResponse = new ArrayList<>();

	    // Récupération des produits via le service (qui retourne une liste de ProductDto)
	    List<ProductDto> products = productService.getProducts(page, limit);

	    // Conversion des ProductDto en ProductResponse via ModelMapper
	    for (ProductDto productDto : products) {
	        ModelMapper modelMapper = new ModelMapper();
	        ProductResponse productResponse = modelMapper.map(productDto, ProductResponse.class);
	        productsResponse.add(productResponse);
	    }

	    // Retourne la liste des produits sous forme de ResponseEntity avec un statut HTTP OK
	    return new ResponseEntity<List<ProductResponse>>(productsResponse, HttpStatus.OK);
	}

	
	 @PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
             produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) throws Exception {
    ModelMapper modelMapper = new ModelMapper();
    ProductDto productDto = modelMapper.map(productRequest, ProductDto.class);
    ProductDto createdProductDto = productService.createProduct(productDto);
    ProductResponse productResponse = modelMapper.map(createdProductDto, ProductResponse.class);

    // Création de la réponse avec message et données
    ApiResponse<ProductResponse> apiResponse = new ApiResponse<>(
        "Product created successfully",
        productResponse
    );

    return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
}
	
	
	 @PutMapping(path = "updateProductById/{productId}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int productId, @RequestBody ProductRequest productRequest) {
	        System.out.println("controller: " + productId);
	        
	        // Convert ProductRequest to ProductDto
	        ProductDto productDto = modelMapper.map(productRequest, ProductDto.class);
	        
	        // Call service to update the product
	        ProductDto updatedProductDto = productService.updateProduct(productId, productDto);
	        
	        // Convert ProductDto to ProductResponse
	        ProductResponse productResponse = modelMapper.map(updatedProductDto, ProductResponse.class);

	        // Return the response with the ACCEPTED status
	        return new ResponseEntity<>(productResponse, HttpStatus.ACCEPTED);
	    
	}

	
	@DeleteMapping(path = "deleteProductById/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId) {
        try {
            // Appeler le service pour supprimer le produit
            productService.deleteProduct(productId);
            // Retourner un message de confirmation avec le statut HTTP 200 OK
            return new ResponseEntity<>("Product with ID " + productId + " has been successfully deleted.", HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            // Retourner un message d'erreur avec le statut HTTP 404 Not Found si le produit n'existe pas
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
	
	

	 @GetMapping(path = "getProductById/{productId}", produces = { "application/json", "application/xml" })
	    public ResponseEntity<ProductResponse> getProductById(@PathVariable int productId) {
	        Optional<ProductDto> productDtoOptional = productService.getProductById(productId);

	        if (productDtoOptional.isPresent()) {
	            ProductResponse productResponse = modelMapper.map(productDtoOptional.get(), ProductResponse.class);
	            return new ResponseEntity<>(productResponse, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	 
	 @PatchMapping(path = "/{productId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	 public ResponseEntity<ProductResponse> patchProduct(
	         @PathVariable int productId,
	         @RequestBody Map<String, Object> updates) {

	     // Appel du service pour mettre à jour partiellement le produit
	     ProductDto updatedProduct = productService.patchProduct(productId, updates);

	     // Mapper le DTO mis à jour vers la réponse
	     ProductResponse productResponse = modelMapper.map(updatedProduct, ProductResponse.class);

	     return new ResponseEntity<>(productResponse, HttpStatus.OK);
	 }
}
