package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class WebsocketUsers {

    /**
     * Map of lunch plan uuid with its corresponding users
     */
    private final Map<String, ConcurrentLinkedQueue<WebSocketSession>> uuidWebSocketMap = new ConcurrentHashMap<>();

    public void add(String uuid, WebSocketSession session){
        var list = uuidWebSocketMap.getOrDefault(uuid, new ConcurrentLinkedQueue<>());
        list.add(session);
        uuidWebSocketMap.put(uuid, list);
    }

    public void remove(String uuid, WebSocketSession session){

        var newData = uuidWebSocketMap.get(uuid)
                .stream().filter(s->!s.getId().equals(session.getId()))
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));

        uuidWebSocketMap.put(uuid, newData);
    }

    public ConcurrentLinkedQueue<WebSocketSession> get(String uuid){
        return uuidWebSocketMap.get(uuid);
    }



}
