package alexeyd.com.configurations;

import alexeyd.com.model.User;
import alexeyd.com.repository.UserRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class AppConfiguration {

    private static final String USERS_DB_NAME = "users";

    @Autowired
    public void prepare(ReactiveMongoOperations mongoOperations,
                        UserRepository userRepository) {

        MongoClient mongoClient = MongoClients.create();
        MongoIterable<String> stringMongoIterable = mongoClient.getDatabase("general").listCollectionNames();
        boolean usersDbIsExists = false;
        for (String collectionsName: stringMongoIterable) {
            if (collectionsName.equals(USERS_DB_NAME)) {
                usersDbIsExists = true;
                break;
            }
        }

        if (!usersDbIsExists) {
            mongoOperations.createCollection(USERS_DB_NAME,
                    CollectionOptions.empty()
                            .maxDocuments(1_000)
                            .size(1024 * 8)
                            .capped()).block();
            User admin = User.builder()
                    .id(1)
                    .name("Joe Doe")
                    .password("12345678")
                    .login("joedow@gmail.com")
                    .role(1)
                    .build();
            User user1 = User.builder()
                    .id(2)
                    .name("Sidorov Igor")
                    .password("12345678")
                    .login("sidorov@tut.by")
                    .role(2)
                    .build();
            User user2 = User.builder()
                    .id(3)
                    .name("Vasilev Ivan")
                    .password("12345678")
                    .login("vasiliev@tut.by")
                    .role(2)
                    .build();
            userRepository.insert(List.of(admin, user1, user2)).blockLast();
        }

    }

}
