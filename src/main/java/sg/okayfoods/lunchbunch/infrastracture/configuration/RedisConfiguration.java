package sg.okayfoods.lunchbunch.infrastracture.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.RedisReceiver;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {
    @Autowired
    RedisReceiver redisReceiver;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build();
        var redis = new RedisStandaloneConfiguration(
                "redis-19078.c10.us-east-1-3.ec2.cloud.redislabs.com", 19078);
        redis.setUsername("default");
        redis.setPassword("fx3386n4qleQFbaxfHg59tnykphOrgSv");
        return new LettuceConnectionFactory(redis, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("lunchbunch");
    }
    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(redisReceiver);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }

}
