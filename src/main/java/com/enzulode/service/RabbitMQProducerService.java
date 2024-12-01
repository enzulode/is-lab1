package com.enzulode.service;

public interface RabbitMQProducerService {

  void sendToRabbitMQ(Object message, String routingKey);
}
