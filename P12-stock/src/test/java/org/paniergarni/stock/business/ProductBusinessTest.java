package org.paniergarni.stock.business;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.paniergarni.stock.dao.ProductRepository;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.exception.StockException;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ProductBusinessTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductBusinessImpl productBusiness;

    private Product product;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        product = new Product();
        product.setId(1L);
        product.setAvailable(true);
        product.setQuantity(5);
    }

    @Test
    public void testUpdateProductQuantityWithSameProductQuantity() {

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        product = productBusiness.updateProductQuantity(5, product.getId());

        assertEquals(product.getOrderProductRealQuantity(), 5);
        assertEquals(product.getQuantity(), 0);
        assertFalse(product.isAvailable());

    }

    @Test
    public void testUpdateProductQuantityWithTooManyQuantity() {

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        product = productBusiness.updateProductQuantity(18, product.getId());

        assertEquals(product.getOrderProductRealQuantity(), 5);
        assertEquals(product.getQuantity(), 0);
        assertFalse(product.isAvailable());
    }

    @Test
    public void testUpdateProductQuantityWithGoodQuantity() {
        product.setQuantity(25);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        product = productBusiness.updateProductQuantity(15, product.getId());

        assertEquals(product.getOrderProductRealQuantity(), 15);
        assertEquals(product.getQuantity(), 10);
        assertTrue(product.isAvailable());
    }

    @Test(expected = StockException.class)
    public void testUpdateProductQuantityWithNotAvailableProduct() {
        product.setQuantity(25);
        product.setAvailable(false);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

       productBusiness.updateProductQuantity(15, product.getId());

    }
}
