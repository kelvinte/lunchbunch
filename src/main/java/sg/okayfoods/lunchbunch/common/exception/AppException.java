package sg.okayfoods.lunchbunch.common.exception;

import org.springframework.web.ErrorResponseException;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;

public class AppException extends ErrorResponseException {
    public  AppException(ErrorCode exception) {
        super(exception.getStatusCode(), exception.getProblemDetail(), null);
    }

}
