package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.LunchPlanService;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.RetrieveSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.SuggestionResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketCommand;

import java.util.List;

@Service
@Slf4j
public class EndSuggestionHandler extends WebsocketCommand<RetrieveSuggestionDTO, LunchPlanWinnerResponseDTO> {

    private final LunchPlanService lunchPlanService;

    public EndSuggestionHandler(LunchPlanService lunchPlanService) {
        this.lunchPlanService = lunchPlanService;
    }

    @Override
    public LunchPlanWinnerResponseDTO handle(WebSocketSession session, WebsocketDTO<RetrieveSuggestionDTO> message) {
        return this.lunchPlanService.end(message.getUuid());
    }

    @Override
    public boolean supports(WebSocketAction action) {
        return WebSocketAction.END_SUGGESTION.equals(action);
    }
}
