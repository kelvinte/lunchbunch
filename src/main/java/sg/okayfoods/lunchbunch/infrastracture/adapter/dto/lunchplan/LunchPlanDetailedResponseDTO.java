package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan;

import lombok.*;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanWinner;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.SuggestionResponseDTO;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LunchPlanDetailedResponseDTO {
    private String id;
    private String uuid;
    private String date;
    private String description;
    private String initiator;
    private boolean isOwner;
    private List<SuggestionResponseDTO> suggestions;

    private LunchPlanWinnerResponseDTO winner;
}
