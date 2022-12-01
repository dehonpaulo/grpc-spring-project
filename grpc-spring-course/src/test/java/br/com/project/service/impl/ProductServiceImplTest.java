package br.com.project.service.impl;

import br.com.project.domain.Product;
import br.com.project.dto.ProductInputDTO;
import br.com.project.dto.ProductOutputDTO;
import br.com.project.exception.BaseBusinessException;
import br.com.project.exception.ProductAlreadyExistsException;
import br.com.project.exception.ProductNotFoundException;
import br.com.project.repository.ProductRepository;
import io.grpc.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductServiceImplTest {
    private final Product product1 = new Product(1L, "Product 1", 4.99, 200);
    private final Product product2 = new Product(2L, "Product 2", 5.99, 300);

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("when create is called with valid data a product is created")
    public void createProductSuccessTest() {
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenAnswer(invocationOnMock -> {
            Product product = (Product) invocationOnMock.getArguments()[0];
            product.setId(1L);
            return product;
        });

        ProductInputDTO inputDTO = new ProductInputDTO("refrigerante", 4.99, 200);
        ProductOutputDTO outputDTO = productService.create(inputDTO);

        Assertions.assertNotNull(outputDTO);
        Assertions.assertEquals(1L, outputDTO.getId());
        Assertions.assertEquals(inputDTO.getName(), outputDTO.getName());
        Assertions.assertEquals(inputDTO.getPrice(), outputDTO.getPrice());
        Assertions.assertEquals(inputDTO.getQuantityInStock(), outputDTO.getQuantityInStock());
    }

    @Test
    public void createProductExceptionTest() {
        Product product = new Product(1L, "refrigerante", 4.99, 200);
        ProductInputDTO inputDTO = new ProductInputDTO("refrigerante", 4.99, 200);

        Mockito.when(productRepository.findByNameIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenAnswer(invocationOnMock -> {
            Product productCreated = (Product) invocationOnMock.getArguments()[0];
            productCreated.setId(1L);
            return productCreated;
        });

        BaseBusinessException ex = Assertions.assertThrows(ProductAlreadyExistsException.class, () -> productService.create(inputDTO));
        Assertions.assertEquals(Status.ALREADY_EXISTS, ex.getStatusCode());

        Mockito.verify(productRepository, Mockito.times(1)).findByNameIgnoreCase(Mockito.anyString());
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void findByIdProductWithInvalidId() {
        Long id = 1L;
        Product product = new Product(id, "refrigerante", 4.99, 200);
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));

        ProductOutputDTO outputDTO = productService.findById(id);

        Assertions.assertNotNull(outputDTO);
        Assertions.assertEquals(id, outputDTO.getId());
        Assertions.assertEquals(product.getName(), outputDTO.getName());
        Assertions.assertEquals(product.getPrice(), outputDTO.getPrice());
        Assertions.assertEquals(product.getQuantityInStock(), outputDTO.getQuantityInStock());

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    public void productNotFoundExceptionTest() {
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    public void deleteProductSuccessTest() {
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product1));
        Mockito.doNothing().when(productRepository).deleteById(Mockito.anyLong());

        productService.delete(1L);

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void deleteProductNotFoundException() {
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.doNothing().when(productRepository).deleteById(Mockito.anyLong());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.delete(1L));

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(productRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    public void findAllProductsSuccessTest() {
        List<Product> products = List.of(product1, product2);
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<ProductOutputDTO> outputDTOS = productService.findAll();

        Assertions.assertNotNull(outputDTOS);
        Assertions.assertEquals(products.size(), outputDTOS.size());
        for(int i = 0; i < outputDTOS.size(); i++) {
            Assertions.assertEquals(products.get(i).getId(), outputDTOS.get(i).getId());
            Assertions.assertEquals(products.get(i).getName(), outputDTOS.get(i).getName());
            Assertions.assertEquals(products.get(i).getPrice(), outputDTOS.get(i).getPrice());
            Assertions.assertEquals(products.get(i).getQuantityInStock(), outputDTOS.get(i).getQuantityInStock());
        }

        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }
}