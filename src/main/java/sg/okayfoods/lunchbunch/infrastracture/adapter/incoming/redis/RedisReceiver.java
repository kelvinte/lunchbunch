package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.redis.RedisDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.RedisSubscriber;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.SuggestionObserver;

import java.util.List;

@Service
public class RedisReceiver implements MessageListener {

    private List<RedisSubscriber> redisSubscribers;
    private String redisInstanceId;

    public RedisReceiver(List<RedisSubscriber> redisSubscribers, RedisIdentifer redisIdentifer) {
        this.redisSubscribers = redisSubscribers;
        this.redisInstanceId =redisIdentifer.getRedisIdentifier();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String receivedMessage = new String(message.getBody());
        System.out.println("Received message: " + receivedMessage);

        RedisDTO redisDTO = JsonUtils.toObject(receivedMessage, RedisDTO.class);

        if(redisInstanceId.equals(redisDTO.getRedisId())){
            // good to send
            for(var sub : redisSubscribers){
                sub.onRedisReceive(redisDTO.getUuid(), redisDTO.getData());
            }
        }
    }
}
