package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<User, String> {
    public User findByName(String name);
}
