package alexeyd.com.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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
@Document(collection = "messages")
public class MessageView {
    @MongoId
    private long id;
    private String topic;
    private String author;
    private boolean deleted = false;

    @JsonFormat(pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationDate;
    private String text;

    public MessageView(String topic, String author, String text) {
        this.topic = topic;
        this.author = author;
        this.text = text;
    }

    public MessageView(String topic, boolean deleted) {
        this.topic = topic;
        this.deleted = deleted;
    }

    public boolean getDeleted() {
        return deleted;
    }
}
