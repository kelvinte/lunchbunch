package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.redis.RedisDTO;

import java.util.UUID;

@Service
public class RedisSender {

    RedisTemplate<String, Object> redisTemplate;

    private ChannelTopic topic;
    private String redisInstanceId;

    public RedisSender( ChannelTopic topic, RedisTemplate<String, Object> redisTemplate, RedisIdentifer redisIdentifer){
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.redisInstanceId = redisIdentifer.getRedisIdentifier();
    }


    public void publish(String lunchPLanUuid, Object data) {
        RedisDTO redisDTO = RedisDTO.builder().redisId(redisInstanceId)
                .uuid(lunchPLanUuid)
                .data(data)
                .build();
        redisTemplate.convertAndSend(topic.getTopic(), JsonUtils.toJson(redisDTO));
    }
}
