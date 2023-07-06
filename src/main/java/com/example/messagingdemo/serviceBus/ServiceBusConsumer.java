package com.example.messagingdemo.serviceBus;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.spring.cloud.service.servicebus.consumer.ServiceBusErrorHandler;
import com.azure.spring.cloud.service.servicebus.consumer.ServiceBusRecordMessageListener;
import com.example.messagingdemo.MenuOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceBusConsumer {


    @Autowired
    ActionService actionService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    ServiceBusRecordMessageListener processMessage() {
        return context -> {
            ServiceBusReceivedMessage message = context.getMessage();
            MenuOrder menuOrder = null;
            try {
                menuOrder = objectMapper.readValue(message.getBody().toString(), MenuOrder.class);
                log.info("*****************SERVICEBUS_Message recieved***** : Processing message. Id: {}, Sequence #: {}. Contents: {}", message.getMessageId(),
                        message.getSequenceNumber(), menuOrder.toString());
                actionService.performAction(menuOrder);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


        };


    }

    @Bean
    ServiceBusErrorHandler processError() {
        return context -> {
            log.error("Error when receiving messages from namespace: {} . Entity: {}",
                    context.getFullyQualifiedNamespace(), context.getEntityPath());
        };
    }


}
