package fr.miaou.frontend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Contact {


    public Long id;
    public String username;
    public String lastMessage;
    public Timestamp timestamp;
    public Status status;

    public Contact(Long id, String username, String lastMessage, Timestamp timestamp, Status status) {
        this.id = id;
        this.username = username;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Contact(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Contact(Long id) {
        this.id = id;
    }
}
