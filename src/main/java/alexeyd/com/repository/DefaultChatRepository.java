package alexeyd.com.repository;

import alexeyd.com.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface DefaultChatRepository extends ReactiveMongoRepository<Message, Long> {

    @Tailable
    Flux<Message> findAllByTopic(String topic);

    void deleteByTopicIn(List<String> ids);

    Mono save(Message message);

}
