package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisIdentifer {

    private String identifier;

    @PostConstruct
    public void generateIdentifier(){
        this.identifier = UUID.randomUUID().toString();
    }
    public String getRedisIdentifier(){
        return identifier;
    }
}
