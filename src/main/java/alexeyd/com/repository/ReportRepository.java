package alexeyd.com.repository;

import alexeyd.com.model.Report;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportRepository extends ReactiveMongoRepository<Report, Long> {

    @Tailable
    Flux<Report> findAllByCodeIsNot(String code);

    Mono save(Report report);
}
