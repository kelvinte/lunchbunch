package sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command;

import org.springframework.web.socket.WebSocketSession;

public interface Command {
    void handle(WebSocketSession session);

}
