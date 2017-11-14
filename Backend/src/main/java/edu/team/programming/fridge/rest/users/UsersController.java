package edu.team.programming.fridge.rest.users;

import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import edu.team.programming.fridge.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/users")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<User> getAllUsers(){
        return usersRepository.findAll();
    }
}
