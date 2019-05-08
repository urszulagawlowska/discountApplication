package gawlowska.urszula.discountApplication.model;

import java.text.NumberFormat;
import java.util.Locale;

public class Discount {
    private double amount;

    public Discount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        NumberFormat form = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
        return "Discount{" +
                "amount=" + form.format(amount) +
                '}';
    }
}
