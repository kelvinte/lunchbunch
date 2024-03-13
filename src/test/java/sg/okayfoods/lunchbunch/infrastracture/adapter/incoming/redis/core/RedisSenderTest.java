package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisSenderTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ChannelTopic topic;

    @Mock
    private RedisIdentifer redisIdentifer;
    private RedisSender redisSender;
    @BeforeEach
    void beforeEach(){

        when(topic.getTopic()).thenReturn("zzzz");
        when(redisIdentifer.getRedisIdentifier()).thenReturn("me");
        redisSender = new RedisSender(topic, redisTemplate, redisIdentifer);

    }
    @Test
    void publish_verifyRedisTemplateCalled() {
        redisSender.publish(new WebsocketDTO());

        verify(redisTemplate).convertAndSend(anyString(), anyString());
    }
}