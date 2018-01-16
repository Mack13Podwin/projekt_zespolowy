package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.SessionToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionTokensRepository extends MongoRepository<SessionToken, String> {
    public SessionToken findByToken(String token);
    public Long deleteByUserId(String userId);
}
