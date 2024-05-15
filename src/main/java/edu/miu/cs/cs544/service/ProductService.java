package edu.miu.cs.cs544.service;


import edu.miu.cs.cs544.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    Product getProductById(Long productId);

    Product addProduct(Product product);

    Product updateProductById(Long productId, Product updatedProduct);

    Product partialUpdateProductById(Long productId, Product partialProduct);

    void deleteProductById(Long productId);

}
