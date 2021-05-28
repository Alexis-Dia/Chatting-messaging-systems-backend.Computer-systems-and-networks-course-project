package alexeyd.com.repository;

import alexeyd.com.model.Message;
import alexeyd.com.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserRepository extends ReactiveMongoRepository<User, Long> {

    Mono<User> findFirstByEmail(String email);

    Flux<User> findAllById(Long id);

    Mono save(User user);
}
