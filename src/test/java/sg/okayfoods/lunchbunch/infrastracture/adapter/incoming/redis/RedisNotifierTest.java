package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketBroadcaster;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RedisNotifierTest {

    @Mock
    WebsocketBroadcaster broadcaster;

    @InjectMocks
    RedisNotifier redisNotifier;

    @Test
    void givenSuggest_onRedisReceive_broadcasterNewSuggestion() {

        WebsocketDTO<CreateSuggestionDTO> createSuggestionDTOWebsocketDTO = new WebsocketDTO<>();
        createSuggestionDTOWebsocketDTO.setUuid("zzzz");
        createSuggestionDTOWebsocketDTO.setAction(WebSocketAction.SUGGEST);
        createSuggestionDTOWebsocketDTO.setData(new CreateSuggestionDTO());
        redisNotifier.onRedisReceive(createSuggestionDTOWebsocketDTO);
        verify(broadcaster).onNewSuggestion(anyString(),any(CreateSuggestionDTO.class));
    }

    @Test
    void givenEndSuggestion_onRedisReceive_broadcasterWinner() {
        WebsocketDTO<LunchPlanWinnerResponseDTO> websocketDTO = new WebsocketDTO<>();
        websocketDTO.setUuid("zzzz");
        websocketDTO.setAction(WebSocketAction.END_SUGGESTION);
        websocketDTO.setData(new LunchPlanWinnerResponseDTO());
        redisNotifier.onRedisReceive(websocketDTO);
        verify(broadcaster).onEndContest(anyString(),any(LunchPlanWinnerResponseDTO.class));
    }

    @Test
    void givenInvalidAction_onRedisReceive_doNothing(){
        WebsocketDTO<LunchPlanWinnerResponseDTO> websocketDTO = new WebsocketDTO<>();
        websocketDTO.setUuid("zzzz");
        websocketDTO.setAction(WebSocketAction.ERROR);
        websocketDTO.setData(new LunchPlanWinnerResponseDTO());
        redisNotifier.onRedisReceive(websocketDTO);
        verify(broadcaster, never()).onNewSuggestion(anyString(),any(CreateSuggestionDTO.class));
        verify(broadcaster, never()).onEndContest(anyString(),any(LunchPlanWinnerResponseDTO.class));

    }
}