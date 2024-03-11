package sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class SuggestionHandler implements Command{
    @Override
    public void handle(WebSocketSession session) {
        if(session.isOpen()){

        }
    }
}
