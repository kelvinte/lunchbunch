package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LunchPlanWinnerResponseDTO {

    private String restaurant;

    private String suggestedBy;
    private String date;
}
