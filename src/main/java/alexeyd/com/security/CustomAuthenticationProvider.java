package alexeyd.com.security;

import alexeyd.com.model.User;
import alexeyd.com.repository.DefaultChatRepository;
import alexeyd.com.repository.UserRepository;
import alexeyd.com.util.CryptoUtils;
import alexeyd.com.util.SDESCypherUtils;
import exceptions.UserNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static alexeyd.com.consts.Common.MSG_ERR_INCORRECT_PASSORD;
import static alexeyd.com.consts.Common.MSG_ERR_USER_WASN_T_FOUND;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private Environment env;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userService;

    @Autowired
    private DefaultChatRepository defaultChatRepository;

    // FIXME: Try to change if-clause to lambda-style using ifPresent. I've noticed that it demands Java 9+ version of compiler
    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //Mono<User> userByLogin = Mono.justOrEmpty(userService.findFirstByEmail(emailAddress));
        String name = authentication.getName();
        name = SDESCypherUtils.encodePhrase(getSecretKey(), name);
        Mono<User> userByLoginMono = userService.findFirstByEmail(name);
        User userByLogin = userByLoginMono.block();

        List<String> collect = defaultChatRepository.findAll().flatMapIterable(p -> Arrays.asList(p.getTopic())).distinct().toStream().collect(Collectors.toList());

        if (userByLogin == null) {
            throw new UserNotFoundException(MSG_ERR_USER_WASN_T_FOUND);
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String decodedPassword = userByLogin.getPassword();
        decodedPassword = SDESCypherUtils.decodePhrase(getSecretKey(), decodedPassword);

        String credentials = authentication.getCredentials().toString();
        if (!credentials.equals(decodedPassword)) {
            //if (!bCryptPasswordEncoder.matches(password, decodedPassword)) {
            throw new UserNotFoundException(MSG_ERR_INCORRECT_PASSORD);
        }

            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        String userRole = userByLogin.getUserRole();
        userRole = SDESCypherUtils.decodePhrase(getSecretKey(), userRole);
        grantedAuthorities.add(new SimpleGrantedAuthority(userRole));

        String email = userByLogin.getEmail();
        email = SDESCypherUtils.decodePhrase(getSecretKey(), email);
        String password = userByLogin.getPassword();
        password = SDESCypherUtils.decodePhrase(getSecretKey(), password);
        return new UsernamePasswordAuthenticationToken(email, password,
            grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private int getSecretKey(){
        return Integer.parseInt(env.getProperty("secretKey"));
    }
}
