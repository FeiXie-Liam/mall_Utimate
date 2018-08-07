package com.example.demo.apitest;

import com.example.demo.entity.request.OrderInfoRequest;
import com.example.demo.entity.request.OrderItemInfoRequest;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DBRider
@DBUnit(caseSensitiveTableNames = true, escapePattern = "`")
public class OrderControllerTest {

    @LocalServerPort
    private int port;

    @Test
    @DataSet("orders.yml")
    public void should_return_orders() {
        RestAssured
                .given()
                .port(port)
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DataSet("orders.yml")
    public void should_return_order_given_id() {
        RestAssured
                .given()
                .port(port)
                .when()
                .get("/orders/1")
                .then()
                .statusCode(200)
                .body("orderItemResponses.name", contains("苹果", "笔记本电脑"))
                .body("orderItemResponses.unit", contains("个", "台"))
                .body("orderItemResponses.price", contains(5f, 7000f));
    }

    @Test
    @DataSet("orders.yml")
    @ExpectedDataSet(value = "expectedAddedOrders.yml", ignoreCols = {"order.id"})
    public void should_add_order_given_order() {
        OrderInfoRequest orderInfoRequest = new OrderInfoRequest();
        List<OrderItemInfoRequest> orderItemInfoRequests = new ArrayList<>();
        OrderItemInfoRequest orderItemInfoRequest = new OrderItemInfoRequest();
        orderItemInfoRequest.setProductCount(2);
        orderItemInfoRequest.setProductId(1L);
        orderItemInfoRequests.add(orderItemInfoRequest);
        orderInfoRequest.setOrderItemInfos(orderItemInfoRequests);
        RestAssured
                .given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .body(orderInfoRequest)
                .post("/orders")
                .then()
                .statusCode(201)
                .header("location", notNullValue());
    }

    @Test
    @DataSet("orders.yml")
    @ExpectedDataSet("updateOrders.yml")
    public void should_update_order_given_order_info_request() {
        OrderItemInfoRequest orderItemInfoRequest = new OrderItemInfoRequest();
        orderItemInfoRequest.setProductCount(2);
        orderItemInfoRequest.setProductId(1L);

        RestAssured
                .given()
                .port(port)
                .when()
                .request()
                .contentType(ContentType.JSON)
                .body(orderItemInfoRequest)
                .put("/orders/1")
                .then()
                .statusCode(204);
    }

    @Test
    @DataSet("orders.yml")
    @ExpectedDataSet("addOrderItem.yml")
    public void should_add_order_item_given_product_id(){
        RestAssured
                .given()
                .port(port)
                .when()
                .post("orders/1/orderItems/1")
                .then()
                .statusCode(201)
                .header("Location", notNullValue());
    }

    @Test
    @DataSet("orders.yml")
    @ExpectedDataSet(value = "addOrderItemNotExist.yml")
    public void should_create_order_item_given_a_not_exist_product_id(){
        RestAssured
                .given()
                .port(port)
                .when()
                .post("orders/1/orderItems/3")
                .then()
                .statusCode(201)
                .header("Location", notNullValue());
    }

    @Test
    @DataSet("orders.yml")
    @ExpectedDataSet("deletedOrders.yml")
    public void should_delete_order_item_given_id() {
        RestAssured
                .given()
                .port(port)
                .when()
                .delete("orders/1/orderItems/1")
                .then()
                .statusCode(204);
    }
}