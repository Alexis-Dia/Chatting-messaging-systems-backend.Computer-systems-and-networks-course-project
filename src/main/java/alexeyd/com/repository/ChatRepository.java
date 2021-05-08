package alexeyd.com.repository;

import alexeyd.com.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Message, Long> {

    @Tailable
    Flux<Message> findAllByTopic(String topic);

}
