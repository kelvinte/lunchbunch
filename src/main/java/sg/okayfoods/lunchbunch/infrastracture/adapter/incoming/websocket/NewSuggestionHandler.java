package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.LunchPlanSuggestionService;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.SuggestionObserver;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketCommand;
import sg.okayfoods.lunchbunch.infrastracture.kafka.KafkaSender;

import java.util.List;

@Service
@Slf4j
public class NewSuggestionHandler extends WebsocketCommand<CreateSuggestionDTO, Void> {

    private LunchPlanSuggestionService lunchPlanSuggestionService;
    private List<SuggestionObserver> suggestionObservers;
    private Validator validator;

    private KafkaSender kafkaSender;
    public NewSuggestionHandler(LunchPlanSuggestionService lunchPlanSuggestionService,
                                KafkaSender kafkaSender,
                                List<SuggestionObserver> suggestionObservers, Validator validator) {
        this.lunchPlanSuggestionService = lunchPlanSuggestionService;
        this.suggestionObservers = suggestionObservers;
        this.validator = validator;
        this.kafkaSender = kafkaSender;
    }

    @Override
    public Void handle(WebSocketSession session, WebsocketDTO<CreateSuggestionDTO> message) {
        log.info("Writing to suggestion handler {}", JsonUtils.toJson(message));
        var validation = validator.validateObject(message.getData());
        if(validation.hasErrors()) {
            throw new AppException(ErrorCode.FAILED_TO_PROCESS_WS);
        }
        this.lunchPlanSuggestionService.create(message.getUuid(), message.getData());

        kafkaSender.send(JsonUtils.toJson(message));
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
