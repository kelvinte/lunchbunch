package sg.okayfoods.lunchbunch.infrastracture.adapter.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command.AuthorizationHandler;
import sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command.Command;
import sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command.SuggestionHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class WebsocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<WebSocketSession> webSocketSessions = new CopyOnWriteArrayList<>();
    private final Map<String, List<WebSocketSession>> lunchplanUsers = new ConcurrentHashMap<>();

    public static final String VALID_QUERY_PARAMS = "suggest";

    private HashMap<String, Command> commandHashMap;

    public WebsocketHandler(AuthorizationHandler authorizationHandler, List<? extends Command> suggestionHandler) {

        for(var actionEnum : WebSocketAction.values()){

        }
        for(var s: suggestionHandler){
            commandHashMap.put(s.)
        }
//        this.authorizationHandler = authorizationHandler;
        this.commandHashMap = new HashMap<>();
        // Change to Spring dependency inject
        commandHashMap.put(VALID_QUERY_PARAMS, suggestionHandler);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        //validate url was correct
        String queryParams = getQueryParam(session.getUri());
        if(!queryParams.equals( VALID_QUERY_PARAMS)){
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        super.handleTextMessage(session, message);
        /*
        SUGGEST
        GET
         */
        if(session.isOpen()) {
            String queuryParam = getQueryParam(session.getUri());
            if(!StringUtils.isEmpty(queuryParam)){
                String payload = message.getPayload();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNote = mapper.readTree(payload);
                String action = rootNote.get("action").asText();
            }
        }
    }

    public void parse(TextMessage textMessage){
        try {
            String payload = textMessage.getPayload();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNote = mapper.readTree(payload);
            String action = rootNote.get("action").asText();

        }catch (IOException e){
            log.error("Failed to parse request");
            throw new AppException(ErrorCode.FAILED_TO_PROCESS_WS);
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        session.close();
    }

    private String getQueryParam(URI uri){
        if(uri == null){
            throw new AppException(ErrorCode.FAILED_TO_PROCESS_WS);
        }
        String query = uri.getQuery();
        return StringUtils.substringBetween(query,"?","=" );
    }
}
