package gawlowska.urszula.discountApplication.repository;

import gawlowska.urszula.discountApplication.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        Product product1 = new Product("Product1", 22.49);
        Product product2 = new Product("Product2", 31.79);
        Product product3 = new Product("Product3", 89.99);
        Product product4 = new Product("Product4", 10.26);
        Product product5 = new Product("Product5", 55.57);
        allProducts.add(product1);
        allProducts.add(product2);
        allProducts.add(product3);
        allProducts.add(product4);
        allProducts.add(product5);
        return allProducts;
    }
}
