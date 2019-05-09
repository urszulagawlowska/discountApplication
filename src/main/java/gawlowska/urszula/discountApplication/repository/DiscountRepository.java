package gawlowska.urszula.discountApplication.repository;

import gawlowska.urszula.discountApplication.model.Discount;
import org.springframework.stereotype.Repository;

@Repository
public class DiscountRepository {

    public static final double DEFAULT_DISCOUNT = 115;

    public Discount getDiscount() {
        Discount totalDiscount = new Discount(DEFAULT_DISCOUNT);
        return totalDiscount;
    }
}
