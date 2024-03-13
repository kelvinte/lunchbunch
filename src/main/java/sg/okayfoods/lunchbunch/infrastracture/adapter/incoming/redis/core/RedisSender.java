package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.redis.RedisDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;

@Service
public class RedisSender {

    RedisTemplate<String, String> redisTemplate;

    private ChannelTopic topic;
    private String redisInstanceId;

    public RedisSender( ChannelTopic topic, RedisTemplate<String, String> redisTemplate, RedisIdentifer redisIdentifer){
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.redisInstanceId = redisIdentifer.getRedisIdentifier();
    }

    public void publish(WebsocketDTO data) {
        RedisDTO redisDTO = RedisDTO.builder()
                .redisId(redisInstanceId)
                .data(data)
                .build();
        redisTemplate.convertAndSend(topic.getTopic(), JsonUtils.toJson(redisDTO));
    }
}
