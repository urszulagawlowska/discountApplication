package gawlowska.urszula.discountApplication.service.impl;

import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.service.DiscountService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountServiceImpl implements DiscountService {
    @Override
    public Map<Product, Discount> getProductsWithDiscounts(List<Product> products, double discount) {
        Map<Product, Discount> productsWithDiscounts = new HashMap<>();
        double unitDiscount = calculateDiscountPerOneUnit(products, discount);
        for (Product product : products) {
            productsWithDiscounts.put(product, new Discount(calculateDiscountForProduct(product, unitDiscount)));
        }
        return null;
    }

    private double calculateDiscountForProduct(Product product, double discountPerOneUnit) {
        return product.getPrice() * discountPerOneUnit;
    }

    private double calculateDiscountPerOneUnit(List<Product> products, double discount) {
        double sum = products.stream().mapToDouble(Product::getPrice).sum();
        return discount/sum;
    }
}
