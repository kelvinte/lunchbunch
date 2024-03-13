package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core;

import lombok.*;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketDTO<T> {
    private String uuid;
    private WebSocketAction action;
    private T data;
}
