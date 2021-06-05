package alexeyd.com.controller;

import alexeyd.com.model.User;
import alexeyd.com.repository.UserRepository;
import alexeyd.com.service.CommonService;
import alexeyd.com.util.CryptoUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static alexeyd.com.consts.Common.ROLE_DRIVER;
import static alexeyd.com.consts.RestNavigation.PATH_USER_CHANGE_NICK_NAME;
import static alexeyd.com.consts.RestNavigation.PATH_USER_SIGN_UP;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@AllArgsConstructor
public class UserController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private final UserRepository userRepository;

    @PostMapping(path = PATH_USER_SIGN_UP, consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> createNewUser(@RequestBody User user) throws Exception {
        user.setUserRole(ROLE_DRIVER);
        user.setId(System.currentTimeMillis());

        user = CryptoUtils.encryptWholeObject(commonService.getSecretKey(), user);
        return userRepository.save(user)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @ResponseBody
    @PutMapping(path = PATH_USER_CHANGE_NICK_NAME)
    public ResponseEntity<String> changeNickName(@RequestParam("userId") String userId, @RequestParam("name") String name) {

        List<User> block = userRepository.findAllById(Long.parseLong(userId)).collectList().block();
        for (User ob: block) {
            ob.setName(name);
            userRepository.save(ob).block();
        }

        return new ResponseEntity<>(name, HttpStatus.OK);
    }

}
