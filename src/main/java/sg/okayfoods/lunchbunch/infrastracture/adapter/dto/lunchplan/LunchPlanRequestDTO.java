package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LunchPlanRequestDTO {
    @NotEmpty
    private String date;
    @NotEmpty
    private String description;
}
