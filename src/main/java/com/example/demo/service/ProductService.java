package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFound;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public Product get(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFound::new);
        return product;
    }
}
