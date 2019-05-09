package gawlowska.urszula.discountApplication.controller;

import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.service.DiscountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DiscountController.class)
public class DiscountControllerTest {

    @MockBean
    private DiscountService service;

    @Autowired
    private MockMvc mockMvc;

    private static final String URI = "/api";

    @Test
    public void getProductsWithDiscounts() throws Exception {
        // given
        Map<Product, Discount> productsWithDiscounts = new HashMap<Product, Discount>();
        productsWithDiscounts.put(new Product("Product1", 500), new Discount(25));
        productsWithDiscounts.put(new Product("Product1", 1500), new Discount(75));
        when(service.getProductsWithDiscounts()).thenReturn(productsWithDiscounts);
        final String getProductsWithDiscounts = URI + "/discounts";
        // when
        mockMvc.perform(get(getProductsWithDiscounts))
                // then
                .andExpect(status().isOk());
    }
}
