package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core.observer;

import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;

public interface EndContestObserver {
    void onEndContest(String uuid, LunchPlanWinnerResponseDTO suggestionDTO);
}
