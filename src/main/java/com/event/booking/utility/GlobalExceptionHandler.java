package com.event.booking.utility;

import com.event.booking.exception.BusinessException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> ioException(IOException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<Object> dataFormatException(DataFormatException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        String errorMapString = errorMap.toString();
        return ResponseHandler.generateResponse(errorMapString, HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> httpMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED, null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Object> httpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getLocalizedMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> noHandlerFound(NoHandlerFoundException  ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getRequestURL() + " does not exist", HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(MissingPathVariableException .class)
    public ResponseEntity<Object> missingPathVariable(MissingPathVariableException ex, WebRequest request) {
        return ResponseHandler.generateResponse("Missing path variable " + ex.getVariableName(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> missingPathVariable(TypeMismatchException ex, WebRequest request) {
        return ResponseHandler.generateResponse("Type mismatch: " + ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> missingPathVariable(BusinessException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getErrorMessage(), ex.getErrorCode(), null);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> missingPathVariable(NullPointerException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, null);
    }

}
