package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class RsEventExceptionHandle {
    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class, IndexOutOfBoundsException.class})
    public ResponseEntity invalid_index_handle(Exception e) {
        Error error = new Error();
        String errorMessage;
        if (e instanceof RsEventNotValidException) {
            errorMessage = ((RsEventNotValidException) e).getErrorMessage();
        } else if (e instanceof IndexOutOfBoundsException) {
            errorMessage = "invalid request param";
        } else {
            errorMessage = "invalid param";
        }
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);


    }

}
