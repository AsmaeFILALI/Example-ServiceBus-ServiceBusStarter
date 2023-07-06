package com.example.messagingdemo.serviceBus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.example.messagingdemo.MenuOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ServiceBusProducer {


    @Autowired
    private ServiceBusSenderClient senderClient;

    ObjectMapper objectMapper = new ObjectMapper();

    public void SendMsg(MenuOrder menuOrder) throws JsonProcessingException {
        senderClient.sendMessage(new ServiceBusMessage(objectMapper.writeValueAsString(menuOrder)));
        log.info("*******************ServiceBUS_MessageSent************: {}", menuOrder.toString());
        senderClient.close();
    }


}



