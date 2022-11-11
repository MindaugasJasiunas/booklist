package com.example.demo.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Slf4j
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map< String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map< String, Object> errorAttributesMap = super.getErrorAttributes(request, options);
        Throwable throwable = getError(request);

        log.error(String.format("ERROR OCCURRED: %s", throwable));

        if(throwable instanceof RuntimeException){
            RuntimeException ex = (RuntimeException) throwable;
            errorAttributesMap.put("message", ex.getMessage());
            errorAttributesMap.put("status", HttpStatus.BAD_REQUEST.value());
            errorAttributesMap.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        if(throwable instanceof ForbiddenError){
            errorAttributesMap.put("status", HttpStatus.FORBIDDEN.value());
        }



        // default status code
        errorAttributesMap.putIfAbsent("status", HttpStatus.BAD_REQUEST.value());
        return errorAttributesMap;
    }
}
