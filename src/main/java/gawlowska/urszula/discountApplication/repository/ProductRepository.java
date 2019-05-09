package gawlowska.urszula.discountApplication.repository;

import gawlowska.urszula.discountApplication.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        Product product1 = new Product("Product1", 12.84);
        Product product2 = new Product("Product2", 11.50);
        Product product3 = new Product("Product3", 8.27);
        Product product4 = new Product("Product4", 5.89);
        Product product5 = new Product("Product5", 13.24);
        allProducts.add(product1);
        allProducts.add(product2);
        allProducts.add(product3);
        allProducts.add(product4);
        allProducts.add(product5);
        return allProducts;
    }
}
