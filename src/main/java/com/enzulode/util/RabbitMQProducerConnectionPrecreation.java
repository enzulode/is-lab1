package com.enzulode.util;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducerConnectionPrecreation implements SmartLifecycle {

  private final ConnectionFactory connectionFactory;
  private Connection connection;
  private boolean running = false;

  public RabbitMQProducerConnectionPrecreation(ConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  @Override
  public void start() {
    if (!running) {
      connection = connectionFactory.createConnection();
      running = true;
    }
  }

  @Override
  public void stop() {
    if (running && connection != null) {
      connection.close();
      running = false;
    }
  }

  @Override
  public boolean isRunning() {
    return running;
  }
}
