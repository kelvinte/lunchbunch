package sg.okayfoods.lunchbunch.infrastracture.adapter.handler;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import sg.okayfoods.lunchbunch.application.AuthenticationService;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;

import java.net.URI;


@Service
public class AuthorizationHandler {

    private final AuthenticationService authenticationService;

    public AuthorizationHandler(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public AppUser handle(WebSocketSession session){
        if(session.getUri() == null){
            throw new AppException(ErrorCode.WS_MISSING_AUTH_TOKEN);
        }
        String query = session.getUri().getQuery();
        String authorization = getAuthorization(query);

        return this.authenticationService.validateToken(authorization);
    }

    private String getAuthorization(String uri){
        String[] result = uri.split("auth=");
        if(result.length == 1){
            throw new AppException(ErrorCode.WS_MISSING_AUTH_TOKEN);
        }
        return result[1];
    }
}
