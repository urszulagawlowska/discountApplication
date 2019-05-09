package gawlowska.urszula.discountApplication.repository;

import gawlowska.urszula.discountApplication.model.Discount;
import org.springframework.stereotype.Repository;

@Repository
public class DiscountRepository {

    public Discount getDiscount() {
        Discount totalDiscount = new Discount(33);
        return totalDiscount;
    }
}
