package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.OrderInfoRequest;
import com.example.demo.entity.request.OrderItemInfoRequest;
import com.example.demo.exception.OrderNotFound;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    private OrderService orderService;

    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        productService = new ProductService(productRepository);

        orderService = new OrderService(orderRepository, productService, orderItemRepository);
    }

    @Test
    public void should_get_all_orders() {
        //given
        Product product = Product.builder()
                .id(10L)
                .imageUrl("/assets")
                .name("测试")
                .price(20D)
                .unit("个")
                .build();

        OrderItem orderItem = new OrderItem(1L, product, 2, new Order());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = Order.builder()
                .id(1L)
                .orderItems(orderItems)
                .build();

        orderItem.setOrder(order);

        List<Order> orders = new ArrayList<>();
        orders.add(order);
        given(orderRepository.findAll())
                .willReturn(orders);

        //when
        Order actual = orderService.getAll().get(0);

        //then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getOrderItems().get(0).getProductCount()).isEqualTo(2);
        assertThat(actual.getOrderItems().get(0).getProduct().getName()).isEqualTo("测试");
        assertThat(actual.getOrderItems().get(0).getProduct().getPrice()).isEqualTo(20D);

    }

    @Test
    public void should_get_order_given_id(){
        //given
        Product product = Product.builder()
                .id(10L)
                .imageUrl("/assets")
                .name("测试")
                .price(20D)
                .unit("个")
                .build();

        OrderItem orderItem = new OrderItem(1L, product, 2, new Order());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = Order.builder()
                .id(1L)
                .orderItems(orderItems)
                .build();

        orderItem.setOrder(order);
        Optional<Order> optOrder = Optional.of(order);
        given(orderRepository.findById(anyLong()))
                .willReturn(optOrder);
        //when
        Order actual = orderService.get(1L);
        //then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getOrderItems().get(0).getProductCount()).isEqualTo(2);
        assertThat(actual.getOrderItems().get(0).getProduct().getName()).isEqualTo("测试");
        assertThat(actual.getOrderItems().get(0).getProduct().getPrice()).isEqualTo(20D);

    }

    @Test(expected = OrderNotFound.class)
    public void should_throw_exception_given_no_exist_id() {
        //given
        Optional<Order> optOrder = Optional.empty();

        given(orderRepository.findById(anyLong()))
                .willReturn(optOrder);

        //when
        orderService.get(1L);

        //then
    }


    @Test
    public void should_add_order_to_repository_given_order() {
        //given
        Product product = Product.builder()
                .id(10L)
                .imageUrl("/assets")
                .name("测试")
                .price(20D)
                .unit("个")
                .build();
        Optional<Product> optProduct = Optional.of(product);

        OrderItem orderItem = new OrderItem(1L, product, 2, new Order());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = Order.builder()
                .id(1L)
                .orderItems(orderItems)
                .build();

        orderItem.setOrder(order);

//        given(productRepository.findById(anyLong()))
//                .willReturn(optProduct);

        given(orderRepository.save(any()))
                .willReturn(order);

        //when
        OrderInfoRequest orderInfoRequest = new OrderInfoRequest();
        orderInfoRequest.setOrderItemInfos(new ArrayList<>());
        Order actual = orderService.add(orderInfoRequest);

        //then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getOrderItems().get(0).getProductCount()).isEqualTo(2);
        assertThat(actual.getOrderItems().get(0).getProduct().getName()).isEqualTo("测试");
        assertThat(actual.getOrderItems().get(0).getProduct().getPrice()).isEqualTo(20D);
    }

    @Test(expected = OrderNotFound.class)
    public void should_throw_exception_when_update_order_id_not_exist(){
        //given

        OrderItemInfoRequest orderItemInfoRequest = new OrderItemInfoRequest();
        orderItemInfoRequest.setProductCount(2);
        orderItemInfoRequest.setProductId(1L);

        Optional<Order> emptyOrder = Optional.empty();

        given(orderRepository.findById(anyLong()))
                .willReturn(emptyOrder);

        //when
        orderService.update(10L, orderItemInfoRequest);
    }
}
