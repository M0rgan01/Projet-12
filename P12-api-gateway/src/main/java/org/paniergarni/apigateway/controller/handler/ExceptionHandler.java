package org.paniergarni.apigateway.controller.handler;

import feign.FeignException;
import feign.RetryableException;
import org.paniergarni.apigateway.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import org.paniergarni.apigateway.security.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public byte[] handleException(FeignException ex) {
        return ex.content();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RetryableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ErrorResponse handleException(RetryableException ex) {
        logger.error("Service unavailable : " + ex.getMessage());
        return ErrorResponse.of("internal.error", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex) {
        logger.error("Internal error : " + ex.getMessage());
        return ErrorResponse.of("internal.error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public ErrorResponse handleException(HttpMessageNotReadableException ex) {
        return ErrorResponse.of("json.error", HttpStatus.PRECONDITION_FAILED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(IllegalArgumentException ex) {
        return ErrorResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleException(AuthenticationException ex) {
        return ErrorResponse.of(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
