package alexeyd.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * @author Alexey Druzik on 03.05.2021
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
public class SpringMongodbApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringMongodbApplication.class, args);
	}

}
