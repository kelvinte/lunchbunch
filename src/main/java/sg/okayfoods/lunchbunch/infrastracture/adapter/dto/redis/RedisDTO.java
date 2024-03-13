package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.redis;


import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisDTO {
    private String redisId;
    private String uuid;
    private Object data;
}
