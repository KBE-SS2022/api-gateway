package messageproducer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceMQConfig {

    @Bean
    public TopicExchange priceExchange() { return new TopicExchange(Constant.PRICE_EXCHANGE); }

    @Bean
    public Queue getPriceQueue() { return new Queue(Constant.GET_PRICE_QUEUE, false); }

    @Bean
    public Binding getPriceBinding(@Qualifier("getPriceQueue") Queue queue,
                                    @Qualifier("priceExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GET_KEY);
    }
}
