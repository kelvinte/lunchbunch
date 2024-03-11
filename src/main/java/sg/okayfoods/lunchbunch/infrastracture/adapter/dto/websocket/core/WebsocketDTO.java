package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketDTO<T> {
    private String uuid;
    private T data;
}
