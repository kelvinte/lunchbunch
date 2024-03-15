package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis;

import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.redis.core.RedisSubscriber;
import sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.WebsocketBroadcaster;
@Service
public class RedisNotifier implements RedisSubscriber {

    private WebsocketBroadcaster broadcaster;

    public RedisNotifier(WebsocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @Override
    public void onRedisReceive(WebsocketDTO websocketDTO) {
        switch (websocketDTO.getAction()){
            case SUGGEST -> {
                broadcaster.onNewSuggestion(websocketDTO.getUuid(), JsonUtils.toObject(websocketDTO.getData(), CreateSuggestionDTO.class));
            }
            case END_SUGGESTION -> {
                broadcaster.onEndContest(websocketDTO.getUuid(), JsonUtils.toObject(websocketDTO.getData(), LunchPlanWinnerResponseDTO.class));
            }
        }
    }
}
