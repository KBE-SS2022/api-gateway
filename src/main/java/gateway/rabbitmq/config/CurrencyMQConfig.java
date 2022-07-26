package gateway.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyMQConfig {

    @Bean
    public TopicExchange currencyExchange() { return new TopicExchange(Constant.CURRENCY_EXCHANGE); }

    @Bean
    public Queue getCurrencyQueue() { return new Queue(Constant.GET_CURRENCY_QUEUE, false); }

    @Bean
    public Binding getCurrencyBinding(@Qualifier("getCurrencyQueue") Queue queue,
                                      @Qualifier("currencyExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GET_KEY);
    }
}