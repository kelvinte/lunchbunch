package sg.okayfoods.lunchbunch.infrastracture.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import sg.okayfoods.lunchbunch.common.constant.UrlConstants;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {


    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, UrlConstants.LUNCH_PLAN_WEBSOCKET).setAllowedOrigins("*");
    }


}
