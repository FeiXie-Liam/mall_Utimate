package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.OrderInfoRequest;
import com.example.demo.entity.request.OrderItemInfoRequest;
import com.example.demo.exception.OrderNotFound;
import com.example.demo.repository.OrderItemRepository;
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

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    public void addOrderItem(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems.stream()
                .filter(orderItem -> orderItem.getProduct().getId().equals(productId))
                .collect(Collectors.toList()).size() == 0) {
            orderItems.add(new OrderItem(productService.get(productId), 1, order));
        } else {
            List<OrderItem> newOrderItems = orderItems.stream().map(orderItem -> {
                if (orderItem.getProduct().getId().equals(productId)) {
                    orderItem.setProductCount(orderItem.getProductCount() + 1);
                }
                return orderItem;
            }).collect(Collectors.toList());
            orderItems = newOrderItems;
        }
        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }

    public void deleteOrderItem(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItem> selectedOrderItem = orderItems.stream()
                .filter(orderItem -> orderItem.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
        if (selectedOrderItem.size() != 0) {
            orderItems.remove(selectedOrderItem.get(0));
            orderItemRepository.delete(selectedOrderItem.get(0));
        }
        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }
}
