  package chat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitMQConfig {
/**------------------------------------direct模式-----------------------------------------------------------------------------**/
	@Bean
	public DirectExchange SendOfflineUsersExchange() {
		return new DirectExchange("SendOfflineUsersExchange");
	}
	
	@Bean
	public Queue SendOfflineUsersQueue() {
		return new Queue("SendOfflineUsersQueue", true);
	}
	
	@Bean
	public Binding SendOfflineUsersBinding() {
		return BindingBuilder.bind(SendMessageToOfflineUsersQueue()).to(SendOfflineUsersExchange()).with("OfflineUserList");
	}

/**------------------------------------Fanout模式-----------------------------------------------------------------------------**/
	@Bean
	public FanoutExchange SendMessageExchange() {
		return new FanoutExchange("SendMessageExchange");
	}

	@Bean
	public Queue SendMessageToOnlineUsersQueue() {
	    return new Queue("SendMessageToOnlineUsersQueue", true); // 在線會員通道
	}
	
	@Bean
	public Queue SendMessageToOfflineUsersQueue() {
		return new Queue("SendMessageToOfflineUsersQueue", true); // 不在線會員通道
	}

	@Bean
	public Binding SendMessageToOnlineUsersBinding() {
	    return BindingBuilder.bind(SendMessageToOnlineUsersQueue()).to(SendMessageExchange());
	}
	
	@Bean
	public Binding SendMessageToOfflineUsersBinding() {
		return BindingBuilder.bind(SendMessageToOfflineUsersQueue()).to(SendMessageExchange());
	}
/**-------------------------------------Topic模式--------------------------------------------------------------------------**/
	@Bean
	public TopicExchange ChatTypeExchange() {
	    return new TopicExchange("ChatTypeExchange");
	}

	@Bean 
	public Queue ChatIdQueue() {
		return new Queue("ChatIdQueue",true);// 片段傳送給誰隊列
	}
	
    @Bean
    public Binding chatIdBinding() {
    	return BindingBuilder.bind(ChatIdQueue()).to(ChatTypeExchange()).with("ChatId.#");
    }
}