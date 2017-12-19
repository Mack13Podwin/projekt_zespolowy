package edu.team.programming.fridge.rest.users;

import edu.team.programming.fridge.domain.User;
import edu.team.programming.fridge.exception.ConflictException;
import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import edu.team.programming.fridge.infrastructure.rest.*;
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
            User userFromDb=usersRepository.findOne(user.getId());
            if(userFromDb!=null){
                userFromDb.setEmail(email);
                userFromDb.setFirstLogin(false);
                usersRepository.save(userFromDb);
                System.out.println(email);
                SimpleMailMessage message=new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("E-mail address changed");
                message.setText("Hello "+userFromDb.getName()+"!\nYour e-mail was changed to "+email);
                mailSender.send(message);
            }else{
                throw new ConflictException();
            }
        }catch(Exception ex) {
            System.out.println("Cannot cast token to user");
        }
    }

    @RequestMapping(value="/remind", method=RequestMethod.POST)
    public void remindPassword(@RequestBody PasswordRemindHolder body){
        User user=usersRepository.findByName(body.getName());
        if(user!=null && user.getEmail().equals(body.getEmail())) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Password reminder");
            message.setText("Hello "+user.getName()+"!\nYour password is " + user.getPassword());
            mailSender.send(message);
        }
    }

    @RequestMapping(value="/login/change", method=RequestMethod.PATCH)
    public void changeLogin(@RequestBody LoginChangeHolder body, @RequestHeader(name = "authorization") String token){
        try{
            User user=(User)SerializationUtils.deserialize(Base64.getDecoder().decode(token.getBytes()));
            User userFromDb=usersRepository.findOne(user.getId());
            if(userFromDb!=null && userFromDb.getName().equals(body.getOldlogin())){
                userFromDb.setName(body.getNewlogin());
                usersRepository.save(userFromDb);
                SimpleMailMessage message=new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Login changed");
                message.setText("Hello "+user.getName()+"!\nYour login was changed to "+userFromDb.getName());
                mailSender.send(message);
            }else{
                throw new ConflictException();
            }
        }catch(Exception ex) {
            System.out.println("Cannot cast token to user");
        }
    }

    @RequestMapping(value="/password/change", method=RequestMethod.PATCH)
    public void changePassword(@RequestBody PasswordChangeHolder body, @RequestHeader(name="authorization") String token){
        try{
            User user=(User)SerializationUtils.deserialize(Base64.getDecoder().decode(token.getBytes()));
            User userFromDb=usersRepository.findOne(user.getId());
            if(userFromDb!=null && userFromDb.getPassword().equals(body.getOldpassword())){
                userFromDb.setPassword(body.getNewpassword());
                usersRepository.save(userFromDb);
                SimpleMailMessage message=new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Password changed");
                message.setText("Hello "+user.getName()+"!\nYour password was changed to "+userFromDb.getPassword());
                mailSender.send(message);
            }else{
                throw new ConflictException();
            }
        }catch(Exception ex) {
            System.out.println("Cannot cast token to user");
        }
    }
}
