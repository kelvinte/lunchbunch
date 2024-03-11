package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.LunchPlanSuggestionService;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.RetrieveSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.SuggestionResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketCommand;

import java.util.List;

@Service
@Slf4j
public class RetrieveSuggestionHandler extends WebsocketCommand<RetrieveSuggestionDTO, List<SuggestionResponseDTO>> {

    private final LunchPlanSuggestionService lunchPlanSuggestionService;

    public RetrieveSuggestionHandler(LunchPlanSuggestionService lunchPlanSuggestionService){
        this.lunchPlanSuggestionService = lunchPlanSuggestionService;
    }

    @Override
    public List<SuggestionResponseDTO> handle(WebSocketSession session, WebsocketDTO<RetrieveSuggestionDTO> message) {
        return lunchPlanSuggestionService.retrieve(message.getUuid());
    }

    @Override
    public boolean supports(WebSocketAction action) {
        return WebSocketAction.RETRIEVE_SUGGESTIONS.equals(action);
    }
}
