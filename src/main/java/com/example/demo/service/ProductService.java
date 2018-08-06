package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFound;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product get(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFound::new);
    }

    public Product add(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    public void update(Long id, Product product) {
        Product selectedProduct = productRepository.findById(id).orElseThrow(ProductNotFound::new);
        selectedProduct.setImageUrl(product.getImageUrl());
        selectedProduct.setUnit(product.getUnit());
        selectedProduct.setPrice(product.getPrice());
        selectedProduct.setName(product.getName());

        productRepository.save(selectedProduct);
    }
}
