package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LunchPlanSuggestionRequestDTO {
    @NotEmpty
    private String restaurantName;
    @NotEmpty
    private String suggestedBy;
}
