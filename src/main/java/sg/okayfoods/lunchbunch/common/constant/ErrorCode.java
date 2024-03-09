package sg.okayfoods.lunchbunch.common.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;

public enum ErrorCode {
    INVALID_USERNAME_OR_PASSWORD("AUTH001", HttpStatus.FORBIDDEN, "Invalid Username or Password"),
    EMAIL_ALREADY_IN_USE("AUTH002", HttpStatus.BAD_REQUEST, "Email Already in use"),
    INVALID_TOKEN("AUTH003", HttpStatus.FORBIDDEN, "Invalid Token"),

    ;

    private String code;
    private HttpStatus statusCode;
    private String message;


    ErrorCode(String code, HttpStatus statusCode, String message) {
    this.code = code;
    this.statusCode = statusCode;
    this.message = message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public ProblemDetail getProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(this.getStatusCode());
        problemDetail.setTitle(String.format("%s %s", this.getCode(), this.getMessage()));
        problemDetail.setProperty("success", false);
        problemDetail.setProperty("code", this.getCode());
        return problemDetail;
    }

    public GenericResponse toGenericResponse(){
        return new GenericResponse(this.getMessage(), null,
                this.statusCode.value(), false);
    }

}
