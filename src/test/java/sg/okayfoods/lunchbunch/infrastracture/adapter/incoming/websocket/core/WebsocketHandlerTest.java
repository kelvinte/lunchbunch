package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.EndSuggestionHandler;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.NewSuggestionHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebsocketHandlerTest {


    @Mock
    EndSuggestionHandler endSuggestionHandler;

    @Mock
    NewSuggestionHandler newSuggestionHandler;

    @Mock
    WebsocketUsers websocketUsers;

    WebsocketHandler websocketHandler;
    @BeforeEach
    void beforeEach(){
        when(endSuggestionHandler.supports(any(WebSocketAction.class))).thenCallRealMethod();
        when(newSuggestionHandler.supports(any(WebSocketAction.class))).thenCallRealMethod();
        List<? extends WebsocketCommand> list = List.of(endSuggestionHandler, newSuggestionHandler);

        websocketHandler = new WebsocketHandler(list, websocketUsers);
    }


    @Test
    void noQueryUrl_afterConnectionEstablished_sessionShouldClose() throws Exception {
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.getUri()).thenReturn(new URI("ws://localhost/xxxxxx"));

        websocketHandler.afterConnectionEstablished(webSocketSession);
        verify(webSocketSession).close(any(CloseStatus.class));

    }
    @Test
    void invalidQuery_afterConnectionEstablished_sessionShouldClose() throws Exception {
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.getUri()).thenReturn(new URI("ws://localhost?wrongparam=1234"));
        websocketHandler.afterConnectionEstablished(webSocketSession);
        verify(webSocketSession).close(any(CloseStatus.class));

    }

    @Test
    void validQuery_afterConnectionEstablished_sessionShouldNotClose() throws Exception {
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.getUri()).thenReturn(new URI("ws://localhost?suggest=1234"));
        websocketHandler.afterConnectionEstablished(webSocketSession);
        verify(webSocketSession, never()).close(any(CloseStatus.class));

    }
    @Test
    void sessionClose_handleTextMessage_verifyHandlerNotInvoked() throws Exception {
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.isOpen()).thenReturn(false);

        var json = """
                {
                    "action":"SUGGEST",
                    "payload":"xxxx"
                }
                """;
        TextMessage textMessage = new TextMessage(json);

        websocketHandler.handleTextMessage(webSocketSession, textMessage);

        verify(newSuggestionHandler, never()).handleInternal(any(WebSocketSession.class), any(WebsocketDTO.class));
    }

    @Test
    void sessionIsOpenButActionIsIncorrect_handleTextMessage_verifySendErrorToSocket()  throws Exception{
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.isOpen()).thenReturn(true);
        when(webSocketSession.getUri()).thenReturn(new URI("ws://localhost?suggest=1234"));

        var json = """
                {
                    "action":"ERROR",
                    "payload":{
                        "restaurant":"test",
                        "suggestedBy":"me"
                    }
                }
                """;
        TextMessage textMessage = new TextMessage(json);

        websocketHandler.handleTextMessage(webSocketSession, textMessage);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());

    }

    @Test
    void sessionIsOpenAndActionIsCorrect_handleTextMessage_verifyHandlerHappened()  throws Exception{
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.isOpen()).thenReturn(true);
        when(webSocketSession.getUri()).thenReturn(new URI("ws://localhost?suggest=1234"));

        var json = """
                {
                    "action":"SUGGEST",
                    "payload":{
                        "restaurant":"test",
                        "suggestedBy":"me"
                    }
                }
                """;
        TextMessage textMessage = new TextMessage(json);

        websocketHandler.handleTextMessage(webSocketSession, textMessage);

        verify(newSuggestionHandler).handleInternal(any(WebSocketSession.class),any(WebsocketDTO.class));

    }

    @Test
    void afterConnectionClosed_verifySessionWasClosed() throws Exception {
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);

        when(webSocketSession.getUri()).thenReturn(new URI("ws://localhost?suggest=1234"));

        doNothing().when(websocketUsers).remove(anyString(),any(WebSocketSession.class));
        websocketHandler.afterConnectionClosed(webSocketSession, CloseStatus.NORMAL);

        verify(webSocketSession).close();
        verify(websocketUsers).remove(anyString(),any(WebSocketSession.class));

    }
}