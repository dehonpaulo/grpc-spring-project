package br.com.project.service.impl;

import br.com.project.domain.Product;
import br.com.project.dto.ProductInputDTO;
import br.com.project.dto.ProductOutputDTO;
import br.com.project.exception.ProductAlreadyExistsException;
import br.com.project.exception.ProductNotFoundException;
import br.com.project.repository.ProductRepository;
import br.com.project.service.IProductService;
import br.com.project.util.ProductConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        checkDuplicity(inputDTO.getName());
        Product product = ProductConverterUtil.productInputDTOToProduct(inputDTO);
        Product productCreated = productRepository.save(product);
        return ProductConverterUtil.productToProductOutputDTO(productCreated);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return ProductConverterUtil.productToProductOutputDTO(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductOutputDTO> findAll() {
        return productRepository.findAll().stream().map(product -> {
            return ProductConverterUtil.productToProductOutputDTO(product);
        }).collect(Collectors.toList());
    }

    public void checkDuplicity(String name) {
        productRepository.findByNameIgnoreCase(name).ifPresent(e -> {
            throw new ProductAlreadyExistsException(name);
        });
    }
}
