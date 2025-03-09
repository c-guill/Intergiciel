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

    @Value("${application.topic}")
    private String TOPIC;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Message message) {
        //logger.info(String.format("#### -> Producing message -> %s", message));
        try
        {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(message);
            System.out.println("Sending message: " + json);
            this.kafkaTemplate.send(TOPIC, json);

        }
        catch (Exception e)
        {
//            logger.info(String.format("#### -> Error sending message -> %s", message));
        }
    }
}
