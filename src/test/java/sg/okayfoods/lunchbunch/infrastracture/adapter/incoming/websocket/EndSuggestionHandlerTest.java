package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.LunchPlanService;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.EndSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core.RedisSender;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.EndSuggestionHandler;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketBroadcaster;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.EndContestObserver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EndSuggestionHandlerTest {

    @Mock
    private LunchPlanService lunchPlanService;


    @Mock
    private RedisSender redisSender;

    @Mock
    private WebsocketBroadcaster websocketBroadcaster;
    EndSuggestionHandler endSuggestionHandler;

    @BeforeEach
    void beforeEach(){
        List<EndContestObserver> endContestObservers = List.of(websocketBroadcaster);

        endSuggestionHandler = new EndSuggestionHandler(lunchPlanService, endContestObservers, redisSender);
    }
    @Test
    void requestOkay_handleWebsocket_broadcastAndSendRedis(){
        LunchPlanWinnerResponseDTO lunchPlanWinner = new LunchPlanWinnerResponseDTO();
        when(lunchPlanService.end(anyString())).thenReturn(lunchPlanWinner);
        doNothing().when(websocketBroadcaster).onEndContest(anyString(),any(LunchPlanWinnerResponseDTO.class));
        doNothing().when(redisSender).publish(any(WebsocketDTO.class));

        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);

        WebsocketDTO<EndSuggestionDTO> websocketDto = new WebsocketDTO<>();
        websocketDto.setAction(WebSocketAction.END_SUGGESTION);
        websocketDto.setData(new EndSuggestionDTO());
        websocketDto.setUuid("zzzz");
        endSuggestionHandler.handle(webSocketSession,websocketDto);

        ArgumentCaptor<WebsocketDTO> websocketDTOArgumentCaptor = ArgumentCaptor.forClass(WebsocketDTO.class);
        verify(redisSender).publish(websocketDTOArgumentCaptor.capture());
        assertEquals(WebSocketAction.END_SUGGESTION, websocketDTOArgumentCaptor.getValue().getAction());

        verify(websocketBroadcaster).onEndContest(anyString(),any(LunchPlanWinnerResponseDTO.class));

    }


}
