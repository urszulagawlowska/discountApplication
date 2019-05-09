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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
        return "Discount{" +
                "amount=" + formatter.format(amount) +
                '}';
    }
}
