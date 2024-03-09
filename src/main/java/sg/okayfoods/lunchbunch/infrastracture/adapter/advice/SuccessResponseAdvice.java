package sg.okayfoods.lunchbunch.infrastracture.adapter.advice;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.web.ProjectingJackson2HttpMessageConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;

@ControllerAdvice

public class SuccessResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Boolean.TRUE;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if(body instanceof String) {
            return body;
        }

        if(body instanceof ByteArrayResource || body instanceof byte[]) {
            return body;
        }

        if(!(body instanceof ResponseEntity<?>) && !(body instanceof ProblemDetail) && !(body instanceof GenericResponse)) {
            return new GenericResponse("Request Processed successfully",  body, HttpStatus.OK.value() , Boolean.TRUE);
        }
        return body;

    }

}
