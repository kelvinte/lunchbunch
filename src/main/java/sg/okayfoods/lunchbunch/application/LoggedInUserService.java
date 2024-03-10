package sg.okayfoods.lunchbunch.application;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;

@Service
public class LoggedInUserService {
    public LoggedInUser getLoggedInUser() {
        return (LoggedInUser) SecurityContextHolder.getContext().getAuthentication();
    }

}
