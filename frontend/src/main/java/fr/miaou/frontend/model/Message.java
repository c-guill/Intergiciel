package fr.miaou.frontend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Message {

    private Contact sender;
    private Contact receiver;
    private String message;
    private String date;

    public Message(Contact sender, Contact receiver, String message, Timestamp date) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.date = date.toLocalDateTime().format(formatter);;
    }


}
