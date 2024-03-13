package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.LunchPlanService;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.RetrieveSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core.RedisSender;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketCommand;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.EndContestObserver;

import java.util.List;

@Service
@Slf4j
public class EndSuggestionHandler extends WebsocketCommand<RetrieveSuggestionDTO, Void> {

    private final LunchPlanService lunchPlanService;

    private List<EndContestObserver> endContestObservers;
    private RedisSender redisSender;
    public EndSuggestionHandler(LunchPlanService lunchPlanService,List<EndContestObserver> endContestObservers,
                                RedisSender redisSender) {
        this.lunchPlanService = lunchPlanService;
        this.endContestObservers = endContestObservers;
        this.redisSender = redisSender;
    }

    @Override
    public Void handle(WebSocketSession session, WebsocketDTO<RetrieveSuggestionDTO> message) {
        var result = this.lunchPlanService.end(message.getUuid());

        for(var obs : endContestObservers){
            obs.onEndContest(message.getUuid(),result);
        }

        WebsocketDTO<LunchPlanWinnerResponseDTO> winnerResponseDTOWebsocketDTO = new WebsocketDTO<>();
        winnerResponseDTOWebsocketDTO.setUuid(message.getUuid());
        winnerResponseDTOWebsocketDTO.setData(result);
        winnerResponseDTOWebsocketDTO.setAction(message.getAction());
        redisSender.publish(winnerResponseDTOWebsocketDTO);
        return null;
    }

    @Override
    public boolean supports(WebSocketAction action) {
        return WebSocketAction.END_SUGGESTION.equals(action);
    }
}
