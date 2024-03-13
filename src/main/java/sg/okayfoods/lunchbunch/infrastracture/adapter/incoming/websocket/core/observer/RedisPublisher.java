package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer;

public interface RedisPublisher {

    void redisSend(String lunchPLanUuid, Object object);
}
