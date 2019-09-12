package org.paniergarni.apigateway.security.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * 
 * Processus d'échec de connection personnalisé
 * 
 * @author Pichat morgan
 *
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
			
		// mise en place du code et du contenu de la réponse
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		 if (e instanceof AuthenticationServiceException){
			 mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
		 }else {
			mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), HttpStatus.UNAUTHORIZED));
		}
	}
}
