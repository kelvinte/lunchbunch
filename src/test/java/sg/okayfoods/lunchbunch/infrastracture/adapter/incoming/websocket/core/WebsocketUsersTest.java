package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.*;


class WebsocketUsersTest {



    @Test
    void add() {
        WebsocketUsers websocketUsers = new WebsocketUsers();
        var session = Mockito.mock(WebSocketSession.class);
        websocketUsers.add("xxxx", session);

        var result = websocketUsers.get("xxxx");
        assertEquals(session, result.peek());
    }

    @Test
    void sessionExist_remove_doNotThrowError() {

        WebsocketUsers websocketUsers = new WebsocketUsers();
        var session = Mockito.mock(WebSocketSession.class);
        Mockito.when(session.getId()).thenReturn("zed");
        websocketUsers.add("xxxx", session);

        websocketUsers.remove("xxxx",session);
        var result = websocketUsers.get("xxxx");
        assertEquals(0,result.size());
    }


    @Test
    void sessionDoesNotExist_remove_doNotThrowError() {

        WebsocketUsers websocketUsers = new WebsocketUsers();
        var session = Mockito.mock(WebSocketSession.class);
        Mockito.when(session.getId()).thenReturn("zed");
        websocketUsers.add("xxxx", session);

        var session2 = Mockito.mock(WebSocketSession.class);
        Mockito.when(session.getId()).thenReturn("zzz");
        websocketUsers.remove("xxxx",session2);
        var result = websocketUsers.get("xxxx");
        assertEquals(1,result.size());
    }

}