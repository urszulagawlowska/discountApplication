package gawlowska.urszula.discountApplication.service.impl;

import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.service.DiscountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscountServiceImpl implements DiscountService {
    private List<Product> allProducts;
    private Discount totalDiscount;
    private double totalDiscountAmount;

    @Override
    public Map<Product, Discount> getProductsWithDiscounts() {
        createData();
        Map<Product, Discount> productsWithDiscounts = new HashMap<>();
        if (validateData()) {
            double unitDiscount = calculateDiscountPerOneUnit();
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
        return roundDoubleToTwoDecimals(discountForProduct);
    }

    private double roundDoubleToTwoDecimals(double doubleToRound) {
        return new BigDecimal(doubleToRound).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
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
        return new BigDecimal(totalDiscountAmount - grantedDiscount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void createData() {
        totalDiscount = new Discount(33.15);
        allProducts = new ArrayList<>();
        totalDiscountAmount = totalDiscount.getAmount();
        Product product1 = new Product("Product1", 12.84);
        Product product2 = new Product("Product2", 11.5);
        Product product3 = new Product("Product3", 8.27);
        Product product4 = new Product("Product4", 5.89);
        Product product5 = new Product("Product5", 13.24);
        allProducts.add(product1);
        allProducts.add(product2);
        allProducts.add(product3);
        allProducts.add(product4);
        allProducts.add(product5);
    }
}
