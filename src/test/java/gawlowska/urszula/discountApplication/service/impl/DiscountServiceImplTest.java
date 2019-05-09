package gawlowska.urszula.discountApplication.service.impl;

import gawlowska.urszula.discountApplication.exceptions.BadInputDataException;
import gawlowska.urszula.discountApplication.model.Discount;
import gawlowska.urszula.discountApplication.model.Product;
import gawlowska.urszula.discountApplication.repository.DiscountRepository;
import gawlowska.urszula.discountApplication.repository.ProductRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceImplTest {

    private List<Product> allProducts = new ArrayList<>();
    private Discount totalDiscount = new Discount(100);
    private static DiscountServiceImpl discountService;

    @Mock
    private static ProductRepository productRepository;

    @Mock
    private static DiscountRepository discountRepository;

    @BeforeAll
    public void setUp() {
        allProducts.add(new Product("Product1", 500));
        allProducts.add(new Product("Product2", 1500));

        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);

        discountService = new DiscountServiceImpl(productRepository, discountRepository);
    }

    @AfterEach
    public void cleanProductsList() {
        allProducts.clear();
    }

    @Test(expected = BadInputDataException.class)
    public void testExceptionWhenIncorrectDiscount() throws BadInputDataException {
        allProducts.add(new Product("Product1", 22.49));
        allProducts.add(new Product("Product2", 31.79));
        allProducts.add(new Product("Product3", 89.99));
        allProducts.add(new Product("Product4", 10.26));
        allProducts.add(new Product("Product5", 55.57));
        totalDiscount.setDiscountAmount(-5);

        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);
        discountService = new DiscountServiceImpl(productRepository, discountRepository);

        discountService.getProductsWithDiscounts();
    }

    @Test(expected = BadInputDataException.class)
    public void testExceptionWhenNoProduct() throws BadInputDataException {
        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);
        discountService = new DiscountServiceImpl(productRepository, discountRepository);

        assertEquals(0, allProducts.size());
        discountService.getProductsWithDiscounts();
    }

    @Test(expected = BadInputDataException.class)
    public void testExceptionWhenTooMuchProducts() throws BadInputDataException {
        allProducts.add(new Product("Product1", 22.49));
        allProducts.add(new Product("Product2", 31.79));
        allProducts.add(new Product("Product3", 89.99));
        allProducts.add(new Product("Product4", 10.26));
        allProducts.add(new Product("Product5", 55.57));
        allProducts.add(new Product("Product6", 150.00));

        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);
        discountService = new DiscountServiceImpl(productRepository, discountRepository);

        assertEquals(6, allProducts.size());
        discountService.getProductsWithDiscounts();
    }

    @Test()
    public void testOneProductCheaperThanTotalDiscount() throws BadInputDataException {
        Product product1 = new Product("Product1", 85.15);
        allProducts.add(new Product("Product1", 85.15));

        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);
        discountService = new DiscountServiceImpl(productRepository, discountRepository);

        assertEquals(1, discountService.getProductsWithDiscounts().size());
        assertEquals(product1.getPrice(), discountService.getProductsWithDiscounts().entrySet().iterator().next().getValue().getDiscountAmount(), 0);
    }

    @Test()
    public void testProductsCostIsLessThanTotalDiscount() throws BadInputDataException {
        Product product1 = new Product("Product1", 5.89);
        Product product2 = new Product("Product1", 3.45);
        allProducts.add(product1);
        allProducts.add(product2);

        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);
        discountService = new DiscountServiceImpl(productRepository, discountRepository);

        assertEquals(2, discountService.getProductsWithDiscounts().size());
        assertEquals(product1.getPrice(), discountService.getProductsWithDiscounts().get(product1).getDiscountAmount(), 0);
        assertEquals(product2.getPrice(), discountService.getProductsWithDiscounts().get(product2).getDiscountAmount(), 0);
    }

    @Test
    public void testDiscountForLastProduct() throws BadInputDataException {
        Product product1 = new Product("Product1", 16.66);
        Product product2 = new Product("Product1", 17.77);
        Product product3 = new Product("Product1", 18.88);
        allProducts.add(product1);
        allProducts.add(product2);
        allProducts.add(product3);
        totalDiscount.setDiscountAmount(30);

        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);
        discountService = new DiscountServiceImpl(productRepository, discountRepository);

        assertEquals(3, discountService.getProductsWithDiscounts().size());
        assertEquals(10.63, discountService.getProductsWithDiscounts().get(product3).getDiscountAmount(), 0);
    }

    @Test
    public void testIfGrantedDiscountIsEqualToTotalDiscount() throws BadInputDataException {
        allProducts.add(new Product("Product1", 12.84));
        allProducts.add(new Product("Product2", 11.50));
        allProducts.add(new Product("Product3", 8.27));
        allProducts.add(new Product("Product4", 5.89));
        allProducts.add(new Product("Product5", 13.24));
        totalDiscount.setDiscountAmount(51.74);

        when(productRepository.getAllProducts()).thenReturn(allProducts);
        when(discountRepository.getDiscount()).thenReturn(totalDiscount);
        discountService = new DiscountServiceImpl(productRepository, discountRepository);

        double grantedDiscount = 0;

        for (Discount discount : discountService.getProductsWithDiscounts().values()) {
            grantedDiscount += discount.getDiscountAmount();
        }

        assertEquals(grantedDiscount, totalDiscount.getDiscountAmount(), 0.001);
    }
}
