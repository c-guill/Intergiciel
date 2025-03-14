package fr.miaou.frontend.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Contact {


    public Long id;
    public String username;
    public String lastMessage;
    public String date;

    public Contact(Long id, String username, String lastMessage, Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.lastMessage = lastMessage;
        if (timestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            this.date = timestamp.toLocalDateTime().format(formatter);
        }
    }

    public Contact(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Contact(Long id) {
        this.id = id;
    }
}
