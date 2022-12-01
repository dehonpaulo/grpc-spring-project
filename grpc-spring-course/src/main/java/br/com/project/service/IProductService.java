package br.com.project.service;

import br.com.project.dto.ProductInputDTO;
import br.com.project.dto.ProductOutputDTO;

import java.util.List;

public interface IProductService {
    ProductOutputDTO create(ProductInputDTO inputDTO);
    ProductOutputDTO findById(Long id);
    void delete(Long id);
    List<ProductOutputDTO> findAll();
}
