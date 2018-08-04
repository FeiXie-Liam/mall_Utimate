package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.OrderInfoRequest;
import com.example.demo.entity.request.OrderItemInfoRequest;
import com.example.demo.exception.OrderNotFound;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order get(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFound::new);
    }

    public Order add(OrderInfoRequest orderInfoRequest) {
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = new Order();
        orderInfoRequest.getOrderItemInfos().forEach(orderItemInfo -> {
            Product product = productService.get(orderItemInfo.getProductId());
            OrderItem curItem = new OrderItem(product, orderItemInfo.getProductCount(), order);
            orderItems.add(curItem);
        });
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        return order;
    }

    public void update(Long id, OrderItemInfoRequest orderItemInfo) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFound::new);
        List<OrderItem> newOrderItems = order.getOrderItems().stream().peek(orderItem -> {
            if (orderItem.getProduct().getId().equals(orderItemInfo.getProductId())) {
                orderItem.setProductCount(orderItemInfo.getProductCount());
            }
        }).collect(Collectors.toList());
        order.setOrderItems(newOrderItems);
        orderRepository.save(order);
    }
}
