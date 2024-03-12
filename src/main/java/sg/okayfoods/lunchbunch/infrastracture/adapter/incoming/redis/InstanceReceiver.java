package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class InstanceReceiver implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String receivedMessage = new String(message.getBody());
        System.out.println("Received message: " + receivedMessage);
        // Process the received message as needed
    }
}
