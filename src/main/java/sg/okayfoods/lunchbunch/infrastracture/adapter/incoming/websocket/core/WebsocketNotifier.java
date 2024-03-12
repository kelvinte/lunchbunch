package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.SuggestionObserver;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WebsocketNotifier implements SuggestionObserver {
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

    @Override
    public void onNewSuggestion(String uuid, CreateSuggestionDTO suggestionDTO) {
        // notify
        var list = uuidWebSocketMap.get(uuid);
        WebsocketResponseDTO<CreateSuggestionDTO> websockResp = new WebsocketResponseDTO<>();
        websockResp.setAction(WebSocketAction.NOTIFY_ONE_SUGGESTION.name());
        websockResp.setData(suggestionDTO);
        String resp = JsonUtils.toJson(websockResp);

        for (var session : list){
            try {
                session.sendMessage(new TextMessage(resp));
            }catch (Exception e){
                log.error("Failed to send: "+e);
            }
        }
    }
}
