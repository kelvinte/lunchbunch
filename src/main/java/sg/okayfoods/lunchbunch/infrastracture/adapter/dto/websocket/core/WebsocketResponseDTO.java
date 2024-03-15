package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebsocketResponseDTO<T> {
    private String action;
    private T data;
}
