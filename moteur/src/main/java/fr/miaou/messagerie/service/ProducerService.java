package fr.miaou.messagerie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.miaou.messagerie.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Message message) {
        try
        {
            String topic = message.getIdDestination() == -1 ? "broadcast" : message.getIdDestination().toString();
            System.out.println(topic);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(message);
            this.kafkaTemplate.send(topic, json);

        }
        catch (Exception e)
        {
            System.err.println("Error while sending message: " + e.getMessage());
        }
    }
}
