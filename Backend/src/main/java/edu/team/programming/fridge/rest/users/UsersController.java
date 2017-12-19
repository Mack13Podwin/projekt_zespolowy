package edu.team.programming.fridge.rest.users;

import edu.team.programming.fridge.domain.User;
import edu.team.programming.fridge.exception.ConflictException;
import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import edu.team.programming.fridge.infrastructure.rest.AuthenticationUser;
import edu.team.programming.fridge.infrastructure.rest.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Base64;

@RestController
@RequestMapping(value="/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JavaMailSender mailSender;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserTO login(@RequestBody AuthenticationUser loggingUser){
        User user=usersRepository.findByName(loggingUser.getLogin());
        if(user.getPassword().equals(loggingUser.getPassword())){
            String token= Base64.getEncoder().encodeToString(SerializationUtils.serialize(user));
            return UserTO.createFromUser(user, token);
        }else{
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return null;
        }
    }

    @RequestMapping(value="/email", method=RequestMethod.POST)
    public void changeEmail(@RequestBody String email, @RequestHeader(name = "authorization") String token){
        try{
            User user=(User)SerializationUtils.deserialize(Base64.getDecoder().decode(token.getBytes()));
            if(usersRepository.findOne(user.getId())!=null){
                user.setEmail(email);
                usersRepository.save(user);
                System.out.println(email);
                SimpleMailMessage message=new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("Intelligent Fridge");
                message.setText("Hello "+user.getName()+"!\nYour e-mail was changed to "+email);
                mailSender.send(message);
            }else{
                throw new ConflictException();
            }
        }catch(Exception ex) {
            System.out.println("Cannot cast token to user");
        }
    }
}
