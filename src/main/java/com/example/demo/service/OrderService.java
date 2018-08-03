package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.exception.OrderNotFound;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order get(Long id){
        return orderRepository.findById(id).orElseThrow(OrderNotFound::new);
    }
}
