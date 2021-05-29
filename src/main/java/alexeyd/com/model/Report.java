package alexeyd.com.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reports")
public class Report {

    @MongoId
    private long id;
    private String method;
    private String url;
    private String code;
    private String userName;
    private String localDateTime;

    public Report(long id, String method, String url, String code, String userName) {
        this.id = id;
        this.method = method;
        this.url = url;
        this.code = code;
        this.userName = userName;
    }
}
