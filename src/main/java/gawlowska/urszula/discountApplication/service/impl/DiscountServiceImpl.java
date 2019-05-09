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

    @Autowired
    public DiscountServiceImpl(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.allProducts = productRepository.getAllProducts();
        this.totalDiscountAmount = discountRepository.getDiscount().getDiscountAmount();
    }

    @Override
    public Map<Product, Discount> getProductsWithDiscounts() throws BadInputDataException {
        Map<Product, Discount> productsWithDiscounts = new HashMap<>();
        if (!dataAreValid()) {
            throw new BadInputDataException("Incorrect input data provided.");
        }
        double roundedProductsTotalCost = roundToTwoDecimalPlaces(getProductsTotalCost(), BigDecimal.ROUND_HALF_UP);
        double roundedTotalDiscountAmount = roundToTwoDecimalPlaces(totalDiscountAmount, BigDecimal.ROUND_HALF_UP);
        if (roundedProductsTotalCost <= roundedTotalDiscountAmount) {
            setProductPriceAsDiscount(productsWithDiscounts);
        } else {
            calculateDiscountsForProducts(productsWithDiscounts);
        }
        return productsWithDiscounts;
    }

    private void setProductPriceAsDiscount(Map<Product, Discount> productsWithDiscounts) {
        for (Product product : allProducts) {
            productsWithDiscounts.put(product, new Discount(product.getPrice()));
        }
    }

    private void calculateDiscountsForProducts(Map<Product, Discount> productsWithDiscounts) {
        double unitDiscount = calculateDiscountPerOneUnit();
        for (int index = 0; index < allProducts.size(); index++) {
            Product product = allProducts.get(index);
            int lastIndex = allProducts.size() - 1;
            if (allProducts.size() > 1 && index == lastIndex) {
                productsWithDiscounts.put(product, new Discount(calculateDiscountForLastProduct(productsWithDiscounts)));
            } else {
                productsWithDiscounts.put(product, new Discount(calculateDiscountPerProduct(product, unitDiscount)));
            }
        }
    }

    private double calculateDiscountForLastProduct(Map<Product, Discount> productDiscountMap) {
        double grantedDiscount = productDiscountMap.entrySet().stream().mapToDouble(entry -> entry.getValue().getDiscountAmount()).sum();
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

    private boolean dataAreValid() {
        return productsAreValid() && discountIsValid();
    }

    private boolean discountIsValid() {
        return totalDiscountAmount > 0;
    }

    private boolean productsAreValid() {
        return allProducts.size() > 0 && allProducts.size() <= 5;
    }
}
