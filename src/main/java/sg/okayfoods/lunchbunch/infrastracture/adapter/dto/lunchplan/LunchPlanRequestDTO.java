package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LunchPlanRequestDTO {
    private String date;
    private String description;
}
