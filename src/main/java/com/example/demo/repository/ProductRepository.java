package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entities.Product;


public interface ProductRepository extends MongoRepository<Product, Integer>{

	
	    //data layer
		Product findByCode(String code);
		Page<Product> findAll(Pageable pageable);
		//Product findById(int id);
		Optional<Product> findById(int productId);
		void deleteById(int id);
}
