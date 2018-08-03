package com.example.demo.entity.request;

import java.util.List;

public class OrderInfoRequest {
    private List<OrderItemInfoRequest> orderItemInfos;

    public List<OrderItemInfoRequest> getOrderItemInfos() {
        return orderItemInfos;
    }

    public void setOrderItemInfos(List<OrderItemInfoRequest> orderItemInfos) {
        this.orderItemInfos = orderItemInfos;
    }
}
