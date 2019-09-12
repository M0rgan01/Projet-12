package org.paniergarni.apigateway.security.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    // HTTP Response Status Code
    private HttpStatus status;

    // General Error message
    private String error;

    private Date timestamp;

    protected ErrorResponse(String error, HttpStatus status) {
        this.error = error;
        this.status = status;
        this.timestamp = new Date();
    }

    public static ErrorResponse of(String error, HttpStatus status) {
        return new ErrorResponse(error, status);
    }

}
