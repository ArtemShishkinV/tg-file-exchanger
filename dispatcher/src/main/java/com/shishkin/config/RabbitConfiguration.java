package com.shishkin.config;

import com.shishkin.Properties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(Properties.RabbitQueue.TEXT_MESSAGE);
    }

    @Bean
    public Queue docMessageQueue() {
        return new Queue(Properties.RabbitQueue.DOC_MESSAGE);
    }

    @Bean
    public Queue photoMessageQueue() {
        return new Queue(Properties.RabbitQueue.PHOTO_MESSAGE);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(Properties.RabbitQueue.ANSWER_MESSAGE);
    }
}
