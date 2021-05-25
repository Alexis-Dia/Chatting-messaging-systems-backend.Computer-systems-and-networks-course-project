package alexeyd.com.model;

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
public class Message {
    @MongoId
    private long id;
    private String topic;
    private String author;
    private boolean deleted = false;

    @JsonFormat(pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationDate;
    private String text;

    public Message(String topic, String author, String text) {
        this.topic = topic;
        this.author = author;
        this.text = text;
    }

    public Message(String topic, boolean deleted) {
        this.topic = topic;
        this.deleted = deleted;
    }

    public boolean getDeleted() {
        return deleted;
    }
}
