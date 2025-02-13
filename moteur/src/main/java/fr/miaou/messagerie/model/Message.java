import java.sql.Timestamp;
import lombok.Data;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private Long idMessage;
    private User user;
    private Long idDestination;
    private String contenu;
    private Timestamp date;

    public Message() {}

    public Message(Long idMessage, User user, Long idDestination, String contenu, Timestamp date) {
        this.idMessage = idMessage;
        this.user = user;
        this.idDestination = idDestination;
        this.contenu = contenu;
        this.date = date;
    }

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public User getuser() {
        return user;
    }

    public void setuser(User user) {
        this.user = user;
    }

    public Long getIdDestination() {
        return idDestination;
    }

    public void setIdDestination(Long idDestination) {
        this.idDestination = idDestination;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "idMessage=" + idMessage +
                ", user=" + user +
                ", idDestination=" + idDestination +
                ", contenu='" + contenu + '\'' +
                ", date=" + date +
                '}';
    }
}
