package sg.okayfoods.lunchbunch.common.exception;

import lombok.Getter;
import org.springframework.web.ErrorResponseException;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;

@Getter
public class AppException extends ErrorResponseException {
    private ErrorCode errorCode;
    public  AppException(ErrorCode exception) {
        super(exception.getStatusCode(), exception.getProblemDetail(), null);
        this.errorCode = exception;
    }

}
