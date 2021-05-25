package alexeyd.com.controller;

import alexeyd.com.model.User;
import alexeyd.com.repository.DefaultChatRepository;
import alexeyd.com.repository.UserRepository;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static alexeyd.com.consts.Common.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private DefaultChatRepository defaultChatRepository;

    @Autowired
    private UserRepository userService;

    @GetMapping("/authenticate")
    public User authenticate(@RequestParam("emailAddress") String emailAddress, @RequestParam("password") String password) {

        //Mono<User> userByLogin = Mono.justOrEmpty(userService.findFirstByEmail(emailAddress));
        Mono<User> userByLoginMono = userService.findFirstByEmail(emailAddress);
        User userByLogin = userByLoginMono.block();

        List<String> collect = defaultChatRepository.findAll().flatMapIterable(p -> Arrays.asList(p.getTopic())).distinct().toStream().collect(Collectors.toList());

        if (userByLogin == null) {
            throw new UserNotFoundException(MSG_ERR_USER_WASN_T_FOUND);
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String decodedPassword = userByLogin.getPassword();

        if (!password.equals(decodedPassword)) {
        //if (!bCryptPasswordEncoder.matches(password, decodedPassword)) {
            throw new UserNotFoundException(MSG_ERR_INCORRECT_PASSORD);
        }

        userByLogin.setPassword(password);
        return userByLogin;
    }

    @PostMapping("/checkUser")
    public void checkUser(@RequestBody User userDto) {

        Mono<User> userByLoginMono = userService.findFirstByEmail(userDto.getEmail());
        User userByLogin = userByLoginMono.block();

        if (userByLogin != null) {
            throw new UserAlreadyExistsException(MSG_ERR_USER_ALREADY_EXISTS);
        }

    }
}
