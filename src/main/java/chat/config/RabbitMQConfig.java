package chat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitMQConfig {

	@Bean
	public DirectExchange SendMessageExchange() {
		return new DirectExchange("SendMessageExchange");
	}

	@Bean
	public TopicExchange ChatTypeExchange() {
	    return new TopicExchange("ChatTypeExchange");
	}

	@Bean
	public Queue SendMessageQueue() {
	    return new Queue("SendMessageQueue", true); // 傳送消息隊列
	}

	@Bean 
	public Queue ChatIdQueue() {
		return new Queue("ChatIdQueue",true);// 片段傳送給誰隊列
	}
	
    @Bean
	public Binding SendMessageBinding() {
	    return BindingBuilder.bind(SendMessageQueue()).to(SendMessageExchange()).with("SendMessage");
	}

	@Bean
    public Binding chatIdBinding() {
    	return BindingBuilder.bind(ChatIdQueue()).to(ChatTypeExchange()).with("ChatId.#");
    }
}