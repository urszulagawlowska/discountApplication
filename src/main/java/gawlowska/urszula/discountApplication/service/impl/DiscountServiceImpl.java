package gawlowska.urszula.discountApplication.service.impl;

import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.service.DiscountService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountServiceImpl implements DiscountService {
    private List<Product> allProducts;
    private double totalDiscountAmount;

    public DiscountServiceImpl(List<Product> allProducts, double totalDiscountAmount) {
        this.allProducts = allProducts;
        this.totalDiscountAmount = totalDiscountAmount;
    }

    @Override
    public Map<Product, Discount> getProductsWithDiscounts() {
        Map<Product, Discount> productsWithDiscounts = new HashMap<>();
        if (validateData()) {
            double unitDiscount = calculateDiscountPerOneUnit();
            for (Product product : allProducts) {
                productsWithDiscounts.put(product, new Discount(calculateDiscountForProduct(product, unitDiscount)));
            }

            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.size() > 1 && i == allProducts.size() - 1) {
                    productsWithDiscounts.put(allProducts.get(i),
                            new Discount(calculateDiscountForLastProduct(productsWithDiscounts)));
                } else {
                    productsWithDiscounts.put(allProducts.get(i),
                            new Discount(calculateDiscountForProduct(allProducts.get(i), unitDiscount)));
                }
            }
        }
        return productsWithDiscounts;
    }

    private double calculateDiscountForProduct(Product product, double discountPerOneUnit) {
        double discountForProduct = product.getPrice() * discountPerOneUnit;
        BigDecimal discountForProductRounded = new BigDecimal(discountForProduct).setScale(2, BigDecimal.ROUND_DOWN);
        return discountForProductRounded.doubleValue();
    }

    private double calculateDiscountPerOneUnit() {
        double sum = allProducts.stream().mapToDouble(Product::getPrice).sum();
        return totalDiscountAmount / sum;
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
        return totalDiscountAmount - grantedDiscount;
    }
}
