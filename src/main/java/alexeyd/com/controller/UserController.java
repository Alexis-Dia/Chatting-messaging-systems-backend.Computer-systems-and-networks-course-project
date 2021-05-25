package alexeyd.com.controller;

import alexeyd.com.model.User;
import alexeyd.com.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import static alexeyd.com.consts.Common.ROLE_DRIVER;
import static alexeyd.com.consts.RestNavigation.PATH_USER_SIGN_UP;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @PostMapping(path = PATH_USER_SIGN_UP, consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> createNewUser(@RequestBody User user) {
        //User user = new User(u.getName(), u.getEmail(), u.getPassword(), ROLE_DRIVER);
        user.setUserRole(ROLE_DRIVER);
        user.setId(System.currentTimeMillis());
        return userRepository.save(user)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
