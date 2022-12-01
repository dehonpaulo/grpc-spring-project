package br.com.project.util;

import br.com.project.domain.Product;
import br.com.project.dto.ProductInputDTO;
import br.com.project.dto.ProductOutputDTO;

public class ProductConverterUtil {
    public static ProductOutputDTO productToProductOutputDTO(Product product) {
        return new ProductOutputDTO(
                product.getId(), product.getName(), product.getPrice(), product.getQuantityInStock()
        );
    }

    public static Product productInputDTOToProduct(ProductInputDTO inputDTO) {
        return new Product(
                null, inputDTO.getName(), inputDTO.getPrice(), inputDTO.getQuantityInStock()
        );
    }
}
