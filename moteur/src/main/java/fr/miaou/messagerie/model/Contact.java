package fr.miaou.messagerie.model;

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
//    public Status status;

    public Contact(Long id, String username, String lastMessage, Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public Contact(User user) {
        this.id = user.getIdUser();
        this.username = user.getNom();
    }
}