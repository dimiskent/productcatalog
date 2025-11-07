package be.skenteridis.productcatalog.service;

import be.skenteridis.productcatalog.model.Product;
import be.skenteridis.productcatalog.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getProducts(String category, Integer page, Integer limit) {
        PageRequest request = PageRequest.of(page, limit, Sort.by("id").ascending());
        if(category != null && !category.trim().isEmpty())
            return repository.findByCategoryIgnoreCase(category, request).getContent();
        else
            return repository.findAll(request).getContent();
    }

    public Product updateProduct(Long id, Product newProduct) {
        Product product = repository.findById(id).map(p -> {
            p.setName(newProduct.getName());
            p.setCategory(newProduct.getCategory());
            p.setPrice(newProduct.getPrice());
            p.setStock(newProduct.getStock());
            return p;
        }).orElse(null);
        return product == null ? null : repository.save(product);
    }

    public Product addProduct(Product product) {
        return repository.save(product);
    }
    public boolean deleteProduct(Long id) {
        Product product = repository.findById(id).orElse(null);
        if(product == null) return false;
        repository.delete(product);
        return true;
    }
}
