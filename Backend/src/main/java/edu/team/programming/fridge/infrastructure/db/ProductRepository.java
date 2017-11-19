package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    public Product findByName(String name);
}
