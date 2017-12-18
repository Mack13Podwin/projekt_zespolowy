package edu.team.programming.fridge.rest.users;

import edu.team.programming.fridge.domain.User;
import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import edu.team.programming.fridge.infrastructure.rest.AuthenticationUser;
import edu.team.programming.fridge.infrastructure.rest.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserTO login(@RequestBody AuthenticationUser loggingUser){
        User user=usersRepository.findByName(loggingUser.getLogin());
        if(user.getPassword().equals(loggingUser.getPassword())){
            return UserTO.createFromUser(user);
        }else{
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return null;
        }
    }

    @RequestMapping(value='/email', method=RequestMethod.POST)
    public void changeEmail(){

    }
}
