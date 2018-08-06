package com.example.demo.apitest;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import io.restassured.RestAssured;
import org.dbunit.database.DatabaseConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class OrderControllerTest {
    @LocalServerPort
    int port;

    @Test
    @DatabaseSetup(connection = DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, value = "classpath:/order.xml")
    public void should_get_all_orders() {
        RestAssured
                .given()
                .port(port)
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("size()", is(3));
    }
}
