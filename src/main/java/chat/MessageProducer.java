package chat;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/*@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Queue queue() {
        return new Queue("testQueue", false);
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("testQueue", message);
        System.out.println("Message Sent: " + message);
    }
}*/
