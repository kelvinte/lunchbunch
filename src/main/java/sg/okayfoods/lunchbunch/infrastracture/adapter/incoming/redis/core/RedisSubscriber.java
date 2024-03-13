package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core;

import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;

public interface RedisSubscriber {
    void onRedisReceive(WebsocketDTO websocketDTO);
}
