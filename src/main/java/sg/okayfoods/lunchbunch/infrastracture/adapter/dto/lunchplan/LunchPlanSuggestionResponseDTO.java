package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan;

import jakarta.persistence.*;
import lombok.*;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LunchPlanSuggestionResponseDTO {

    private Long id;

    private String restaurantName;

    private String suggestedBy;
}
