package edu.team.programming.fridge.rest.users;

import edu.team.programming.fridge.AuthorizationService;
import edu.team.programming.fridge.domain.User;
import edu.team.programming.fridge.exception.AuthorizationException;
import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import edu.team.programming.fridge.infrastructure.rest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AuthorizationService authorizationService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserTO login(@RequestBody AuthenticationUser loggingUser) throws AuthorizationException {
        User user=usersRepository.findByName(loggingUser.getLogin());
        if(user!=null && user.getPassword().equals(loggingUser.getPassword())){
            String token= authorizationService.createToken(user);
            return UserTO.createFromUser(user, token);
        }else{
            throw new AuthorizationException();
        }
    }

    @RequestMapping(value="/email", method=RequestMethod.POST)
    public void changeEmail(@RequestBody String email, @RequestHeader(name = "authorization") String token) throws AuthorizationException {
        User user=authorizationService.authorizeUser(token);
        if(user!=null){
            user.setEmail(email);
            user.setFirstLogin(false);
            usersRepository.save(user);
            System.out.println(email);
            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("E-mail address changed");
            message.setText("Hello "+user.getName()+"!\nYour e-mail was changed to "+email);
            mailSender.send(message);
        }else{
            throw new AuthorizationException();
        }
    }

    @RequestMapping(value="/remind", method=RequestMethod.POST)
    public void remindPassword(@RequestBody PasswordRemindHolder body) throws AuthorizationException {
        User user=usersRepository.findByName(body.getName());
        if(user!=null && user.getEmail().equals(body.getEmail())) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Password reminder");
            message.setText("Hello "+user.getName()+"!\nYour password is " + user.getPassword());
            mailSender.send(message);
        }else{
            throw new AuthorizationException();
        }
    }

    @RequestMapping(value="/login/change", method=RequestMethod.PATCH)
    public void changeLogin(@RequestBody LoginChangeHolder body, @RequestHeader(name = "authorization") String token) throws AuthorizationException {
        User user=authorizationService.authorizeUser(token);
        if(user!=null && user.getName().equals(body.getOldlogin())){
            user.setName(body.getNewlogin());
            usersRepository.save(user);
            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Login changed");
            message.setText("Hello "+user.getName()+"!\nYour login was changed to "+user.getName());
            System.out.println("Sending email!");
            mailSender.send(message);
        }else{
            throw new AuthorizationException();
        }
    }

    @RequestMapping(value="/password/change", method=RequestMethod.PATCH)
    public void changePassword(@RequestBody PasswordChangeHolder body, @RequestHeader(name="authorization") String token) throws AuthorizationException {
        User user=authorizationService.authorizeUser(token);
        if(user!=null && user.getPassword().equals(body.getOldpassword())){
            user.setPassword(body.getNewpassword());
            usersRepository.save(user);
            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Password changed");
            message.setText("Hello "+user.getName()+"!\nYour password was changed to "+user.getPassword());
            mailSender.send(message);
        }else{
            throw new AuthorizationException();
        }
    }
}
