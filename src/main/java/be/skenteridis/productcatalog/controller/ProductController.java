package be.skenteridis.productcatalog.controller;

import be.skenteridis.productcatalog.model.Product;
import be.skenteridis.productcatalog.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService productService) {
        service = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(name = "page", required = false) Integer page,
                                         @RequestParam(name = "limit", required = false) Integer limit,
                                         @RequestParam(name = "category", required = false) String category) {
        page = page != null ? page-1 : 0;
        if(limit == null) limit = 10;
        List<Product> products = service.getProducts(category, page, limit);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addProduct(product));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product newProduct) {
        Product product = service.updateProduct(id, newProduct);
        return product == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!")
                : ResponseEntity.ok(product);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = service.deleteProduct(id);
        return isDeleted ? ResponseEntity.ok("Product deleted successfully!")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
    }
}
