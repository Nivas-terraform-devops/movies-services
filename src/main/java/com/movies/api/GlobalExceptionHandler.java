package com.movies.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.movies.exception.InvalidDataException;
import com.movies.exception.NotFoundException;

import lombok.Getter;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Getter
    public static class ErrorResponse {

        private final String message;
        private final String reason;

        ErrorResponse(String message, String reason) {
            this.message = message;
            this.reason = reason;
        }
    }
    
    // 400 ERROR BAD REQUEST
    @ExceptionHandler({
        InvalidDataException.class , HttpMessageNotReadableException.class})
@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ErrorResponse handleInvalidDataException(Exception ex) {

    log.warn(ex.getMessage());

    return new ErrorResponse(
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
           "BRO NIVU PAPISTUNV CORECTEY BODY INPUT SNED CHEYU ADO OKKATI EMPTY GA PAMPINCHAKU"
    );
}
     
    //404 Exception 
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {

        log.warn(ex.getMessage());

        return new ErrorResponse(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                 ex.getMessage()
        );
}
    //500 INTERNAL SERVER ERROR 
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnknownException(Exception ex) {

        log.error(ex.getMessage());

        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                 ex.getMessage()
        );
    }
}

    