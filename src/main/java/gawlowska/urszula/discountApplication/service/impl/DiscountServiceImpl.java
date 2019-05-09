package gawlowska.urszula.discountApplication.service.impl;

import gawlowska.urszula.discountApplication.exceptions.BadInputDataException;
import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.repository.DiscountRepository;
import gawlowska.urszula.discountApplication.repository.ProductRepository;
import gawlowska.urszula.discountApplication.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscountServiceImpl implements DiscountService {

    public static final int TWO_DECIMAL_PLACES = 2;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private List<Product> allProducts;
    private double totalDiscountAmount;

    public DiscountServiceImpl(@Autowired ProductRepository productRepository, @Autowired DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.allProducts = productRepository.getAllProducts();
        this.totalDiscountAmount = discountRepository.getDiscount().getAmount();
    }

    @Override
    public Map<Product, Discount> getProductsWithDiscounts() throws BadInputDataException {
        Map<Product, Discount> productsWithDiscounts = new HashMap<>();
        if (!validateData()) {
            throw new BadInputDataException("Incorrect input data provided.");
        }
        double roundedProductsTotalCost = roundToTwoDecimalPlaces(getProductsTotalCost(), BigDecimal.ROUND_HALF_UP);
        double roundedTotalDiscountAmount = roundToTwoDecimalPlaces(totalDiscountAmount, BigDecimal.ROUND_HALF_UP);
        if (roundedProductsTotalCost <= roundedTotalDiscountAmount) {
            assignProductPriceAsDiscount(productsWithDiscounts);
        } else {
            calculateDiscountsForProducts(productsWithDiscounts);
        }
        return productsWithDiscounts;
    }

    private void assignProductPriceAsDiscount(Map<Product, Discount> productsWithDiscounts) {
        for (Product product : allProducts) {
            productsWithDiscounts.put(product, new Discount(product.getPrice()));
        }
    }

    private void calculateDiscountsForProducts(Map<Product, Discount> productsWithDiscounts) {
        double unitDiscount = calculateDiscountPerOneUnit();
        for (int i = 0; i < allProducts.size(); i++) {
            Product product = allProducts.get(i);
            boolean isLastProduct = i == allProducts.size() - 1;
            boolean listContainsMoreThanOneProduct = allProducts.size() > 1;
            if (listContainsMoreThanOneProduct && isLastProduct) {
                productsWithDiscounts.put(product, new Discount(calculateDiscountForLastProduct(productsWithDiscounts)));
            } else {
                productsWithDiscounts.put(product, new Discount(calculateDiscountPerProduct(product, unitDiscount)));
            }
        }
    }

    private double calculateDiscountForLastProduct(Map<Product, Discount> productDiscountMap) {
        double grantedDiscount = productDiscountMap.entrySet().stream().mapToDouble(entry -> entry.getValue().getAmount()).sum();
        return roundToTwoDecimalPlaces(totalDiscountAmount - grantedDiscount, BigDecimal.ROUND_HALF_UP);
    }

    private double calculateDiscountPerProduct(Product product, double discountPerOneUnit) {
        return roundToTwoDecimalPlaces(product.getPrice() * discountPerOneUnit, BigDecimal.ROUND_DOWN);
    }

    private double getProductsTotalCost() {
        return allProducts.stream().mapToDouble(Product::getPrice).sum();
    }

    private double calculateDiscountPerOneUnit() {
        return totalDiscountAmount / getProductsTotalCost();
    }

    private double roundToTwoDecimalPlaces(double doubleToRound, int roundingMode) {
        return new BigDecimal(doubleToRound).setScale(TWO_DECIMAL_PLACES, roundingMode).doubleValue();
    }

    private boolean validateData() {
        boolean dataAreValid = true;
        boolean productsAreValid = true;
        boolean discountIsValid = true;

        if (totalDiscountAmount < 0) {
            discountIsValid = false;
        }
        if (allProducts.size() == 0 || allProducts.size() > 5) {
            productsAreValid = false;
        }
        if (!productsAreValid || !discountIsValid) {
            dataAreValid = false;
        }
        return dataAreValid;
    }
}
