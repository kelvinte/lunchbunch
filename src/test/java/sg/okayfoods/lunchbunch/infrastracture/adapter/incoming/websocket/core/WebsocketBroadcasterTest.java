package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebsocketBroadcasterTest {

    @Mock
    private WebsocketUsers websocketUsers;
    @InjectMocks
    private WebsocketBroadcaster websocketBroadcaster;

    @Test
    void validUuid_onNewSuggestion_verifySendMessage() throws IOException{
        ConcurrentLinkedQueue<WebSocketSession> webSocketSessions = new ConcurrentLinkedQueue<>();
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        webSocketSessions.add(webSocketSession);

        doNothing().when(webSocketSession).sendMessage(any(TextMessage.class));
        when(websocketUsers.get(eq("aaaa"))).thenReturn(webSocketSessions);

        websocketBroadcaster.onNewSuggestion("aaaa",new CreateSuggestionDTO());
        verify(webSocketSession).sendMessage(any(TextMessage.class));
    }
    @Test
    void invalidUuid_onNewSuggestion_doNothing() throws IOException{
        ConcurrentLinkedQueue<WebSocketSession> webSocketSessions = new ConcurrentLinkedQueue<>();
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        webSocketSessions.add(webSocketSession);


        when(websocketUsers.get(eq("aaaa"))).thenReturn(null);

        websocketBroadcaster.onNewSuggestion("aaaa",new CreateSuggestionDTO());
        verify(webSocketSession,never()).sendMessage(any(TextMessage.class));
    }

    @Test
    void validUuid_onEndContest_verifySendMessage() throws IOException{
        ConcurrentLinkedQueue<WebSocketSession> webSocketSessions = new ConcurrentLinkedQueue<>();
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        webSocketSessions.add(webSocketSession);

        doNothing().when(webSocketSession).sendMessage(any(TextMessage.class));
        when(websocketUsers.get(eq("aaaa"))).thenReturn(webSocketSessions);

        websocketBroadcaster.onEndContest("aaaa",new LunchPlanWinnerResponseDTO());
        verify(webSocketSession).sendMessage(any(TextMessage.class));
    }
    @Test
    void invalidUuid_onEndContest_doNothing() throws IOException{
        ConcurrentLinkedQueue<WebSocketSession> webSocketSessions = new ConcurrentLinkedQueue<>();
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        webSocketSessions.add(webSocketSession);


        when(websocketUsers.get(eq("aaaa"))).thenReturn(null);

        websocketBroadcaster.onEndContest("aaaa",new LunchPlanWinnerResponseDTO());
        verify(webSocketSession,never()).sendMessage(any(TextMessage.class));
    }

}