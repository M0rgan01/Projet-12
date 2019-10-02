package org.paniergarni.apigateway.security.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.paniergarni.apigateway.security.exception.AuthMethodNotSupportedException;
import org.paniergarni.apigateway.security.exception.JsonException;
import org.paniergarni.apigateway.security.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Processus d'échec de connection personnalisé
 *
 * @author Pichat morgan
 * <p>
 * 20 Juillet 2019
 */
@Component
public class LoginAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    public LoginAwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof AuthenticationServiceException) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE));

        } else if (e instanceof AuthMethodNotSupportedException){
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
            mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED));

        } else if (e instanceof JsonException){
            response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
            mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), HttpStatus.PRECONDITION_FAILED));

        }else
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), HttpStatus.UNAUTHORIZED));
        }
    }

