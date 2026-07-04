package com.swetonyancelmo.service;

import com.swetonyancelmo.dto.CreateProductDTO;
import com.swetonyancelmo.dto.ProductResponseDTO;
import com.swetonyancelmo.exceptions.ResourceNotFoundException;
import com.swetonyancelmo.model.Product;
import com.swetonyancelmo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll().stream()
                .map(product -> new ProductResponseDTO(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        return repository.findById(id)
                .map(product -> new ProductResponseDTO(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice()
                ))
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
    }

    @Transactional
    public ProductResponseDTO createProduct(CreateProductDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());

        repository.save(product);

        return new ProductResponseDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

}
