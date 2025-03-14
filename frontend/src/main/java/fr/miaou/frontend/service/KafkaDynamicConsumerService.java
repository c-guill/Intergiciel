package fr.miaou.frontend.service;

import fr.miaou.frontend.config.KafkaDynamicConsumerConfig;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.Optional;
import java.util.Random;

@Service
public class KafkaDynamicConsumerService {

    @Autowired
    private KafkaDynamicConsumerConfig kafkaDynamicConsumerConfig;

    private ConcurrentMessageListenerContainer<String, String> privateContainer;
    private ConcurrentMessageListenerContainer<String, String> broadcastContainer;
    private ConcurrentMessageListenerContainer<String, String> userContainer;

    @Autowired
    private WebSocketService webSocketService;


    public void startListening(String groupId) {
        if (privateContainer == null) {
            MessageListener<String, String> listener = record -> {
                this.webSocketService.sendMessage(record.value());
            };
            privateContainer = kafkaDynamicConsumerConfig.kafkaListenerContainer(groupId, Optional.empty(), listener);
        }

        if (broadcastContainer == null) {
            MessageListener<String, String> listener = record -> {
                this.webSocketService.sendMessageBroadcast(record.value());
            };
            broadcastContainer = kafkaDynamicConsumerConfig.kafkaListenerContainer(groupId, Optional.of("broadcast"), listener);
        }

        if (userContainer == null) {
            MessageListener<String, String> listener = record -> {
                this.webSocketService.manageUser(record.value());
            };
            userContainer = kafkaDynamicConsumerConfig.kafkaListenerContainer(groupId, Optional.of("user-status"), listener);
        }

        if (!broadcastContainer.isRunning()) {
            broadcastContainer.start();
        }

        if (!privateContainer.isRunning()) {
            privateContainer.start();
        }

        if (!userContainer.isRunning()) {
            userContainer.start();
        }
    }

    public void stopListening() {
        if (privateContainer != null) {
            privateContainer.stop();
        }
    }


}
