package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.Barcode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BarcodeRepository extends MongoRepository<Barcode,String>{
    public Barcode findByBarcode(String barcode);
}
