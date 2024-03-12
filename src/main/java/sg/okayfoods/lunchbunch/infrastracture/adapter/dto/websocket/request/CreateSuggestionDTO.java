package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSuggestionDTO {


    @NotEmpty
    private String restaurant;

    @NotEmpty
    private String suggestedBy;
}
