package sg.okayfoods.lunchbunch.infrastracture.adapter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class WebsocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<WebSocketSession> webSocketSessions = new CopyOnWriteArrayList<>();
    private final Map<String, List<WebSocketSession>> lunchplanUsers = new ConcurrentHashMap<>();

    private final AuthorizationHandler authorizationHandler;

    public WebsocketHandler(AuthorizationHandler authorizationHandler) {
        super();
        this.authorizationHandler = authorizationHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected! "+session.getId());

        AppUser user = authorizationHandler.handle(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info("handle text message! "+session.getId()+ " "+message.toString());
//        message.getPayload()
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
