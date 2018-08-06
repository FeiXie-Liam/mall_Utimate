package com.example.demo.entity.response;

import com.example.demo.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long orderItemId;
    private String name;
    private String unit;
    private Double price;
    private int count;

    public OrderItemResponse(OrderItem orderItem){
        this.orderItemId = orderItem.getId();
        this.name = orderItem.getProduct().getName();
        this.unit = orderItem.getProduct().getUnit();
        this.price = orderItem.getProduct().getPrice();
        this.count = orderItem.getProductCount();
    }
}
