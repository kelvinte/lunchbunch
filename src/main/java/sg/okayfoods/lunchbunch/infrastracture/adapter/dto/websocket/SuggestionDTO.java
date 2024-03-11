package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionDTO {


    private Long id;

    private String restaurantName;

    private String suggestedBy;
    private String lunchPlanUuid;
}
