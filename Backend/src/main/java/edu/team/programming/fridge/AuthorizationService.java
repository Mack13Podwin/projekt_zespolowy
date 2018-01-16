package edu.team.programming.fridge;

import edu.team.programming.fridge.domain.SessionToken;
import edu.team.programming.fridge.domain.User;
import edu.team.programming.fridge.exception.AuthorizationException;
import edu.team.programming.fridge.infrastructure.db.SessionTokensRepository;
import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService {

    @Autowired
    SessionTokensRepository sessionTokensRepository;

    @Autowired
    UsersRepository usersRepository;

    public User authorizeUser(String token) throws AuthorizationException {
        SessionToken sessionToken=sessionTokensRepository.findByToken(token);
        if(sessionToken==null){
            throw new AuthorizationException();
        }else{
            return usersRepository.findOne(sessionToken.getUserId());
        }
    }

    public String createToken(User user){
        sessionTokensRepository.deleteByUserId(user.getId());
        String uuid= UUID.randomUUID().toString();
        SessionToken sessionToken=SessionToken.builder().token(uuid).userId(user.getId()).build();
        sessionTokensRepository.save(sessionToken);
        return uuid;
    }
}
