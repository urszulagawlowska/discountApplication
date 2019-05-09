package gawlowska.urszula.discountApplication.controller;

import gawlowska.urszula.discountApplication.exceptions.BadInputDataException;
import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping(value = "/discounts")
    public ResponseEntity<Map<Product, Discount>> getProductsWithDiscounts() throws BadInputDataException {
        return ResponseEntity.ok(discountService.getProductsWithDiscounts());
    }
}
