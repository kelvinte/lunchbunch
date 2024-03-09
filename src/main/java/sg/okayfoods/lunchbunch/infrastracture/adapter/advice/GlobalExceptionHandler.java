package sg.okayfoods.lunchbunch.infrastracture.adapter.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;

@ControllerAdvice
@Order(2)
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public GenericResponse handle2(Exception ex) {
        log.error("Failed to process request", ex);
        return new GenericResponse("Failed to process request",  null, HttpStatus.INTERNAL_SERVER_ERROR.value() ,false);
    }

}
