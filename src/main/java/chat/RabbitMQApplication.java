package chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*@SpringBootApplication
public class RabbitMQApplication implements CommandLineRunner {

    @Autowired
    private MessageProducer messageProducer;

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 發送一條測試消息
//        messageProducer.sendMessage("Hello, RabbitMQ!");
        messageProducer.sendMessage("你好, RabbitMQ!");
    }
}*/