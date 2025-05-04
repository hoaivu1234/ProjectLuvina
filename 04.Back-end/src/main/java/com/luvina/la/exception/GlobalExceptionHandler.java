package com.luvina.la.exception;

import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * GlobalExceptionHandler.java, 5/4/2025 hoaivd
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<BaseResponse> handlingRuntimeException(Exception exception) {
        ErrorMessage errorMessage = new ErrorMessage("ER023", new ArrayList<>());
        BaseResponse response = new BaseResponse(500, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse> handleNotFound(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage("ER022", new ArrayList<>());
        BaseResponse response = new BaseResponse(404, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
