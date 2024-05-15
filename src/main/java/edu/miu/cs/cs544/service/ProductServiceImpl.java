package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.AuditData;
import edu.miu.cs.cs544.domain.Product;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.enums.Constants;
import edu.miu.cs.cs544.exception.AppException;
import edu.miu.cs.cs544.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new AppException(Constants.GET_PRODUCT_FAIL.getmessage());
        }
    }

    @Override
    public Product getProductById(Long productId) {
        try {
            return productRepository.findById(productId).get();
        } catch (Exception e) {
            throw new AppException(Constants.GET_PRODUCT_FAIL.getmessage());
        }
    }

    @Override
    public Product addProduct(Product product) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomerResponseDTO customer = (CustomerResponseDTO) authentication.getPrincipal();
            String username = String.valueOf(customer.getUser()).split(" ")[0];

            AuditData auditEntry = new AuditData();
            auditEntry.setCreatedBy(username);
            auditEntry.setCreatedOn(LocalDateTime.now());
            auditEntry.setUpdatedBy(username);
            auditEntry.setUpdatedOn(LocalDateTime.now());
            product.setAuditData(auditEntry);
            return productRepository.save(product);
        } catch (Exception e) {
            throw new AppException(Constants.ADD_PRODUCT_FAIL.getmessage());
        }
    }

    @Override
    public Product updateProductById(Long productId, Product updatedProduct) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomerResponseDTO customer = (CustomerResponseDTO) authentication.getPrincipal();
            String username = String.valueOf(customer.getUser()).split(" ")[0];
            // Retrieves the existing product by ID
            Product existingProduct = productRepository.findById(productId).get();
            AuditData auditEntry = existingProduct.getAuditData();

            // Updates the properties of the existing product
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setNight_rate(updatedProduct.getNight_rate());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setNumber_of_beds(updatedProduct.getNumber_of_beds());
            existingProduct.setExcerpt(updatedProduct.getExcerpt());
            existingProduct.setType(updatedProduct.getType());
            existingProduct.setAvailability(updatedProduct.isAvailability());

            auditEntry.setUpdatedBy(username);
            auditEntry.setUpdatedOn(LocalDateTime.now());
            existingProduct.setAuditData(auditEntry);

            // Saves the updated product back to the database
            return productRepository.save(existingProduct);

        } catch (Exception e) {
            throw new AppException(Constants.UPDATE_PRODUCT_FAIL.getmessage());
        }
    }

    public Product partialUpdateProductById(Long productId, Product partialProduct) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomerResponseDTO customer = (CustomerResponseDTO) authentication.getPrincipal();
            String username = String.valueOf(customer.getUser()).split(" ")[0];
            // Retrieves the existing product by ID
            Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new AppException("Product not found"));

            AuditData auditEntry = existingProduct.getAuditData();

            // Update fields based on the provided partialProduct
            if (partialProduct.getName() != null) {
                existingProduct.setName(partialProduct.getName());
            }
            if (partialProduct.getDescription() != null) {
                existingProduct.setDescription(partialProduct.getDescription());
            }
            if (partialProduct.getExcerpt() != null) {
                existingProduct.setExcerpt(partialProduct.getExcerpt());
            }
            if (partialProduct.getNight_rate() != 0.0) {
                existingProduct.setNight_rate(partialProduct.getNight_rate());
            }
            if (partialProduct.getNumber_of_beds() != null) {
                existingProduct.setNumber_of_beds(partialProduct.getNumber_of_beds());
            }
            if (partialProduct.isAvailability() != existingProduct.isAvailability()) {
                existingProduct.setAvailability(partialProduct.isAvailability());
            }

            // Update audit data
            auditEntry.setUpdatedBy(username);
            auditEntry.setUpdatedOn(LocalDateTime.now());
            existingProduct.setAuditData(auditEntry);

            // Save the partially updated product back to the database
            return productRepository.save(existingProduct);
        } catch (Exception e) {
            throw new AppException(Constants.UPDATE_PRODUCT_FAIL.getmessage());
        }
    }

    @Override
    public void deleteProductById(Long productId) {
        try {
            Product product = productRepository.findById(productId).get();
            productRepository.delete(product);
        } catch (Exception e) {
            throw new AppException(Constants.DELETE_PRODUCT_FAIL.getmessage());
        }
    }
}
