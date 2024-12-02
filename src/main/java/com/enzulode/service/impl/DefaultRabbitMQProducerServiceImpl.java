package com.enzulode.service.impl;

import com.enzulode.service.RabbitMQProducerService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultRabbitMQProducerServiceImpl implements RabbitMQProducerService {

  private final AmqpTemplate amqpTemplate;

  @Value("${interactive.exchange}")
  private String exchange;

  public DefaultRabbitMQProducerServiceImpl(
      @Qualifier("defaultTemplate") AmqpTemplate amqpTemplate) {
    this.amqpTemplate = amqpTemplate;
  }

  @Override
  public void sendToRabbitMQ(Object message) {
    amqpTemplate.convertAndSend(exchange, "", message);
  }
}
