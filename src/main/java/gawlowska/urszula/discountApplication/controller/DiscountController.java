package gawlowska.urszula.discountApplication.controller;

import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping(value = "/discounts")
    public ResponseEntity<?> getProductsWithDiscounts() {
        if (discountService.getProductsWithDiscounts().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect input data");
        } else {
            return ResponseEntity.ok(discountService.getProductsWithDiscounts());
        }
    }
}
