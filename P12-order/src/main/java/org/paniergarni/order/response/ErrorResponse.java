package org.paniergarni.order.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Data
public class ErrorResponse {

    private List<ErrorDetails> errors;
    private String error;
    private Date timestamp;
    private HttpStatus status;

    protected ErrorResponse(List<ErrorDetails> errors, HttpStatus status) {
        this.errors = errors;
        this.timestamp = new Date();
        this.status = status;
    }

    protected ErrorResponse(String error, HttpStatus status) {
        this.error = error;
        this.timestamp = new Date();
        this.status = status;
    }

    public static ErrorResponse of( String error, HttpStatus status) {
        return new ErrorResponse(error, status);
    }

    public static ErrorResponse of( List<ErrorDetails> errors, HttpStatus status) {
        return new ErrorResponse(errors, status);
    }

    @Data
    public static class ErrorDetails {
        private String fieldName;
        private String message;
    }
}
