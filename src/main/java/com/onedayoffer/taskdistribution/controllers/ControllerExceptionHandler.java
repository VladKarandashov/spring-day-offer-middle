package com.onedayoffer.taskdistribution.controllers;

import com.onedayoffer.taskdistribution.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<String> handle(BusinessException e) {
        log.error("Возникло бизнес-исключение: ", e);
        var response = e.toString();
        return ResponseEntity.internalServerError()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handle(Exception e) {
        log.error("Возникло непредвиденное исключение: ", e);
        var response = e.getMessage();
        return ResponseEntity.internalServerError()
                .body(response);
    }

}
