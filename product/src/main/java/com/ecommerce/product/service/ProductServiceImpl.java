package com.ecommerce.product.service;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product ,productRequest);
        return mapToProductResponse(productRepository.save(product));
    }

    @Override
    public Optional<ProductResponse> updateProduct(Long id , ProductRequest productRequest) {
     System.out.println(id);
      return   productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct , productRequest);
                   Product savedProduct = productRepository.save(existingProduct);
                  return  mapToProductResponse(savedProduct);
                });
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    public Optional<ProductResponse> getProductById(String id) {
        return productRepository.findByIdAndActiveTrue(Long.valueOf(id))
                .map(this::mapToProductResponse);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(existingProd -> {
                     existingProd.setActive(false);
                    productRepository.save(existingProd);
                    return true;
                        }).orElse(false);
    }

    @Override
    public List<ProductResponse> searchProduct(String keyword) {
        return productRepository.searchProduct(keyword)
                .stream()
                .map(this::mapToProductResponse).toList();
    }

    private void updateProductFromRequest(Product product , ProductRequest productRequest)
    {

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
    }

    private ProductResponse mapToProductResponse(Product product)
    {
        ProductResponse productResponse =new ProductResponse();
        productResponse.setId(String.valueOf(product.getId()));
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setCategory(product.getCategory());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setActive(product.getActive());

        return productResponse;
    }
}
