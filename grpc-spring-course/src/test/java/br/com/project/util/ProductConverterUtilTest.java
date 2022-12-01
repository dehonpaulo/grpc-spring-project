package br.com.project.util;

import br.com.project.domain.Product;
import br.com.project.dto.ProductInputDTO;
import br.com.project.dto.ProductOutputDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductConverterUtilTest {

    @Test
    public void productToProductoutputDTOTest() {
        Product product = new Product(1L, "refrigerante", 4.99, 200);
        ProductOutputDTO outputDTO = ProductConverterUtil.productToProductOutputDTO(product);

        Assertions.assertNotNull(outputDTO);
        Assertions.assertEquals(outputDTO.getId(), product.getId());
        Assertions.assertEquals(outputDTO.getName(), product.getName());
        Assertions.assertEquals(outputDTO.getPrice(), product.getPrice());
        Assertions.assertEquals(outputDTO.getQuantityInStock(), product.getQuantityInStock());
    }

    @Test
    public void productInputDTOToProductTest() {
        ProductInputDTO inputDTO = new ProductInputDTO("refrigerante", 4.99, 200);
        Product product = ProductConverterUtil.productInputDTOToProduct(inputDTO);

        Assertions.assertNotNull(product);
        Assertions.assertEquals(product.getId(), null);
        Assertions.assertEquals(product.getName(), inputDTO.getName());
        Assertions.assertEquals(product.getPrice(), inputDTO.getPrice());
        Assertions.assertEquals(product.getQuantityInStock(), inputDTO.getQuantityInStock());
    }
}
