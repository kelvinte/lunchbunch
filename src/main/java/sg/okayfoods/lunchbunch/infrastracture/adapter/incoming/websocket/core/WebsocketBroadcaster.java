package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core.RedisSender;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.EndContestObserver;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer.SuggestionObserver;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;

@Service
@Slf4j
public class WebsocketBroadcaster implements SuggestionObserver, EndContestObserver {

    private WebsocketUsers websocketUsers;

    public WebsocketBroadcaster(
            WebsocketUsers websocketUsers){
        this.websocketUsers = websocketUsers;
    }


    @Override
    public void onNewSuggestion(String uuid, CreateSuggestionDTO suggestionDTO) {
        // notify
        var list = websocketUsers.get(uuid);
         if(list!=null) {
            WebsocketResponseDTO<CreateSuggestionDTO> websockResp = new WebsocketResponseDTO<>();
            websockResp.setAction(WebSocketAction.NOTIFY_ONE_SUGGESTION.name());
            websockResp.setData(suggestionDTO);
            String resp = JsonUtils.toJson(websockResp);

            for (var session : list) {
                try {
                    session.sendMessage(new TextMessage(resp));
                } catch (Exception e) {
                    log.error("Failed to send: " + e);
                }
            }
        }
    }

    @Override
    public void onEndContest(String uuid, LunchPlanWinnerResponseDTO winnerDTO) {
        var list = websocketUsers.get(uuid);
        if(list!=null) {
            WebsocketResponseDTO<LunchPlanWinnerResponseDTO> websockResp = new WebsocketResponseDTO<>();
            websockResp.setAction(WebSocketAction.END_SUGGESTION.name());
            websockResp.setData(winnerDTO);
            String resp = JsonUtils.toJson(websockResp);

            for (var session : list) {
                try {
                    session.sendMessage(new TextMessage(resp));
                } catch (Exception e) {
                    log.error("Failed to send: " + e);
                }
            }
        }
    }



}
