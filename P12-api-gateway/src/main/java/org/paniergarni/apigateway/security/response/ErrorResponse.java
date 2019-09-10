package org.paniergarni.apigateway.security.response;

import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Personnalisation d'une réponse HTTP pour les erreur
 * 
 * @author pichat morgan
 *
 * 20 Juillet 2019
 *
 */
public class ErrorResponse {
    // HTTP Response Status Code
    private final HttpStatus status;

    // General Error message
    private final String message;

    // Error code
    private final String exceptionClass;

    private final Date timestamp;

    protected ErrorResponse(final String message, final String exceptionClass, HttpStatus status) {
        this.message = message;
        this.exceptionClass = exceptionClass;
        this.status = status;
        this.timestamp = new Date();
    }

    public static ErrorResponse of(final String message, final String exceptionClass, HttpStatus status) {
        return new ErrorResponse(message, exceptionClass, status);
    }

    public Integer getStatus() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
