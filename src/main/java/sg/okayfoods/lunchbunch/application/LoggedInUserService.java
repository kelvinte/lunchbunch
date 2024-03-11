package sg.okayfoods.lunchbunch.application;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;

@Service
public class LoggedInUserService {
    public LoggedInUser getLoggedInUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
            return null;
        }else{
            return (LoggedInUser) authentication;
        }
    }

}
