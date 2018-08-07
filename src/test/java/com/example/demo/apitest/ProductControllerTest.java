package com.example.demo.apitest;

import com.example.demo.entity.Product;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class ProductControllerTest {

    @LocalServerPort
    int port;

    @Test
    @DatabaseSetup("classpath:/product.xml")
    public void should_get_product_given_product_id() {
        given()
                .port(port)
                .when()
                .get("/products/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Mr"));
    }

    @Test
    @DatabaseSetup("classpath:/product.xml")
    public void should_get_all_product() {
        given()
                .port(port)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("id", contains(1, 2))
                .body("name", contains("Mr", "Mr1"));
    }

    @Test
    @DatabaseSetup("classpath:/product.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:/expectedAddedProduct.xml")
    public void should_add_product_given_product() {
        Product product = new Product();
        product.setName("add product");
        product.setImageUrl("./images");
        product.setPrice(3D);
        product.setUnit("元");

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .body(product)
                .post("/products")
                .then()
                .statusCode(201)
                .header("location", notNullValue());

    }

    @Test
    @DatabaseSetup("classpath:/product.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:/expectedUpdateProduct.xml")
    public void should_update_product_when_call_update() {
        Product product = new Product();
        product.setName("updateTest");
        product.setImageUrl("./image");
        product.setPrice(3D);
        product.setUnit("分");

        given().
                port(port)
                .when()
                .request()
                .contentType(ContentType.JSON)
                .body(product)
                .put("/products/1")
                .then()
                .statusCode(204);
    }
}