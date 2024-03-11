package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSuggestionDTO {


    private String restaurant;

    private String suggestedBy;
}
