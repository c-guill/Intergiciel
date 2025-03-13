package fr.miaou.frontend.service;

import fr.miaou.frontend.config.KafkaDynamicConsumerConfig;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.Random;

@Service
public class KafkaDynamicConsumerService {

    @Autowired
    private KafkaDynamicConsumerConfig kafkaDynamicConsumerConfig;

    private ConcurrentMessageListenerContainer<String, String> container;

    @Autowired
    private WebSocketService webSocketService;


    public void startListening(String groupId) {
        if (container == null) {
            MessageListener<String, String> listener = record -> {
                System.out.println(record.value());
                this.webSocketService.sendMessage(record.value());
            };
            container = kafkaDynamicConsumerConfig.kafkaListenerContainer(groupId, listener);
        }
        if (!container.isRunning()) {
            container.start();
        }
    }

    public void stopListening() {
        if (container != null) {
            container.stop();
        }
    }
}
