package sg.okayfoods.lunchbunch.infrastracture.adapter.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;

import java.util.List;

@ControllerAdvice
@Order(1)
@Slf4j

public class SpecificExceptionHandler {
    @ExceptionHandler({AppException.class})
    @ResponseBody
    public ResponseEntity<GenericResponse> handle(AppException ex) {
        log.error("App Exception", ex);
        var genericResponse = new GenericResponse(ex.getBody().getTitle(), null,
                ex.getStatusCode().value(), false);
        return ResponseEntity.status(ex.getStatusCode()).body(genericResponse);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseBody
    public GenericResponse handleForbidden(BadCredentialsException ex) {
        log.error("Bad Credential Exception", ex);
        return new GenericResponse(ex.getMessage(), null,
                HttpStatus.FORBIDDEN.value(), false);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GenericResponse handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errors = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errors.append(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        return new GenericResponse(errors.toString(),null,
                HttpStatus.BAD_REQUEST.value(), false);
    }

}
