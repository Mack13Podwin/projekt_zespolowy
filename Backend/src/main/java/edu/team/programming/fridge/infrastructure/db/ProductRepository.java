package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    public Product findByName(String name);
    public List<Product> findByBarcode(String barCode);
    public List<Product> findByFridgeid(String fridgeid);
    public List<Product> findByBarcodeAndFridgeid(String barcode, String fridgeid);
    public List<Product> findByFridgeidAndRemovingdateIsNull(String fridgeId);
    public List<Product> findByRemovingdateBeforeAndFridgeid(Date removingDate, String fridgeId);
    public List<Product> findByFridgeidAndExpirationdateBeforeAndRemovingdateNotNull(String fridgeid,
                                                                                     Date expirationDate);
    public List<Product> findByFridgeidAndTypeAndRemovingdateIsNull(String fridgeid,String type);
}
