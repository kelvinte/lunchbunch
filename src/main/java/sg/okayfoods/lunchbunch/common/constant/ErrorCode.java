package sg.okayfoods.lunchbunch.common.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;

public enum ErrorCode {
    INVALID_USERNAME_OR_PASSWORD("AUTH001", HttpStatus.FORBIDDEN, "Invalid Username or Password"),
    EMAIL_ALREADY_IN_USE("AUTH002", HttpStatus.BAD_REQUEST, "Email Already in use"),
    INVALID_TOKEN("AUTH003", HttpStatus.FORBIDDEN, "Invalid Token"),

    UNKNOWN_AUTH_ERROR_OCCURRED("AUTH004", HttpStatus.FORBIDDEN, "Unknown AUTH Error occurred"),
    WS_MISSING_AUTH_TOKEN("WSAUTH001", HttpStatus.BAD_REQUEST, "Websocket URI missing authorization token"),
    FAILED_TO_PARSE_WEBSOCKET_ACTION("WSAUTH002", HttpStatus.BAD_REQUEST, "Websocket Action was incorrect"),
    FAILED_TO_PROCESS_WS("WSAUTH003", HttpStatus.BAD_REQUEST, "Failed to process websocket"),
    LUNCH_PLAN_ENDED_ALREADY("LP001", HttpStatus.BAD_REQUEST ,"Lunch Plan has already ended" ),
    NOT_EXISTING("LP002",HttpStatus.NOT_FOUND ,"Lunch plan not existing" );


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
