package gawlowska.urszula.discountApplication.service.impl;

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

    public static final int DECIMAL_PLACES = 2;
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
    public Map<Product, Discount> getProductsWithDiscounts() {
        Map<Product, Discount> productsWithDiscounts = new HashMap<>();
        if (validateData()) {
            double unitDiscount = calculateDiscountPerOneUnit();
            for (int i = 0; i < allProducts.size(); i++) {
                Product product = allProducts.get(i);
                if (allProducts.size() > 1 && i == allProducts.size() - 1) {
                    productsWithDiscounts.put(product,
                            new Discount(getMaxDiscount(product, calculateDiscountForLastProduct(productsWithDiscounts))));
                } else {
                    productsWithDiscounts.put(product,
                            new Discount(getMaxDiscount(product, calculateDiscountForProduct(product, unitDiscount))));
                }
            }
        }
        return productsWithDiscounts;
    }

    private double getMaxDiscount(Product product, double calculatedDiscount) {
        double price = product.getPrice();
        return Math.min(calculatedDiscount, price);
    }

    private double calculateDiscountForProduct(Product product, double discountPerOneUnit) {
        double discountForProduct = product.getPrice() * discountPerOneUnit;
        return roundDoubleToTwoDecimals(discountForProduct);
    }

    private double roundDoubleToTwoDecimals(double doubleToRound) {
        return new BigDecimal(doubleToRound).setScale(DECIMAL_PLACES, BigDecimal.ROUND_DOWN).doubleValue();
    }

    private double calculateDiscountPerOneUnit() {
        double discountPerOneUnit;
        double totalCostOfProducts = allProducts.stream().mapToDouble(Product::getPrice).sum();
        discountPerOneUnit = totalDiscountAmount / totalCostOfProducts;
        return discountPerOneUnit;
    }

    private boolean validateData() {
        boolean dataAreValid = true;
        boolean productsAreValid = true;
        boolean discountIsValid = true;

        if (!(totalDiscountAmount >= 0)) {
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

    private double calculateDiscountForLastProduct(Map<Product, Discount> productDiscountMap) {
        double grantedDiscount = productDiscountMap.entrySet().stream().mapToDouble(entry -> entry.getValue().getAmount()).sum();
        return new BigDecimal(totalDiscountAmount - grantedDiscount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
