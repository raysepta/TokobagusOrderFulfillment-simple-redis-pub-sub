package co.g2academy.tokobagusorderfulfillment;

import co.g2academy.tokobagusorderfulfillment.subscriber.OrderMessageSubscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@SpringBootApplication
@EnableCaching
public class TokobagusOrderFulfillmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokobagusOrderFulfillmentApplication.class, args);
    }

    @Bean
    public JedisConnectionFactory initJedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

//    @Bean
//    public RedisTemplate<String, String> redisTemplate() {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(initJedisConnectionFactory());
//        RedisSerializer<String> serializer = new GenericToStringSerializer<>(String.class);
//        redisTemplate.setDefaultSerializer(serializer);
//        return redisTemplate;
//    }
    @Bean
    public MessageListenerAdapter messageListener(OrderMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    public RedisMessageListenerContainer messageContainer(MessageListenerAdapter adapter) {
        ChannelTopic topic = new ChannelTopic("orderMessageQueue");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(initJedisConnectionFactory());
        container.addMessageListener(adapter, topic);
        return container;
    }



}
