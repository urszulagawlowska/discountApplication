package gawlowska.urszula.discountApplication.model;

import java.text.NumberFormat;
import java.util.Locale;

public class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        NumberFormat form = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + form.format(price) +
                '}';
    }
}
