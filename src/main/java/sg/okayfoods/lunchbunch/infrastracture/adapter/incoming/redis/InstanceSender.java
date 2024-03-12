package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@EnableScheduling
public class InstanceSender {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private ChannelTopic topic;

//    @Scheduled(fixedRate = 5000)
    public void publish() {
        String uuid = UUID.randomUUID().toString();
        redisTemplate.convertAndSend(topic.getTopic(),"TESTTTT"+uuid);
    }
}
