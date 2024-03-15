package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.websocket.core;

import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.common.constant.WebSocketAction;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.core.WebsocketDTO;

import java.lang.reflect.ParameterizedType;

public abstract class WebsocketCommand<T, R> {
    private Class<T> requestClass;
	protected WebsocketCommand(){
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        requestClass = (Class<T>) superClass.getActualTypeArguments()[0];
    }

    public R handleInternal(WebSocketSession session, WebsocketDTO<T> message){
        beforeHandle(session, message);
        return handle(session, message);
    }
    public void beforeHandle(WebSocketSession session, WebsocketDTO<T> message){
        // Perform authorization or whatever is needed
    }

    public abstract R handle(WebSocketSession session,WebsocketDTO<T> message);

    public abstract boolean supports(WebSocketAction action);


    public Class<T> getMethodClass() {
        return requestClass;
    }
}
