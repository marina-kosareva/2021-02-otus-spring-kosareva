package ru.otus.reactive.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Getter
@Setter
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.put("status", getStatus());
        return map;
    }
}
