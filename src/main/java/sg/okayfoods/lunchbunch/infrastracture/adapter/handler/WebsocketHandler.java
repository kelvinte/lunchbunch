package sg.okayfoods.lunchbunch.infrastracture.adapter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebsocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    Random r = new Random();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
//        Message theMsg = new Message();
//        theMsg.setName("Kelvin");
//
//        TextMessage message = new TextMessage(objectMapper.writeValueAsString(theMsg));
//        session.sendMessage(message);
//
//
//
//        sessions.add(session);
    }

}
