package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.LunchPlanSuggestionService;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.EndSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core.RedisSender;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketBroadcaster;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.EndContestObserver;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.SuggestionObserver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NewSuggestionHandlerTest {

    @Mock
    private LunchPlanSuggestionService lunchPlanSuggestionService;
    @Mock
    private WebsocketBroadcaster websocketBroadcaster;
    @Mock
    private Validator validator;
    @Mock
    private RedisSender redisSender;

    private NewSuggestionHandler newSuggestionHandler;
    @BeforeEach
    void beforeEach(){
        List<SuggestionObserver> observers = List.of(websocketBroadcaster);

        newSuggestionHandler = new NewSuggestionHandler(lunchPlanSuggestionService,observers, validator,redisSender);
    }

    @Test
    void validationFailed_throwException(){
        Errors errors = Mockito.mock(Errors.class);
        when(validator.validateObject(any())).thenReturn(errors);

        when(errors.hasErrors()).thenReturn(true);

        assertThrows(AppException.class, ()->{
            WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);

            WebsocketDTO<CreateSuggestionDTO> websocketDto = new WebsocketDTO<>();
            websocketDto.setAction(WebSocketAction.SUGGEST);
            websocketDto.setData(new CreateSuggestionDTO());
            websocketDto.setUuid("zzzz");
            newSuggestionHandler.handle(webSocketSession, websocketDto);
        });
    }


    @Test
    void validationSuccess_handleWebsocket_broadcastAndSendRedis(){
        Errors errors = Mockito.mock(Errors.class);
        when(validator.validateObject(any())).thenReturn(errors);

        when(errors.hasErrors()).thenReturn(false);

        doNothing().when(lunchPlanSuggestionService).create(anyString(),any(CreateSuggestionDTO.class));

        doNothing().when(websocketBroadcaster).onNewSuggestion(anyString(),any(CreateSuggestionDTO.class));

        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);

        WebsocketDTO<CreateSuggestionDTO> websocketDto = new WebsocketDTO<>();
        websocketDto.setAction(WebSocketAction.SUGGEST);
        websocketDto.setData(new CreateSuggestionDTO());
        websocketDto.setUuid("zzzz");
        newSuggestionHandler.handle(webSocketSession, websocketDto);


        ArgumentCaptor<WebsocketDTO> websocketDTOArgumentCaptor = ArgumentCaptor.forClass(WebsocketDTO.class);
        verify(redisSender).publish(websocketDTOArgumentCaptor.capture());
        assertEquals(WebSocketAction.SUGGEST, websocketDTOArgumentCaptor.getValue().getAction());

        verify(websocketBroadcaster).onNewSuggestion(anyString(),any(CreateSuggestionDTO.class));

    }
}