package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.redis;


import lombok.*;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisDTO {
    private String redisId;
    private WebsocketDTO data;
}
