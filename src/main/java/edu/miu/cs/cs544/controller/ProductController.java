package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.domain.Product;
import edu.miu.cs.cs544.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.addProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProductById(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        return new ResponseEntity<>(productService.updateProductById(productId, updatedProduct), HttpStatus.OK);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> partialUpdateProductById(@PathVariable Long productId, @RequestBody Product partialProduct) {
        return new ResponseEntity<>(productService.partialUpdateProductById(productId, partialProduct), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
