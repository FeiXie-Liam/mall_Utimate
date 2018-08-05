package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFound;
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
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        productService = new ProductService(productRepository);
    }

    @Test
    public void should_get_product_given_id() {
        //given
        Product product = Product.builder()
                .id(10L)
                .imageUrl("/assets")
                .name("测试")
                .price(20D)
                .unit("个")
                .build();

        Optional<Product> optProduct = Optional.of(product);

        given(productRepository.findById(anyLong()))
                .willReturn(optProduct);

        //when
        Product actual = productService.get(1L);

        //then
        assertThat(actual.getId()).isEqualTo(10L);
        assertThat(actual.getImageUrl()).isEqualTo("/assets");
        assertThat(actual.getName()).isEqualTo("测试");
        assertThat(actual.getPrice()).isEqualTo(20D);
        assertThat(actual.getUnit()).isEqualTo("个");

    }

    @Test(expected = ProductNotFound.class)
    public void should_throw_exception_given_no_exist_id() {
        //given
        Optional<Product> optProduct = Optional.empty();

        given(productRepository.findById(anyLong()))
                .willReturn(optProduct);

        //when
        Product actual = productService.get(1L);

        //then
    }

    @Test
    public void should_add_product_to_repository_given_product() {
        //given
        Product product = Product.builder()
                .id(10L)
                .imageUrl("/assets")
                .name("测试")
                .price(20D)
                .unit("个")
                .build();

        given(productRepository.save(any(Product.class)))
                .willReturn(product);

        //when
        Product actual = productService.add(product);

        //then
        assertThat(actual.getId()).isEqualTo(10L);
        assertThat(actual.getImageUrl()).isEqualTo("/assets");
        assertThat(actual.getName()).isEqualTo("测试");
        assertThat(actual.getPrice()).isEqualTo(20D);
        assertThat(actual.getUnit()).isEqualTo("个");
    }

    @Test
    public void should_get_all_products() {
        //given
        Product product = Product.builder()
                .id(10L)
                .imageUrl("/assets")
                .name("测试")
                .price(20D)
                .unit("个")
                .build();

        List<Product> products = new ArrayList<>();
        products.add(product);

        given(productRepository.findAll())
                .willReturn(products);

        //when
        List<Product> actual = productService.getAll();

        //then
        assertThat(actual.get(0).getId()).isEqualTo(10L);
        assertThat(actual.get(0).getImageUrl()).isEqualTo("/assets");
        assertThat(actual.get(0).getName()).isEqualTo("测试");
        assertThat(actual.get(0).getPrice()).isEqualTo(20D);
        assertThat(actual.get(0).getUnit()).isEqualTo("个");
    }

    @Test
    public void remove() {
    }
}
