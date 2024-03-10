package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan;

import lombok.*;

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
    private List<LunchPlanSuggestionResponseDTO> suggestions;
}
