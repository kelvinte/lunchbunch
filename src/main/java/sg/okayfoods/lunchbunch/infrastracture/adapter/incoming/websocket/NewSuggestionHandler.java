package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.LunchPlanSuggestionService;
import sg.okayfoods.lunchbunch.application.observer.SuggestionObserver;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketCommand;

import java.util.List;

@Service
@Slf4j
public class NewSuggestionHandler extends WebsocketCommand<CreateSuggestionDTO, Void> {

    private LunchPlanSuggestionService lunchPlanSuggestionService;
    private List<SuggestionObserver> suggestionObservers;

    public NewSuggestionHandler(LunchPlanSuggestionService lunchPlanSuggestionService, List<SuggestionObserver> suggestionObservers) {
        this.lunchPlanSuggestionService = lunchPlanSuggestionService;
        this.suggestionObservers = suggestionObservers;
    }

    @Override
    public Void handle(WebSocketSession session, WebsocketDTO<CreateSuggestionDTO> message) {
        log.info("Writing to suggestion handler {}", JsonUtils.toJson(message));
        this.lunchPlanSuggestionService.create(message.getUuid(), message.getData());

        for(var obs : suggestionObservers){
            obs.onNewSuggestion(message.getUuid(), message.getData());
        }

        return null;
    }

    @Override
    public boolean supports(WebSocketAction action) {
        return WebSocketAction.SUGGEST.equals(action);
    }
}
