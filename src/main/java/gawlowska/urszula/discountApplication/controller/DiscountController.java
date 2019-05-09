package gawlowska.urszula.discountApplication.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gawlowska.urszula.discountApplication.exceptions.BadInputDataException;
import gawlowska.urszula.discountApplication.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping(value = "/discounts", produces = "application/json")
    public ResponseEntity<String> getProductsWithDiscounts() throws BadInputDataException {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String json = gson.toJson(discountService.getProductsWithDiscounts());
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
