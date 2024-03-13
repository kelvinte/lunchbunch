package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.redis.RedisDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.RedisNotifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisReceiverTest {

    @Mock
    RedisNotifier redisNotifier;

    @Mock
    RedisIdentifer redisIdentifer;
    RedisReceiver redisReceiver;

    @BeforeEach
    void beforeEach(){
        when(redisIdentifer.getRedisIdentifier()).thenReturn("me");
        redisReceiver = new RedisReceiver(List.of(redisNotifier), redisIdentifer);

    }
    @Test
    void messageIsFromSameInstance_onMessage_shouldNotPassToNotifier() {
        Message message = Mockito.mock(Message.class);
        RedisDTO redisDTO = new RedisDTO();
        redisDTO.setRedisId("me");
        redisDTO.setData(new WebsocketDTO());

        when(message.getBody()).thenReturn(JsonUtils.toJson(redisDTO).getBytes());

        redisReceiver.onMessage(message, "azd".getBytes());
        verify(redisNotifier,never()).onRedisReceive(any(WebsocketDTO.class));
    }

    @Test
    void messageIsFromOtherInstance_onMessage_shouldPassToNotifier(){
        Message message = Mockito.mock(Message.class);
        RedisDTO redisDTO = new RedisDTO();
        redisDTO.setRedisId("other");
        redisDTO.setData(new WebsocketDTO());

        when(message.getBody()).thenReturn(JsonUtils.toJson(redisDTO).getBytes());

        redisReceiver.onMessage(message, "azd".getBytes());
        verify(redisNotifier).onRedisReceive(any(WebsocketDTO.class));
    }
}