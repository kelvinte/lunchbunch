package sg.okayfoods.lunchbunch.infrastracture.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import sg.okayfoods.lunchbunch.common.constant.UrlConstants;
import sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command.AuthorizationHandler;
import sg.okayfoods.lunchbunch.infrastracture.adapter.handler.WebsocketHandler;
import sg.okayfoods.lunchbunch.infrastracture.adapter.handler.command.SuggestionHandler;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private AuthorizationHandler authorizationHandler;
    @Autowired
    private SuggestionHandler suggestionHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), UrlConstants.LUNCH_PLAN_WEBSOCKET).setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebsocketHandler(authorizationHandler, suggestionHandler);
    }
}
