package gawlowska.urszula.discountApplication.model;

public class Discount {
    private double discountAmount;

    public Discount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
