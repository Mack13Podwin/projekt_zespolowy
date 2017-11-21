package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    public Product findByName(String name);
    public List<Product> findByBarcode(String barCode);
}
