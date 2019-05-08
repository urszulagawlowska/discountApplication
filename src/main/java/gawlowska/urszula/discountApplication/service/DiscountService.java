package gawlowska.urszula.discountApplication.service;

import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;

import java.util.Map;

public interface DiscountService {
    Map<Product, Discount> getProductsWithDiscounts();
}
