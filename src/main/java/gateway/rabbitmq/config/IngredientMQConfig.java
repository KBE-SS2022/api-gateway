package gateway.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IngredientMQConfig {

    @Bean
    public TopicExchange ingredientExchange() { return new TopicExchange(Constant.INGREDIENT_EXCHANGE); }

    @Bean
    public Queue getAllIngredientQueue() { return new Queue(Constant.GETALL_INGREDIENT_QUEUE, false); }

    @Bean
    public Queue getIngredientQueue() { return new Queue(Constant.GET_INGREDIENT_QUEUE, false); }

    @Bean
    public Binding getIngredientsBinding(@Qualifier("getAllIngredientQueue") Queue queue,
                                    @Qualifier("ingredientExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GETALL_KEY);
    }

    @Bean
    public Binding getIngredientByIDBinding(@Qualifier("getIngredientQueue") Queue queue,
                                       @Qualifier("ingredientExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GET_KEY);
    }
}
