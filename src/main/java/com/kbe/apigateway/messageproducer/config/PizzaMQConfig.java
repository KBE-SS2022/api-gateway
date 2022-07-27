package com.kbe.apigateway.messageproducer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PizzaMQConfig {

    @Bean
    public TopicExchange pizzaExchange() { return new TopicExchange(Constant.PIZZA_EXCHANGE); }

    @Bean
    public Queue getAllPizzaQueue() { return new Queue(Constant.GETALL_PIZZA_QUEUE, false); }

    @Bean
    public Queue getPizzaQueue() { return new Queue(Constant.GET_PIZZA_QUEUE, false); }

    @Bean
    public Queue createPizzaQueue() { return new Queue(Constant.CREATE_PIZZA_QUEUE, false); }

    @Bean
    public Binding getPizzasBinding(@Qualifier("getAllPizzaQueue") Queue queue,
                                    @Qualifier("pizzaExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GETALL_KEY);
    }

    @Bean
    public Binding getPizzaByIDBinding(@Qualifier("getPizzaQueue") Queue queue,
                                       @Qualifier("pizzaExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GET_KEY);
    }

    @Bean
    public Binding createPizzaBinding(@Qualifier("createPizzaQueue") Queue queue,
                                      @Qualifier("pizzaExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.CREATE_KEY);
    }
}
