package alexeyd.com.configurations;

import alexeyd.com.model.Report;
import alexeyd.com.model.User;
import alexeyd.com.repository.ReportRepository;
import alexeyd.com.repository.UserRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class AppConfiguration {

    private static final String USERS_DB_NAME = "users";
    private static final String REPORTS_DB_NAME = "reports";
    private static final String MESSAGES_DB_NAME = "messages";

/*

    @Bean
    public CommonsRequestLoggingFilter logFilter(ReportRepository reportRepository) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Report report = new Report();
        report.setId(System.currentTimeMillis());
        report.setMethod("POST");
        report.setUrl("channels/addNewMessage");
        report.setLocalDateTime(LocalDateTime.now());
        report.setUserName("auth.getName()");
        report.setCode("200");
        reportRepository.save(report).block();

        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
*/

    @Autowired
    public void prepare(ReactiveMongoOperations mongoOperations,
                        UserRepository userRepository) {

        MongoClient mongoClient = MongoClients.create();
        MongoIterable<String> stringMongoIterable = mongoClient.getDatabase("general").listCollectionNames();
        boolean usersDbIsExists = false;
        boolean reportsDbIsExists = false;
        boolean messagesDbIsExists = false;
        for (String collectionsName: stringMongoIterable) {
            if (collectionsName.equals(USERS_DB_NAME)) {
                usersDbIsExists = true;
                //break;
            } else if (collectionsName.equals(REPORTS_DB_NAME)) {
                reportsDbIsExists = true;
            } else if (collectionsName.equals(MESSAGES_DB_NAME)) {
                messagesDbIsExists = true;
            }
        }

        if (!usersDbIsExists) {
            mongoOperations.createCollection(USERS_DB_NAME,
                    CollectionOptions.empty()
                            .maxDocuments(1_000)
                            .size(1024 * 8)).block();
            User admin = User.builder()
                    .id(1)
                    .name("Joe Doe")
                    .password("12345678")
                    .email("joedow@gmail.com")
                    .userRole("ADMIN")
                    .build();
            User user1 = User.builder()
                    .id(2)
                    .name("Sidorov Igor")
                    .password("12345678")
                    .email("sidorov@tut.by")
                    .userRole("DRIVER")
                    .build();
            User user2 = User.builder()
                    .id(3)
                    .name("Vasilev Ivan")
                    .password("12345678")
                    .email("vasiliev@tut.by")
                    .userRole("DRIVER")
                    .build();
            userRepository.insert(List.of(admin, user1, user2)).blockLast();
        }

        if (!reportsDbIsExists) {
            mongoOperations.createCollection(REPORTS_DB_NAME,
                    CollectionOptions.empty()
                            .maxDocuments(1_000)
                            .size(1024 * 8)
                            .capped()).block();
        }

        if (!messagesDbIsExists) {
            mongoOperations.createCollection(MESSAGES_DB_NAME,
                    CollectionOptions.empty()
                            .maxDocuments(1_000)
                            .size(1024 * 8)
                            .capped()).block();
        }
    }

}
