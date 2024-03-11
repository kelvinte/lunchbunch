package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketResponseDTO;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class WebsocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public static final String VALID_QUERY_PARAMS = "suggest";

    private HashMap<WebSocketAction, WebsocketCommand> commandHashMap;
    private WebsocketNotifier websocketNotifier;

    public WebsocketHandler(List<? extends WebsocketCommand> handlers, WebsocketNotifier websocketNotifier) {

        this.websocketNotifier = websocketNotifier;
        commandHashMap = new HashMap<>();

        for(var actionEnum : WebSocketAction.values()){

            for(var handler: handlers){
                if(handler.supports(actionEnum)){
                    if(commandHashMap.containsKey(actionEnum)){
                        throw new BeanCreationException("Duplicate handler for " + actionEnum.name());
                    }
                    commandHashMap.put(actionEnum, handler);
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String uuid = getSuggestionUUID(session.getUri());
            this.websocketNotifier.add(uuid, session);
        }catch (AppException e){
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(session.isOpen()) {
            String uuid = getSuggestionUUID(session.getUri());
            if(!StringUtils.isEmpty(uuid)){
                String payload = message.getPayload();
                JsonNode rootNode = objectMapper.readTree(payload);
                String action = rootNode.get("action").asText();

                WebsocketCommand command = commandHashMap.get(WebSocketAction.valueOf(action));
                if(command == null){
                    throw new AppException(ErrorCode.FAILED_TO_PARSE_WEBSOCKET_ACTION);
                }
                Object res = null;
                if(rootNode.has("data")) {
                    String data = rootNode.get("data").toString();
                    res = JsonUtils.toObject(data,  command.getMethodClass());
                }

                WebsocketDTO websocketDTO = WebsocketDTO.builder()
                        .uuid(uuid)
                        .data(res)
                        .build();
                var response = command.handleInternal(session, websocketDTO);

                if(response!=null) {
                    WebsocketResponseDTO<Object> websockResp = new WebsocketResponseDTO<>();
                    websockResp.setAction(action);
                    websockResp.setData(response);
                    session.sendMessage(new TextMessage(JsonUtils.toJson(websockResp)));
                }
            }
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        session.close();
        String uuid = getSuggestionUUID(session.getUri());
        this.websocketNotifier.remove(uuid, session);
    }

    private String getSuggestionUUID(URI uri){
        String query = uri.getQuery();

        String parameter = StringUtils.substringBefore(query, "=");
        if(!parameter.equals( VALID_QUERY_PARAMS)){
            throw new AppException(ErrorCode.FAILED_TO_PROCESS_WS);
        }
        return StringUtils.substringAfter(query, "=");
    }
}
