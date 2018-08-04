package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.request.OrderInfoRequest;
import com.example.demo.entity.request.OrderItemInfoRequest;
import com.example.demo.exception.OrderNotFound;
import com.example.demo.exception.ProductNotFound;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody OrderInfoRequest orderInfoRequest) {
        Order order = orderService.add(orderInfoRequest);
        return ResponseEntity.created(URI.create("/orders" + order.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody OrderItemInfoRequest orderItemInfo) {
        orderService.update(id, orderItemInfo);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(OrderNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void orderNotFoundHandler() {
    }

    @ExceptionHandler(ProductNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void productNotFoundHandler() {
    }

}
