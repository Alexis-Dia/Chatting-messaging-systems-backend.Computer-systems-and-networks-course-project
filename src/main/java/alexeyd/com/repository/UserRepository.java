package alexeyd.com.repository;

import alexeyd.com.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository  extends ReactiveMongoRepository<User, Long> {
}
