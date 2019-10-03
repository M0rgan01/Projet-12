package org.paniergarni.stock.controller.handler;


import org.paniergarni.stock.exception.CriteriaException;
import org.paniergarni.stock.exception.StockException;
import org.paniergarni.stock.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandleException {

    private static final Logger logger = LoggerFactory.getLogger(HandleException.class);

    @ExceptionHandler(StockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(StockException ex) {
        return ErrorResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(MethodArgumentNotValidException ex) {

        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        List<ErrorResponse.ErrorDetails> errorDetails = new ArrayList<>();
        for (FieldError fieldError : errors) {
            ErrorResponse.ErrorDetails error = new ErrorResponse.ErrorDetails();
            error.setFieldName(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());
            errorDetails.add(error);
        }

        return ErrorResponse.of(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(IllegalArgumentException ex) {
        return ErrorResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex) {
        logger.error("Internal error : " + ex.getMessage());
        return ErrorResponse.of("internal.error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(HttpMessageNotReadableException ex) {
        return ErrorResponse.of("json.error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CriteriaException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorResponse handleException(CriteriaException ex) {
        return ErrorResponse.of(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
