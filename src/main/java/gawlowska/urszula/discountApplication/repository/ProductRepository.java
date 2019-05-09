package gawlowska.urszula.discountApplication.repository;

import gawlowska.urszula.discountApplication.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    public List<Product> getAllProducts() {
        return createDefaultProductsList();
    }

    private List<Product> createDefaultProductsList() {
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product("Product1", 22.49));
        allProducts.add(new Product("Product2", 31.79));
        allProducts.add(new Product("Product3", 89.99));
        allProducts.add(new Product("Product4", 10.26));
        allProducts.add(new Product("Product5", 55.57));
        return allProducts;
    }
}
