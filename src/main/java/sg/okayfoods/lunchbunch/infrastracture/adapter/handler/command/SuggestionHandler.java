package sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.WebSocketRequestDTO;

@Service
public class SuggestionHandler implements Command{

    @Override
    public void handle(WebSocketSession session, TextMessage message) {
        if(session.isOpen()){
            String json = message.getPayload();
            // use same as sqs
        }
    }
}
