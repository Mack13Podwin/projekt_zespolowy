package edu.team.programming.fridge.rest.users;

import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import edu.team.programming.fridge.infrastructure.rest.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User loggingUser){
        System.out.println(loggingUser.toString());
        edu.team.programming.fridge.domain.User user=usersRepository.findByName(loggingUser.getLogin());
        if(user.getPassword().equals(loggingUser.getPassword())){
            return user.getFridgeid();
        }else{
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return "";
        }
    }
}
