package fr.miaou.messagerie.service;

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

    public void sendMessage(String message) {
        //logger.info(String.format("#### -> Producing message -> %s", message));
        try
        {
            System.out.println("Sending message: " + message);
            this.kafkaTemplate.send(TOPIC, message);

        }
        catch (Exception e)
        {
//            logger.info(String.format("#### -> Error sending message -> %s", message));
        }
    }
}
