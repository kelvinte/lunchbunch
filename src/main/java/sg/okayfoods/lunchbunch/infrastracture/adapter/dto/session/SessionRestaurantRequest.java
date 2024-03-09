package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.session;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionRestaurantRequest {

    private String restaurantName;

    private Long suggestedBy;
}
