package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer;

public interface RedisSubscriber {
    void onRedisReceive(String uuid, Object data);
}
