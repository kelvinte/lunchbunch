package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer;

import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;

public interface SuggestionObserver {
    void onNewSuggestion(String uuid, CreateSuggestionDTO suggestionDTO);
}
