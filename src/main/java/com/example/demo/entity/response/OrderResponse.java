package com.example.demo.entity.response;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {
    List<OrderItemResponse> orderItemResponses;

    public OrderResponse(Order order) {
        this.orderItemResponses = new ArrayList<>();
        order.getOrderItems().forEach(orderItem -> this.orderItemResponses.add(new OrderItemResponse(orderItem)));
    }

}
