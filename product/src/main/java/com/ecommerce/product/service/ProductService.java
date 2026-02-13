package com.ecommerce.product.service;



import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);


    Optional<ProductResponse> updateProduct(Long id , ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    Optional<ProductResponse> getProductById(String id);

    boolean deleteProduct(Long id);

    List<ProductResponse> searchProduct(String keyword);
}
